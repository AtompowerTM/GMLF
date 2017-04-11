package com.example.atom.gamylife;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Atom on 11/04/2017.
 */

public class QuestsFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        // Declare your first fragment here


        return inflater.inflate(R.layout.activity_quests_fragment, viewGroup, false);
    }
}
