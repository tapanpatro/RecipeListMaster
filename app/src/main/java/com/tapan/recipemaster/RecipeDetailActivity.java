package com.tapan.recipemaster;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

public class RecipeDetailActivity extends AppCompatActivity implements RecipeDetailFragment.DetailStepOnClickListener{


    Recipe recipe;
    RecipeDetailFragment recipeDetailFragment;
    VideoDescriptionFragment videoDescriptionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_recipe_detail);

        //geting the recipe obj from recipelistActivity
        recipe = getIntent().getParcelableExtra(getString(R.string.intent_recipe));
        ArrayList<Ingredient> ingredientArrayList = recipe.ingredientsArrayList;
        ArrayList<Step> stepsArrayList = recipe.stepsArrayList;

        Bundle bundle = new Bundle();

        //Setting the argument for recipedetailfragment with ingredient list and step list.
        bundle.putParcelableArrayList(getString(R.string.ingredient_extra),ingredientArrayList);
        bundle.putParcelableArrayList(getString(R.string.step_extra),stepsArrayList);
        recipeDetailFragment = new RecipeDetailFragment();
        recipeDetailFragment.setArguments(bundle);

        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fl_fragment_detail, recipeDetailFragment).commit();


        // When Two pane layout is true
        if (findViewById(R.id.fl_fragment_video_detail) !=null){
            videoDescriptionFragment = new VideoDescriptionFragment();
            Bundle bundleVDetails = new Bundle();
            bundleVDetails.putString(getString(R.string.video_url),stepsArrayList.get(0).videoUrl);
            bundleVDetails.putString(getString(R.string.description_url),stepsArrayList.get(0).description);
            bundleVDetails.putString(getString(R.string.thumb_url),stepsArrayList.get(0).thumbnailUrl);
            videoDescriptionFragment.setArguments(bundleVDetails);

            android.support.v4.app.FragmentTransaction videoFragmentTransaction = getSupportFragmentManager().beginTransaction();
            videoFragmentTransaction.add(R.id.fl_fragment_video_detail, videoDescriptionFragment).commit();
        }

    }


    @Override
    public void onDetailStepItemClicked(Bundle bundle) {
        if (findViewById(R.id.fl_fragment_video_detail) ==null){
            Intent intent = new Intent(this,VideoDescriptionActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }else{
            //for widerScreen
            videoDescriptionFragment = new VideoDescriptionFragment();
            videoDescriptionFragment.setArguments(bundle);
            android.support.v4.app.FragmentTransaction videoFragmentTransaction = getSupportFragmentManager().beginTransaction();
            videoFragmentTransaction.replace(R.id.fl_fragment_video_detail, videoDescriptionFragment).commit();
        }

    }
}
