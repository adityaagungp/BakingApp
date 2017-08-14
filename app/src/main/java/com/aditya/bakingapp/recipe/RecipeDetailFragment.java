package com.aditya.bakingapp.recipe;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aditya.bakingapp.R;
import com.aditya.bakingapp.adapter.StepAdapter;
import com.aditya.bakingapp.view.ItemClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeDetailFragment extends Fragment implements ItemClickListener {

    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.emptyText)
    TextView emptyText;

    private StepAdapter mAdapter;

    public RecipeDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);
        emptyText.setText(getString(R.string.empty_step));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        list.setLayoutManager(layoutManager);
        mAdapter = new StepAdapter(getContext(), this);
        return view;
    }

    @Override
    public void onItemClick(int index) {

    }
}
