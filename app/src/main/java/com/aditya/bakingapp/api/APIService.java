package com.aditya.bakingapp.api;

import com.aditya.bakingapp.object.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

interface APIService {

    @GET("baking.json")
    Call<List<Recipe>> getRecipes();
}
