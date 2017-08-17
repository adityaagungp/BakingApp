package com.aditya.bakingapp.recipe;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.aditya.bakingapp.R;
import com.aditya.bakingapp.object.Recipe;
import com.aditya.bakingapp.util.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeActivity extends AppCompatActivity {

    @BindView(R.id.masterContent)
    FrameLayout masterContent;
    @Nullable
    @BindView(R.id.childContent)
    FrameLayout childContent;

    private FragmentManager mFragmentManager;
    private boolean mTwoPane;
    private Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        mFragmentManager = getSupportFragmentManager();
        mTwoPane = (childContent != null);
        Intent intent = getIntent();
        if (intent.hasExtra(Constants.Param.RECIPE)) {
            mRecipe = intent.getParcelableExtra(Constants.Param.RECIPE);
            getSupportActionBar().setTitle(mRecipe.getName());
        }

        if (savedInstanceState == null){
            if (mRecipe != null){
                initRecipeDetailFragment();
                if (mTwoPane) initIngredientsFragment();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(Constants.Param.RECIPE, mRecipe);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);
        mRecipe = inState.getParcelable(Constants.Param.RECIPE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mTwoPane) {
            super.onBackPressed();
        } else {
            if (mFragmentManager.getBackStackEntryCount() > 0) {
                mFragmentManager.popBackStack();
            } else {
                super.onBackPressed();
            }
        }
    }

    public Recipe getRecipe() {
        return mRecipe;
    }

    public void showChildFragment(Fragment fragment) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.childContent, fragment);
        transaction.commit();
    }

    private void initRecipeDetailFragment() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.Param.TWO_PANE, mTwoPane);
        fragment.setArguments(bundle);
        transaction.replace(R.id.masterContent, fragment);
        transaction.commit();
    }

    private void initIngredientsFragment() {
        IngredientsFragment fragment = new IngredientsFragment();
        Bundle data = new Bundle();
        data.putBoolean(Constants.Param.TWO_PANE, mTwoPane);
        fragment.setArguments(data);
        showChildFragment(fragment);
    }
}
