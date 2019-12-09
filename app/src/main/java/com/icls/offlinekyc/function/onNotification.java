package com.icls.offlinekyc.function;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.icls.offlinekyc.Database.OrganizationTypeParam;
import com.icls.offlinekyc.R;
import com.icls.offlinekyc.adapters.NotificationAdapter;
import com.icls.offlinekyc.adapters.phapacommunityAdapter;
import com.icls.offlinekyc.commonshare.common;
import com.icls.offlinekyc.function.Models.NotificationPenidingModel;
import com.icls.offlinekyc.helper.MasterNotification;
import com.icls.offlinekyc.roomdb.OrganizationRepository;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class onNotification extends Fragment implements NotificationAdapter.ItemClickListener {

    public NotificationAdapter adapter;
    ArrayList<String> notificationArrayTitle;
    ArrayList<String> notificationArrayNotificationID;
    ArrayList<String> notificationArrayReplyAllowed;
    ArrayList<String> notificationArrayDescription;
    ArrayList<String> notificationOrgIdfk;
    ArrayList<String> notificationMsgTo;
    TextView noNotifications;
    // for debugging
    private static final String TAG = "onNotification";


    private static Handler mhandler =new Handler();
    View view=getView();
    //--------------------------------------------
    public static ArrayList<NotificationPenidingModel> notificationlist= new ArrayList<NotificationPenidingModel>(  );
    public RecyclerView recyclerView;
    Button btn_accept,btn_reject;
    private Handler mHandler;
    private List<Integer> msglistCount;

    public String NumberCheckResponse;
    public String PendingReqRespo;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences( "LoginDetails", MODE_PRIVATE );
        String PhNO=sharedPreferences.getString( "PhoneNo","" );



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        common.PRESENTSCREEN = "";
        msglistCount.clear();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle( "NOTIFICATION" );
        common.PRESENTSCREEN = "notification";
        try {
            view=inflater.inflate(R.layout.activity_on_notification, container, false);


            initRecyclerView();
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("RealIDNumber", MODE_PRIVATE);
            String RealID =sharedPreferences.getString("RealID", "");

        } catch (Exception e) {
            e.printStackTrace();
        }



        init();
        onSubmit();
        RecyclerView recyclerView =  (RecyclerView) view.findViewById(R.id.notificationRecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        if(common.PRESENTSCREEN.equalsIgnoreCase(  "notification")){
            runeveryFivesecUntillDestoyed();
        }
        // Inflate the layout for this fragment
        return view;
    }

    private void runeveryFivesecUntillDestoyed() {
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            public void run() {
                //Do Something
                Log.d( TAG, "im running every 5 sec" );
                // do your stuff
                if(common.PRESENTSCREEN.equalsIgnoreCase("notification" )){
                    Log.d( TAG, "im running every 5 sec for notificationchat" );
                    getNotifications1();

                }
                mHandler.postDelayed(this, 5000);
            }
        }, 5000);
    }

    private void getNotifications1() {
        final OrganizationRepository noteRepository = new OrganizationRepository(getContext());

        noteRepository.getMasterNotification().observe( onNotification.this, notes -> {
            notificationArrayTitle=new ArrayList<>(  );
            notificationArrayDescription=new ArrayList<>(  );
            notificationArrayNotificationID=new ArrayList<>(  );
            notificationArrayReplyAllowed= new ArrayList<>(  );
            notificationOrgIdfk= new ArrayList<>(  );
            notificationMsgTo= new ArrayList<>(  );

            notificationArrayTitle.clear();
            notificationArrayDescription.clear();
            notificationArrayNotificationID.clear();
            notificationArrayReplyAllowed.clear();
            notificationOrgIdfk.clear();
            notificationMsgTo.clear();

            for (MasterNotification note : notes) {
                notificationArrayTitle.add( note.getOrganization_name() );
                notificationArrayDescription.add( note.getMessage_subject() );
                notificationArrayNotificationID.add( note.getNotification_id() );
                notificationArrayReplyAllowed.add( note.getReply_allowed() );
                notificationOrgIdfk.add( note.getOrganization_idfk() );
                notificationMsgTo.add( note.getMessage_from() );
            }
                    msglistCount.add( notificationArrayTitle.size() ) ;

                    int last =msglistCount.get( msglistCount.size()-1) ;
                    int lastbutone =  msglistCount.get(  msglistCount.size()-2);
                    Log.d( TAG, "openNotificationChat1: "+" "+notificationArrayTitle.size()+" "+last+" " +lastbutone );
                    if (last!=lastbutone) {
                        if (notes.size()!=0) {
                            recyclerView.setVisibility( View.VISIBLE );
                            noNotifications.setVisibility( View.GONE );

                            getActivity().runOnUiThread( () -> {
                                adapter = new NotificationAdapter(view.getContext(),
                                        notificationArrayTitle,
                                        notificationArrayDescription,
                                        notificationArrayNotificationID,
                                        notificationArrayReplyAllowed,
                                        notificationOrgIdfk,
                                        notificationMsgTo);
                                adapter.setClickListener( onNotification.this);
                                recyclerView.setAdapter(adapter);
                            } );
                        } else {
                            recyclerView.setVisibility( View.GONE );
                            noNotifications.setVisibility( View.VISIBLE);
                        }


                    }
            noteRepository.closeDB();



        } );


    }


    // method to setting up recycler view.
    private void initRecyclerView(){
        noNotifications = view.findViewById( R.id.noNotifications );

        recyclerView = view.findViewById( R.id.notificationRecyclerview );
        recyclerView.setLayoutManager( new LinearLayoutManager( getContext() ) );


    }
    private void onSubmit() {

    }




    private void init() {
        msglistCount = new ArrayList<>(  );
        msglistCount.add( 0 );
        msglistCount.add( 0 );
        btn_accept=view.findViewById( R.id.btn_accept );
        btn_reject=view.findViewById( R.id.btn_reject );
        getNotifications();
    }


    public void getNotifications() {
        final OrganizationRepository noteRepository = new OrganizationRepository(getContext());

        noteRepository.getMasterNotification().observe( onNotification.this, notes -> {
            notificationArrayTitle=new ArrayList<>(  );
            notificationArrayDescription=new ArrayList<>(  );
            notificationArrayNotificationID=new ArrayList<>(  );
            notificationArrayReplyAllowed= new ArrayList<>(  );
            notificationOrgIdfk= new ArrayList<>(  );
            notificationMsgTo= new ArrayList<>(  );
            for (MasterNotification note : notes) {
                notificationArrayTitle.add( note.getOrganization_name() );
                notificationArrayDescription.add( note.getMessage_subject() );
                notificationArrayNotificationID.add( note.getNotification_id() );
                notificationArrayReplyAllowed.add( note.getReply_allowed() );
                notificationOrgIdfk.add( note.getOrganization_idfk() );
                notificationMsgTo.add( note.getMessage_from() );
            }
            noteRepository.closeDB();

            if (notes.size()!=0) {
                recyclerView.setVisibility( View.VISIBLE );
                noNotifications.setVisibility( View.GONE );

                getActivity().runOnUiThread( () -> {
                    adapter = new NotificationAdapter(view.getContext(),
                            notificationArrayTitle,
                            notificationArrayDescription,
                            notificationArrayNotificationID,
                            notificationArrayReplyAllowed,
                            notificationOrgIdfk,
                            notificationMsgTo);
                    adapter.setClickListener( onNotification.this);
                    recyclerView.setAdapter(adapter);
                } );
            } else {
                recyclerView.setVisibility( View.GONE );
                noNotifications.setVisibility( View.VISIBLE);
            }


        } );




    }
    private boolean isNetworkConnected() {
        ConnectivityManager
                connmanager = (ConnectivityManager) getContext()
                .getSystemService( Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connmanager.getActiveNetworkInfo();
        boolean isConnected = (activeNetwork != null) && (activeNetwork.isConnectedOrConnecting());
        return  isConnected;
    }


    @Override
    public void onItemClick(View view, int position) {

    }
}
