package com.tapan.recipemaster;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by hp on 7/11/2017.
 */

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeListViewHolder> {


    private ArrayList<Recipe> recipeArrayList;
    private RecipeItemOnClickListenerInteface recipeItemOnClickListenerInteface;

    public RecipeListAdapter(ArrayList<Recipe> recipeArrayList, RecipeItemOnClickListenerInteface recipeItemOnClickListenerInteface) {
        this.recipeArrayList = recipeArrayList;
        this.recipeItemOnClickListenerInteface = recipeItemOnClickListenerInteface;
    }

    @Override
    public RecipeListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.single_recipe_list_item, parent, false);
        return new RecipeListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeListViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return recipeArrayList.size();
    }

    //Interface to handle onClickListener on the recyclerview
    public interface RecipeItemOnClickListenerInteface {
        void onRecipeItemClicked(Recipe recipe);
    }


    public class RecipeListViewHolder extends RecyclerView.ViewHolder {

        ImageView recipeImage;
        TextView recipeName;
        TextView recipeServings;
        LinearLayout containerItem;

        public RecipeListViewHolder(View view) {
            super(view);
            recipeImage = (ImageView) view.findViewById(R.id.img_recipe);
            recipeName = (TextView) view.findViewById(R.id.tv_recipe_name);
            recipeServings = (TextView) view.findViewById(R.id.tv_servings);
            containerItem = (LinearLayout) view.findViewById(R.id.ll_contain_recipe_item);

            containerItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Recipe recipe = recipeArrayList.get(position);
                    recipeItemOnClickListenerInteface.onRecipeItemClicked(recipe);
                }
            });
        }

        public void bind(int position) {
            int recipeId = recipeArrayList.get(position).recipeId;
            String name = recipeArrayList.get(position).name;
            //recipeName.setPaintFlags(recipeName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            recipeName.setText(name);

            String servingsString = "Servings " + recipeArrayList.get(position).servings ;
            recipeServings.setText(servingsString);
        }

    }
}
