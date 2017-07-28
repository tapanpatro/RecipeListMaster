package com.tapan.recipemaster.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by hp on 7/27/2017.
 */

public class ExilePref {

    private final String UNIQUE_ID = "UNIQUE_ID";

    public void saveData(Context context, String hashMapString){
        SharedPreferences profileState = context.getSharedPreferences(UNIQUE_ID,0);
        SharedPreferences.Editor editor = profileState.edit();
        editor.putString("data", hashMapString);
        editor.apply();
    }


    public String getData(Context context){
        SharedPreferences profileState = context.getSharedPreferences(UNIQUE_ID,0);
        return profileState.getString("data","");
    }

}
