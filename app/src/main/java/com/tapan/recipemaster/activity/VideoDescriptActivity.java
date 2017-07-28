package com.tapan.recipemaster.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.tapan.recipemaster.R;
import com.tapan.recipemaster.fragment.VideoDescriptFragment;

public class VideoDescriptActivity extends AppCompatActivity implements VideoDescriptFragment.NextButtonClickListner{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getSupportActionBar().hide();
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setContentView(R.layout.activity_video_descript);


        //creating a new fragment when any fragment does not exists
        if (savedInstanceState == null) {
            //Passing the data to the fragment
            VideoDescriptFragment videoDescriptFragment = new VideoDescriptFragment();
            videoDescriptFragment.setArguments(getIntent().getExtras());
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.fl_fragment_video_detail, videoDescriptFragment).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNextButtonClick(Bundle bundle) {
        VideoDescriptFragment videoDescriptFragment = new VideoDescriptFragment();
        videoDescriptFragment.setArguments(bundle);
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.fl_fragment_video_detail, videoDescriptFragment).commit();

    }
}
