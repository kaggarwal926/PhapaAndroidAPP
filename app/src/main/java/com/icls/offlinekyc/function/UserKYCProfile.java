package com.icls.offlinekyc.function;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.icls.offlinekyc.R;

import org.json.JSONException;
import org.json.JSONObject;

public class UserKYCProfile extends AppCompatActivity {

    public String response="";
    public TextView tvName,tvDoB,tvGender,tvMobile,tvAddress;
    public ImageView ivPhoto;
    Button btnKYC;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_kycprofile);

        response=getIntent().getStringExtra("response");
        tvName=findViewById(R.id.tv_name);
        tvDoB=findViewById(R.id.doB);
        tvGender=findViewById(R.id.gender);
        tvMobile=findViewById(R.id.mobile);
        tvAddress=findViewById(R.id.address);
        ivPhoto=findViewById(R.id.iv_photo);
        btnKYC=findViewById(R.id.btn_kyc);
        parseResponse(response);

        btnKYC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getBaseContext(),DrawerActivity.class);
                intent.putExtra("menuid",R.id.nav_off_line_aadhar);
                startActivity(intent);
            }
        });
    }
    public void parseResponse(String response){
        try {
            JSONObject jsonObject=new JSONObject(response);
            tvName.setText(jsonObject.optString("Name",""));
            tvMobile.setText(jsonObject.optString("Mobile"));
            tvAddress.setText(jsonObject.optString("Address"));
            tvDoB.setText(jsonObject.optString("DOB"));
            tvGender.setText(jsonObject.optString("Gender"));
            byte[] imageBytes = Base64.decode(jsonObject.optString("Userimage"), Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            ivPhoto.setImageBitmap(decodedImage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
