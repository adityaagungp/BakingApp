package com.aditya.bakingapp.recipe;

import com.aditya.bakingapp.object.Ingredient;
import com.aditya.bakingapp.object.Recipe;
import com.aditya.bakingapp.object.Step;

import java.util.List;

/**
 * Created by ASUS A456U on 14/08/2017.
 */

public class RecipePresenterImpl implements RecipePresenter {

    private Recipe mRecipe;
    private RecipeView mView;

    public RecipePresenterImpl(RecipeView view){
        mView = view;
    }

    @Override
    public Recipe getRecipe() {
        return mRecipe;
    }

    @Override
    public void setRecipe(Recipe recipe) {
        mRecipe = recipe;
        mView.showRecipe(recipe);
    }

    @Override
    public List<Ingredient> getIngredients() {
        try {
            return mRecipe.getIngredients();
        } catch (NullPointerException e){
            return null;
        }
    }

    @Override
    public Step getStep(int i) {
        try {
            return mRecipe.getSteps().get(i);
        } catch (NullPointerException | IndexOutOfBoundsException e){
            return null;
        }
    }
}
