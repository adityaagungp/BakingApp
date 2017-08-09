package com.aditya.bakingapp.recipes;

import android.util.Log;

import com.aditya.bakingapp.object.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASUS A456U on 07/08/2017.
 */

public class RecipesPresenterImpl implements RecipesPresenter {

    private List<Recipe> mRecipes;
    private RecipesView mView;
    private RecipesInteractor mInteractor;

    public RecipesPresenterImpl(RecipesView view){
        mView = view;
        mRecipes = new ArrayList<>();
        mInteractor = new RecipesInteractorImpl(this);
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
