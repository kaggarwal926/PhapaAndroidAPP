package com.icls.offlinekyc.function.MyOrgTabView.MyOrgTabViewFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.icls.offlinekyc.R;
import com.icls.offlinekyc.adapters.OrgSpecificServiceRequestingAdapter;
import com.icls.offlinekyc.commonshare.common;
import com.icls.offlinekyc.function.Models.MytabServiceReqModal;
import com.icls.offlinekyc.helper.MasterServiceList;
import com.icls.offlinekyc.roomdb.OrganizationRepository;

import java.util.ArrayList;


public class MyOrgServiceRequest extends Fragment implements OrgSpecificServiceRequestingAdapter.ItemClickListener{
    public OrgSpecificServiceRequestingAdapter adapter;
    View view ;
    RecyclerView recyclerView;
    ArrayList<MytabServiceReqModal> OrgspecificServiceReq;

    public MyOrgServiceRequest() {
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
        view =inflater.inflate( R.layout.fragment_my_org_service_request, container, false );


        init();
        button();
        String orgID = common.SELECTEDORGID;
        getOrganizationSpecificServices(orgID);
        return view ;
    }

    private void getOrganizationSpecificServices(String orgID) {

        final OrganizationRepository noteRepository = new OrganizationRepository(getContext());

        noteRepository.getSpecificServiceReq(orgID).observe( MyOrgServiceRequest.this, notes -> {

            OrgspecificServiceReq = new ArrayList<>(  );
            for (MasterServiceList note : notes) {

                MytabServiceReqModal mytabServiceReqModal = new MytabServiceReqModal();
                mytabServiceReqModal.setOrganization_idfk( note.getOrganization_idfk() );
                mytabServiceReqModal.setOrganization_name( note.getOrganization_name() );
                mytabServiceReqModal.setPartner_id( note.getPartner_id() );
                mytabServiceReqModal.setPartner_name( note.getPartner_name() );
                mytabServiceReqModal.setService_ID( note.getService_ID() );
                mytabServiceReqModal.setService_Name( note.getService_Name() );
                mytabServiceReqModal.setService_Description( note.getService_Description() );
                mytabServiceReqModal.setServiceFee( note.getServiceFee() );
                mytabServiceReqModal.setDuration_To_Complete( note.getDuration_To_Complete() );
                mytabServiceReqModal.setService_Description( note.getService_Description() );
                OrgspecificServiceReq.add(  mytabServiceReqModal);

            }
            noteRepository.closeDB();
            getActivity().runOnUiThread( () -> {
                if (!OrgspecificServiceReq.isEmpty() || !OrgspecificServiceReq.equals(null) || !OrgspecificServiceReq.equals(0)) {
                    final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                    recyclerView.setLayoutManager(layoutManager);
                    adapter = new OrgSpecificServiceRequestingAdapter(getContext(), getActivity(),
                            OrgspecificServiceReq);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                    SnapHelper snapHelper = new PagerSnapHelper();
                    snapHelper.attachToRecyclerView(recyclerView);
                    adapter.setClickListener( MyOrgServiceRequest.this);
                    recyclerView.setAdapter(adapter);
                }

            } );

        });
    }

    private void button() {
    }

    private void init() {
        recyclerView = (RecyclerView) view.findViewById( R.id.ServiceReqRecyclerView );
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }


    @Override
    public void onItemClick(View view, int position) {

    }
}
