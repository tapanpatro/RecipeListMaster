package com.tapan.recipemaster.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tapan.recipemaster.R;
import com.tapan.recipemaster.model.Recipe;

import java.util.ArrayList;

/**
 * Created by hp on 7/11/2017.
 */

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeListViewHolder> {


    private Context context;
    private ArrayList<Recipe> recipeArrayList;
    private RecipeItemOnClickListenerInterface recipeItemOnClickListenerInterface;

    public RecipeListAdapter(Context context, ArrayList<Recipe> recipeArrayList, RecipeItemOnClickListenerInterface recipeItemOnClickListenerInterface) {
        this.context = context;
        this.recipeArrayList = recipeArrayList;
        this.recipeItemOnClickListenerInterface = recipeItemOnClickListenerInterface;
    }

    /* public RecipeListAdapter(ArrayList<Recipe> recipeArrayList,
                             RecipeItemOnClickListenerInterface recipeItemOnClickListenerInterface) {
        this.recipeArrayList = recipeArrayList;
        this.recipeItemOnClickListenerInterface = recipeItemOnClickListenerInterface;
    }*/

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
        }

    }


    @Override
    public RecipeListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.single_recipe_list_item, parent, false);
        return new RecipeListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeListViewHolder holder, int position) {

        final Recipe recipe = recipeArrayList.get(position);
        holder.recipeName.setText(recipe.name);

        if (!TextUtils.isEmpty(recipe.imageUrl)) {
            Picasso.with(context)
                    .load(recipe.imageUrl)
                    .centerCrop()
                    .placeholder(R.drawable.cupcake)
                    .into(holder.recipeImage);
        }

        holder.recipeServings.setText("Servings: "+String.valueOf(recipe.servings));

        holder.containerItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recipeItemOnClickListenerInterface.onRecipeItemClicked(recipe);
            }
        });

    }

    @Override
    public int getItemCount() {
        return recipeArrayList.size();
    }

    //Interface to handle onClickListener on the recyclerView
    public interface RecipeItemOnClickListenerInterface {
        void onRecipeItemClicked(Recipe recipe);
    }



}
