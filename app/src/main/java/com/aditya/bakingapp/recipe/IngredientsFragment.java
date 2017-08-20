package com.aditya.bakingapp.recipe;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aditya.bakingapp.R;
import com.aditya.bakingapp.adapter.IngredientAdapter;
import com.aditya.bakingapp.object.Ingredient;
import com.aditya.bakingapp.object.Recipe;
import com.aditya.bakingapp.util.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsFragment extends Fragment {

    @BindView(R.id.ingredientList)
    RecyclerView ingredientList;
    @BindView(R.id.emptyText)
    TextView emptyText;

    private IngredientAdapter mAdapter;
    private String mRecipeName;
    private List<Ingredient> ingredients;
    private boolean mTwoPane;

    public IngredientsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ingredients, container, false);
        ButterKnife.bind(this, view);
        Bundle data = getArguments();
        mTwoPane = data.containsKey(Constants.Param.TWO_PANE) && data.getBoolean(Constants.Param.TWO_PANE);
        emptyText.setText(getString(R.string.empty_ingredient));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        ingredientList.setLayoutManager(layoutManager);
        mAdapter = new IngredientAdapter(getContext());
        ingredientList.setAdapter(mAdapter);
        DividerItemDecoration divider = new DividerItemDecoration(ingredientList.getContext(), layoutManager.getOrientation());
        ingredientList.addItemDecoration(divider);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RecipeActivity) {
            Recipe recipe = ((RecipeActivity) context).getRecipe();
            if (recipe != null) {
                mRecipeName = recipe.getName();
                ingredients = recipe.getIngredients();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<Ingredient> savedIngredients = new ArrayList<>();
        savedIngredients.addAll(ingredients);
        outState.putParcelableArrayList(Constants.Param.INGREDIENTS, savedIngredients);
        outState.putString(Constants.Param.RECIPE_NAME, mRecipeName);
        outState.putBoolean(Constants.Param.TWO_PANE, mTwoPane);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle inState) {
        super.onActivityCreated(inState);
        if (inState != null) {
            ArrayList<Ingredient> savedIngredients = inState.getParcelableArrayList(Constants.Param.INGREDIENTS);
            ingredients = new ArrayList<>();
            if (savedIngredients != null){
                ingredients.addAll(savedIngredients);
            }
            mRecipeName = inState.getString(Constants.Param.RECIPE_NAME);
            mTwoPane = inState.getBoolean(Constants.Param.TWO_PANE);
        }
        showIngredients(ingredients);
    }

    public static IngredientsFragment newInstance(boolean twoPane){
        IngredientsFragment fragment = new IngredientsFragment();
        Bundle data = new Bundle();
        data.putBoolean(Constants.Param.TWO_PANE, twoPane);
        fragment.setArguments(data);
        return fragment;
    }

    public void showIngredients(List<Ingredient> ingredients) {
        if (!mTwoPane) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(String.format(getString(R.string.ingredients_of), mRecipeName));
        }
        if (ingredients == null || ingredients.isEmpty()) {
            emptyText.setVisibility(View.VISIBLE);
        } else {
            mAdapter.setIngredients(ingredients);
            emptyText.setVisibility(View.GONE);
        }
    }
}
