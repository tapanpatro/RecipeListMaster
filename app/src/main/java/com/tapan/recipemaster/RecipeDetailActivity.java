package com.tapan.recipemaster;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

public class RecipeDetailActivity extends AppCompatActivity implements IngredientsStepsFragment.DetailStepOnClickListener{


    Recipe recipe;
    IngredientsStepsFragment ingredientsStepsFragment;
    VideoDescriptionFragment videoDescriptionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_recipe_detail);

        recipe = getIntent().getParcelableExtra("Recipe");
        ArrayList<Ingredient> ingredientArrayList = recipe.ingredientsArrayList;
        ArrayList<Step> stepsArrayList = recipe.stepsArrayList;

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("Ingredients",ingredientArrayList);
        bundle.putParcelableArrayList("Steps",stepsArrayList);
        ingredientsStepsFragment = new IngredientsStepsFragment();
        ingredientsStepsFragment.setArguments(bundle);

        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fl_fragment_detail,ingredientsStepsFragment).commit();


        // When Two pane layout is true
        if (findViewById(R.id.fl_fragment_video_detail) !=null){
            videoDescriptionFragment = new VideoDescriptionFragment();
            Bundle bundleVDetails = new Bundle();
            bundleVDetails.putString(getString(R.string.video_url),stepsArrayList.get(0).videoUrl);
            bundleVDetails.putString(getString(R.string.steps_details),stepsArrayList.get(0).description);
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
            //for tablets
            videoDescriptionFragment = new VideoDescriptionFragment();
            videoDescriptionFragment.setArguments(bundle);
            android.support.v4.app.FragmentTransaction videoFragmentTransaction = getSupportFragmentManager().beginTransaction();
            videoFragmentTransaction.replace(R.id.fl_fragment_video_detail, videoDescriptionFragment).commit();
        }

    }
}
