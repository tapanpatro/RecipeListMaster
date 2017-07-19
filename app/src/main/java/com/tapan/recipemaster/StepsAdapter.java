package com.tapan.recipemaster;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by hp on 7/11/2017.
 */

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.ViewHolder> {

    private ArrayList<Step> stepArrayList;
    private StepsOnClickListener stepsOnClickListener;

    public StepsAdapter(ArrayList<Step> stepArrayList, StepsOnClickListener stepsOnClickListener){
        this.stepArrayList= stepArrayList;
        this.stepsOnClickListener = stepsOnClickListener;
    }

    @Override
    public StepsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.single_item_step,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return stepArrayList.size();
    }

    public interface StepsOnClickListener{
        void onStepItemClicked(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView stepShortDescription;

        public ViewHolder(View view){
            super(view);
            stepShortDescription = (TextView) view.findViewById(R.id.tv_steps);
            stepShortDescription.setOnClickListener(this);
        }

        public void bind(int position) {
            stepShortDescription.setText(stepArrayList.get(position).shortDescription);
        }

        @Override
        public void onClick(View v) {
            stepsOnClickListener.onStepItemClicked(getAdapterPosition());
        }
    }

}
