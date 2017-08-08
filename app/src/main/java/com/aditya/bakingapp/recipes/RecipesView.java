package com.aditya.bakingapp.recipes;

import com.aditya.bakingapp.object.Recipe;

import java.util.List;

/**
 * Created by ASUS A456U on 07/08/2017.
 */

public interface RecipesView {

    void onBeforeGetRecipes();

    void onLoadingRecipes();

    void onShowRecipes(List<Recipe> recipes);
}
