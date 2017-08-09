package com.aditya.bakingapp.recipes;

import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aditya.bakingapp.R;
import com.aditya.bakingapp.adapter.RecipeAdapter;
import com.aditya.bakingapp.object.Recipe;
import com.aditya.bakingapp.util.Constants;
import com.aditya.bakingapp.util.NetworkUtils;
import com.aditya.bakingapp.view.ItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RecipesView, ItemClickListener{

    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.emptyText) TextView emptyText;
    @BindView(R.id.list) RecyclerView list;

    private RecipesPresenter mPresenter;
    private RecipeAdapter mAdapter;
    private int mNumberColumn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mPresenter = new RecipesPresenterImpl(this);
        mAdapter = new RecipeAdapter(this, this);
        mAdapter.setRecipes(mPresenter.getRecipes());

        if (savedInstanceState !=null && savedInstanceState.containsKey(Constants.Param.RECIPES)){
            ArrayList<Recipe> recipes = savedInstanceState.getParcelableArrayList(Constants.Param.RECIPES);
            mPresenter.setRecipes(recipes);
        }

        fetchRecipes();

        //TODO: Modify number of columns for specific width screen
        mNumberColumn = 1;
        GridLayoutManager layoutManager = new GridLayoutManager(this, mNumberColumn);
        list.setLayoutManager(layoutManager);
        list.setAdapter(mAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putParcelableArrayList(Constants.Param.RECIPES, (ArrayList<? extends Parcelable>) mPresenter.getRecipes());
        super.onSaveInstanceState(outState);
    }

    public void onBeforeGetRecipes() {
        list.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        emptyText.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onShowRecipes(List<Recipe> recipes) {
        progressBar.setVisibility(View.INVISIBLE);
        mAdapter.setRecipes(recipes);
        showRecipes(recipes);
    }

    @Override
    public void onErrorLoadingRecipes(String message) {
        progressBar.setVisibility(View.INVISIBLE);
        showRecipes(mPresenter.getRecipes());
        Snackbar.make(getWindow().getDecorView().getRootView(), message, Snackbar.LENGTH_LONG);
    }

    @Override
    public void onItemClick(int index) {

    }

    private void fetchRecipes(){
        if (NetworkUtils.isOnline(this)){
            onBeforeGetRecipes();
            mPresenter.loadRecipes();
        } else {
            onErrorLoadingRecipes(getString(R.string.no_internet));
        }
    }

    private void showRecipes(List<Recipe> recipes){
        if (recipes == null || recipes.isEmpty()){
            emptyText.setVisibility(View.VISIBLE);
            list.setVisibility(View.INVISIBLE);
        } else {
            emptyText.setVisibility(View.INVISIBLE);
            list.setVisibility(View.VISIBLE);
        }
    }
}
