package com.aditya.bakingapp.recipe;

import com.aditya.bakingapp.object.Ingredient;
import com.aditya.bakingapp.object.Recipe;
import com.aditya.bakingapp.object.Step;

import java.util.List;

/**
 * Created by ASUS A456U on 14/08/2017.
 */

public interface RecipePresenter {

    Recipe getRecipe();

    void setRecipe(Recipe recipe);

    List<Ingredient> getIngredients();

    Step getStep(int i);
}
