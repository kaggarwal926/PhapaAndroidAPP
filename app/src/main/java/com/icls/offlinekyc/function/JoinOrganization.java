package com.icls.offlinekyc.function;

import android.arch.lifecycle.Observer;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.icls.offlinekyc.Database.OrganizationTypeParam;
import com.icls.offlinekyc.R;
import com.icls.offlinekyc.adapters.phapacommunityAdapter;
import com.icls.offlinekyc.commonshare.common;
import com.icls.offlinekyc.roomdb.OrganizationRepository;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class JoinOrganization extends Fragment implements phapacommunityAdapter.ItemClickListener {

    private static final String TAG = "joinOrganization";
    List<OrganizationTypeParam> organization = new ArrayList<>();
    List<String> org = new ArrayList<>();
    List<String> orgLogo = new ArrayList<>();
    List<String> orgStatus = new ArrayList<>();

    phapacommunityAdapter adapter;
    RecyclerView recyclerView;
    public  boolean isEmpty = false;
    private View rootView = null;
    List<OrganizationTypeParam> OrgList;
    public JoinOrganization() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phapa_communities, container, false);
        getActivity().setTitle( "PHAPA ORGANIZATIONS" );

        recyclerView = (RecyclerView) view.findViewById(R.id.rvAnimals);

        getJoinedOrg();
        return view;
    }

    public void getJoinedOrg() {
        final OrganizationRepository noteRepository = new OrganizationRepository(getContext());

        noteRepository.getTasks(common.STATUS_PAYMENT, common.STATUS_APPROVED, common.STATUS_WAITING_PAYMENT, common.STATUS_NOT_JOINED).observe(JoinOrganization.this, new Observer<List<OrganizationTypeParam>>() {
            @Override
            public void onChanged(@Nullable List<OrganizationTypeParam> notes) {

                OrgList=new ArrayList<>(  );
                for (OrganizationTypeParam note : notes) {

                    OrgList.add( note );
                    orgStatus.add( note.getStatus() );
                    org.add(note.getOrganization_name());
                }
                noteRepository.closeDB();
                if (!organization.isEmpty() || !organization.equals(null) || !organization.equals(0)) {
                    final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                    recyclerView.setLayoutManager(layoutManager);
                    adapter = new phapacommunityAdapter(getContext(), org,OrgList);
                    adapter.setClickListener(JoinOrganization.this);
                    recyclerView.setAdapter(adapter);
                }


            }
    });
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    public void getJoinedOrganization() {


        String extendedUrl = "OrganizationListjoin/listOrganization/";
        final SharedPreferences prefs = getActivity().getSharedPreferences("PASSCODEDB", MODE_PRIVATE);
        String user = prefs.getString("user", "9999");// "9999" is the default value.
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(common.PHAPAURL + extendedUrl)
                .addHeader("Client-Service", "frontend-client")
                .addHeader("Auth-key", "simplerestapi")
                .addHeader("Content-Type", "application/json")
                .addHeader("User-ID", common.ID)
                .addHeader("Authorization", common.TOKEN)
                .addHeader("Accept", "*/*")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final OrganizationRepository noteRepository = new OrganizationRepository(getActivity());

                int code = response.code();
                if (code == 200) {
                    final String myResponse = response.body().string();
                    try {
                        final JSONObject obj = new JSONObject(myResponse);
                        //if (myResponse.contains("member_occupation")) {

                        getActivity().runOnUiThread(new Runnable() {
                            @Override

                            public void run() {
                                try {

                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            // Do something after 5s = 5000ms
                                            //buttons[inew][jnew].setBackgroundColor(Color.BLACK);
                                        }
                                    }, 2000);
                                    organization.clear();
                                    org.clear();
                                    JSONObject jsonRootObject = new JSONObject(myResponse);
                                    JSONArray jsonArray = jsonRootObject.optJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        String organizationId = jsonObject.optString("organization_id").toString();
                                        String organization_name = jsonObject.optString("organization_name").toString();
                                        String address = jsonObject.optString("address").toString();
                                        String website = jsonObject.optString("website").toString();
                                        String organization_info = jsonObject.optString("organization_info").toString();
                                        String Phone = jsonObject.optString("Phone").toString();
                                        String email = jsonObject.optString("email").toString();
                                        String logo = jsonObject.optString("logo").toString();
                                        String joinedStatus = jsonObject.optString("status").toString();
                                        String synStatus = jsonObject.optString("syn_status");
                                        String userOrgMemId = jsonObject.optString("org_mem_id");
                                        String userMemberId = jsonObject.optString("member_idfk");
                                        String parentid = jsonObject.optString("P_idfk");
                                        String state = jsonObject.optString("state");
                                        String city = jsonObject.optString("city");
                                        String zip = jsonObject.optString("zip");
                                        String country = jsonObject.optString("country");
                                        String orgTypeIDFK = jsonObject.optString("orgTypeIDFK");
                                        String Joining_Remarks = jsonObject.optString("Joining_Remarks");

                                        org.add(organization_name);
                                        orgLogo.add(logo);
                                    }
                                    noteRepository.closeDB();
                                    if (!org.isEmpty() || !org.equals(null) || !org.equals(0)) {
                                        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                                        recyclerView.setLayoutManager(layoutManager);
                                        adapter = new phapacommunityAdapter(getContext(), org, OrgList);
                                        adapter.setClickListener(JoinOrganization.this);
                                        recyclerView.setAdapter(adapter);
                                    }





                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                }
            }
        });
    }


}
