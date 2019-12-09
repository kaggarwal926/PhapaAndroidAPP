package com.icls.offlinekyc.Registration;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.icls.offlinekyc.R;
import com.icls.offlinekyc.login.LoginInfo;

import org.json.JSONException;
import org.json.JSONObject;

import static com.icls.offlinekyc.function.DrawerActivity.iclServerCommonUrl;

public class Registration extends AppCompatActivity {

    Button register_btn;
    TextInputEditText Username, mob_no, email_id, Regipassword, ConfRegipassword, userloginid;

    String Fullname, MobileNumber, LoginID, EmailID, Password, ConfirmPassword;
    String realId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_registration );


        init();
        onSubmit();
    }

    private void init() {
        userloginid = findViewById( R.id.userloginid );
        Username = findViewById( R.id.Username );
        mob_no = findViewById( R.id.mob_no );
        email_id = findViewById( R.id.email_id );
        Regipassword = findViewById( R.id.Regipassword );
        ConfRegipassword = findViewById( R.id.ConfRegipassword );
        register_btn = findViewById( R.id.register_btn );
    }

    private void onSubmit() {
        register_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean internetStatus = isNetworkConnected();
                if (!internetStatus) {
                    Toast.makeText( getApplicationContext(), "Please connect" +
                            "to Internet", Toast.LENGTH_LONG ).show();
                    return;
                }

                // getting user entered input stroing in string
                Fullname = Username.getText().toString();
                MobileNumber = mob_no.getText().toString().trim();
                LoginID = userloginid.getText().toString().trim();
                EmailID = email_id.getText().toString().trim();
                Password = Regipassword.getText().toString().trim();
                ConfirmPassword = ConfRegipassword.getText().toString().trim();


                if (Fullname.isEmpty() || (Fullname.length() == 0) || Fullname.equals( "" ) || Fullname == null) {
                    Username.setError( getString( R.string.regUserNameError ) );
                    Username.requestFocus();
                } else if (Fullname.length() < 3) {
                    Username.setError( "Minimum 3 characters" );
                    Username.requestFocus();
                } else if (Fullname.length() > 15) {
                    Username.setError( "Name should be less than 15 characters" );
                    Username.requestFocus();

                } else if (MobileNumber.isEmpty() || (MobileNumber.length() == 0) || MobileNumber.equals( "" ) || MobileNumber == null) {
                    mob_no.setError( getString( R.string.mobNoError ) );
                    mob_no.requestFocus();
                } else if (MobileNumber.length() < 10) {
                    mob_no.setError( "Please Enter 10 Digit Mobile Number" );
                    mob_no.requestFocus();
                } else if (MobileNumber.length() > 10) {
                    mob_no.setError( "Please Enter 10 Digit Mobile Number" );
                    mob_no.requestFocus();
                } else if (LoginID.isEmpty() || (LoginID.length() == 0) || LoginID.equals( "" ) || LoginID == null) {
                    userloginid.setError( getString( R.string.loginIdError ) );
                    userloginid.requestFocus();
                }else if (LoginID.length() < 3) {
                    userloginid.setError( "Minimum 3 characters" );
                    userloginid.requestFocus();
                } else if (LoginID.length() > 15) {
                    userloginid.setError( "LoginID should be less than 15 characters" );
                    userloginid.requestFocus();
                } else if (EmailID.isEmpty() || (EmailID.length() == 0) || EmailID.equals( "" ) || EmailID == null) {
                    email_id.setError( getString( R.string.emailIdError ) );
                    email_id.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher( EmailID ).matches()) {
                    email_id.setError( "Invalid Email Address" );
                    email_id.requestFocus();
                } else if (Password.isEmpty() || (Password.length() == 0) || Password.equals( "" ) || Password == null) {
                    Regipassword.setError( getString( R.string.passwordError ) );
                    Regipassword.requestFocus();
                } else if (Password.length() < 8) {
                    Regipassword.setError( "password should be Minimum 8 characters" );
                    Regipassword.requestFocus();
                } else if (Password.length() > 15) {
                    Regipassword.setError( "password should be less than 15 characters" );
                    Regipassword.requestFocus();
                }else if (ConfirmPassword.isEmpty() || (ConfirmPassword.length() == 0) || ConfirmPassword.equals( "" ) || ConfirmPassword == null) {
                    ConfRegipassword.setError( getString( R.string.confirmPasswordError ) );
                    ConfRegipassword.requestFocus();

                } else if (ConfirmPassword.length() < 8) {
                    ConfRegipassword.setError( "password should be Minimum 8 characters" );
                    ConfRegipassword.requestFocus();
                } else if (ConfirmPassword.length() > 15) {
                    ConfRegipassword.setError( "password should be less than 15 characters" );
                    ConfRegipassword.requestFocus();
                } else if (!Password.equals( ConfirmPassword )) {
                    ConfRegipassword.setError( getString( R.string.passMissMatchError ) );
                    ConfRegipassword.requestFocus();
                }


                registerUser();

            }
        } );
    }

    private void registerUser() {
        try {
            String URL = iclServerCommonUrl+"ICL_coreEngine";
            // creating JSON object //
            JSONObject jsonBody = new JSONObject();

            jsonBody.put( "Event", "Register" );
            jsonBody.put( "Type", "User" );
            jsonBody.put( "Loginid", LoginID );
            jsonBody.put( "Password", Password );
            jsonBody.put( "confirmpassword", ConfirmPassword );
            jsonBody.put( "Email", EmailID );
            jsonBody.put( "Name", Fullname );
            jsonBody.put( "Mobile", MobileNumber );

            //   sending Post request and getting respose //
            registerFormSend regiFormSend = new registerFormSend();
            regiFormSend.execute( URL, jsonBody.toString() );
            String response = regiFormSend.getResponse();
            if (!response.equals( "null" )) {
                JSONObject obj = new JSONObject( response );
                // extracting Real ID  from response
                  realId= obj.getString("RealId");
                Toast.makeText( this,realId, Toast.LENGTH_LONG ).show();
                Log.e( "TAG", "registerUser: "+ realId );
                SharedPreferences sharedPreferences = getSharedPreferences("RealIDNumber", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("RealID",realId);
                editor.commit();
                Toast.makeText( getApplicationContext(), "User Registered", Toast.LENGTH_LONG ).show();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private boolean isNetworkConnected() {
        ConnectivityManager
                connmanager = (ConnectivityManager) getApplicationContext()
                .getSystemService( Context.CONNECTIVITY_SERVICE );
        NetworkInfo activeNetwork = connmanager.getActiveNetworkInfo();
        boolean isConnected = (activeNetwork != null) && (activeNetwork.isConnectedOrConnecting());
        return isConnected;
    }


    public void gotoLogin(View view) {
        Intent intent = new Intent( getApplicationContext(), LoginInfo.class );
        startActivity( intent );
    }


}
