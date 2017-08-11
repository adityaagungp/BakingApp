package com.aditya.bakingapp.home;

import com.aditya.bakingapp.object.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASUS A456U on 07/08/2017.
 */

public class HomePresenterImpl implements HomePresenter {

    private List<Recipe> mRecipes;
    private HomeView mView;
    private HomeInteractor mInteractor;

    public HomePresenterImpl(HomeView view){
        mView = view;
        mRecipes = new ArrayList<>();
        mInteractor = new HomeInteractorImpl(this);
    }

    @Override
    public void loadRecipes() {
        mInteractor.loadRecipes();
    }

    @Override
    public void failLoadingRecipes(String message) {
        mView.onErrorLoadingRecipes(message);
    }

    @Override
    public List<Recipe> getRecipes() {
        return mRecipes;
    }

    @Override
    public void setRecipes(List<Recipe> recipes) {
        mRecipes = recipes;
        mView.onShowRecipes(mRecipes);
    }
}
