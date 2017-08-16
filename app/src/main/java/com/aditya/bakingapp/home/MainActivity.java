package com.aditya.bakingapp.home;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aditya.bakingapp.R;
import com.aditya.bakingapp.adapter.RecipeAdapter;
import com.aditya.bakingapp.object.Recipe;
import com.aditya.bakingapp.recipe.RecipeActivity;
import com.aditya.bakingapp.util.Constants;
import com.aditya.bakingapp.util.NetworkUtils;
import com.aditya.bakingapp.view.ItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements HomeView, ItemClickListener {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.emptyText)
    TextView emptyText;
    @BindView(R.id.list)
    RecyclerView list;

    private HomePresenter mPresenter;
    private RecipeAdapter mAdapter;
    private int mNumberColumn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mPresenter = new HomePresenterImpl(this);
        mAdapter = new RecipeAdapter(this, this);
        mAdapter.setRecipes(mPresenter.getRecipes());

        if (savedInstanceState != null && savedInstanceState.containsKey(Constants.Param.RECIPES)) {
            ArrayList<Recipe> recipes = savedInstanceState.getParcelableArrayList(Constants.Param.RECIPES);
            mPresenter.setRecipes(recipes);
        }
        if (mPresenter.getRecipes() == null || mPresenter.getRecipes().isEmpty()) {
            fetchRecipes();
        }

        mNumberColumn = list.getTag().equals("true") ? 2 : 1;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            mNumberColumn += 1;
        }
        GridLayoutManager layoutManager = new GridLayoutManager(this, mNumberColumn);
        list.setLayoutManager(layoutManager);
        list.setAdapter(mAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
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
        Recipe recipe = mPresenter.getRecipeAt(index);
        if (recipe != null) {
            toRecipeDetail(recipe);
        }
    }

    private void fetchRecipes() {
        if (NetworkUtils.isOnline(this)) {
            onBeforeGetRecipes();
            mPresenter.loadRecipes();
        } else {
            onErrorLoadingRecipes(getString(R.string.no_internet));
        }
    }

    private void showRecipes(List<Recipe> recipes) {
        if (recipes == null || recipes.isEmpty()) {
            emptyText.setVisibility(View.VISIBLE);
            list.setVisibility(View.INVISIBLE);
        } else {
            emptyText.setVisibility(View.INVISIBLE);
            list.setVisibility(View.VISIBLE);
        }
    }

    private void toRecipeDetail(Recipe recipe) {
        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra(Constants.Param.RECIPE, recipe);
        startActivity(intent);
    }
}
