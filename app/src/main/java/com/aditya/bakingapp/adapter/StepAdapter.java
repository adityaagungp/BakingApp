package com.aditya.bakingapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aditya.bakingapp.R;
import com.aditya.bakingapp.object.Step;
import com.aditya.bakingapp.view.ItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ASUS A456U on 09/08/2017.
 */

public class StepAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_HEADER = 0;
    private static final int VIEW_STEP = 1;

    private Context context;
    private View header;
    private List<Step> steps = new ArrayList<>();
    private ItemClickListener listener;

    public StepAdapter(Context context, ItemClickListener listener){
        this.context = context;
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position){
        if (header != null && position == 0)
            return VIEW_HEADER;
        else
            return VIEW_STEP;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_HEADER){
            return new HeaderHolder(header);
        } else {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.item_step, parent, false);
            return new StepViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (header == null || position != 0){
            ((StepViewHolder) holder).bind(steps.get(getStepPosition(position)));
        }
    }

    @Override
    public int getItemCount() {
        return (header == null ? 0 : 1) + steps.size();
    }

    public void setHeader(View v){
        header = v;
        notifyDataSetChanged();
    }

    public void setSteps(List<Step> steps){
        this.steps = steps;
        notifyDataSetChanged();
    }

    private int getStepPosition(int position){
        int offset = (header == null) ? 0 : 1;
        return position - offset;
    }

    class HeaderHolder extends RecyclerView.ViewHolder {
        HeaderHolder(View itemView){
            super(itemView);
        }
    }

    class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.shortDescription) TextView shortDescription;

        StepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(Step step){
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(step.getId()).append(' ').append(step.getShortDescription());
            shortDescription.setText(new String(stringBuilder));
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getStepPosition(getAdapterPosition());
            listener.onItemClick(clickedPosition);
        }
    }
}
