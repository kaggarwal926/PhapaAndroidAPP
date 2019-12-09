package com.icls.offlinekyc.function.OfflineAdhaarKyc;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.icls.offlinekyc.R;
import com.icls.offlinekyc.commonshare.common;
import com.icls.offlinekyc.function.DrawerActivity;
import com.icls.offlinekyc.function.additionalProfile;
import com.icls.offlinekyc.roomdb.OrganizationRepository;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;

public class OfflineKycAuto extends Fragment {

    public final static String MYEIDURL = "https://myeidapi.co.in:8443/ICL_ReaLId/eKYC";
    public final static String APIKEY = "f3feeb5ebeed293f6983589d3851b486599b6ec46f65e66caa4e4826f2e146ec";

    private ProgressDialog dialog = null;
    private static final String TAG = "OfflineKycAuto";
    public String captchaImagestring = "";
    public static String sessionkey = "";
    View view;
    TextInputEditText aadhaar_no, securitycode, aadhaarOtp, sharecode;
    Button proceedbtn, send_otpbtn, proceedforkyc;
    ImageView captcha;
    CheckBox consentekyc;
    ConstraintLayout screen1, screen2, screen3;
    ScrollView scrollviewaadhaar;
    ImageButton reloadCaptchaImagebtn;
    public String phoneNumber;

    @SuppressLint("ValidFragment")
    public OfflineKycAuto(String phoneNumber) {
        // Required empty public constructor
        this.phoneNumber = phoneNumber;
    }
    public OfflineKycAuto() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate( R.layout.fragment_offline_kyc_auto, container, false );


        init();
        Buttonpress();

