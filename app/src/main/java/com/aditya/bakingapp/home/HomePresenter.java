package com.aditya.bakingapp.home;

import android.content.Context;

import com.aditya.bakingapp.object.Recipe;

import java.util.List;

/**
 * Created by ASUS A456U on 07/08/2017.
 */

public interface HomePresenter {

    void loadRecipes();

    void failLoadingRecipes(String message);

    List<Recipe> getRecipes();

    void setRecipes(List<Recipe> recipes);

    Recipe getRecipeAt(int index);

    void setActiveRecipeId(Context context, long id);
}
