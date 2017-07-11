package com.tapan.recipemaster;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

public class VideoDescriptionActivity extends AppCompatActivity {

    VideoDescriptionFragment videoDescriptionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            //Get full screen in landscape mode
            requestWindowFeature(Window.FEATURE_NO_TITLE); //Remove title bar
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//Remove notification bar
            ActionBar actionBar = getSupportActionBar();
            actionBar.hide();
        }else{
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setContentView(R.layout.activity_step_video_and_description);


        //creating a new fragment when any fragment does not exists
        if (savedInstanceState ==null){
            videoDescriptionFragment = new VideoDescriptionFragment();
            //Passing the data to the fragment
            videoDescriptionFragment.setArguments(getIntent().getExtras());

            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.fl_fragment_video_detail,videoDescriptionFragment).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
