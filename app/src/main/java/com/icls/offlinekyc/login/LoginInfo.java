package com.icls.offlinekyc.login;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.security.NetworkSecurityPolicy;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.icls.offlinekyc.R;
import com.icls.offlinekyc.Registration.Registration;
import com.icls.offlinekyc.commonshare.common;
import com.icls.offlinekyc.function.DrawerActivity;
import com.icls.offlinekyc.function.Service.MyService.DownloadService;
import com.icls.offlinekyc.helper.CoordinatorsPOJO;
import com.icls.offlinekyc.helper.MemberTypeMasterPOJO;
import com.icls.offlinekyc.helper.PrimaryOrganisationPOJO;
import com.icls.offlinekyc.roomdb.OrganizationRepository;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginInfo extends AppCompatActivity {
    public static final String INTENT_PHONENUMBER = "phonenumber";
    private static final String TAG = "LoginInfo";
    public static List<String> logoPaths1;
    public static LoginInfo mLogin_activity;
    public static List<PrimaryOrganisationPOJO> posts;
    public static List<MemberTypeMasterPOJO> memberPosts;
    public static List<CoordinatorsPOJO> coordinatorsPosts;
    EditText edtmobno, edt_otp, edtpasscode, edtcreatePasscode, edt_name;
    ImageView image;
    TextView resetPassCode, status;
    Button btngenotp, btnLogin, passCodeCreateSubmitBtn, passCodeSubmitBtn;
    ConstraintLayout loginLayout;
    ConstraintLayout enterPasscodeLayout, createPasscodeLayout;
    String request = " ";
    TextView expiresIn, resendtextview;

    private String mobileNumberforcreatepasscode;
    private ProgressDialog dialog = null;

    public static void downloadFileFromServer(String filename, String urlString) throws IOException {
        BufferedInputStream in = null;
        FileOutputStream fout = null;
        try {
            URL url = new URL( urlString );

            in = new BufferedInputStream( url.openStream() );
            String root = Environment.getExternalStorageDirectory().toString();
            fout = new FileOutputStream( root + "/Phapa/Documents/" + filename );


            byte[] data = new byte[1024];
            int count;
            while ((count = in.read( data, 0, 1024 )) != -1) {
                fout.write( data, 0, count );
                System.out.println( count );
            }
        } finally {
            if (in != null)
                in.close();
            if (fout != null)
                fout.close();
        }

    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login_info );
        mLogin_activity = LoginInfo.this;
        isReadStoragePermissionGranted();
        isWriteStoragePermissionGranted();
        if (NetworkSecurityPolicy.getInstance().isCleartextTrafficPermitted()) {
            Log.d( "Cleartext:::", "true" );
        } else {
            Log.d( "Cleartext:::", "false" );

        }
        common.ORG_NAME = "";
        common.SELECTEDORGID = "";


        init();
        onButtonPress();
        checkSmsPermission( false );



        if (getIntent() != null && getIntent().getStringExtra( "status" ).equalsIgnoreCase( "AlreadyLoggedIn" )) {
            enterPasscodeLayout.setVisibility( View.VISIBLE );
            edtpasscode.setText( "" );
            createPasscodeLayout.setVisibility( View.GONE );
            loginLayout.setVisibility( View.GONE );
        } else if (getIntent() != null && getIntent().getStringExtra( "status" ).equalsIgnoreCase( "notLoggedIn" )) {
            enterPasscodeLayout.setVisibility( View.GONE );
            createPasscodeLayout.setVisibility( View.GONE );
            loginLayout.setVisibility( View.VISIBLE );

            //noteRepository.closeDB();
        } else if (getIntent() != null && getIntent().getStringExtra( "status" ).equalsIgnoreCase( "logout" )) {
            enterPasscodeLayout.setVisibility( View.VISIBLE );
            createPasscodeLayout.setVisibility( View.GONE );
            loginLayout.setVisibility( View.GONE );

        }

    }

    public boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission( Manifest.permission.READ_EXTERNAL_STORAGE )
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v( TAG, "Permission is granted1" );
                return true;
            } else {

                Log.v( TAG, "Permission is revoked1" );
                ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3 );
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v( TAG, "Permission is granted1" );
            return true;
        }
    }

    public boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission( android.Manifest.permission.WRITE_EXTERNAL_STORAGE )
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v( TAG, "Permission is granted2" );
                return true;
            } else {

                Log.v( TAG, "Permission is revoked2" );
                ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2 );
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v( TAG, "Permission is granted2" );
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult( requestCode, permissions, grantResults );
        Log.d( TAG, "Response received" );
        try {
            switch (requestCode) {
                case 2:
                    Log.d( TAG, "External storage2" );
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Log.v( TAG, "Permission: " + permissions[0] + "was " + grantResults[0] );
                        //resume tasks needing this permission

                    } else {
                        //progress.dismiss();
                    }
                    break;

                case 3:
                    Log.d( TAG, "External storage1" );
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Log.v( TAG, "Permission: " + permissions[0] + "was " + grantResults[0] );
                        //resume tasks needing this permission

                    } else {
                        //progress.dismiss();
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init() {
        status = findViewById( R.id.Status );
        edtmobno = findViewById( R.id.edtmobno );
        edt_name = findViewById( R.id.edt_name );
        resetPassCode = findViewById( R.id.resetPassCode );
        edt_otp = findViewById( R.id.edt_otp );
        btngenotp = findViewById( R.id.btngenotp );
        btnLogin = findViewById( R.id.btnlogin );
        passCodeCreateSubmitBtn = findViewById( R.id.passCodeCreateSubmitBtn );
        passCodeSubmitBtn = findViewById( R.id.passCodeSubmitBtn );
        loginLayout = findViewById( R.id.loginLayout );
        enterPasscodeLayout = findViewById( R.id.enterPasscodeLayout );
        createPasscodeLayout = findViewById( R.id.createPasscodeLayout );
        edtpasscode = findViewById( R.id.edtpasscode );
        edtpasscode.setText( "" );
        edtcreatePasscode = findViewById( R.id.edtcreatePasscode );
        image = findViewById( R.id.image );
        expiresIn = findViewById( R.id.expiresIn );
        resendtextview = findViewById( R.id.resendtextview );

    }

    private void getPrimaryOrganizationDropDown() {
        okhttp3.OkHttpClient client = new okhttp3.OkHttpClient();
        String extendedUrl = "List_cntrl/organizationMasterList";

        // put your json here
        final okhttp3.Request request = new okhttp3.Request.Builder()
                .url( common.PHAPAURL + extendedUrl )
                .addHeader( "Client-Service", "frontend-client" )
                .addHeader( "Auth-key", "simplerestapi" )
                .addHeader( "Content-Type", "application/json" )
                .addHeader( "User-ID", common.ID )
                .addHeader( "Authorization", common.TOKEN )
                .addHeader( "Accept", "*/*" )
                .build();
        client.newCall( request ).enqueue( new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d( TAG, "Not able to connect PrimaryOrganization" );
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                final String myResponse = response.body().string();

                try {
                    final JSONArray jsonArray = new JSONArray( myResponse );
                    if (myResponse.contains( "organization_name" )) {
                        runOnUiThread( new Runnable() {
                            @Override
                            public void run() {

                                //here we wil make a list and send it to our spinner
                                Log.d( TAG, "Got PrimaryOrganization List " );
                                Gson gson = new Gson();
                                String jsonOutput = myResponse;
                                Type listType = new TypeToken<List<PrimaryOrganisationPOJO>>() {
                                }.getType();
                                posts = gson.fromJson( jsonOutput, listType );

                                final OrganizationRepository noteRepository = new OrganizationRepository( getApplicationContext() );
                                for (int i = 0; i < posts.size(); i++) {
                                    String organizationId = posts.get( i ).getOrganization_id();
                                    String organizationName = posts.get( i ).getOrganization_name();
                                    noteRepository.insertOrganization( organizationId, organizationName );
                                }
                            }
                        } );
                    } else {
                        Log.d( TAG, " PrimaryOrganization List Not found " );
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } );
    }

    private void getCoordinatorNameDropDown() {
        okhttp3.OkHttpClient client = new okhttp3.OkHttpClient();
        String extendedUrl = "List_cntrl/CoordinatorsORSupervisor";

        // put your json here
        final okhttp3.Request request = new okhttp3.Request.Builder()
                .url( common.PHAPAURL + extendedUrl )
                .addHeader( "Client-Service", "frontend-client" )
                .addHeader( "Auth-key", "simplerestapi" )
                .addHeader( "Content-Type", "application/json" )
                .addHeader( "User-ID", common.ID )
                .addHeader( "Authorization", common.TOKEN )
                .addHeader( "Accept", "*/*" )
                .build();
        client.newCall( request ).enqueue( new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d( TAG, "Not able to connect CoordinatorsORSupervisor" );
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {

                final String myResponse = response.body().string();

                try {
                    final JSONArray jsonArray = new JSONArray( myResponse );
                    if (myResponse.contains( "user_name" )) {
                        runOnUiThread( new Runnable() {
                            @Override
                            public void run() {
                                //here we wil make a list and send it to our spinner
                                Log.d( TAG, "Got CoordinatorsORSupervisor List " );
                                Gson gson = new Gson();
                                String jsonOutput = myResponse;
                                Type listType = new TypeToken<List<CoordinatorsPOJO>>() {
                                }.getType();
                                coordinatorsPosts = gson.fromJson( jsonOutput, listType );

                                String[] mMemberListDataList = new String[coordinatorsPosts.size()];
                                final OrganizationRepository noteRepository = new OrganizationRepository( getApplicationContext() );
                                for (int i = 0; i < coordinatorsPosts.size(); i++) {
                                    String userName = coordinatorsPosts.get( i ).getUser_name();
                                    String loginId = coordinatorsPosts.get( i ).getLogin_id();
                                    if (loginId != null){
                                        noteRepository.insertCoordinator( loginId, userName );
                                    }


                                }
                            }
                        } );
                    } else {
                        Log.d( TAG, " CoordinatorsORSupervisor List Not found " );
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } );
    }

    private void getMemberTypeMasterDropDown() {

        okhttp3.OkHttpClient client = new okhttp3.OkHttpClient();
        String extendedUrl = "List_cntrl/memberTypeMaster";

        // put your json here
        final okhttp3.Request request = new okhttp3.Request.Builder()
                .url( common.PHAPAURL + extendedUrl )
                .addHeader( "Client-Service", "frontend-client" )
                .addHeader( "Auth-key", "simplerestapi" )
                .addHeader( "Content-Type", "application/json" )
                .addHeader( "User-ID", common.ID )
                .addHeader( "Authorization", common.TOKEN )
                .addHeader( "Accept", "*/*" )
                .build();
        client.newCall( request ).enqueue( new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d( TAG, "Not able to connect member type master" );
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                final String myResponse = response.body().string();
                final OrganizationRepository noteRepository = new OrganizationRepository( getApplicationContext() );

                try {

                    if (myResponse.contains( "memberTypeID" )) {
                        runOnUiThread( new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    JSONObject object = new JSONObject( myResponse );
                                    JSONArray data = object.getJSONArray( "data" );
                                    for (int i = 0; i < data.length(); i++) {
                                        JSONObject orgGroup = data.getJSONObject( i );
                                        String memberTypeId = orgGroup.getString( "memberTypeID" );
                                        String memberType = orgGroup.getString( "memberTypeCode" );
                                        String memberTypeGroup = orgGroup.getString( "memberTypeGroup" );
                                        noteRepository.insertMemberType( memberType, memberTypeId, memberTypeGroup );
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                //here we wil make a list and send it to our spinner

                            }
                        } );
                    } else {
                        Log.d( TAG, " Member Type List Not found " );
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } );
    }

    public void getAadharProfile() {
        SharedPreferences sharedPreferences = getSharedPreferences( "LoginDetails", MODE_PRIVATE );
        final String PhoneNo = sharedPreferences.getString( "PhoneNo", "" );
        OkHttpClient client = new OkHttpClient();
        String url = common.PHAPAURL + "myeid/getAdhareProfile/" + PhoneNo;
        final Request request = new Request.Builder()
                .url( url )
                .get()
                .addHeader( "Client-Service", "frontend-client" )
                .addHeader( "Auth-key", "simplerestapi" )
                .addHeader( "Content-Type", "application/json" )
                .addHeader( "User-ID", common.ID )
                .addHeader( "Authorization", common.TOKEN )
                .addHeader( "Accept", "*/*" )
                .build();
        client.newCall( request ).enqueue( new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String myResponse = response.body().string();

                if (!myResponse.equals( null ) || !myResponse.equals( "NA" ) || !myResponse.isEmpty()) {


                    JSONObject obj = null;
                    try {
                        obj = new JSONObject( myResponse );

                        if (obj.optString( "status" ).equals( "401" ) && obj.optString( "message" ).equals( "Your session has been expired." )) {

                        } else {
                            String image = obj.optString( "mem_profile_pic", "" );

                            String dob = obj.optString( "member_dob", "" );
                            String address = obj.optString( "member_permanent_address", "" );
                            String gender = obj.optString( "member_sex", "" );
                            String mobile = obj.optString( "member_mobile_no", "" );
                            String name = obj.optString( "member_fullname", "" );
                            //String error = "NA";
                            final OrganizationRepository noteRepository = new OrganizationRepository( getApplicationContext() );
                            noteRepository.setAadharData( image, dob, address, gender, mobile, name, common.NEWRECORD );
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        } );
    }

    public void getJoinedOrganization() {
        /*create foldr to save oraganisation logo */
        requestPermissions( new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1024 );
        int check = ActivityCompat.checkSelfPermission( getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE );
        if (check == PackageManager.PERMISSION_GRANTED) {
            //Create app folder
            String folder_main = "Phapa";
            File f = new File( Environment.getExternalStorageDirectory(), folder_main );
            try {
                if (!f.exists()) {
                    f.mkdirs();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            requestPermissions( new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1024 );
            String folder_main = "Phapa";
            File f = new File( Environment.getExternalStorageDirectory(), folder_main );
            try {
                if (!f.exists()) {
                    f.mkdirs();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        /*create foldr to save oraganisation logo */

        String extendedUrl = "OrganizationListjoin/listOrganization";
        final SharedPreferences prefs = getSharedPreferences( "PASSCODEDB", MODE_PRIVATE );
        String user = prefs.getString( "user", "9999" );// "9999" is the default value.
        okhttp3.OkHttpClient client = new okhttp3.OkHttpClient();
        final okhttp3.Request request = new okhttp3.Request.Builder()
                .url( common.PHAPAURL + extendedUrl )
                .addHeader( "Client-Service", "frontend-client" )
                .addHeader( "Auth-key", "simplerestapi" )
                .addHeader( "Content-Type", "application/json" )
                .addHeader( "User-ID", common.ID )
                .addHeader( "Authorization", common.TOKEN )
                .addHeader( "Accept", "*/*" )
                .build();
        client.newCall( request ).enqueue( new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                final OrganizationRepository noteRepository = new OrganizationRepository( getApplicationContext() );

                int code = response.code();
                if (code == 200) {
                    final String myResponse = response.body().string();
                    try {
                        final JSONObject obj = new JSONObject( myResponse );

                        try {
                            logoPaths1 = new ArrayList<>();
                            JSONObject jsonRootObject = new JSONObject( myResponse );
                            JSONArray jsonArray = jsonRootObject.optJSONArray( "data" );
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject( i );
                                String organizationId = jsonObject.optString( "organization_id" );
                                String organization_name = jsonObject.optString( "organization_name" );
                                String address = jsonObject.optString( "address" );
                                String website = jsonObject.optString( "website" );
                                String organization_info = jsonObject.optString( "organization_info" );
                                String Phone = jsonObject.optString( "Phone" );
                                String email = jsonObject.optString( "email" );
                                String logo = jsonObject.optString( "logo" );

                                if (!(logo.equals( "" ) || logo.equals( " " ) || logo.equalsIgnoreCase( "NA" ))) {

                                    if (!(organizationId.equals( "999" ) || (organizationId.equals( "1007" )) || (organizationId.equals( "1012" )))) {

                                        logoPaths1.add( logo );
                                    }

                                }

                                String joinedStatus = jsonObject.optString( "status" );
                                String synStatus = jsonObject.optString( "syn_status" );
                                String userOrgMemId = jsonObject.optString( "org_mem_id" );
                                String userMemberId = jsonObject.optString( "member_idfk" );
                                String parentid = jsonObject.optString( "P_idfk" );
                                String state = jsonObject.optString( "state" );
                                String city = jsonObject.optString( "city" );
                                String zip = jsonObject.optString( "zip" );
                                String country = jsonObject.optString( "country" );
                                String orgTypeIDFK = jsonObject.optString( "orgTypeIDFK" );
                                String Joining_Remarks = jsonObject.optString( "Joining_Remarks" );
                                String primaryOrgIdfk = jsonObject.optString( "primary_org_idfk" );

                                // addidtional fileds added later //

                                String is_memeber = "";
                                String location = "";
                                String member_Since = "";
                                String date_of_initiation = "";
                                String date_of_joining = "";

                                // addidtional fileds added later //
                                noteRepository.insertTask( organizationId, organization_name, address, website
                                        , organization_info, Phone, email, logo, joinedStatus, synStatus,
                                        userOrgMemId, userMemberId, parentid, state, city, zip, country,
                                        orgTypeIDFK, Joining_Remarks, primaryOrgIdfk,
                                        is_memeber,
                                        location,
                                        member_Since,
                                        date_of_initiation,
                                        date_of_joining );
                            }
                            Log.d( TAG, "logopath array size" + logoPaths1.size() );

                            startService( new Intent( LoginInfo.this, DownloadService.class ) );
                            stopAnimation();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }



                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                }
            }
        } );
    }

    private void getAdditionalProfile() {
        final OrganizationRepository noteRepository = new OrganizationRepository( getApplicationContext() );
        final SharedPreferences prefs = getSharedPreferences( "PASSCODEDB", MODE_PRIVATE );
        String user = prefs.getString( "user", "9999" );// "9999" is the default value.

        okhttp3.OkHttpClient client = new okhttp3.OkHttpClient();
        okhttp3.MediaType mediaType = okhttp3.MediaType.parse( "application/json" );
        okhttp3.MediaType JSON = okhttp3.MediaType.parse( "application/json; charset=utf-8" );
        String extendedUrl = "myeid/GetQuickRegistration/";
        final okhttp3.Request request = new okhttp3.Request.Builder()
                .url( common.PHAPAURL + extendedUrl + user )
                .addHeader( "Client-Service", "frontend-client" )
                .addHeader( "Auth-key", "simplerestapi" )
                .addHeader( "Content-Type", "application/json" )
                .addHeader( "User-ID", common.ID )
                .addHeader( "Authorization", common.TOKEN )
                .addHeader( "Accept", "*/*" )
                .build();
        client.newCall( request ).enqueue( new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {

                final String myResponse = response.body().string();

                try {
                    final JSONObject obj = new JSONObject( myResponse );
                    if (response.code() == 200) {
                        runOnUiThread( new Runnable() {
                            @Override
                            public void run() {
                                //Member profile data.
                                JSONObject jsonObject = obj.optJSONObject( "data" );
                                String member_id = jsonObject.optString( "member_id" );
                                SharedPreferences sharedPreferences = getSharedPreferences( "LoginDetails", MODE_PRIVATE );
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString( "member_id", member_id );
                                String member_occupation = jsonObject.optString( "member_occupation" );
                                String member_local_add = jsonObject.optString( "member_local_add" );
                                //String member_sex = jsonObject.optString("member_sex");
                                String village_name = jsonObject.optString( "village_name" );
                                String area_name = jsonObject.optString( "area_name" );
                                String reg_city = jsonObject.optString( "reg_city" );
                                String member_local_add_taluk = jsonObject.optString( "member_local_add_taluk" );
                                String reg_state = jsonObject.optString( "reg_state" );
                                String member_local_add_pincode = jsonObject.optString( "member_local_add_pincode" );
                                String alternative_Number = jsonObject.optString( "alternative_Number" );
                                String memTypeIDFK = jsonObject.optString( "memTypeIDFK" );
                                String memberTypeCode = jsonObject.optString( "memberTypeCode" );
                                String organization_id = jsonObject.optString( "organization_id" );
                                String organization_name = jsonObject.optString( "organization_name" );
                                String agent_id = jsonObject.optString( "agent_id" );
                                String agent_name = jsonObject.optString( "agent_name" );
                                String Member_group_idfk = jsonObject.optString( "Member_group_idfk" );
                                String member_groupName = jsonObject.optString( "member_group" );
                                String primary_org_id = jsonObject.optString( "primary_org_id" );
                                String organisation_group_type = jsonObject.optString( "organisation_group_type" );

                                // additional fileds added later //

                                String email = "";
                                String marital_status = "";
                                String membership_status = "";
                                String date_Of_joining = "";

                                // additional fileds added later //

                                //Aadhaar Json Fields
                                JSONObject aadhaarJsonObj = obj.optJSONObject( "adhar" );
                                String mem_profile_pic = aadhaarJsonObj.optString( "mem_profile_pic" );
                                if (mem_profile_pic != null && !mem_profile_pic.isEmpty() && !mem_profile_pic.equals( "NA" )) {
                                    Bitmap bitmap = BitmapFactory.decodeFile( Environment.getExternalStorageDirectory().getPath() + "/" + "Download/" + common.ID + "profile" + ".png" );

                                } else {

                                }
                                String member_dob = aadhaarJsonObj.optString( "member_dob" );
                                String member_permanent_address = aadhaarJsonObj.optString( "member_permanent_address" );
                                String member_sex = aadhaarJsonObj.optString( "member_sex" );
                                String member_fullname = aadhaarJsonObj.optString( "member_fullname" );
                                String member_mobile_no = aadhaarJsonObj.optString( "member_mobile_no" );

                                JSONObject profileJsonObj = obj.optJSONObject( "profile" );
                                String userprofile = profileJsonObj.optString( "userprofile" );
                                String profilePicName = null;

                                /**
                                 * To check file permission.
                                 * Create app folder
                                 */
                                requestPermissions( new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1024 );
                                int check = ActivityCompat.checkSelfPermission( getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE );
                                if (check == PackageManager.PERMISSION_GRANTED) {
                                    //Create app folder
                                    String folder_main = "Phapa";
                                    File f = new File( Environment.getExternalStorageDirectory(), folder_main );
                                    try {
                                        if (!f.exists()) {
                                            f.mkdirs();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    requestPermissions( new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1024 );
                                    String folder_main = "Phapa";
                                    File f = new File( Environment.getExternalStorageDirectory(), folder_main );
                                    try {
                                        if (!f.exists()) {
                                            f.mkdirs();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }



                                if (userprofile != null && !userprofile.isEmpty() && !userprofile.equals( "NA" )) {
                                    requestPermissions( new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1024 );
                                    String timeStamp = new SimpleDateFormat( "yyyyMMdd_HHmmss" ).format( new Date() );
                                    profilePicName = userprofile.substring( userprofile.lastIndexOf( "/" ) + 1 );

                                    Picasso.get().load( common.PHAPHAFILEURL + userprofile )
                                            .into( getTarget( profilePicName ) );

                                }
                                noteRepository.deleteProfileData();
                                if (member_id != null && !member_id.isEmpty() && !member_id.equals( "NA" )) {
                                    noteRepository.insertAdditional( member_id, member_occupation,
                                            member_local_add, village_name,
                                            area_name, reg_city,
                                            member_local_add_taluk, reg_state,
                                            member_local_add_pincode,
                                            alternative_Number, memTypeIDFK,
                                            memberTypeCode, organization_id,
                                            organization_name, agent_id,
                                            agent_name, member_dob,
                                            member_permanent_address, member_sex, member_fullname,
                                            member_mobile_no,
                                            profilePicName, Member_group_idfk,
                                            member_groupName,
                                            common.NOCHANGE, primary_org_id,
                                            organisation_group_type,
                                            email,
                                            marital_status,
                                            membership_status,
                                            date_Of_joining );
                                }
                            }
                        } );

                    } else {
                        Log.d( TAG, "I am in else" );
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } );

    }

    private void getMasterNotifications() {

        okhttp3.OkHttpClient client = new okhttp3.OkHttpClient();

        okhttp3.MediaType mediaType = okhttp3.MediaType.parse( "application/x-www-form-urlencoded" );
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url( common.PHAPAURL + "ChatConverstion_cntrl/notification" )
                .get()
                .addHeader( "Auth-key", "simplerestapi" )
                .addHeader( "Content-Type", "application/x-www-form-urlencoded" )
                .addHeader( "Authorization", common.TOKEN )
                .addHeader( "User-ID", common.ID )
                .addHeader( "Client-Service", "frontend-client" )
                .build();
        client.newCall( request ).enqueue( new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {

                final OrganizationRepository noteRepository = new OrganizationRepository( getApplicationContext() );
                final String myResponse = response.body().string();

                try {


                    JSONObject jsn = new JSONObject( myResponse );
                    if (jsn.has( "data" )) {

                        JSONArray arr = jsn.getJSONArray( "data" );
                        if (arr.length() != 0) {
                            for (int i = 0; i < arr.length(); i++) {
                                String message_to_id = arr.getJSONObject( i ).optString( "message_to_id" );
                                String notification_id = arr.getJSONObject( i ).optString( "notification_id" );
                                String reply_allowed = arr.getJSONObject( i ).optString( "reply_allowed" );
                                String message_subject = arr.getJSONObject( i ).optString( "message_subject" );
                                String parent_messaage_id = arr.getJSONObject( i ).optString( "parent_messaage_id" );
                                String organization_idfk = arr.getJSONObject( i ).optString( "organization_idfk" );
                                String organization_name = arr.getJSONObject( i ).optString( "organization_name" );
                                String message_from = arr.getJSONObject( i ).optString( "message_from" );
                                String message_type_idfk = arr.getJSONObject( i ).optString( "message_type_idfk" );
                                String message = arr.getJSONObject( i ).optString( "message" );
                                String message_to = arr.getJSONObject( i ).optString( "message_to" );
                                String read = arr.getJSONObject( i ).optString( "read" );
                                String syn_status = arr.getJSONObject( i ).optString( "syn_status" );


                                // now inserting data to local db//
                                noteRepository.insertMasterNotification( message_to_id, notification_id, reply_allowed, message_subject,
                                        parent_messaage_id, organization_idfk, organization_name, message_from, message_type_idfk, message, message_to, read, syn_status );

                            }

                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } );
    }

    private void getOrganizationGroupMaster() {
        okhttp3.OkHttpClient client = new okhttp3.OkHttpClient();
        String extendedUrl = "List_cntrl/MemberGroupMaster";
        final OrganizationRepository noteRepository = new OrganizationRepository( getApplicationContext() );
        // put your json here
        final okhttp3.Request request = new okhttp3.Request.Builder()
                .url( common.PHAPAURL + extendedUrl )
                .addHeader( "Client-Service", "frontend-client" )
                .addHeader( "Auth-key", "simplerestapi" )
                .addHeader( "Content-Type", "application/json" )
                .addHeader( "User-ID", common.ID )
                .addHeader( "Authorization", common.TOKEN )
                .addHeader( "Accept", "*/*" )
                .build();
        client.newCall( request ).enqueue( new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d( TAG, "Not able to connect member organization master" );
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                final String myResponse = response.body().string();

                try {

                    if (myResponse.contains( "data" )) {
                        runOnUiThread( new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject object = new JSONObject( myResponse );
                                    JSONArray data = object.getJSONArray( "data" );
                                    for (int i = 0; i < data.length(); i++) {
                                        JSONObject orgGroup = data.getJSONObject( i );
                                        String memberGroupId = orgGroup.getString( "member_group_id" );
                                        String memberGroup = orgGroup.getString( "member_group" );
                                        noteRepository.setOrganizationGroupMaster( memberGroupId, memberGroup );
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } );
                    } else {
                        Log.d( TAG, " Member Type List Not found " );
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } );
    }

    public void getSupportConversationHistory() {
        okhttp3.OkHttpClient client = new okhttp3.OkHttpClient();
        String extendedUrl = "ChatConverstion_cntrl/getALLSupportConversationHistory";
        final OrganizationRepository noteRepository = new OrganizationRepository( getApplicationContext() );
        // put your json here
        final okhttp3.Request request = new okhttp3.Request.Builder()
                .url( common.PHAPAURL + extendedUrl )
                .addHeader( "Client-Service", "frontend-client" )
                .addHeader( "Auth-key", "simplerestapi" )
                .addHeader( "Content-Type", "application/json" )
                .addHeader( "User-ID", common.ID )
                .addHeader( "Authorization", common.TOKEN )
                .addHeader( "Accept", "*/*" )
                .build();
        client.newCall( request ).enqueue( new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d( TAG, "Not able to connect member organization master" );
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                final String myResponse = response.body().string();

                try {

                    try {
                        JSONObject object = new JSONObject( myResponse );
                        JSONArray data = object.getJSONArray( "data" );
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject orgGroup = data.getJSONObject( i );

                            String organizationId = orgGroup.getString( "organization_id" );
                            String memberId = orgGroup.getString( "member_id" );
                            String MessageForm = orgGroup.getString( "Message_form" );
                            String MessageTo = orgGroup.getString( "Message_to" );
                            String Message = orgGroup.getString( "Message" );
                            String attachmentName = orgGroup.getString( "attachment_name" );
                            String fileExt = orgGroup.getString( "file_ext" );
                            String filePath = orgGroup.getString( "file_path" );
                            String mimeTyp = orgGroup.getString( "mime_typ" );
                            String ParentId = orgGroup.getString( "Parent_id" );
                            String read = orgGroup.getString( "read" );
                            String synStatus = orgGroup.getString( "syn_status" );
                            String createdDate = orgGroup.getString( "created_date" );
                            String file_path = orgGroup.optString( "file_path", "" );
                            String local_filepath = "";

                            String fileUrl = common.PHAPHAFILEURL + file_path + attachmentName;


                            if (!attachmentName.equalsIgnoreCase( "" )) {
                                local_filepath = "/Phapa/Documents/" + attachmentName;
                                downloadFileFromServer( attachmentName, fileUrl );
                            }




                            /*downloading the chat images into documents folder*/
                            noteRepository.setSupportConversationHistory( organizationId, memberId,
                                    MessageForm, MessageTo, Message, attachmentName, fileExt, filePath,
                                    mimeTyp, ParentId, read, synStatus, createdDate, local_filepath );
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d( TAG, "run: " + e.getMessage() );
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } );
    }

    private void getPrimaryOrgGroup() {
        okhttp3.OkHttpClient client = new okhttp3.OkHttpClient();
        String extendedUrl = "List_cntrl/GetOrganizationGroupMaster";
        final OrganizationRepository noteRepository = new OrganizationRepository( getApplicationContext() );
        // put your json here
        final okhttp3.Request request = new okhttp3.Request.Builder()
                .url( common.PHAPAURL + extendedUrl )
                .addHeader( "Client-Service", "frontend-client" )
                .addHeader( "Auth-key", "simplerestapi" )
                .addHeader( "Content-Type", "application/json" )
                .addHeader( "User-ID", common.ID )
                .addHeader( "Authorization", common.TOKEN )
                .addHeader( "Accept", "*/*" )
                .build();
        client.newCall( request ).enqueue( new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d( TAG, "Not able to connect member organization master" );
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                final String myResponse = response.body().string();

                try {
                    //final JSONArray jsonArray = new JSONArray(myResponse);
                    if (myResponse.contains( "data" )) {
                        runOnUiThread( new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject object = new JSONObject( myResponse );
                                    JSONArray data = object.getJSONArray( "data" );
                                    for (int i = 0; i < data.length(); i++) {
                                        JSONObject orgGroup = data.getJSONObject( i );
                                        String primaryOrgId = orgGroup.getString( "primary_org_id" );
                                        String organisationGroupType = orgGroup.getString( "organisation_group_type" );
                                        String organisationGroupTypeCode = orgGroup.getString( "organisation_group_type_code" );
                                        noteRepository.setprimaryOrgGroupMaster( primaryOrgId, organisationGroupType, organisationGroupTypeCode );
                                    }
                                } catch (Exception e) {

                                    e.printStackTrace();
                                }
                            }
                        } );

                    } else {

                        Log.d( TAG, " Member Type List Not found " );
                    }
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }

        } );


    }

    private void getAllServiceChatHistory() {
        /* create foldr to save chat documnets */
        requestPermissions( new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1024 );
        int check = ActivityCompat.checkSelfPermission( getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE );
        if (check == PackageManager.PERMISSION_GRANTED) {
            //Create app folder
            String folder_main = "Phapa/Documents";
            File f = new File( Environment.getExternalStorageDirectory(), folder_main );
            try {
                if (!f.exists()) {
                    f.mkdirs();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            requestPermissions( new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1024 );
            String folder_main = "Phapa/Documents";
            File f = new File( Environment.getExternalStorageDirectory(), folder_main );
            try {
                if (!f.exists()) {
                    f.mkdirs();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        /* create foldr to save chat documnets */

        /*server call */
        okhttp3.OkHttpClient client = new okhttp3.OkHttpClient();

        okhttp3.MediaType mediaType = okhttp3.MediaType.parse( "application/x-www-form-urlencoded" );
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url( common.PHAPAURL + "ChatConverstion_cntrl/getALLConverstionServiceHistory" )
                .get()
                .addHeader( "Auth-key", "simplerestapi" )
                .addHeader( "Content-Type", "application/x-www-form-urlencoded" )
                .addHeader( "Authorization", common.TOKEN )
                .addHeader( "User-ID", common.ID )
                .addHeader( "Client-Service", "frontend-client" )
                .build();
        client.newCall( request ).enqueue( new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {

                final OrganizationRepository noteRepository = new OrganizationRepository( getApplicationContext() );
                final String myResponse = response.body().string();



                try {
                    JSONObject jsn = new JSONObject( myResponse );
                    JSONArray arr = jsn.getJSONArray( "data" );
                    if (arr.length() != 0) {
                        for (int i = 0; i < arr.length(); i++) {
                            String id = arr.getJSONObject( i ).optString( "id", "" );
                            String Service_Msg_id = arr.getJSONObject( i ).optString( "Service_Msg_id", "" );
                            String Service_id = arr.getJSONObject( i ).optString( "Service_id", "" );
                            String organization_id = arr.getJSONObject( i ).optString( "organization_id", "" );
                            String member_id = arr.getJSONObject( i ).optString( "member_id", "" );
                            String Message_form = arr.getJSONObject( i ).optString( "Message_form", "" );
                            String Message_to = arr.getJSONObject( i ).optString( "Message_to", "" );
                            String Message = arr.getJSONObject( i ).optString( "Message", "" );
                            String attachment_name = arr.getJSONObject( i ).optString( "attachment_name", "" );
                            String file_ext = arr.getJSONObject( i ).optString( "file_ext", "" );
                            String mime_typ = arr.getJSONObject( i ).optString( "mime_typ", "" );
                            String Parent_id = arr.getJSONObject( i ).optString( "Parent_id", "" );
                            String read = arr.getJSONObject( i ).optString( "read", "" );
                            String syn_status = arr.getJSONObject( i ).optString( "syn_status", "" );
                            String created_date = arr.getJSONObject( i ).optString( "created_date", "" );
                            String file_path = arr.getJSONObject( i ).optString( "file_path", "" );
                            String local_filepath = "";
                            /*downloading the chat images into documents folder*/


                            String fileUrl = common.PHAPHAFILEURL + file_path + attachment_name;


                            if (!attachment_name.equalsIgnoreCase( "" )) {
                                local_filepath = "/Phapa/Documents/" + attachment_name;
                                downloadFileFromServer( attachment_name, fileUrl );
                            }



                            /*downloading the chat images into documents folder*/

                            // now inserting data to local db//
                            noteRepository.insertAllServiceChatHistory(
                                    Service_Msg_id, Service_id,
                                    organization_id, member_id,
                                    Message_form, Message_to, Message, attachment_name, file_ext,
                                    mime_typ,
                                    Parent_id, read, syn_status, created_date, file_path, local_filepath );

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } );
    }

    private void SaveImage(Bitmap finalBitmap, String filename) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File( root + "/Phapa/Documents" );
        if (!myDir.exists()) {
            myDir.mkdirs();
        }

        File file = new File( myDir, filename );
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream( file );
            finalBitmap.compress( Bitmap.CompressFormat.JPEG, 90, out );
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getAllNotificationChatHistory() {


        /* create foldr to save chat documnets */
        requestPermissions( new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1024 );
        int check = ActivityCompat.checkSelfPermission( getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE );
        if (check == PackageManager.PERMISSION_GRANTED) {
            //Create app folder
            String folder_main = "Phapa/Documents";
            File f = new File( Environment.getExternalStorageDirectory(), folder_main );
            try {
                if (!f.exists()) {
                    f.mkdirs();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            requestPermissions( new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1024 );
            String folder_main = "Phapa/Documents";
            File f = new File( Environment.getExternalStorageDirectory(), folder_main );
            try {
                if (!f.exists()) {
                    f.mkdirs();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        /* create foldr to save chat documnets */


        okhttp3.OkHttpClient client = new okhttp3.OkHttpClient();

        okhttp3.MediaType mediaType = okhttp3.MediaType.parse( "application/x-www-form-urlencoded" );
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url( common.PHAPAURL + "ChatConverstion_cntrl/getALLConverstionNotificationHistory" )
                .get()
                .addHeader( "Auth-key", "simplerestapi" )
                .addHeader( "Content-Type", "application/x-www-form-urlencoded" )
                .addHeader( "Authorization", common.TOKEN )
                .addHeader( "User-ID", common.ID )
                .addHeader( "Client-Service", "frontend-client" )
                .build();
        client.newCall( request ).enqueue( new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {

                final OrganizationRepository noteRepository = new OrganizationRepository( getApplicationContext() );
                final String myResponse = response.body().string();
                Log.e("Chat History",myResponse+"");

                try {
                    JSONObject jsn = new JSONObject( myResponse );
                    JSONArray arr = jsn.getJSONArray( "data" );
                    if (arr.length() != 0) {
                        for (int i = 0; i < arr.length(); i++) {
                            String id = arr.getJSONObject( i ).optString( "id", "" );
                            String notification_idfk = arr.getJSONObject( i ).optString( "notification_idfk", "" );
                            String organization_id = arr.getJSONObject( i ).optString( "organization_id", "" );
                            String member_id = arr.getJSONObject( i ).optString( "member_id", "" );
                            String Message_form = arr.getJSONObject( i ).optString( "Message_form", "" );
                            String Message_to = arr.getJSONObject( i ).optString( "Message_to", "" );
                            String Message = arr.getJSONObject( i ).optString( "Message", "" );
                            String attachment_name = arr.getJSONObject( i ).optString( "attachment_name", "" );
                            String file_ext = arr.getJSONObject( i ).optString( "file_ext", "" );
                            String mime_typ = arr.getJSONObject( i ).optString( "mime_typ", "" );
                            String Parent_id = arr.getJSONObject( i ).optString( "Parent_id", "" );
                            String read = arr.getJSONObject( i ).optString( "read", "" );
                            String syn_status = arr.getJSONObject( i ).optString( "syn_status", "" );
                            String created_date = arr.getJSONObject( i ).optString( "created_date", "" );
                            String file_path = arr.getJSONObject( i ).optString( "file_path", "" );
                            String local_filepath = "";
                            /*downloading the chat images into documents folder*/
                            String fileUrl = common.PHAPHAFILEURL + file_path + attachment_name;


                            if (!attachment_name.equalsIgnoreCase( "" )) {
                                local_filepath = "/Phapa/Documents/" + attachment_name;
                                downloadFileFromServer( attachment_name, fileUrl );
                            }


                            /*downloading the chat images into documents folder*/


                            // now inserting data to local db//
                            noteRepository.insertAllChatHistory(
                                    notification_idfk,
                                    organization_id, member_id,
                                    Message_form, Message_to, Message, attachment_name, file_ext,
                                    mime_typ,
                                    Parent_id, read, syn_status, created_date, file_path, local_filepath );

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } );
    }

    private void getMasterServiceList() {
        okhttp3.OkHttpClient client = new okhttp3.OkHttpClient();

        okhttp3.MediaType mediaType = okhttp3.MediaType.parse( "application/x-www-form-urlencoded" );
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url( common.PHAPAURL + "List_cntrl/organizationServicesList" )
                .get()
                .addHeader( "Auth-key", "simplerestapi" )
                .addHeader( "Content-Type", "application/x-www-form-urlencoded" )
                .addHeader( "Authorization", common.TOKEN )
                .addHeader( "User-ID", common.ID )
                .addHeader( "Client-Service", "frontend-client" )
                .build();
        client.newCall( request ).enqueue( new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                final OrganizationRepository noteRepository = new OrganizationRepository( getApplicationContext() );
                final String myResponse = response.body().string();
                Log.w( TAG, "get all master service requesting list: " + myResponse );
                try {
                    JSONObject jsn = new JSONObject( myResponse );
                    JSONArray arr = jsn.getJSONArray( "data" );
                    if (arr.length() != 0) {
                        for (int i = 0; i < arr.length(); i++) {
                            String organization_idfk = arr.getJSONObject( i ).optString( "organization_idfk", "" );
                            String Service_ID = arr.getJSONObject( i ).optString( "Service_ID", "" );
                            String Service_Name = arr.getJSONObject( i ).optString( "Service_Name", "" );
                            String Duration_To_Complete = arr.getJSONObject( i ).optString( "Duration_To_Complete", "" );
                            String Service_Description = arr.getJSONObject( i ).optString( "Service_Description", "" );
                            String serviceFee = arr.getJSONObject( i ).optString( "serviceFee", "" );
                            String organization_name = arr.getJSONObject( i ).optString( "organization_name", "" );
                            String partner_id = arr.getJSONObject( i ).optString( "partner_id", "" );
                            String partner_name = arr.getJSONObject( i ).optString( "partner_name", "" );

                            // additional fields added later //

                            String org_service_id = "";
                            String eligibility = "";
                            String documents = "";
                            String process = "";
                            String co_ordionator = "";


                            // additional fields added later //

                            // now inserting data to local db//
                            noteRepository.insertMasterServiceList( organization_idfk, Service_ID,
                                    Service_Name, Duration_To_Complete,
                                    Service_Description, serviceFee, organization_name, partner_id,
                                    partner_name, org_service_id,
                                    eligibility, documents, process, co_ordionator );

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } );
    }


    protected void startAnimation(String message) {

        if (this == null)
            return;
        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );
        } else {
            setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE );
        }

        if (dialog == null) {
            dialog = new ProgressDialog( this, R.style.transparent_dialog );
            dialog.setCanceledOnTouchOutside( false );
        }

        final String msg = message;
        dialog.setMessage( msg );
        dialog.show();
    }

    protected void stopAnimation() {

        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
        setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_SENSOR );
    }

    private Target getTarget(String profilePicName) {
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {

                File file = new File( Environment.getExternalStorageDirectory().getPath() + "/" + "Phapa/" + profilePicName );
                if (file.exists())
                    file.delete();
                try {
                    file.createNewFile();
                    FileOutputStream ostream = new FileOutputStream( file );
                    bitmap.compress( Bitmap.CompressFormat.JPEG, 80, ostream );
                    ostream.flush();
                    ostream.close();

                } catch (IOException e) {
                    Log.e( "IOException", e.getLocalizedMessage() );
                }
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        image.setTag( target );
        return target;
    }



    private Target getTargetDocuments(String filename) {
        Target target = new Target() {

            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread( new Runnable() {
                    @Override
                    public void run() {
                        File file = new File( Environment.getExternalStorageDirectory().getPath() + "/" + "Phapa" + filename );
                        if (file.exists())
                            file.delete();
                        try {
                            file.createNewFile();
                            FileOutputStream ostream = new FileOutputStream( file );
                            bitmap.compress( Bitmap.CompressFormat.JPEG, 80, ostream );
                            ostream.flush();
                            ostream.close();

                        } catch (IOException e) {
                            Log.e( "IOException", e.getLocalizedMessage() );
                        }
                    }
                } ).start();

            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        return target;
    }

    public void onButtonPress() {
        btnLogin.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnLogin.setEnabled( false );
                boolean internetStatus = isNetworkConnected();
                if (!internetStatus) {
                    Toast.makeText( getApplicationContext(), "Please connect" +
                            " Internet", Toast.LENGTH_LONG ).show();
                    return;
                }
                final String phoneNumber = getMobNO();

                if (phoneNumber.equals( "" ) || phoneNumber.equals( null )) {
                    Toast.makeText( getApplicationContext(), "Please enter" +
                            "10 Digit Number", Toast.LENGTH_SHORT ).show();
                    return;
                }
                if (phoneNumber.length() < 10) {
                    Toast.makeText( getApplicationContext(), "Please enter" +
                            "10 Digit Number", Toast.LENGTH_LONG ).show();
                    return;
                }


                //  verify otp //

                runOnUiThread( new Runnable() {
                    @Override
                    public void run() {
                        startAnimation( "" );
                    }
                } );

                String mobilenumber = getMobNO();
                String enteredOtp = getOtpEntered();
                OkHttpClient client = new OkHttpClient();
                RequestBody reqbody = RequestBody.create( null, new byte[0] );

                Request request = new Request.Builder()
                        .url( "http://control.msg91.com/api/verifyRequestOTP.php?authkey=303643A12XmkZV5dcbf28a&mobile=91" + mobilenumber + "&otp=" + enteredOtp )
                        .post( reqbody )
                        .addHeader( "Accept", "*/*" )
                        .build();
                client.newCall( request ).enqueue( new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();


                        LoginInfo.this.runOnUiThread( new Runnable() {
                            @Override
                            public void run() {
                                stopAnimation();
                                btnLogin.setEnabled( true );
                                Toast.makeText( LoginInfo.this, "Something went wrong ..please try again " + e.getMessage().toString(), Toast.LENGTH_SHORT ).show();
                            }
                        } );
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String myresponse = response.body().string();
                        Log.w( TAG, "verify otp = " + myresponse );

                        try {
                            JSONObject json = new JSONObject( myresponse );
                            String res = json.getString( "message" );
                            String res2 =  json.getString( "type" );

                            if ((res.equalsIgnoreCase( "otp_verified" )) && (res2.equalsIgnoreCase( "success" ))) {
                                //after Otp is verified the login will happen//

                                LoginStart();

                            } else {
                                LoginInfo.this.runOnUiThread( new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText( LoginInfo.this, "OTP Verification Failed", Toast.LENGTH_LONG ).show();
                                        stopAnimation();
                                        btnLogin.setEnabled( true );
                                    }
                                } );
                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                            LoginInfo.this.runOnUiThread( new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText( LoginInfo.this, "Unable to connect,..please try again " + e.getMessage().toString(), Toast.LENGTH_LONG ).show();
                                    stopAnimation();
                                    btnLogin.setEnabled( true );
                                }
                            } );
                        }
                    }
                } );
                // end verify otp //


            }
        } );
        //  on set passcode btn press //

        passCodeCreateSubmitBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int count = 0;
                String passcode = edtcreatePasscode.getText().toString();
                //Counts each character except space
                for (int i = 0; i < passcode.length(); i++) {
                    if (passcode.charAt( i ) != ' ')
                        count++;
                }
                if (count < 4 || count > 4) {
                    status.setText( "Please enter 4 digit passcode" );
                    //Toast.makeText(getApplicationContext(),"Please enter 4 digit passcode",Toast.LENGTH_LONG);
                    return;
                }

                SharedPreferences.Editor editor1 = getSharedPreferences( "PASSCODEDB", MODE_PRIVATE ).edit();
                editor1.putInt( "passcode", Integer.parseInt( edtcreatePasscode.getText().toString() ) );
                String mob = getMobNO();
                editor1.apply();
                LoginInfo.this.runOnUiThread( new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText( LoginInfo.this, "New passcode is set", Toast.LENGTH_LONG ).show();
                        enterPasscodeLayout.setVisibility( View.VISIBLE );
                        createPasscodeLayout.setVisibility( View.GONE );
                        loginLayout.setVisibility( View.GONE );
                        edtcreatePasscode.setText( "" );
                    }
                } );
            }
        } );

        //  on set passcode btn press ///
        //  on passcode submit btn press ///

        passCodeSubmitBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final String phoneNumber = getMobNO();
                    SharedPreferences.Editor editor1 = getSharedPreferences( "PASSCODEDB", MODE_PRIVATE ).edit();

                    SharedPreferences prefs = getSharedPreferences( "PASSCODEDB", MODE_PRIVATE );

                    int passcode1 = prefs.getInt( "passcode", 0 );

                    if (!edtpasscode.getText().toString().equalsIgnoreCase( "" ) || !edtpasscode.getText().toString().isEmpty() || edtpasscode.getText() != null) {
                        if (passcode1 == Integer.parseInt( edtpasscode.getText().toString() )) {

                            LoginInfo.this.runOnUiThread( new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText( getApplicationContext(), "Login successful", Toast.LENGTH_LONG ).show();


                                    SharedPreferences prefs = getSharedPreferences( "PASSCODEDB", MODE_PRIVATE );
                                    String user = prefs.getString( "user", "9999" );// "9999" is the default value.
                                    String id = prefs.getString( "id", "noid" );
                                    common.ID = id;
                                    Log.d( "commonID", id );

                                    String token = prefs.getString( "token", "notoken" );
                                    common.TOKEN = token;
                                    Log.d( "Token", token );
                                    String KYC = prefs.getString( "KYC", "nostatus" );
                                    if (KYC.equalsIgnoreCase( "Not Completed" )) {
                                        Intent intent = new Intent( getApplicationContext(), DrawerActivity.class );
                                        intent.putExtra( "PhoneNo", phoneNumber );
                                        intent.putExtra( "menuid", R.id.nav_DashBoard );
//                                      intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity( intent );
                                        finish();
                                    } else if (KYC.equalsIgnoreCase( "Completed" )) {
                                        Intent intent = new Intent( getApplicationContext(), DrawerActivity.class );
                                        intent.putExtra( "PhoneNo", phoneNumber );
                                        intent.putExtra( "menuid", R.id.nav_DashBoard );
//                                      intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity( intent );
                                        finish();
                                    }


                                }
                            } );
                        } else {
                            LoginInfo.this.runOnUiThread( new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText( LoginInfo.this, "Incorrect Passcode", Toast.LENGTH_LONG ).show();

                                }
                            } );
                        }
                    } else {
                        runOnUiThread( () -> {
                            Toast.makeText( LoginInfo.this, "Please enter passcode to verify", Toast.LENGTH_LONG ).show();
                        } );
                    }


                } catch (Exception e) {
                    Toast.makeText( LoginInfo.this, "Please enter your passcode to verify", Toast.LENGTH_LONG ).show();
                }
            }
        } );

        //  on passcode submit btn press ///


        // reset passcode //

        resetPassCode.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLogin.setEnabled( true );
                btngenotp.setEnabled( true );

                final OrganizationRepository noteRepository = new OrganizationRepository( getApplicationContext() );
                noteRepository.removeAdditionalProfileDB();
                noteRepository.removeMemberTypeMaster();
                noteRepository.removePrimaryOrganization();
                noteRepository.removeCoordinatorName();
                noteRepository.removeJoinedOrganization();
                noteRepository.removeKYCAadhar();
                noteRepository.removeAllChatHistory();
                noteRepository.removeMasterNotification();
                noteRepository.removeMasterServiceList();
                noteRepository.removeOrganizationGroup();
                noteRepository.removeAllChatServiceHistory();
                noteRepository.removeSupportConvHistory();
                noteRepository.removePrimaryOrgGroup();
                noteRepository.closeDB();
                common.ORG_NAME = "";
                common.SELECTEDORGID = "";

                enterPasscodeLayout.setVisibility( View.GONE );
                createPasscodeLayout.setVisibility( View.GONE );
                loginLayout.setVisibility( View.VISIBLE );

                SharedPreferences preferences = getSharedPreferences( "PASSCODEDB", MODE_PRIVATE );
                SharedPreferences.Editor editor2 = preferences.edit();
                editor2.remove( "passcode" );
                //                editor2.clear();
                editor2.commit();
                String mobile = getMobNO();
                SharedPreferences.Editor editor1 = getSharedPreferences( "PASSCODEDB", MODE_PRIVATE ).edit();

                SharedPreferences prefs = getSharedPreferences( "PASSCODEDB", MODE_PRIVATE );
                String username = prefs.getString( "USERNAME", "" );
                String mob = prefs.getString( "user", "" );
                runOnUiThread( () -> {
                    edt_otp.setText( "" );
                    edtmobno.setText( mob );
                    edtmobno.setEnabled( false );
                    edt_name.setText( username );
                    edt_name.setEnabled( false );
                } );

            }
        } );

        // reset passcode //


        //        generate otp api  //

        btngenotp.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isNetworkConnected()) {
                    startAnimation( "" );
                    try {
                        InputMethodManager imm = (InputMethodManager) getSystemService( INPUT_METHOD_SERVICE );
                        imm.hideSoftInputFromWindow( getCurrentFocus().getWindowToken(), 0 );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    btngenotp.setEnabled( false );
                    String mobNumber = getMobNO();
                    if (mobNumber.length()<10){
                        btngenotp.setEnabled( true );
                        stopAnimation();
                        Toast.makeText( LoginInfo.this, "Please enter 10 digit Mobile number", Toast.LENGTH_LONG ).show();

                    }else {
                        generateOtp();
                    }

                } else {
                    Toast.makeText( LoginInfo.this, "Please Check Internet Connection", Toast.LENGTH_LONG ).show();
                    return;
                }

            }
        } );
        //   end generate otp api  //

        // resend otp //

        resendtextview.setOnClickListener( v -> {
//            resendOTP();
            generateOtp();
        } );


        // resend otp //


    }

    private void generateOtp() {
        String mobNumber = getMobNO();

        OkHttpClient client = new OkHttpClient();
        RequestBody reqbody = RequestBody.create( null, new byte[0] );

        Request request = new Request.Builder()
                .url( "https://api.msg91.com/api/sendotp.php?otp_length=6&&otp_expiry=10&authkey=303643A12XmkZV5dcbf28a&mobile=91" + mobNumber + "&message=" )
                .post( reqbody )
                .addHeader( "Accept", "*/*" )
                .build();

        client.newCall( request ).enqueue( new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                stopAnimation();
                e.printStackTrace();
                runOnUiThread( () -> {
                    stopAnimation();
                    btngenotp.setEnabled( true );
                    Toast.makeText( LoginInfo.this, "Unable to connect, please try again later", Toast.LENGTH_LONG ).show();
                } );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String myResponse = response.body().string();
                try {
                    JSONObject json = new JSONObject( myResponse );
                    String res = json.getString( "type" );
                    String mob = getMobNO();
                    if (res.equalsIgnoreCase( "success" )) {
                        stopAnimation();
                        LoginInfo.this.runOnUiThread( new Runnable() {
                            @Override
                            public void run() {
                                String mob = getMobNO();
                                // starts count down timer ..
                                startCountdown();
                                Toast.makeText( LoginInfo.this, "OTP sent to " + mob, Toast.LENGTH_LONG ).show();
                            }
                        } );

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    stopAnimation();
                    runOnUiThread( () -> {
                        stopAnimation();
                        Toast.makeText( LoginInfo.this, "Unable to connect, please try again later", Toast.LENGTH_LONG  ).show();
                    } );
                }

            }
        } );
    }

    private void startCountdown() {
        runOnUiThread( () -> {
            resendtextview.setVisibility( View.GONE );
            expiresIn.setVisibility( View.VISIBLE );
        } );
        new CountDownTimer( 45000, 1000 ) {

            public void onTick(long millisUntilFinished) {
                runOnUiThread( () -> {
                    expiresIn.setText( "Resend OTP in: " + millisUntilFinished / 1000 + " Sec" );
                } );
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                runOnUiThread( () -> {
                    expiresIn.setVisibility( View.GONE );
                    resendtextview.setVisibility( View.VISIBLE );
                } );
            }

        }.start();


    }

    private void LoginStart() {

        // phapa login start
        final String phoneNumber = getMobNO();
        final JSONObject jsonBody1 = new JSONObject();
        try {
            String name = edt_name.getText().toString();
            common.name = name;
            jsonBody1.put( "username", name );
            jsonBody1.put( "phoneNumber", phoneNumber );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse( "application/json" );
        MediaType JSON = MediaType.parse( "application/json; charset=utf-8" );
        // put your json here
        final RequestBody body1 = RequestBody.create( JSON, jsonBody1.toString() );
        Request request = new Request.Builder()
                .url( common.PHAPAURL + "auth/signup" )
                .post( body1 )
                .addHeader( "Client-Service", "frontend-client" )
                .addHeader( "Auth-key", "simplerestapi" )
                .addHeader( "Content-Type", "application/json" )
                .addHeader( "Accept", "*/*" )
                .build();
        client.newCall( request ).enqueue( new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                LoginInfo.this.runOnUiThread( new Runnable() {
                    @Override
                    public void run() {
                        stopAnimation();
                        btnLogin.setEnabled( true );
                        Toast.makeText( LoginInfo.this, "Login Failure " + e.getMessage(), Toast.LENGTH_LONG ).show();
                    }
                } );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String myresponse = response.body().string();
                Log.w( TAG, "phapa login" + myresponse );


                try {
                    JSONObject json = new JSONObject( myresponse );
                    String phapaLoginStatus = json.getString( "status" );
                    String phapaLoginMessage = json.getString( "message" );
                    String phapaUserId = json.getString( "id" );
                    String phapaToken = json.getString( "token" );
                    String isRegisteredPhapaUser = json.getString( "Register" );
                    String isKycDone = json.getString( "KYC" );

                    // store token,id,isRegistered,IsKyc in shared pref//
                    SharedPreferences.Editor editor1 = getSharedPreferences( "PASSCODEDB", MODE_PRIVATE ).edit();
                    editor1.putString( "id", phapaUserId );
                    editor1.putString( "token", phapaToken );
                    editor1.putString( "KYC", isKycDone );
                    editor1.putString( "USERNAME", edt_name.getText().toString() );
                    editor1.apply();


                    // store token,id,isRegistered,IsKyc in shared pref//

                    if (phapaLoginStatus.equalsIgnoreCase( "200" ) &&
                            phapaLoginMessage.equalsIgnoreCase( "Successfully login." )) {


                        LoginInfo.this.runOnUiThread( new Runnable() {
                            @Override
                            public void run() {

                                new Thread( new Runnable() {
                                    public void run() {
                                        SharedPreferences prefs = getSharedPreferences( "PASSCODEDB", MODE_PRIVATE );
                                        String user = prefs.getString( "user", "9999" );// "9999" is the default value.
                                        String id = prefs.getString( "id", "noid" );
                                        common.ID = id;
                                        String token = prefs.getString( "token", "notoken" );
                                        common.TOKEN = token;
                                        getMasterServiceList();
                                        getAllNotificationChatHistory();
                                        getAdditionalProfile();
                                        getAllServiceChatHistory();
                                        getMemberTypeMasterDropDown();
                                        getCoordinatorNameDropDown();
                                        getPrimaryOrganizationDropDown();
                                        getJoinedOrganization();
                                        getMasterNotifications();
                                        getOrganizationGroupMaster();
                                        getSupportConversationHistory();
                                        getPrimaryOrgGroup();

                                        SharedPreferences sharedPreferences = getSharedPreferences( "LoginDetails", MODE_PRIVATE );
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString( "Status", "Verified" );
                                        editor.putString( "PhoneNo", phoneNumber );
                                        editor.commit();

                                        //  passcode verify //
                                        //created shared preference for passcode //
                                        LoginInfo.this.runOnUiThread( new Runnable() {
                                            @Override
                                            public void run() {
                                                SharedPreferences prefs = getSharedPreferences( "PASSCODEDB", MODE_PRIVATE );
                                                String user = prefs.getString( "user", "9999" );// "9999" is the default value.

                                                if (user.equalsIgnoreCase( "9999" ) || user.equalsIgnoreCase( edtmobno.getText().toString() )) // if passcode is zero then get it entered by user
                                                {
                                                    SharedPreferences.Editor editor1 = getSharedPreferences( "PASSCODEDB", MODE_PRIVATE ).edit();
                                                    editor1.putString( "user", phoneNumber );
                                                    editor1.putString( "username", edt_name.getText().toString() );
                                                    editor1.apply();
                                                    enterPasscodeLayout.setVisibility( View.GONE );
                                                    createPasscodeLayout.setVisibility( View.VISIBLE );
                                                    loginLayout.setVisibility( View.GONE );

                                                } else if (user.equalsIgnoreCase( edtmobno.getText().toString() )) {
                                                    enterPasscodeLayout.setVisibility( View.VISIBLE );
                                                    createPasscodeLayout.setVisibility( View.GONE );
                                                    loginLayout.setVisibility( View.GONE );
                                                }
                                            }
                                        } );
                                        //   passcode verify //


                                    }
                                } ).start();

                            }
                        } );

                        // login with icl ends //


                    } else {

                        LoginInfo.this.runOnUiThread( new Runnable() {
                            @Override
                            public void run() {
                                stopAnimation();
                                btnLogin.setEnabled( true );
                                Toast.makeText( LoginInfo.this, "Login Failure", Toast.LENGTH_LONG ).show();
                            }
                        } );

                    }


                } catch (JSONException e) {

                    e.printStackTrace();
                    LoginInfo.this.runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            stopAnimation();
                            btnLogin.setEnabled( true );
                            Toast.makeText( LoginInfo.this, "Login Failure " + e.getMessage(), Toast.LENGTH_LONG ).show();
                        }
                    } );
                }


            }
        } );

        // Phapa Login //

    }

    public String getOtpEntered() {

        return edt_otp.getText().toString().replaceAll( "\\D", "" ).trim();
    }

    public void resendOTP() {
        startAnimation( "" );
        String mobNumber = getMobNO();
        OkHttpClient client = new OkHttpClient();
        RequestBody reqbody = RequestBody.create( null, new byte[0] );

        Request request = new Request.Builder()
                .url( "https://api.msg91.com/api/v5/otp/retry?authkey=303643A12XmkZV5dcbf28a&mobile=91" + mobNumber + "&retrytype=text" )
                .post( reqbody )
                .addHeader( "Accept", "*/*" )
                .build();
        client.newCall( request ).enqueue( new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                stopAnimation();
                runOnUiThread( () -> {
                    stopAnimation();
                    Toast.makeText( LoginInfo.this, "Something went wrong please try again later", Toast.LENGTH_LONG ).show();
                } );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String myRespose = response.body().string();
                try {
                    JSONObject json = new JSONObject( myRespose );
                    String res = json.getString( "type" );
                    String mob = getMobNO();
                    if (res.equalsIgnoreCase( "success" )) {

                        runOnUiThread( () -> {
                            stopAnimation();

                            // starts count down timer ..
                            startCountdown();
                            Toast.makeText( LoginInfo.this, "OTP sent successfully to " + mob, Toast.LENGTH_LONG ).show();
                        } );

                    } else if (res.equalsIgnoreCase( "error" )) {
                        runOnUiThread( () -> {
                            stopAnimation();
                            resendtextview.setVisibility( View.GONE );
                            Toast.makeText( LoginInfo.this, "Something went wrong please try again later", Toast.LENGTH_LONG ).show();
                        } );
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    stopAnimation();
                    runOnUiThread( () -> {
                        stopAnimation();
                        Toast.makeText( LoginInfo.this, "Something went wrong please try again later", Toast.LENGTH_LONG ).show();
                    } );
                }
            }
        } );
    }

    public boolean isNetworkConnected() {
        ConnectivityManager
                connmanager = (ConnectivityManager) getApplicationContext()
                .getSystemService( Context.CONNECTIVITY_SERVICE );
        NetworkInfo activeNetwork = connmanager.getActiveNetworkInfo();
        boolean isConnected = (activeNetwork != null) && (activeNetwork.isConnectedOrConnecting());
        return isConnected;
    }

    public String getMobNO() {
        return edtmobno.getText().toString().replaceAll( "\\D", "" ).trim();
    }


    public void checkSmsPermission(boolean skipPermissionCheck) {
        if (!skipPermissionCheck && ContextCompat.checkSelfPermission( this, Manifest.permission.READ_SMS ) ==
                PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.READ_SMS}, 0 );
        }
    }

    public void goToRegistration(View view) {
        Intent intent = new Intent( getApplicationContext(), Registration.class );
        startActivity( intent );
    }
}
