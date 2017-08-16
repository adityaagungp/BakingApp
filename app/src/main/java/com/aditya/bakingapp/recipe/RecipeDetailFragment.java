package com.aditya.bakingapp.recipe;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.aditya.bakingapp.R;
import com.aditya.bakingapp.adapter.StepAdapter;
import com.aditya.bakingapp.object.Recipe;
import com.aditya.bakingapp.object.Step;
import com.aditya.bakingapp.util.Constants;
import com.aditya.bakingapp.view.ItemClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailFragment extends Fragment implements ItemClickListener, View.OnClickListener {

    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.emptyText)
    TextView emptyText;

    private StepAdapter mAdapter;
    private Recipe mRecipe;
    private boolean mTwoPane;

    public RecipeDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        Bundle data = getArguments();
        if (data.containsKey(Constants.Param.TWO_PANE)) {
            mTwoPane = data.getBoolean(Constants.Param.TWO_PANE);
        } else {
            mTwoPane = false;
        }
        ButterKnife.bind(this, view);
        emptyText.setText(getString(R.string.empty_step));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        list.setLayoutManager(layoutManager);
        mAdapter = new StepAdapter(getContext(), this);
        list.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RecipeActivity) {
            mRecipe = ((RecipeActivity) context).getRecipe();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Constants.Param.RECIPE, mRecipe);
        outState.putBoolean(Constants.Param.TWO_PANE, mTwoPane);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle inState) {
        super.onActivityCreated(inState);
        if (inState != null) {
            mRecipe = inState.getParcelable(Constants.Param.RECIPE);
            mTwoPane = inState.getBoolean(Constants.Param.TWO_PANE);
        }
        showRecipe(mRecipe);
    }

    @Override
    public void onItemClick(int index) {
        Step step = mRecipe.getSteps().get(index);
        if (step != null) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            StepDetailFragment fragment = new StepDetailFragment();
            Bundle data = new Bundle();
            data.putParcelable(Constants.Param.STEP, step);
            data.putBoolean(Constants.Param.TWO_PANE, mTwoPane);
            fragment.setArguments(data);
            if (mTwoPane) {
                transaction.replace(R.id.childContent, fragment);
            } else {
                transaction.replace(R.id.masterContent, fragment);
                transaction.addToBackStack(null);
            }
            transaction.commit();
        }
    }

    public void showRecipe(Recipe recipe) {
        if (!mTwoPane) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(mRecipe.getName());
        }
        if (recipe.getIngredients() != null && !recipe.getIngredients().isEmpty()) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.header_steps, null, false);
            Button btnSeeIngredients = (Button) view.findViewById(R.id.btnSeeIngredients);
            btnSeeIngredients.setOnClickListener(this);
            mAdapter.setHeader(view);
        }
        mAdapter.setSteps(recipe.getSteps());
        if (recipe.getSteps().isEmpty()) {
            emptyText.setVisibility(View.VISIBLE);
        } else {
            emptyText.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        IngredientsFragment fragment = new IngredientsFragment();
        Bundle data = new Bundle();
        data.putBoolean(Constants.Param.TWO_PANE, mTwoPane);
        fragment.setArguments(data);
        if (mTwoPane) {
            transaction.add(R.id.childContent, fragment);
        } else {
            transaction.add(R.id.masterContent, fragment);
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }
}
