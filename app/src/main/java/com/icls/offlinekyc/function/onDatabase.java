package com.icls.offlinekyc.function;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.icls.offlinekyc.Database.Repository;
import com.icls.offlinekyc.R;
import com.icls.offlinekyc.function.Models.NotificationPenidingModel;

import java.util.ArrayList;

public class onDatabase extends Fragment {


    // for debugging
    private static final String TAG = "onDatabase";
    // for recycler view notification----------
    //private ArrayList<String> mNames = new ArrayList<>();
    //--------------------------------------------
    public static ArrayList<NotificationPenidingModel> notificationlist = new ArrayList<NotificationPenidingModel>();
    private static Handler mhandler = new Handler();
    public RecyclerView recyclerView;
    public String NumberCheckResponse;
    public String PendingReqRespo;
    View view = getView();
    Button dbCreate, btn_reject;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Notification");
        try {
            view = inflater.inflate(R.layout.activity_on_database, container, false);
            dbCreate = view.findViewById(R.id.createDB);
            dbCreate.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View view) {
                    Repository createDB = new Repository(getContext());
                    createDB.insertTaskmembertypemaster("11",
                            "101",
                            "Hello DB",
                            "05",
                            "IT", "Software Engg.",
                            "I am an Engg.", "Vasu",
                            0);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }


}