        return view;
    }

    private void init() {
        screen1 = view.findViewById( R.id.screen1 );
        screen2 = view.findViewById( R.id.screen2 );
        screen3 = view.findViewById( R.id.screen3 );
        reloadCaptchaImagebtn = view.findViewById( R.id.reloadCaptchaImagebtn );
        consentekyc = view.findViewById( R.id.consentekyc );
        proceedforkyc = view.findViewById( R.id.proceedforkyc );
        aadhaar_no = view.findViewById( R.id.aadhaar_no );
        securitycode = view.findViewById( R.id.securitycode );
        aadhaarOtp = view.findViewById( R.id.aadhaar_otp );
        sharecode = view.findViewById( R.id.sharecode );
        send_otpbtn = view.findViewById( R.id.send_otp );
        captcha = view.findViewById( R.id.captcha );
        proceedbtn = view.findViewById( R.id.proceed );
        scrollviewaadhaar = view.findViewById( R.id.scrollviewaadhaar );
    }

    private void Buttonpress() {

        // after the consent proceed for kyc
        proceedforkyc.setOnClickListener( v -> {
            if (consentekyc.isChecked()) {
                screen1.setVisibility( View.GONE );
                screen2.setVisibility( View.VISIBLE );
                getcaptcha();
            } else {
                getActivity().runOnUiThread( () -> {
                    Toast.makeText( getActivity(), "Please provide the consent to proceed with the KYC", Toast.LENGTH_LONG ).show();
                } );
            }
        } );

        // send otp if aadhaar and captcha is entered
        send_otpbtn.setOnClickListener( v -> {
            startAnimation( "" );
            if (aadhaar_no.getText().toString().matches( "" )) {
                stopAnimation();
                getActivity().runOnUiThread( () -> {
                    Toast.makeText( getActivity(), "Please Enter Aadhaar Number", Toast.LENGTH_LONG ).show();
                } );

            } else if (securitycode.getText().toString().matches( "" )) {
                stopAnimation();
                getActivity().runOnUiThread( () -> {
                    Toast.makeText( getActivity(), "Please Enter Captcha", Toast.LENGTH_LONG ).show();
                } );
            } else {
                getOtp();
            }
        } );

        // after entering the otp proceed to finish kyc

        proceedbtn.setOnClickListener( v -> {
            getActivity().runOnUiThread( () -> {
                startAnimation( "" );
            } );
            if (aadhaarOtp.getText().toString().matches( "" )) {
                getActivity().runOnUiThread( () -> {
                    Toast.makeText( getActivity(), "Please Enter the OTP", Toast.LENGTH_LONG ).show();
                } );
            } else if (sharecode.getText().toString().matches( "" )) {
                getActivity().runOnUiThread( () -> {
                    Toast.makeText( getActivity(), "Please Enter 4 digit Share Code", Toast.LENGTH_LONG ).show();
                } );
            } else {
                getAadhaardata();
            }
        } );

        // reload imagebutoon
        reloadCaptchaImagebtn.setOnClickListener( v -> {
            getcaptcha();
        } );
    }

    private void getAadhaardata() {

        final JSONObject jsonBody1 = new JSONObject();
        try {
            jsonBody1.put( "Event", "eKYCGetKYCBlob" );
            jsonBody1.put( "OTP", aadhaarOtp.getText().toString() );
            jsonBody1.put( "Aadhaar", aadhaar_no.getText().toString() );
            jsonBody1.put( "Sessionkey", sessionkey );
            jsonBody1.put( "Key", APIKEY );
            jsonBody1.put( "Sharecode", sharecode.getText().toString() );

        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse( "application/json" );
        client.setConnectTimeout(60, TimeUnit.SECONDS); // connect timeout
        client.setReadTimeout(60, TimeUnit.SECONDS);
        client.setWriteTimeout( 60,TimeUnit.SECONDS );
        final RequestBody body1 = RequestBody.create( mediaType, jsonBody1.toString() );
        final Request request = new Request.Builder()
                .url( MYEIDURL)
                .post( body1 )
                .addHeader( "Content-Type", "application/json" )
                .addHeader( "Accept", "*/*" )
                .build();
        client.newCall( request ).enqueue( new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
                stopAnimation();




                final String mMessage = e.getMessage().toString();



                if (e instanceof SocketTimeoutException) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d( TAG, "aadhaar: "+e.getMessage() );
                            getActivity().runOnUiThread( () -> {

                                Toast.makeText( getActivity(), "Something went wrong please try again later "+mMessage, Toast.LENGTH_LONG ).show();
                            } );
                            Intent intent = new Intent(getActivity(), DrawerActivity.class);
                            intent.putExtra("menuid", R.id.nav_off_line_aadhar);
                            startActivity(intent);
                        }
                    });
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getActivity().runOnUiThread( () -> {
                                Toast.makeText( getActivity(), mMessage, Toast.LENGTH_LONG ).show();
                            } );
                            Intent intent = new Intent(getActivity(), DrawerActivity.class);
                            intent.putExtra("menuid", R.id.nav_off_line_aadhar);
                            startActivity(intent);
                        }
                    });
                }

            }

            @Override
            public void onResponse(Response response) throws IOException {
                final String myResponse = response.body().string();
//                Log.d( TAG, "onResponse: " + myResponse );
                try {
                    JSONObject jsn = new JSONObject( myResponse );
                    if (jsn.optString( "Status" ).equals( "200 OK" ) && jsn.optString( "Error" ).equals( "N" )) {
                        String address = jsn.optString( "Address" );
                        String dob = jsn.optString( "DOB" );
                        String image = jsn.optString( "Userimage" );
                        String gender = jsn.optString( "Gender" );
                        String name = jsn.optString( "Name" );
                        String mob = phoneNumber;

                        sendtoPhapa(address,dob,image,gender,name,mob);


                    } else if (jsn.optString( "Status" ).equals( "200 OK" ) && jsn.optString( "Error" ).equals( "Y" )) {
                        String error = jsn.optString( "Errormessage" );
                        stopAnimation();
                        getActivity().runOnUiThread( () -> {
                            Toast toast = Toast.makeText(getActivity(),error, Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();                        } );
                        Intent intent = new Intent( getContext(), DrawerActivity.class );
                        intent.putExtra( "menuid", R.id.nav_off_line_aadhar );
                        startActivity( intent );


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    stopAnimation();
                    getActivity().runOnUiThread( () -> {
                        Toast.makeText( getActivity(), "Something went wrong please try again later", Toast.LENGTH_LONG ).show();
                    } );
                    Intent intent = new Intent(getActivity(), DrawerActivity.class);
                    intent.putExtra("menuid", R.id.nav_off_line_aadhar);
                    startActivity(intent);
                }

            }
        } );
    }

    private void sendtoPhapa(String address,String dob,String image,String gender,String name,String mob) {
        try {


            final JSONObject jsonBody1 = new JSONObject();
            try {
                jsonBody1.put( "mem_profile_pic", image );
                jsonBody1.put( "member_dob", dob );
                jsonBody1.put( "member_permanent_address", address );
                jsonBody1.put( "member_sex", gender );
                jsonBody1.put( "member_fullname", name );
                jsonBody1.put( "member_mobile_no", mob );

            } catch (JSONException e) {
                e.printStackTrace();
            }
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse( "application/json" );
            MediaType JSON = MediaType.parse( "application/json; charset=utf-8" );
            // put your json here
            final RequestBody body1 = RequestBody.create( JSON, jsonBody1.toString() );
            final Request request = new Request.Builder()
                    .url( common.PHAPAURL + "myeid/update/" + common.ID )
                    .post( body1 )
                    .addHeader( "Client-Service", "frontend-client" )
                    .addHeader( "Auth-key", "simplerestapi" )
                    .addHeader( "Content-Type", "application/json" )
                    .addHeader( "User-ID", common.ID )
                    .addHeader( "Authorization", common.TOKEN )
                    .addHeader( "Accept", "*/*" )
                    .build();
            client.newCall( request ).enqueue( new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    e.printStackTrace();
                    stopAnimation();
                    getActivity().runOnUiThread( () -> {
                        Toast.makeText( getActivity(), "Something went wrong please try again later", Toast.LENGTH_LONG ).show();
                    } );
                    Intent intent = new Intent(getActivity(), DrawerActivity.class);
                    intent.putExtra("menuid", R.id.nav_off_line_aadhar);
                    startActivity(intent);
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    final String myResponse = response.body().string();
//                    Log.w( TAG, "send adhaar details to phapa " + myResponse );
                    try {
                        JSONObject obj = new JSONObject( myResponse );
                        if ((obj.getString( "Profile saved" ).equals( "Yes" ))&&
                                (obj.getString( "status" ).equals( "200" ))){

                            final OrganizationRepository noteRepository = new OrganizationRepository( getContext() );
                            //download aadhaar image
                            byte[] imageBytes = Base64.decode( image, Base64.DEFAULT );
                            Bitmap decodedImage = BitmapFactory.decodeByteArray( imageBytes, 0, imageBytes.length );
                            String timeStamp = new SimpleDateFormat( "yyyyMMdd_HHmmss" ).format( new Date() );
                            String fname = common.ID + "_" + timeStamp + ".jpg";
                            sameImage( decodedImage, fname );
//                            Log.d( TAG, "onResponse: "+dob +address +gender +mob +name  );
                            noteRepository.setAadharData( fname, dob, address, gender, mob, name, common.NOCHANGE );

                            SharedPreferences.Editor editor1 = getActivity().getSharedPreferences( "PASSCODEDB", MODE_PRIVATE ).edit();
                            editor1.putString( "KYC", "Completed" );
                            editor1.apply();

                            stopAnimation();

                            Intent intent = new Intent( getContext(), additionalProfile.class );
                            startActivity( intent );
                        } else {
                            stopAnimation();
                            getActivity().runOnUiThread( () -> {
                                Toast.makeText( getActivity(), "Phapa Server Error", Toast.LENGTH_LONG ).show();
                            } );
                            Intent intent = new Intent(getActivity(), DrawerActivity.class);
                            intent.putExtra("menuid", R.id.nav_off_line_aadhar);
                            startActivity(intent);
                        }



                    } catch (JSONException e) {
                        e.printStackTrace();
                        stopAnimation();
                        getActivity().runOnUiThread( () -> {
                            Toast.makeText( getActivity(), "Phapa Server Error", Toast.LENGTH_LONG ).show();
                        } );
                        Intent intent = new Intent(getActivity(), DrawerActivity.class);
                        intent.putExtra("menuid", R.id.nav_off_line_aadhar);
                        startActivity(intent);
                    }
                }
            } );


        } catch (Exception e) {
            e.printStackTrace();
            stopAnimation();
            getActivity().runOnUiThread( () -> {
                Toast.makeText( getActivity(), "Phapa Server Error", Toast.LENGTH_LONG ).show();
            } );
            Intent intent = new Intent(getActivity(), DrawerActivity.class);
            intent.putExtra("menuid", R.id.nav_off_line_aadhar);
            startActivity(intent);
        }
    }

    public void sameImage(Bitmap decodedImage, String fname) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File( root + "/Phapa" );
        myDir.mkdirs();


        File file = new File( myDir, fname );

        //File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + "Phapa/" + common.ID +".png" );
        if (file.exists())
            file.delete();

        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream( file );
            //decodedImage.compress(Bitmap.CompressFormat.PNG, 100, out);
            decodedImage.compress( Bitmap.CompressFormat.JPEG, 100, out );
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    protected void startAnimation(String message) {

        if (this == null)
            return;
        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            getActivity().setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );
        } else {
            getActivity().setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE );
        }

        if (dialog == null) {
            dialog = new ProgressDialog( getContext(), R.style.transparent_dialog );
            dialog.setCanceledOnTouchOutside( false );
        }

        final String msg = message;
        dialog.setMessage( msg );
        dialog.show();
    }

    protected void stopAnimation() {

        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
        getActivity().setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_SENSOR );
    }
    private void getOtp() {


        final JSONObject jsonBody1 = new JSONObject();
        try {
            jsonBody1.put( "Event", "eKYCGetOTP" );
            jsonBody1.put( "Captcha", securitycode.getText().toString() );
            jsonBody1.put( "Aadhaar", aadhaar_no.getText().toString() );
            jsonBody1.put( "Sessionkey", sessionkey );
            jsonBody1.put( "Key", APIKEY );

        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(30, TimeUnit.SECONDS); // connect timeout
        client.setReadTimeout(10, TimeUnit.SECONDS);
        MediaType mediaType = MediaType.parse( "application/json" );
        final RequestBody body1 = RequestBody.create( mediaType, jsonBody1.toString() );
        final Request request = new Request.Builder()
                .url( MYEIDURL )
                .post( body1 )
                .addHeader( "Content-Type", "application/json" )
                .addHeader( "Accept", "*/*" )
                .build();
        client.newCall( request ).enqueue( new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
               /* stopAnimation();
                getActivity().runOnUiThread( () -> {
                    Toast.makeText( getActivity(), "Something went wrong please try again later", Toast.LENGTH_LONG ).show();
                } );
                Intent intent = new Intent(getActivity(), DrawerActivity.class);
                intent.putExtra("menuid", R.id.nav_off_line_aadhar);
                startActivity(intent);*/


                final String mMessage = e.getMessage().toString();



                if (e instanceof SocketTimeoutException) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getActivity().runOnUiThread( () -> {
                                Toast.makeText( getActivity(), "Something went wrong please try again later", Toast.LENGTH_LONG ).show();
                            } );
                            Intent intent = new Intent(getActivity(), DrawerActivity.class);
                            intent.putExtra("menuid", R.id.nav_off_line_aadhar);
                            startActivity(intent);
                        }
                    });
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getActivity().runOnUiThread( () -> {
                                Toast.makeText( getActivity(), mMessage, Toast.LENGTH_LONG ).show();
                            } );
                            Intent intent = new Intent(getActivity(), DrawerActivity.class);
                            intent.putExtra("menuid", R.id.nav_off_line_aadhar);
                            startActivity(intent);
                        }
                    });
                }


            }

            @Override
            public void onResponse(Response response) throws IOException {
                final String myResponse = response.body().string();
                Log.d( TAG, "onResponse: " + myResponse );

                try {
                    JSONObject jsn = new JSONObject( myResponse );
                    if (jsn.optString( "Status" ).equals( "200 OK" ) && jsn.optString( "Error" ).equals( "N" )) {

                        String msg = jsn.optString( "Message" );

                        getActivity().runOnUiThread( () -> {
                            Toast toast = Toast.makeText(getActivity(),msg, Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            send_otpbtn.setEnabled( false );
                            screen1.setVisibility( View.GONE );
                            screen2.setVisibility( View.GONE );
                            screen3.setVisibility( View.VISIBLE );
                            scrollviewaadhaar.post( () -> {
                                scrollviewaadhaar.fullScroll( ScrollView.FOCUS_DOWN );
                            } );
                            stopAnimation();
                        } );


                    } else if (jsn.optString( "Status" ).equals( "200 OK" ) && jsn.optString( "Error" ).equals( "Y" )) {
                        String error = jsn.optString( "Errormessage" );
                        getActivity().runOnUiThread( () -> {
                            Toast toast = Toast.makeText(getActivity(),error, Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();                             stopAnimation();
                        } );
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    getActivity().runOnUiThread( () -> {
                        Toast.makeText( getActivity(), "Something went wrong please try again later", Toast.LENGTH_LONG ).show();
                        stopAnimation();
                    } );

                    Intent intent = new Intent(getActivity(), DrawerActivity.class);
                    intent.putExtra("menuid", R.id.nav_off_line_aadhar);
                    startActivity(intent);
                }

            }
        } );
    }

    private void getcaptcha() {
        getActivity().runOnUiThread( () -> {
            startAnimation("");
        } );
        final JSONObject jsonBody1 = new JSONObject();
        try {
            jsonBody1.put( "Event", "eKYCGetCaptcha" );
            jsonBody1.put( "Key", APIKEY );
            jsonBody1.put( "Consent", "Y" );

        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(30, TimeUnit.SECONDS); // connect timeout
        client.setReadTimeout(10, TimeUnit.SECONDS);
        MediaType mediaType = MediaType.parse( "application/json" );
        final RequestBody body1 = RequestBody.create( mediaType, jsonBody1.toString() );
        final Request request = new Request.Builder()
                .url( MYEIDURL )
                .post( body1 )
                .addHeader( "Content-Type", "application/json" )
                .addHeader( "Accept", "*/*" )
                .build();
        client.newCall( request ).enqueue( new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread( () -> {
                    Toast.makeText( getActivity(), "Unable to connect Aadhaar server, please try again later", Toast.LENGTH_LONG ).show();
                    stopAnimation();
                } );
                Intent intent = new Intent(getActivity(), DrawerActivity.class);
                intent.putExtra("menuid", R.id.nav_off_line_aadhar);
                startActivity(intent);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                final String myResponse = response.body().string();
                Log.d( TAG, "onResponse: " + myResponse );
                try {
                    JSONObject jsn = new JSONObject( myResponse );
                    if ((jsn.optString( "Status" ).equals( "200 OK" )) && (jsn.optString( "Error" ).equals( "N" ))) {

                        captchaImagestring = jsn.optString( "Captcha" );
                        sessionkey = jsn.optString( "Sessionkey" );
                        byte[] decodedString = Base64.decode( captchaImagestring, Base64.DEFAULT );
                        Bitmap captchaImage = BitmapFactory.decodeByteArray( decodedString, 0, decodedString.length );
                        stopAnimation();
                        getActivity().runOnUiThread( () -> {
                            captcha.setImageBitmap( captchaImage );
                        } );

                    } else if ((jsn.optString( "Status" ).equals( "200 OK" ) )&&( jsn.optString( "Error" ).equals( "Y" ))) {
                        String error = jsn.optString( "Errormessage" );
                        stopAnimation();
                        getActivity().runOnUiThread( () -> {
                            Toast.makeText( getActivity(), error, Toast.LENGTH_LONG ).show();

                        } );
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    getActivity().runOnUiThread( () -> {
                        Toast.makeText( getActivity(), "Unable to connect Aadhaar server, please try again later", Toast.LENGTH_LONG ).show();
                        stopAnimation();
                    } );
                    Intent intent = new Intent(getActivity(), DrawerActivity.class);
                    intent.putExtra("menuid", R.id.nav_off_line_aadhar);
                    startActivity(intent);
                }

            }
        } );


    }


}
