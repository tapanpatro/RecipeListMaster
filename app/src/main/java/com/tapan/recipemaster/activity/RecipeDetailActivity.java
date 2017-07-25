package com.tapan.recipemaster.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.tapan.recipemaster.R;
import com.tapan.recipemaster.fragment.RecipeDetailFragment;
import com.tapan.recipemaster.fragment.VideoDescriptFragment;
import com.tapan.recipemaster.model.Ingredient;
import com.tapan.recipemaster.model.Recipe;
import com.tapan.recipemaster.model.Step;

import java.util.ArrayList;

public class RecipeDetailActivity extends AppCompatActivity implements RecipeDetailFragment.DetailStepOnClickListener {


    Bundle bundle = new Bundle();

    public static final String EXTRA_PLANT_ID = "com.tapan.recipemaster.extra.RECIPE_ID";

    Recipe recipe;
    RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
    VideoDescriptFragment videoDescriptFragment = new VideoDescriptFragment();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_recipe_detail);

        //getting the recipe obj from recipelistActivity
        recipe = getIntent().getParcelableExtra(getString(R.string.intent_recipe));


        //Intent intent = getIntent();
        long widgetBundleId = getIntent().getLongExtra(EXTRA_PLANT_ID,0);
        Log.v("recipeBundle", String.valueOf(widgetBundleId));


        ArrayList<Ingredient> ingredientArrayList = recipe.ingredientsArrayList;
        ArrayList<Step> stepsArrayList = recipe.stepsArrayList;

        //Setting the argument for recipedetailfragment with ingredient list and step list.
        bundle.putParcelableArrayList(getString(R.string.ingredient_extra), ingredientArrayList);
        bundle.putParcelableArrayList(getString(R.string.step_extra), stepsArrayList);
        recipeDetailFragment.setArguments(bundle);

        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fl_fragment_detail, recipeDetailFragment).commit();


        // When Two pane layout is true
        if (findViewById(R.id.fl_fragment_video_detail) != null) {
            Bundle bundleVDetails = new Bundle();
            bundleVDetails.putString(getString(R.string.video_url), stepsArrayList.get(0).videoUrl);
            bundleVDetails.putString(getString(R.string.description_url), stepsArrayList.get(0).description);
            bundleVDetails.putString(getString(R.string.thumb_url), stepsArrayList.get(0).thumbnailUrl);
            videoDescriptFragment.setArguments(bundleVDetails);

            android.support.v4.app.FragmentTransaction videoFragmentTransaction = getSupportFragmentManager().beginTransaction();
            videoFragmentTransaction.add(R.id.fl_fragment_video_detail, videoDescriptFragment).commit();
        }

    }


    @Override
    public void onDetailStepItemClicked(Bundle bundle) {
        if (findViewById(R.id.fl_fragment_video_detail) == null) {
            Intent intent = new Intent(this, VideoDescriptActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else {
            //for widerScreen
            videoDescriptFragment.setArguments(bundle);
            android.support.v4.app.FragmentTransaction videoFragmentTransaction = getSupportFragmentManager().beginTransaction();
            videoFragmentTransaction.replace(R.id.fl_fragment_video_detail, videoDescriptFragment).commit();
        }

    }
}
