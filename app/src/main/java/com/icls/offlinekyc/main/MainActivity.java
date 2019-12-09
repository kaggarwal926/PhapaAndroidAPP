package com.icls.offlinekyc.main;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.icls.offlinekyc.R;
import com.icls.offlinekyc.login.LoginInfo;

import static com.icls.offlinekyc.function.DrawerActivity.iclServerCommonUrl;

public class MainActivity extends AppCompatActivity {


    private static final String URL =iclServerCommonUrl+"ICL_coreEngine";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = getSharedPreferences("LoginDetails", MODE_PRIVATE);
         String Status =sharedPreferences.getString("Status", "");
        isStoragePermissionGranted();
             if (Status.equals("Verified")) {


                 Intent intent=new Intent(getBaseContext(),LoginInfo.class);
                 intent.putExtra("status","AlreadyLoggedIn");
                 startActivity(intent);
                 finish();

             } else {
                 Intent loginIntent = new Intent(getApplicationContext(), LoginInfo.class);
                 loginIntent.putExtra("status","notLoggedIn");
                 startActivity(loginIntent);
                 finish();
             }
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("Permission","Permission is granted");
                return true;
            } else {

                Log.v("Permission","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("Permission","Permission is granted");
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Log.v("Permission","Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }
    }
}

