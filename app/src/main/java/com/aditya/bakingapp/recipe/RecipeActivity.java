package com.aditya.bakingapp.recipe;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.aditya.bakingapp.R;
import com.aditya.bakingapp.object.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeActivity extends AppCompatActivity {

    @BindView(R.id.masterContent) FrameLayout masterContent;
    @BindView(R.id.childContent) FrameLayout childContent;

    private FragmentManager mFragmentManager;
    private Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        ButterKnife.bind(this);
    }
}
