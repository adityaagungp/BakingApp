package com.aditya.bakingapp.api;

import com.aditya.bakingapp.BuildConfig;
import com.aditya.bakingapp.object.Recipe;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ASUS A456U on 07/08/2017.
 */

public class APIRequest {

    private static APIRequest instance = new APIRequest();

    private OkHttpClient httpClient = new OkHttpClient.Builder().connectTimeout(3, TimeUnit.SECONDS).build();

    private Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    private Retrofit retrofit = new Retrofit.Builder()
            .client(httpClient)
            .baseUrl(BuildConfig.API_BASE)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    private APIService service = retrofit.create(APIService.class);

    APIRequest(){

    }

    public static APIRequest getInstance(){
        return instance;
    }

    public void getRecipes(Callback<List<Recipe>> callback){
        service.getRecipes().enqueue(callback);
    }
}
