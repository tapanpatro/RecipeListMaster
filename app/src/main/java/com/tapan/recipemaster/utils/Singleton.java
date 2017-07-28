package com.tapan.recipemaster.utils;

/**
 * Created by hp on 7/27/2017.
 */

public class Singleton {

    //class refrence
    private static Singleton singleton = null;

    //holds pref obj
    private ExilePref exilePref = null;

    //Returns singleton
    public static Singleton getInstance(){

        if (singleton == null){
            singleton = new Singleton();
        }
        return singleton;
    }


    //return pref obj
    public ExilePref getPreference(){

        if (exilePref == null){
            exilePref = new ExilePref();
        }
        return exilePref;
    }

}
