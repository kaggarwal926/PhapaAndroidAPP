package com.icls.offlinekyc.function.MyOrgTabView;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.icls.offlinekyc.R;
import com.icls.offlinekyc.commonshare.common;
import com.icls.offlinekyc.function.DrawerActivity;
import com.icls.offlinekyc.function.MyOrgTabView.MyOrgTabViewFragments.MyOrgAbout;
import com.icls.offlinekyc.function.MyOrgTabView.MyOrgTabViewFragments.MyOrgNotification;
import com.icls.offlinekyc.function.MyOrgTabView.MyOrgTabViewFragments.MyOrgServiceRequest;
import com.icls.offlinekyc.function.MyOrgTabView.MyOrgTabViewFragments.MyOrgTabSupport;
import com.icls.offlinekyc.function.MyOrgTabView.MyOrgTabViewFragments.MyOrganizationNotifications;


import static com.icls.offlinekyc.commonshare.common.ORG_NAME;

public class MyOrgTabViewActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String TAG = "MyOrgTabViewActivity";
    private String orgName = "";
    private String intetfrom = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_my_org_tab_view );


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);


        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        int defaultValue = 1;
        if(getIntent() != null){
            int page = getIntent().getIntExtra("TabName", defaultValue);

            intetfrom = getIntent().getStringExtra( "intentfrom" );
            viewPager.setCurrentItem(page);


        }
        getSupportActionBar().setTitle(ORG_NAME);
        setupViewPager(viewPager);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    private void setupViewPager(ViewPager viewPager) {

        MyorgTabAdapter adapter = new MyorgTabAdapter(getSupportFragmentManager());
        if (intetfrom.equalsIgnoreCase( "intentfromMyorganization" )){
            adapter.addFragment(new MyOrganizationNotifications(),"NOTIFICATION" );
        }else  {
            adapter.addFragment(new MyOrgNotification(), "NOTIFICATION");
        }


        adapter.addFragment(new MyOrgServiceRequest(), "SERVICE REQUEST");
        adapter.addFragment(new MyOrgTabSupport(), "SUPPORT");
        adapter.addFragment(new MyOrgAbout(), "ABOUT");
        viewPager.setAdapter(adapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here

                Intent intent = new Intent( MyOrgTabViewActivity.this, DrawerActivity.class);
                intent.putExtra("menuid",  common.drawerID);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
