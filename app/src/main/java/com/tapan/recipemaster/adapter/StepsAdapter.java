package com.tapan.recipemaster.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tapan.recipemaster.R;
import com.tapan.recipemaster.model.Step;

import java.util.ArrayList;

/**
 * Created by hp on 7/11/2017.
 */

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.ViewHolder> {

    private ArrayList<Step> stepArrayList;
    private StepsItemOnClickListener stepsItemOnClickListener;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextShortDescription;
        public ViewHolder(View view){
            super(view);
            mTextShortDescription = (TextView) view.findViewById(R.id.tv_steps);
        }
    }

    public StepsAdapter(ArrayList<Step> stepArrayList, StepsItemOnClickListener stepsItemOnClickListener){
        this.stepArrayList= stepArrayList;
        this.stepsItemOnClickListener = stepsItemOnClickListener;
    }

    @Override
    public StepsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.single_item_step, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Step step = stepArrayList.get(position);
        holder.mTextShortDescription.setText(step.stepDescription);
        holder.mTextShortDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stepsItemOnClickListener.onStepItemClicked(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stepArrayList.size();
    }

    public interface StepsItemOnClickListener {
        void onStepItemClicked(int position);
    }

}
