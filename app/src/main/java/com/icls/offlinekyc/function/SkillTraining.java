package com.icls.offlinekyc.function;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.icls.offlinekyc.R;


public class SkillTraining extends Fragment {
View view;
    public SkillTraining() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate( R.layout.fragment_skill_training, container, false );
        getActivity().setTitle( "SKILL TRAINING" );
        return view ;  }

}
