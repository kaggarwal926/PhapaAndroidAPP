package com.icls.offlinekyc.function;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.icls.offlinekyc.Database.OrganizationTypeParam;
import com.icls.offlinekyc.R;
import com.icls.offlinekyc.adapters.SupportChartAdapter;
import com.icls.offlinekyc.adapters.myOrganizationAdapter;
import com.icls.offlinekyc.adapters.phapacommunityAdapter;
import com.icls.offlinekyc.commonshare.common;
import com.icls.offlinekyc.function.MyOrgTabView.MyOrgTabViewFragments.MyOrgTabSupport;
import com.icls.offlinekyc.roomdb.OrganizationRepository;

import java.util.ArrayList;
import java.util.List;


public class MyOrganization extends Fragment implements myOrganizationAdapter.ItemClickListener{
    private static final String TAG = "MyOrganization";
    List<String> org = new ArrayList<>();
    List<OrganizationTypeParam> helperOrg = new ArrayList<>();
    RecyclerView recyclerView;
    myOrganizationAdapter adapter;
    private Handler mHandler;
    private List<Integer> msglistCount;
    LinearLayout nolistlayout;
    TextView clickhere;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_organization, container, false);
        common.PRESENTSCREEN = "mycommunities";

        getActivity().setTitle( "MY ORGANIZATIONS" );
        msglistCount = new ArrayList<>(  );
        msglistCount.add( 0 );
        msglistCount.add( 0 );
        recyclerView = (RecyclerView) view.findViewById(R.id.rvAnimals);
        nolistlayout = view.findViewById( R.id.nolistlayout );
        clickhere = view.findViewById( R.id.clickhere );

        getMyOrganization();
        if(common.PRESENTSCREEN.equalsIgnoreCase(  "mycommunities")){
            runeveryFivesecUntillDestoyed();
        }
        nolistClick();
        return view;
    }

    private void nolistClick() {
        clickhere.setOnClickListener( v -> {
            Intent intent = new Intent(getActivity(), DrawerActivity.class);
            intent.putExtra("menuid", R.id.nav_phapa_communities);
            startActivity(intent);
        } );
    }


    public void getMyOrganization() {
        final OrganizationRepository noteRepository = new OrganizationRepository(getContext());
        noteRepository.getMyOrganization(common.STATUS_JOINED_MEMBER).observe(MyOrganization.this, new Observer<List<OrganizationTypeParam>>() {
            @Override
            public void onChanged(@Nullable List<OrganizationTypeParam> notes) {
                helperOrg = new ArrayList<>();
                for (OrganizationTypeParam note : notes) {
                    org.add(note.getOrganization_name());
                    helperOrg.add( note );

                }
                noteRepository.closeDB();
                if ((org!=null)&&(!org.isEmpty()) && (org.size()!=0)) {
                    final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                    recyclerView.setLayoutManager(layoutManager);
                    adapter = new myOrganizationAdapter(getContext(), org, helperOrg);
                    adapter.setClickListener(MyOrganization.this);
                    recyclerView.setAdapter(adapter);
                    nolistlayout.setVisibility( View.GONE );
                } else {
                   getActivity().runOnUiThread( () -> {
                       nolistlayout.setVisibility( View.VISIBLE );
                       recyclerView.setVisibility( View.GONE );
                   } );
                }


            }
        });
    }
    private void runeveryFivesecUntillDestoyed() {
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            public void run() {
                //Do Something
                Log.d( TAG, "im running every 5 sec" );
                // do your stuff
                if(common.PRESENTSCREEN.equalsIgnoreCase("mycommunities" )){
                    Log.d( TAG, "im running every 5 sec for my communities" );
                    getMyOrganization1();

                }
                mHandler.postDelayed(this, 5000);
            }
        }, 5000);

    }

    private void getMyOrganization1() {

        final OrganizationRepository noteRepository = new OrganizationRepository(getContext());
        noteRepository.getMyOrganization(common.STATUS_JOINED_MEMBER).observe(MyOrganization.this, new Observer<List<OrganizationTypeParam>>() {
            @Override
            public void onChanged(@Nullable List<OrganizationTypeParam> notes) {
                helperOrg = new ArrayList<>();
                helperOrg.clear();
                org.clear();
                for (OrganizationTypeParam note : notes) {
                    org.add(note.getOrganization_name());
                    helperOrg.add( note );

                }

                msglistCount.add( helperOrg.size() ) ;

                int last = msglistCount.get( msglistCount.size()-1) ;
                int lastbutone =  msglistCount.get(  msglistCount.size()-2);
                Log.d( TAG, "openNotificationChat1: "+" "+helperOrg.size()+" "+last+" " +lastbutone );
                if ((last!=lastbutone) ) {

                    if ((helperOrg!=null)&&(!helperOrg.isEmpty()) && (helperOrg.size()!=0)) {

                        getActivity().runOnUiThread( () -> {
                            nolistlayout.setVisibility( View.GONE );
                            recyclerView.setVisibility( View.VISIBLE );
                        } );
                        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                        recyclerView.setLayoutManager(layoutManager);

                        adapter = new myOrganizationAdapter(getContext(), org, helperOrg);
                        adapter.setClickListener(MyOrganization.this);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();



                    } else {
                        getActivity().runOnUiThread( () -> {
                            nolistlayout.setVisibility( View.VISIBLE );
                            recyclerView.setVisibility( View.GONE );
                        } );
                    }

                }
                noteRepository.closeDB();

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        common.PRESENTSCREEN = "";
        msglistCount.clear();
    }

    @Override
    public void onItemClick(View view, int position) {

    }
}
