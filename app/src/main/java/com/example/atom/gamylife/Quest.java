package com.example.atom.gamylife;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Atom on 06/04/2017.
 */

public class Quest implements Parcelable {

    private String name;

    public void Quest(String name) {

        this.name = name;
    }

    public Quest(Parcel in) {

        String data [] = new String[1];

        in.readStringArray(data);
        name = data[0];
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        public Quest createFromParcel(Parcel in) {

            return new Quest(in);
        }

        public Quest[] newArray(int size) {

            return new Quest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
