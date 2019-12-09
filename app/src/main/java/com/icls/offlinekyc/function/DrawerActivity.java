package com.icls.offlinekyc.function;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.icls.offlinekyc.R;
import com.icls.offlinekyc.commonshare.common;
import com.icls.offlinekyc.function.OfflineAdhaarKyc.OfflineKycAuto;
import com.icls.offlinekyc.function.Service.MyService.MyService;
import com.icls.offlinekyc.login.LoginInfo;

import java.text.DateFormatSymbols;

import static android.content.ContentValues.TAG;


public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener {

    protected FrameLayout frameLayout;
    CheckBox ch_pan, ch_dl, ch_voter, ch_pds;
    boolean pan, dl, voter, pds;
    Button btn_submit;
    String agencySelected;
    private View navHeader;
    public static boolean flag = false;
    static TextView textCartItemCount;
    public static int mCartItemCount = 10;
    public static String iclServerCommonUrl = "https://myeidapi.co.in:8443/ICL_ReaLId/";


    private boolean bound = false;

    //Edit text phone number
    String phoneNumber;


    String PhoneNo, TokenID;
    private DateFormatSymbols ApplicationController;
    public String transactionID;
    TextView nameindrawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        Log.e("DrawerActivity", "DrawerActivity");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        isStoragePermissionGranted();


//        startService(new Intent(this, MyService.class));
        new AsyncTaskRunner().execute();

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                phoneNumber = null;
            } else {
                phoneNumber = extras.getString("PhoneNo");
            }
        } else {
            phoneNumber = (String) savedInstanceState.getSerializable("PhoneNo");
        }

        int id = getIntent().getIntExtra("menuid", R.id.nav_off_line_aadhar);

        SharedPreferences sharedPreferences = this.getSharedPreferences("LoginDetails", MODE_PRIVATE);
        final String PhoneNo = sharedPreferences.getString("PhoneNo", "");

        displaySelectedScreen(id);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        TextView textView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.myeid);

        textView.setText(PhoneNo);

        if (getIntent() != null) {

            if (getIntent().getStringExtra("organizationName") != null) {
                flag = true;
                String orgName = getIntent().getStringExtra("organizationName");
                Fragment fragment = new OrganizationProfile();
                Bundle arguments = new Bundle();
                arguments.putString("organizationName", orgName);
                fragment.setArguments(arguments);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frame, fragment);
                ft.commit();

            }


        }
        nameindrawer = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nameindrawer);

        final SharedPreferences prefs = getSharedPreferences("PASSCODEDB", MODE_PRIVATE);
        String name = prefs.getString("USERNAME", "Name");
        if (name != null) {
            nameindrawer.setText(name);
        }
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
            //resume tasks needing this permission
        }
    }

    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {


    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }


    @Override
    protected void onStart() {
        super.onStart();

    }


    public static void setupBadge(int mCartItemCount) {

        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                textCartItemCount.setVisibility(View.GONE);

            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                textCartItemCount.setVisibility(View.VISIBLE);

            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);


        return true;
    }

    @Override
    public void onBackPressed() {


        if (flag) {
            displaySelectedScreen(R.id.nav_DashBoard);
        } else if (LoginInfo.mLogin_activity.isDestroyed()) {
            displaySelectedScreen(R.id.nav_DashBoard);
            Log.d(TAG, "onBackPressed: ");
        } else {
            displaySelectedScreen(R.id.nav_DashBoard);
            super.onBackPressed();
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.action_cart:
                Log.d(TAG, "onOptionsItemSelected: clicked on bell ..");
                displaySelectedScreen(R.id.nav_notification);
                return true;
            case R.id.usericon:
                Intent intent = new Intent(getApplicationContext(), additionalProfile.class);
                startActivity(intent);
                return true;
            case R.id.menuitemlogout:

                Intent loginIntent = new Intent(getApplicationContext(), LoginInfo.class);
                loginIntent.putExtra("status", "logout");
                startActivity(loginIntent);
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_off_line_aadhar) {
            displaySelectedScreen(R.id.nav_off_line_aadhar);
        } else if (id == R.id.nav_notification) {
            displaySelectedScreen(R.id.nav_notification);
        } else if (id == R.id.nav_contactUs) {
            displaySelectedScreen(R.id.nav_contactUs);
        } else if (id == R.id.nav_logout) {
            displaySelectedScreen(R.id.nav_logout);
        } else if (id == R.id.nav_DashBoard) {
            displaySelectedScreen(R.id.nav_DashBoard);
        } else if (id == R.id.nav_phapa_communities) {
            displaySelectedScreen(R.id.nav_phapa_communities);
        } else if (id == R.id.nav_my_organisation) {
            displaySelectedScreen(R.id.nav_my_organisation);
        } else if (id == R.id.nav_service_req) {
            displaySelectedScreen(R.id.nav_service_req);
        } else if (id == R.id.nav_payments) {
            displaySelectedScreen(R.id.nav_payments);
        } else if (id == R.id.nav_group_purchase) {
            displaySelectedScreen(R.id.nav_group_purchase);
        } else if (id == R.id.nav_job_placement) {
            displaySelectedScreen(R.id.nav_job_placement);
        } else if (id == R.id.nav_skill_training) {
            displaySelectedScreen(R.id.nav_skill_training);
        } else if (id == R.id.nav_health_wellness) {
            displaySelectedScreen(R.id.nav_health_wellness);
        } else if (id == R.id.nav_group_insurance) {
            displaySelectedScreen(R.id.nav_group_insurance);
        } else if (id == R.id.nav_easy_access) {
            displaySelectedScreen(R.id.nav_easy_access);
        } else if (id == R.id.nav_affordable_housing) {
            displaySelectedScreen(R.id.nav_affordable_housing);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void displaySelectedScreen(int itemId) {

        common.drawerID = itemId;
        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {


            case R.id.nav_off_line_aadhar:
                fragment = new OfflineKycAuto(phoneNumber);
                break;

            case R.id.nav_phapa_communities:
                fragment = new JoinOrganization();
                break;
            case R.id.nav_my_organisation:
                fragment = new MyOrganization();
                break;
            case R.id.nav_notification:
                fragment = new onNotification();
                break;

            case R.id.nav_contactUs:
                fragment = new ContactUs();
                break;

            case R.id.nav_DashBoard:
                fragment = new Dashboard();
                break;
            case R.id.nav_service_req:
                fragment = new ServiceRequestingMaster();
                break;
            case R.id.nav_payments:
                fragment = new payments();
                break;
            case R.id.nav_group_purchase:
                fragment = new GroupPurchasing();
                break;
            case R.id.nav_job_placement:
                fragment = new JobPlacements();
                break;
            case R.id.nav_skill_training:
                fragment = new SkillTraining();
                break;
            case R.id.nav_health_wellness:
                fragment = new HealthWellness();
                break;
            case R.id.nav_group_insurance:
                fragment = new GroupInsurance();
                break;
            case R.id.nav_easy_access:
                fragment = new EasyAcceess();
                break;
            case R.id.nav_affordable_housing:
                fragment = new AffordableHousing();
                break;

            case R.id.nav_logout:

                Intent loginIntent = new Intent(getApplicationContext(), LoginInfo.class);
                loginIntent.putExtra("status", "logout");
                startActivity(loginIntent);

                break;
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame, fragment);
            ft.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    protected void onResume() {
        super.onResume();

//        startService(new Intent(this, MyService.class));
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {


        String resp;

        @Override
        protected String doInBackground(String... params) {
            startService(new Intent(DrawerActivity.this, MyService.class));
            return resp;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation

        }


        @Override
        protected void onPreExecute() {

        }


        @Override
        protected void onProgressUpdate(String... text) {


        }
    }

}

