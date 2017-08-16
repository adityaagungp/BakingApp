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

/**
 * A simple {@link Fragment} subclass.
 */
public class IngredientsFragment extends Fragment {

    @BindView(R.id.list)
    RecyclerView list;
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
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);
        Bundle data = getArguments();
        if (data.containsKey(Constants.Param.TWO_PANE)) {
            mTwoPane = data.getBoolean(Constants.Param.TWO_PANE);
        }
        emptyText.setText(getString(R.string.empty_ingredient));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        list.setLayoutManager(layoutManager);
        mAdapter = new IngredientAdapter(getContext());
        mAdapter.setIngredients(ingredients);
        list.setAdapter(mAdapter);
        DividerItemDecoration divider = new DividerItemDecoration(list.getContext(), layoutManager.getOrientation());
        list.addItemDecoration(divider);
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
            ingredients.addAll(savedIngredients);
            mRecipeName = inState.getString(Constants.Param.RECIPE_NAME);
            mTwoPane = inState.getBoolean(Constants.Param.TWO_PANE);
        }
        showIngredients(ingredients);
    }

    public void showIngredients(List<Ingredient> ingredients) {
        if (!mTwoPane) {
            StringBuilder builder = new StringBuilder();
            builder.append(getString(R.string.ingredients_of)).append(' ').append(mRecipeName);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(new String(builder));
        }
        if (ingredients == null || ingredients.isEmpty()) {
            list.setVisibility(View.GONE);
            emptyText.setVisibility(View.VISIBLE);
        } else {
            list.setVisibility(View.VISIBLE);
            emptyText.setVisibility(View.GONE);
        }
    }
}
