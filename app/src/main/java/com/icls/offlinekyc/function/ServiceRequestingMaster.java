package com.icls.offlinekyc.function;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.icls.offlinekyc.R;
import com.icls.offlinekyc.adapters.ServiceRequestingAdapter;
import com.icls.offlinekyc.function.Models.ServiceReqMasterHelperModal;
import com.icls.offlinekyc.helper.MasterNotification;
import com.icls.offlinekyc.helper.MasterServiceList;
import com.icls.offlinekyc.roomdb.OrganizationRepository;

import java.util.ArrayList;


public class ServiceRequestingMaster extends Fragment implements ServiceRequestingAdapter.ItemClickListener  {
    public ServiceRequestingAdapter adapter;
    ArrayList<ServiceReqMasterHelperModal> ServiceReqMasterListHelperList;
    View view ;
    RecyclerView recyclerView;

    public ServiceRequestingMaster() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate( R.layout.fragment_service_requesting_master, container, false );
        getActivity().setTitle( "Service Requesting" );
        init();
        onButton();


        return view;
    }

    private void init() {
        recyclerView = view.findViewById( R.id.serviceReqRecyclerview );
        recyclerView.setLayoutManager( new LinearLayoutManager( getContext() ) );
        getServiceRequestMasterList();
    }

    private void getServiceRequestMasterList() {
        final OrganizationRepository noteRepository = new OrganizationRepository(getContext());

        noteRepository.getMasterServiceRequestMasterList().observe( ServiceRequestingMaster.this, notes -> {
            ServiceReqMasterListHelperList=new ArrayList<>(  );

            for (MasterServiceList note : notes) {
                ServiceReqMasterHelperModal serviceReqMasterHelperModal = new ServiceReqMasterHelperModal();

                serviceReqMasterHelperModal.setOrganization_idfk( note.getOrganization_idfk() );
                serviceReqMasterHelperModal.setDuration_To_Complete( note.getDuration_To_Complete() );
                serviceReqMasterHelperModal.setOrganization_name( note.getOrganization_name() );
                serviceReqMasterHelperModal.setPartner_id( note.getPartner_id() );
                serviceReqMasterHelperModal.setPartner_name( note.getPartner_name() );
                serviceReqMasterHelperModal.setServiceFee( note.getServiceFee() );
                serviceReqMasterHelperModal.setService_ID( note.getService_ID() );
                serviceReqMasterHelperModal.setService_Name( note.getService_Name() );
                serviceReqMasterHelperModal.setService_Description( note.getService_Description() );
                ServiceReqMasterListHelperList.add( serviceReqMasterHelperModal );
            }
            noteRepository.closeDB();

            getActivity().runOnUiThread( () -> {
                adapter = new ServiceRequestingAdapter(view.getContext(),
                        ServiceReqMasterListHelperList);
                adapter.setClickListener(ServiceRequestingMaster.this);
                recyclerView.setAdapter(adapter);
            } );

        } );
    }

    private void onButton() {
    }


    @Override
    public void onItemClick(View view, int position) {

    }
}
