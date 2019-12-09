package com.icls.offlinekyc.function;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.icls.offlinekyc.R;
import com.icls.offlinekyc.helper.KYCAadhar;
import com.icls.offlinekyc.roomdb.OrganizationRepository;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class onProfile extends Fragment {
    private static final String TAG = "onProfile";
    public TextView tvName, tvDoB, tvGender, tvMobile, tvAddress;
    public ImageView ivPhoto;
    public Button btnKYC, btngotoOfflineAadhaar;
    View view = getView();
    RelativeLayout userProfileLayout, notOnBoarded;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_on_profile, container, false);
        getActivity().setTitle("KYC Profile");

        tvName = view.findViewById(R.id.profile_name);
        tvDoB = view.findViewById(R.id.ProfiledoB);
        tvGender = view.findViewById(R.id.Profilegender);
        tvMobile = view.findViewById(R.id.Profile_mobile);
        tvAddress = view.findViewById(R.id.Profile_address);
        ivPhoto = view.findViewById(R.id.profile_photo);
        btnKYC = view.findViewById(R.id.profilebtn_kyc);
        btngotoOfflineAadhaar = view.findViewById(R.id.btngotoOfflineAadhaar);
        userProfileLayout = view.findViewById(R.id.userProfileLayout);
        notOnBoarded = view.findViewById(R.id.notOnBoarded);

        btnKYC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), DrawerActivity.class);
                intent.putExtra("menuid", R.id.nav_off_line_aadhar);
                startActivity(intent);
            }
        });
        btngotoOfflineAadhaar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), DrawerActivity.class);
                intent.putExtra("menuid", R.id.nav_off_line_aadhar);
                startActivity(intent);
            }
        });

        // Inflate the layout for this fragment
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("LoginDetails", MODE_PRIVATE);
        final String PhoneNo = sharedPreferences.getString("PhoneNo", "");

        getProfileData();
        return view;
    }

    public void getProfileData() {
        final OrganizationRepository noteRepository = new OrganizationRepository(getActivity());
        noteRepository.getProfile().observe(onProfile.this, new Observer<List<KYCAadhar>>() {
                    @Override
                    public void onChanged(@Nullable List<KYCAadhar> kycAadhars) {
                        for (KYCAadhar profileData : kycAadhars) {
                            String image = profileData.getMem_profile_pic();
                            if (!image.equals( null ) || !image.isEmpty()) {
                                byte[] imageBytes = Base64.decode(image, Base64.DEFAULT);
                                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                                ivPhoto.setImageBitmap(decodedImage);
                            }else {
                                ivPhoto.setImageResource( R.drawable.usericon );
                            }
                            tvDoB.setText(profileData.getMember_dob());
                            tvGender.setText(profileData.getMember_sex());
                            tvAddress.setText(profileData.getMember_permanent_address());
                            tvName.setText(profileData.getMember_fullname());
                            tvMobile.setText(profileData.getMember_mobile_no());
                        }

                    }

    });
}

    public void parseResponse(final String response) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                tvName.setText(jsonObject.optString("Name", ""));
                tvMobile.setText(jsonObject.optString("Mobile"));
                tvAddress.setText(jsonObject.optString("Address"));
                tvDoB.setText(jsonObject.optString("DOB"));
                tvGender.setText(jsonObject.optString("Gender"));
                byte[] imageBytes = Base64.decode(jsonObject.optString("UserImage"), Base64.DEFAULT);
                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                ivPhoto.setImageBitmap(decodedImage);
            }
        });


    }
}
