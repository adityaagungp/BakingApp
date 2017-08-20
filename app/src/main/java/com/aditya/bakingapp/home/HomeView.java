package com.aditya.bakingapp.home;

import com.aditya.bakingapp.object.Recipe;

import java.util.List;

interface HomeView {

    void onShowRecipes(List<Recipe> recipes);

    void onErrorLoadingRecipes(String message);
}
