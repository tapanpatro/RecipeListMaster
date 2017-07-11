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

public class IngredientsRecyclerViewAdapter extends RecyclerView.Adapter<IngredientsRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Ingredient> ingredientArrayList;

    public IngredientsRecyclerViewAdapter(ArrayList<Ingredient> ingredientArrayList){
        this.ingredientArrayList=ingredientArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.single_item_ingredient,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return ingredientArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView mTextIngName;

        public ViewHolder(View view){
            super(view);
            mTextIngName = (TextView) view.findViewById(R.id.tv_ingrident);
        }

        public void bind(int position) {
            String quantity = ingredientArrayList.get(position).quantity + " "+ ingredientArrayList.get(position).measure;
            mTextIngName.setText("   ."+ingredientArrayList.get(position).ingredients+ quantity);
        }
    }

}
