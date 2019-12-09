package com.icls.offlinekyc.function;

import android.arch.lifecycle.Observer;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.icls.offlinekyc.Database.OrganizationTypeParam;
import com.icls.offlinekyc.R;
import com.icls.offlinekyc.commonshare.common;
import com.icls.offlinekyc.function.Interfaces.IOnBackPressed;
import com.icls.offlinekyc.roomdb.OrganizationRepository;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.BitmapFactory.decodeFile;
import static android.support.constraint.Constraints.TAG;
import static com.icls.offlinekyc.commonshare.common.joinRequestSend;
import static com.icls.offlinekyc.commonshare.common.UPDATEDRECORD;


public class OrganizationProfile extends Fragment implements IOnBackPressed {
    List<OrganizationTypeParam> organization = new ArrayList<>();
    TextView org_name, tv_address, tv_website, tv_about, tv_phoneno;
    String orgName, orgId, orgInfo, orgLogo, website, address, email, phoneNo, userMemberId, status;
    String synStatus;
    ImageView org_image;
    TextView tv_join_remark;
    Button btnOrgJoin;
    private View rootView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_organization_profile, null);
        getActivity().setTitle("Organization Profile");
        init();
        Bundle arguments = getArguments();
        String organizationName = arguments.getString("organizationName");
        getOrganization(organizationName);
        btnOrgJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinOrganization();
            }
        });
        return rootView;
    }

    @Override
    public boolean onBackPressed() {
        return true;

    }

    public void init() {
        org_name = rootView.findViewById(R.id.org_name);
        tv_address = rootView.findViewById(R.id.tv_address);
        tv_website = rootView.findViewById(R.id.tv_website);
        tv_about = rootView.findViewById(R.id.tv_about);
        tv_phoneno = rootView.findViewById(R.id.tv_phoneno);
        org_image = rootView.findViewById(R.id.orgLogoImageview );
        btnOrgJoin = rootView.findViewById(R.id.btn_join);
        tv_join_remark = rootView.findViewById(R.id.tv_join_remark);
    }

    public void getOrganization(String organizationName) {
        final OrganizationRepository noteRepository = new OrganizationRepository(getActivity());
        noteRepository.getOrganization(organizationName).observe(OrganizationProfile.this, new Observer<List<OrganizationTypeParam>>() {
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
                    synStatus = note.getSyn_status();
                }
                noteRepository.closeDB();
                org_name.setText(orgName);
                tv_address.setText(address);
                tv_website.setText(website);
                tv_about.setText(orgInfo);
                tv_phoneno.setText(phoneNo);
                if (status.equals("Waiting For Payment")) {
                    btnOrgJoin.setVisibility(View.GONE);
                    tv_join_remark.setText("Waiting For Approval");
                    tv_join_remark.setEnabled(false);
                }

                if (orgLogo != null && !orgLogo.isEmpty() && !orgLogo.equals("NA")) {
                    String logoname = orgLogo.substring(orgLogo.lastIndexOf("/")+1);

                    Bitmap bitmap = decodeFile( Environment.getExternalStorageDirectory()
                            .getPath() + "/Phapa/"+ logoname);
                    org_image.setImageBitmap( bitmap );

                } else {
                    /*do nothing*/
                }
            }
        });
    }

    public void joinOrganization() {
        final OrganizationRepository noteRepository = new OrganizationRepository(getContext());
        String remark = String.valueOf(tv_join_remark.getText());
        OrganizationTypeParam orgTypeParam = new OrganizationTypeParam();
        orgTypeParam.setSyn_status(UPDATEDRECORD);
        orgTypeParam.setJoining_Remarks(remark);
        orgTypeParam.setState(joinRequestSend);
        orgTypeParam.setOrganization_id(orgId);
        noteRepository.updateOrganization(UPDATEDRECORD, remark, joinRequestSend, orgId);
        tv_join_remark.setText(joinRequestSend);
        tv_join_remark.setEnabled(false);
        btnOrgJoin.setVisibility(View.GONE);

    }

}
