package com.aditya.bakingapp.home;

import com.aditya.bakingapp.api.APIRequest;
import com.aditya.bakingapp.object.Recipe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ASUS A456U on 08/08/2017.
 */

public class HomeInteractorImpl implements HomeInteractor, Callback<List<Recipe>> {

    private HomePresenter mPresenter;
    private Realm mRealm = Realm.getDefaultInstance();

    public HomeInteractorImpl(HomePresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void loadRecipes() {
        APIRequest.getInstance().getRecipes(this);
    }

    @Override
    public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
        if (response.isSuccessful()) {
            List<Recipe> recipes = response.body();
            mPresenter.setRecipes(recipes);
            storeRecipes(recipes);
        } else {
            List<Recipe> recipes = retrieveStoredRecipes();
            if (recipes == null || recipes.isEmpty()) {
                try {
                    mPresenter.failLoadingRecipes(response.errorBody().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                mPresenter.setRecipes(recipes);
            }
        }
    }

    @Override
    public void onFailure(Call<List<Recipe>> call, Throwable t) {
        List<Recipe> recipes = retrieveStoredRecipes();
        if (recipes == null || recipes.isEmpty()) {
            mPresenter.failLoadingRecipes(t.getMessage());
        } else {
            mPresenter.setRecipes(recipes);
        }
    }

    private void storeRecipes(List<Recipe> recipes) {
        RealmList<Recipe> realmRecipes = new RealmList<>(recipes.toArray(new Recipe[recipes.size()]));
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(realmRecipes);
        mRealm.commitTransaction();
    }

    private List<Recipe> retrieveStoredRecipes() {
        RealmResults<Recipe> results = mRealm.where(Recipe.class).findAll();
        List<Recipe> recipes = new ArrayList<>();
        recipes.addAll(results);
        return recipes;
    }
}
