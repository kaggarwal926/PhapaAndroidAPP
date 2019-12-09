package com.icls.offlinekyc.function.MyOrgTabView.MyOrgTabViewFragments;

import android.arch.lifecycle.Observer;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.icls.offlinekyc.Database.OrganizationTypeParam;
import com.icls.offlinekyc.R;
import com.icls.offlinekyc.commonshare.common;
import com.icls.offlinekyc.roomdb.OrganizationRepository;

import java.util.List;

import static android.graphics.BitmapFactory.decodeFile;


public class MyOrgAbout extends Fragment {

    private View rootView = null;
    TextView org_name, tv_address, tv_website, tv_about, tv_phoneno;
    String orgName, orgId, orgInfo, orgLogo, website, address, email, phoneNo, userMemberId, status;
    ImageView org_image;
    String organizationName;

    public MyOrgAbout() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_my_org_about, null);
        getActivity().setTitle("About");
        init();
        getOrganization( common.ORG_NAME );
        // Inflate the layout for this fragment
        return rootView;
        //return inflater.inflate( R.layout.fragment_my_org_about, container, false );
    }

    public void getOrganization(String organizationName) {
        final OrganizationRepository noteRepository = new OrganizationRepository(getActivity());
        noteRepository.getOrganization(organizationName).observe(MyOrgAbout.this, new Observer<List<OrganizationTypeParam>>() {
            @Override
            public void onChanged(@Nullable List<OrganizationTypeParam> notes) {
                for (OrganizationTypeParam note : notes) {
                    orgName = note.getOrganization_name();
                    orgId = note.getOrganization_id();
                    orgInfo = note.getOrganization_info();
                    orgLogo = note.getLogo();
                    website = note.getWebsite();
                    address = note.getAddress();
                    email = note.getEmail();
                    phoneNo = note.getPhone();
                    userMemberId = note.getMember_idfk();
                    status = note.getStatus();
                    //synStatus = note.getSyn_status();
                }
                noteRepository.closeDB();
                if (orgId != null) {
                    if (!orgName.equals("null") || !orgName.equals("NA") || !orgName.isEmpty()) {
                        org_name.setText(orgName);
                    }
                    if (!address.isEmpty() || !address.equals("null") || !address.equals("NA")) {
                        tv_address.setText(address);
                    }
                    if (!website.isEmpty() || !website.equals("null") || !website.equals("NA")) {
                        tv_website.setText(website);
                    }
                    if (!orgInfo.isEmpty() || !orgInfo.equals("null") || !orgInfo.equals("NA")) {
                        tv_about.setText(orgInfo);
                    }
                    if (!phoneNo.isEmpty() || phoneNo.equals("null") || !phoneNo.equals("NA")) {
                        tv_phoneno.setText(phoneNo);
                    }
                }
                if (orgLogo != null && !orgLogo.isEmpty() && !orgLogo.equals("NA")) {

                    String logoname = orgLogo.substring(orgLogo.lastIndexOf("/")+1);

                    Bitmap bitmap = decodeFile( Environment.getExternalStorageDirectory()
                            .getPath() + "/Phapa/"+ logoname);
                    org_image.setImageBitmap( bitmap );

                } else {

                }
            }
        });
    }

    public void init() {

        org_name = rootView.findViewById(R.id.morg_name);
        tv_address = rootView.findViewById(R.id.mtv_address);
        tv_website = rootView.findViewById(R.id.mtv_website);
        tv_about = rootView.findViewById(R.id.mtv_about);
        tv_phoneno = rootView.findViewById(R.id.mtv_phoneno);
        org_image = rootView.findViewById(R.id.orgLogoImageview );
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
