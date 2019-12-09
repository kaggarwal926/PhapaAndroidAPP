package com.icls.offlinekyc.function.MyOrgTabView.MyOrgTabViewFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.icls.offlinekyc.R;
import com.icls.offlinekyc.adapters.MyorgNotificationAdapter;
import com.icls.offlinekyc.adapters.NotificationAdapter;
import com.icls.offlinekyc.commonshare.common;
import com.icls.offlinekyc.function.Models.NotificationPenidingModel;
import com.icls.offlinekyc.helper.MasterNotification;
import com.icls.offlinekyc.roomdb.OrganizationRepository;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class MyOrganizationNotifications extends Fragment implements MyorgNotificationAdapter.ItemClickListener {

    public MyorgNotificationAdapter adapter;
    ArrayList<String> notificationArrayTitle;
    ArrayList<String> notificationArrayNotificationID;
    ArrayList<String> notificationArrayReplyAllowed;
    ArrayList<String> notificationArrayDescription;
    ArrayList<String> notificationOrgIdfk;
    ArrayList<String> notificationMsgTo;
    String selectedOrgId="";
    TextView noNotifications;
    // for debugging
    private static final String TAG = "onNotification";
// for recycler view notification----------
    //private ArrayList<String> mNames = new ArrayList<>();

    private static Handler mhandler =new Handler();
    View view=getView();
    //--------------------------------------------
    public static ArrayList<NotificationPenidingModel> notificationlist= new ArrayList<NotificationPenidingModel>(  );
    public RecyclerView recyclerView;
    Button btn_accept,btn_reject;

    public String NumberCheckResponse;
    public String PendingReqRespo;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences( "LoginDetails", MODE_PRIVATE );
        String PhNO=sharedPreferences.getString( "PhoneNo","" );



    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle( "Notification" );
        if(getActivity().getIntent()!=null){
           selectedOrgId= common.SELECTEDORGID;
        }
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


        // Inflate the layout for this fragment
        return view;
    }




    // method to setting up recycler view.
    private void initRecyclerView(){
        Log.d( TAG, "initRecyclerView: init RecyclerView" );
        recyclerView = view.findViewById( R.id.notificationRecyclerview );
        noNotifications = view.findViewById( R.id.noNotifications );
        recyclerView.setLayoutManager( new LinearLayoutManager( getContext() ) );


    }
    private void onSubmit() {

    }




    private void init() {

        btn_accept=view.findViewById( R.id.btn_accept );
        btn_reject=view.findViewById( R.id.btn_reject );
        getNotifications();
    }


    private  void getNotifications() {

        final OrganizationRepository noteRepository = new OrganizationRepository(getContext());

        noteRepository.getMyOrgNotifications( selectedOrgId ).observe( MyOrganizationNotifications.this, notes -> {
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

            if (notes.size()!=0 ) {
                noNotifications.setVisibility( View.GONE );
                recyclerView.setVisibility( View.VISIBLE );

                getActivity().runOnUiThread( () -> {
                    adapter = new MyorgNotificationAdapter(view.getContext(),
                            notificationArrayTitle,
                            notificationArrayDescription,
                            notificationArrayNotificationID,
                            notificationArrayReplyAllowed,
                            notificationOrgIdfk,
                            notificationMsgTo);
                    adapter.setClickListener( this);
                    recyclerView.setAdapter(adapter);
                } );
            }else {
                recyclerView.setVisibility( View.GONE );
                noNotifications.setVisibility( View.VISIBLE );
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
