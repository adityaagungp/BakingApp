package com.aditya.bakingapp.recipes;

import android.util.Log;

import com.aditya.bakingapp.api.APIRequest;
import com.aditya.bakingapp.object.Recipe;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ASUS A456U on 08/08/2017.
 */

public class RecipesInteractorImpl implements RecipesInteractor, Callback<List<Recipe>> {

    private RecipesPresenter mPresenter;

    public RecipesInteractorImpl(RecipesPresenter presenter){
        mPresenter = presenter;
    }

    @Override
    public void loadRecipes() {
        APIRequest.getInstance().getRecipes(this);
    }

    @Override
    public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
        if (response.isSuccessful()) {
            mPresenter.setRecipes(response.body());
        } else {
            try {
                mPresenter.failLoadingRecipes(response.errorBody().string());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailure(Call<List<Recipe>> call, Throwable t) {
        mPresenter.failLoadingRecipes(t.getMessage());
    }
}
