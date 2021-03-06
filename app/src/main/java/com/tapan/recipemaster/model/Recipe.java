package com.tapan.recipemaster.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by hp on 7/11/2017.
 */

public class Recipe implements Parcelable {

    public int recipeId;
    public String name;
    public String imageUrl;
    public ArrayList<Ingredient> ingredientsArrayList;
    public ArrayList<Step> stepsArrayList;
    public int servings;


    public Recipe(int recipeId, String name, String imageUrl, ArrayList<Ingredient> ingredientsArrayList, ArrayList<Step> stepsArrayList, int servings) {
        this.recipeId = recipeId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.ingredientsArrayList = ingredientsArrayList;
        this.stepsArrayList = stepsArrayList;
        this.servings = servings;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.recipeId);
        dest.writeString(this.name);
        dest.writeString(this.imageUrl);
        dest.writeTypedList(this.ingredientsArrayList);
        dest.writeTypedList(this.stepsArrayList);
        dest.writeInt(this.servings);
    }

    protected Recipe(Parcel in) {
        this.recipeId = in.readInt();
        this.name = in.readString();
        this.imageUrl = in.readString();
        this.ingredientsArrayList = in.createTypedArrayList(Ingredient.CREATOR);
        this.stepsArrayList = in.createTypedArrayList(Step.CREATOR);
        this.servings = in.readInt();
    }

    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel source) {
            return new Recipe(source);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

}
