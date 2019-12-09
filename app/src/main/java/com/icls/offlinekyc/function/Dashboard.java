package com.icls.offlinekyc.function;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.icls.offlinekyc.R;


public class Dashboard extends Fragment {
    View view ;
    LinearLayout MyOrganization,PhapaOrganization,payments,
            group_purchase,job_placements,skill_training,
            health_wellness,group_insurance,
            easy_access,affordable_housing;
    public Dashboard() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle( "DASHBOARD" );
        // Inflate the layout for this fragment
        view = inflater.inflate( R.layout.fragment_dashboard, container, false ) ;
        init();
        buttonPress();

        return view;
    }

    private void buttonPress() {
        MyOrganization.setOnClickListener( v -> {
            Intent intent = new Intent( getActivity(),DrawerActivity.class );
            intent.putExtra( "menuid",R.id.nav_my_organisation );
            startActivity( intent );
        } );
        PhapaOrganization.setOnClickListener( v -> {

            Intent intent = new Intent( getActivity(),DrawerActivity.class );
            intent.putExtra( "menuid",R.id.nav_phapa_communities );
            startActivity( intent );
        });
        payments.setOnClickListener( v -> {
            Intent intent = new Intent( getActivity(),DrawerActivity.class );
            intent.putExtra( "menuid",R.id.nav_payments );
            startActivity( intent );
        } );
        group_purchase.setOnClickListener( v -> {
            Intent intent = new Intent( getActivity(),DrawerActivity.class );
            intent.putExtra( "menuid",R.id.nav_group_purchase );
            startActivity( intent );
        } );
        job_placements.setOnClickListener( v -> {
            Intent intent = new Intent( getActivity(),DrawerActivity.class );
            intent.putExtra( "menuid",R.id.nav_job_placement );
            startActivity( intent );
        } );
        skill_training.setOnClickListener( v -> {
            Intent intent = new Intent( getActivity(),DrawerActivity.class );
            intent.putExtra( "menuid",R.id.nav_skill_training );
            startActivity( intent );
        } );
        health_wellness.setOnClickListener( v -> {
            Intent intent = new Intent( getActivity(),DrawerActivity.class );
            intent.putExtra( "menuid",R.id.nav_health_wellness );
            startActivity( intent );
        } );
        group_insurance.setOnClickListener( v -> {
            Intent intent = new Intent( getActivity(),DrawerActivity.class );
            intent.putExtra( "menuid",R.id.nav_group_insurance );
            startActivity( intent );
        } );
        easy_access.setOnClickListener( v -> {
            Intent intent = new Intent( getActivity(),DrawerActivity.class );
            intent.putExtra( "menuid",R.id.nav_easy_access );
            startActivity( intent );
        } );
        affordable_housing.setOnClickListener( v -> {
            Intent intent = new Intent( getActivity(),DrawerActivity.class );
            intent.putExtra( "menuid",R.id.nav_affordable_housing );
            startActivity( intent );
        } );
    }

    private void init() {
        MyOrganization = view.findViewById( R.id.Myorganization );
        PhapaOrganization = view.findViewById( R.id.PhapaOrganization);
        payments = view.findViewById( R.id.payments );
        group_purchase = view.findViewById( R.id.group_purchase );
        job_placements = view.findViewById( R.id.job_placements );
        skill_training = view.findViewById( R.id.skill_training );
        health_wellness = view.findViewById( R.id.health_wellness );
        group_insurance = view.findViewById( R.id.group_insurance );
        easy_access = view.findViewById( R.id.easy_access );
        affordable_housing = view.findViewById( R.id.affordable_housing );

    }


}
