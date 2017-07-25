package com.tapan.recipemaster.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tapan.recipemaster.R;
import com.tapan.recipemaster.model.Ingredient;

import java.util.ArrayList;

/**
 * Created by hp on 7/11/2017.
 */

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {

    private ArrayList<Ingredient> ingredientArrayList;

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView mTextIngName;
        private ViewHolder(View view){
            super(view);
            mTextIngName = (TextView) view.findViewById(R.id.tv_ingrident);
        }
    }

    public IngredientsAdapter(ArrayList<Ingredient> ingredientArrayList){
        this.ingredientArrayList = ingredientArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.single_item_ingredient, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Ingredient ingredient = ingredientArrayList.get(position);
        holder.mTextIngName.setText("."+ingredient.ingredients+ "("+
                ingredient.quantity + " "+ ingredient.measure
                +")");
    }

    @Override
    public int getItemCount() {
        return ingredientArrayList.size();
    }

}
