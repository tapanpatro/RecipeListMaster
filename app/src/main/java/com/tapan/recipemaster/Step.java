package com.tapan.recipemaster;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hp on 7/11/2017.
 */

public class Step implements Parcelable {


    int stepId;
    String shortDescription;
    String description;
    String videoUrl;
    String thumbnailUrl;

    public Step(int stepId, String shortDescription , String description, String videoUrl , String thumbnailUrl){
        this.stepId = stepId;
        this.shortDescription=shortDescription;
        this.description=description;
        this.videoUrl=videoUrl;
        this.thumbnailUrl=thumbnailUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.stepId);
        dest.writeString(this.shortDescription);
        dest.writeString(this.description);
        dest.writeString(this.videoUrl);
        dest.writeString(this.thumbnailUrl);
    }

    protected Step(Parcel in) {
        this.stepId = in.readInt();
        this.shortDescription = in.readString();
        this.description = in.readString();
        this.videoUrl = in.readString();
        this.thumbnailUrl = in.readString();
    }

    public static final Parcelable.Creator<Step> CREATOR = new Parcelable.Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel source) {
            return new Step(source);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

}
