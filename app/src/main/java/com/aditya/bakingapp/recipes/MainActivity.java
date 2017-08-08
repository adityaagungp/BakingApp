package com.aditya.bakingapp.recipes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aditya.bakingapp.R;
import com.aditya.bakingapp.object.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RecipesView{

    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.emptyText) TextView emptyText;
    @BindView(R.id.list) RecyclerView list;

    private RecipesPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    public void onBeforeGetRecipes() {

    }

    @Override
    public void onLoadingRecipes() {
        presenter.loadRecipes();
    }

    @Override
    public void onShowRecipes(List<Recipe> recipes) {

    }
}
