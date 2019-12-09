package com.icls.offlinekyc.function;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.icls.offlinekyc.R;

public class ContactUs extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle( "ABOUT US" );
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.content_on_contact_us, container, false);
    }
}
