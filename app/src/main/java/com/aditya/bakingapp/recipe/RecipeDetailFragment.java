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

    @BindView(R.id.stepList)
    RecyclerView stepList;
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
        View view = inflater.inflate(R.layout.fragment_steps, container, false);
        Bundle data = getArguments();
        mTwoPane = data.containsKey(Constants.Param.TWO_PANE) && data.getBoolean(Constants.Param.TWO_PANE);
        ButterKnife.bind(this, view);
        emptyText.setText(getString(R.string.empty_step));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        stepList.setLayoutManager(layoutManager);
        mAdapter = new StepAdapter(getContext(), this);
        stepList.setAdapter(mAdapter);
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
            StepDetailFragment fragment = StepDetailFragment.newInstance(step, mTwoPane);
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            if (mTwoPane) {
                transaction.replace(R.id.childContent, fragment);
            } else {
                transaction.replace(R.id.masterContent, fragment);
                transaction.addToBackStack(null);
            }
            transaction.commit();
        }
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        IngredientsFragment fragment = IngredientsFragment.newInstance(mTwoPane);
        if (mTwoPane) {
            transaction.add(R.id.childContent, fragment);
        } else {
            transaction.add(R.id.masterContent, fragment);
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    public static RecipeDetailFragment newInstance(boolean twoPane){
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        Bundle data = new Bundle();
        data.putBoolean(Constants.Param.TWO_PANE, twoPane);
        fragment.setArguments(data);
        return fragment;
    }

    private void showRecipe(Recipe recipe) {
        if (!mTwoPane) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(mRecipe.getName());
        }
        View view = LayoutInflater.from(getContext()).inflate(R.layout.header_steps, null, false);
        Button btnSeeIngredients = (Button) view.findViewById(R.id.btnSeeIngredients);
        btnSeeIngredients.setOnClickListener(this);
        mAdapter.setHeader(view);
        mAdapter.setSteps(recipe.getSteps());
        if (recipe.getSteps().isEmpty()) {
            emptyText.setVisibility(View.VISIBLE);
        } else {
            emptyText.setVisibility(View.GONE);
        }
    }
}
