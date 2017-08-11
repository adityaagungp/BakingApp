package com.aditya.bakingapp.home;

import com.aditya.bakingapp.object.Recipe;

import java.util.List;

/**
 * Created by ASUS A456U on 07/08/2017.
 */

public interface HomeView {

    void onShowRecipes(List<Recipe> recipes);

    void onErrorLoadingRecipes(String message);
}
