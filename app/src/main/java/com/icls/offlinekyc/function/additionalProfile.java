//For offline code

package com.icls.offlinekyc.function;

import android.Manifest;
import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.icls.offlinekyc.commonshare.VolleyMultiPartRequest;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.google.android.gms.common.util.IOUtils;
import com.icls.offlinekyc.Database.OrganizationTypeParam;
import com.icls.offlinekyc.R;
import com.icls.offlinekyc.adapters.AdditionalDocumentAdapter;
import com.icls.offlinekyc.adapters.phapacommunityAdapter;
import com.icls.offlinekyc.commonshare.VolleySingleton;
import com.icls.offlinekyc.commonshare.common;
import com.icls.offlinekyc.function.MyOrgTabView.MyOrgTabViewActivity;
import com.icls.offlinekyc.helper.CoordinatorsPOJO;
import com.icls.offlinekyc.helper.MemberTypeMasterPOJO;
import com.icls.offlinekyc.helper.OrganizationGroupMaster;
import com.icls.offlinekyc.helper.PrimaryOrgGroupMaster;
import com.icls.offlinekyc.helper.PrimaryOrganisationPOJO;
import com.icls.offlinekyc.roomdb.OrganizationRepository;
import com.icls.offlinekyc.roomdb.UserProfile;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.icls.offlinekyc.commonshare.common.ORG_NAME;

public class additionalProfile extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Toolbar toolbar;
    private static final String TAG = "additionalProfile";
    private static final int PICK_PHOTO_FOR_AVATAR = 0;
    private static final int SELECTED_DOCUMENT = 1;
    public static Context context;
    public static List<String> OrgList = new ArrayList<>();
    public static List<String> memGropu = new ArrayList<>();
    public static List<String> primaryOrgGroupList = new ArrayList<>();
    public static List<String> primaryOrgGroupListMain = new ArrayList<>();
    public static List<String> memberTypeList = new ArrayList<>();
    public static List<String> membergroup = new ArrayList<>();
    public static List<String> coordinatorsPosts = new ArrayList<>();
    public static List<MemberTypeMasterPOJO> memberTypes = new ArrayList<MemberTypeMasterPOJO>();
    public static List<OrganizationGroupMaster> memberGroupTypes = new ArrayList<OrganizationGroupMaster>();
    public static List<PrimaryOrgGroupMaster> primaryOrgGroupMaster = new ArrayList<PrimaryOrgGroupMaster>();
    public static List<OrganizationTypeParam> primaryOrg = new ArrayList<OrganizationTypeParam>();
    public static List<OrganizationGroupMaster> OrgGroup = new ArrayList<OrganizationGroupMaster>();
    public static List<PrimaryOrgGroupMaster> primaryOrgGroupobj = new ArrayList<PrimaryOrgGroupMaster>();
    public static List<CoordinatorsPOJO> coordinatorNames = new ArrayList<CoordinatorsPOJO>();
    public String base64ProfilePic = "";
    public File sourceFile;
    public String ProfilepicName;
    public Bitmap bmp;
    Uri docUri;
    ImageView additionalprofile_photo, edtpic, editInfo, kyc_verify_logo;
    TextView addprofile_name, kyc_verify_status;
    Button btn_upload_doc;
    Spinner etOccupation,
            SpmemberTypeGroup,  /*select member type group*/
            etMemberType, /*select member type*/

    etCoordinatorName,
            etPrimaryOrgGroup, /*select org group*/
            etPrimaryOrganization,  /*select org*/
            etOrganizationGroup;
    // comment by harish


    //==========

    Switch switchPaymentStatus;
    EditText etAddress;
    TextView selectDocument;
    Button btnInfoSumbit, btnDocSumbit;
    RecyclerView documentRecyclerView;
    String profileImage, imagePath;
    ArrayAdapter<CharSequence> etOccupation_adapter;
    ArrayAdapter<CharSequence> etGender_adapter;
    String[] mMemberListDataList;
    Hashtable<String, String> memType;
    Hashtable<String, String> coordinatorType;
    Hashtable<String, String> organizationType;
    OrganizationRepository noteRepository = null;
    List<String> profOccupation = new ArrayList<>();
    List<UserProfile> addiProfileData = new ArrayList<>();
    private String docTypeId;
    private String base64Document;
    private String DocumentfileName, filePathDoc;
    private ArrayList<String> DocumentList = new ArrayList<>();
    private ArrayList<String> documentList = new ArrayList<>();
    private AdditionalDocumentAdapter additionalDocumentAdapter;
    private LinearLayout cardDoc;
    private TextView viewDocuments;
    private Spinner etGender, document_type;
    File myDir;


    private Button btn_update_kyc;
    int orgGroupcount = 0, MemTypeGroupcount = 0;
    int orgcount = 0, MemTypecount = 0;
    private EditText etmobile_number,
            etZipcode,
            etState,
            etCity, etTaluk, etArea, etVillage,
            etReferredBy, etReferredContact, etEmployeeName, etEmployeeContact, etLocalAddress,
            et_PermenaentAdd,
            et_name, et_localAddress, et_address, et_date_of_birth, et_alt_mobile_number;
    private ArrayAdapter<String> etMemberType_adapter, etMemberType_adapter1, etCoordinatorName_adapter, etPrimaryOrganization_adapter,
            etOrganizationType_adapter, etMemberTypeGroupAdapter, etPrimaryOrgAdapter;

    private String fullname, mobileNumber, altMobNumber, Dob, permanentAdd, gender, mobile, Occupation, zipcode, state, city, taluk,
            areaName, village, localAddress, memberType, memberTypeGroup, memberTypeGroupName, memberTypeId, memberTypeGroupId, memberGroupId, memberGroup,
            coordinatorId, coordinator, primaryOrganizationId, primaryOrganization, primaryOrganGroupId, primaryOrganGroupName, phoneNumber;
    //String

    String orgName, membberTypeCode, organisationTroupType;
    String document_type_name, document_type_id;

    private Target getTarget() {
        Target target = new Target() {

            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {

                    @Override
                    public void run() {

                        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + "Download/" + common.ID + ".png");
                        try {
                            file.createNewFile();
                            FileOutputStream ostream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
                            ostream.flush();
                            ostream.close();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //setContentView(R.layout.activity_additional_profile);
                                    Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath() + "/" + "Download/" + common.ID + ".png");
                                    additionalprofile_photo.setImageBitmap(bitmap);
                                }
                            });
                            /*ostream.flush();
                            ostream.close();*/

                        } catch (IOException e) {
                            Log.e("IOException", e.getLocalizedMessage());
                        }
                    }
                }).start();

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_profile);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Profile");

        context = getApplicationContext();
        init();
        final SharedPreferences prefs = getSharedPreferences("PASSCODEDB", MODE_PRIVATE);
        String name = prefs.getString("USERNAME", "Name");
        if (name != null) {
            addprofile_name.setText(name);
        }
        buttonPress();
        noteRepository = new OrganizationRepository(getApplicationContext());
        isDBEmpty();
        //Calls API for getting userProfileValues
        if (getIntent() != null) {
            // getAdditionalProfile();
        }

        //Declare the timer
        Timer t = new Timer();
        //Set the schedule function and rate
        t.scheduleAtFixedRate(new TimerTask() {

                                  @Override
                                  public void run() {
                                      //Called each time when 1000 milliseconds (1 second) (the period parameter)
                                      if (isNetworkConnected())
                                          new AsyncTaskRunnerUpdateProfileFromServer().execute();
                                  }

                              },
                //Set how long before to start calling the TimerTask (in milliseconds)
                0,
                //Set the amount of time between each execution (in milliseconds)
                30000);

        /**
         * Listener to set values of Member type.
         * based on which member group selected.
         */
        SpmemberTypeGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (++MemTypeGroupcount > 1) {
                    etMemberType.setEnabled(false);
                    String spinnerValue = SpmemberTypeGroup.getSelectedItem().toString();
                    String member_group_id = null;
                    for (OrganizationGroupMaster orgMember : OrgGroup) {
                        if (orgMember.getMember_group().equals(spinnerValue)) {
                            member_group_id = orgMember.getMember_group_id();
                            etMemberType.setEnabled(true);
                        }
                    }
                    memberTypeList = new ArrayList<>();

                    for (MemberTypeMasterPOJO memTypes : memberTypes) {
                        if (memTypes.getMemberTypeGroup().equals(member_group_id)) {
                            memberTypeList.add(memTypes.getMemberTypeCode());
                        }
                    }
                    Collections.sort(memberTypeList);
                    memberTypeList.add(0, "Select member type");
                    etMemberType_adapter = new ArrayAdapter<String>(additionalProfile.this, android.R.layout.simple_spinner_dropdown_item, memberTypeList);
                    etMemberType.setAdapter(etMemberType_adapter);
                    /*if (membberTypeCode != null) {
                        int spinnerPosition = etMemberType_adapter1.getPosition(membberTypeCode);
                        etMemberType.setSelection(spinnerPosition);
                    }*/
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

        etMemberType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                /*if (membberTypeCode != null) {
                    int spinnerPosition = etMemberType_adapter1.getPosition(membberTypeCode);
                    etMemberType.setSelection(spinnerPosition);
                }*/
                /*if (++MemTypecount > 1) {

                    //etMemberType.setEnabled(false);
                    String spinnerValue = SpmemberTypeGroup.getSelectedItem().toString();
                    String member_group_id = null;
                    for (OrganizationGroupMaster orgMember : OrgGroup) {
                        if (orgMember.getMember_group().equals(spinnerValue)) {
                            member_group_id = orgMember.getMember_group_id();
                            etMemberType.setEnabled(true);
                        }
                    }
                    memberTypeList = new ArrayList<>();
                    memberTypeList.add("Select member type");
                    for (MemberTypeMasterPOJO memTypes : memberTypes) {
                        if (memTypes.getMemberTypeGroup().equals(member_group_id)) {
                            memberTypeList.add(memTypes.getMemberTypeCode());
                        }
                    }
                    etMemberType_adapter = new ArrayAdapter<String>(additionalProfile.this, android.R.layout.simple_spinner_dropdown_item, memberTypeList);
                    etMemberType.setAdapter(etMemberType_adapter);
                }*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });


        etPrimaryOrgGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (++orgGroupcount > 1) {
                    //etPrimaryOrganization.setEnabled(false);
                    String spinnerValue = etPrimaryOrgGroup.getSelectedItem().toString();
                    String primaryOrgId = null;
                    for (PrimaryOrgGroupMaster primaryOrgGroup : primaryOrgGroupobj) {
                        if (primaryOrgGroup.getOrganisationGroupType().equalsIgnoreCase(spinnerValue)) {

                            primaryOrgId = primaryOrgGroup.getPrimaryOrgId();
                            //etPrimaryOrganization.setEnabled(true);
                        }
                    }

                    OrgList = new ArrayList<>();

                    for (OrganizationTypeParam primaryOrg : primaryOrg) {
                        if (primaryOrg.getPrimary_org_idfk().equals(primaryOrgId)) {
                            OrgList.add(primaryOrg.getOrganization_name());
                        }
                    }
                    Collections.sort(OrgList);
                    OrgList.add(0, "Select organization");
                    etPrimaryOrganization_adapter = new ArrayAdapter<String>(additionalProfile.this, android.R.layout.simple_spinner_dropdown_item, OrgList);
                    etPrimaryOrganization.setAdapter(etPrimaryOrganization_adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



    }

    private void bitmapToImageFile() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try (FileOutputStream out = new FileOutputStream("MYeIDProfileImage")) {
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                    // PNG is a lossless format, the compression factor (100) is ignored
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void buttonPress() {
        // select profile picture //
        edtpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

                startActivityForResult(chooserIntent, PICK_PHOTO_FOR_AVATAR);
            }
        });

        btn_update_kyc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DrawerActivity.class);
                intent.putExtra("menuid", R.id.nav_off_line_aadhar);
                startActivity(intent);

            }
        });

        //Data validation
        btnInfoSumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ValidateData()) {
                    final OrganizationRepository noteRepository = new OrganizationRepository(getApplicationContext());

                    noteRepository.updateProfile(
                            fullname,
                            permanentAdd,
                            localAddress,
                            altMobNumber,
                            Dob,
                            Occupation,
                            gender,
                            village,
                            areaName,
                            city,
                            taluk,
                            state,
                            zipcode,
                            memberTypeGroupId,
                            memberTypeGroupName,
                            memberType,
                            memberTypeId,
                            coordinator,
                            coordinatorId,
                            primaryOrganization,
                            primaryOrganizationId,
                            profileImage,
                            primaryOrganGroupName,
                            primaryOrganGroupId,
                            common.NEWRECORD);

                    if (isNetworkConnected())
                        //sendUploadedDocument(docTypeId, DocumentfileName, base64Document);
                        sendDoc();
                    new AsyncTaskRunnerUpdateProfileToServer().execute(profileImage);
                }
            }
        });

        btn_upload_doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(additionalProfile.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(additionalProfile.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, SELECTED_DOCUMENT);
                } else {
                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                    i.setType("*/*");
                    startActivityForResult(i, SELECTED_DOCUMENT);
//                    Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(intent, SELECTED_DOCUMENT);
//                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                    intent.setType("file/*");
//                    startActivityForResult(intent, SELECTED_DOCUMENT);
                }
            }
        });
    }

    private boolean ValidateData() {
        fullname = et_name.getText().toString();
        altMobNumber = et_alt_mobile_number.getText().toString();
        permanentAdd = et_address.getText().toString();
        localAddress = et_localAddress.getText().toString();
        Dob = et_date_of_birth.getText().toString();
        gender = etGender.getSelectedItem().toString();
        mobile = etmobile_number.getText().toString();
        Occupation = etOccupation.getSelectedItem().toString();
        zipcode = etZipcode.getText().toString();
        state = etState.getText().toString();
        city = etCity.getText().toString();
        taluk = etTaluk.getText().toString();
        areaName = etArea.getText().toString();
        village = etVillage.getText().toString();


        memberTypeGroup = SpmemberTypeGroup.getSelectedItem().toString();
        if (memberTypeGroup.equals("Select member group")) {
            memberTypeGroup = null;
        } else {
            for (OrganizationGroupMaster memType : OrgGroup) {
                if (memberTypeGroup.equals(memType.getMember_group())) {
                    memberTypeGroupId = memType.getMember_group_id();
                    memberTypeGroupName = memberTypeGroup;
                }
            }
        }

        memberType = etMemberType.getSelectedItem().toString();
        if (memberType.equals("Select member type")) {
            memberType = null;
        } else {
            for (MemberTypeMasterPOJO memType : memberTypes) {
                if (memberType.equals(memType.getMemberTypeCode())) {
                    memberTypeId = memType.getMemberTypeID();
                }
            }
        }


        coordinator = etCoordinatorName.getSelectedItem().toString();
        if (coordinator.equals("Select Co-ordinator")) {
            coordinator = null;
        } else {
            for (CoordinatorsPOJO coordinatorsPOJO : coordinatorNames) {
                if (coordinator.equals(coordinatorsPOJO.getUser_name())) {
                    coordinatorId = coordinatorsPOJO.getLogin_id();
                }
            }
        }
        //public static List<CoordinatorsPOJO> coordinatorNames
        primaryOrganization = etPrimaryOrganization.getSelectedItem().toString();
        if (primaryOrganization.equals("Select organization")) {
            primaryOrganization = null;
            primaryOrganizationId = null;
        } else {
            for (OrganizationTypeParam primaryOrganizationobj : primaryOrg) {

                if (primaryOrganization.equalsIgnoreCase(primaryOrganizationobj.getOrganization_name())) {
                    primaryOrganizationId = primaryOrganizationobj.getOrganization_id();
                }
            }
        }

        primaryOrganGroupName = etPrimaryOrgGroup.getSelectedItem().toString();
        if (primaryOrganGroupName.equals("Select organization group")) {
            primaryOrganGroupName = null;
            primaryOrganGroupId = null;
        } else {
            for (PrimaryOrgGroupMaster primaryOrgGroupMaster : primaryOrgGroupobj) {
                if (primaryOrganGroupName.equals(primaryOrgGroupMaster.getOrganisationGroupType())) {
                    primaryOrganGroupId = primaryOrgGroupMaster.getPrimaryOrgId();
                }
            }
        }


        if (TextUtils.equals(mobile, "") || mobile.length() < 10) {
            etmobile_number.setError("Please Enter Correct Mobile Number");
            etmobile_number.setFocusable(true);
            Toast.makeText(context, "Please Enter Correct Mobile Number", Toast.LENGTH_LONG).show();
            return false;
        } else if (TextUtils.equals(Occupation, "Select Occupation*")) {
            Toast.makeText(context, "Please Select Occupation", Toast.LENGTH_LONG).show();
            return false;

        } else if (TextUtils.equals(city, "")) {
            etCity.setError("Please Enter City Name");
            etCity.setFocusable(true);
            Toast.makeText(context, "Please Enter City Name", Toast.LENGTH_LONG).show();
            return false;

        } else if (TextUtils.equals(coordinator, "Coordinator Name / Supervisor Name*")) {
            Toast.makeText(context, "Please Select Coordinator Name / Supervisor Name*", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }

    }

    private void init() {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginDetails", MODE_PRIVATE);
        phoneNumber = (sharedPreferences.getString("PhoneNo", ""));
        Log.d(TAG, "init: " + phoneNumber);
        additionalprofile_photo = findViewById(R.id.additionalprofile_photo);
        edtpic = findViewById(R.id.edtpic);
        selectDocument = findViewById(R.id.selectImage);
        document_type = findViewById(R.id.document_type);
        addprofile_name = findViewById(R.id.addprofile_name);
        kyc_verify_status = findViewById(R.id.kyc_verify_status);
        kyc_verify_logo = findViewById(R.id.kyc_verify_logo);
        btn_update_kyc = findViewById(R.id.btn_update_kyc);
        btn_upload_doc = findViewById(R.id.upload);
        SharedPreferences prefs = getSharedPreferences("PASSCODEDB", MODE_PRIVATE);
        String KYC = prefs.getString("KYC", "nostatus");
        if (KYC.equalsIgnoreCase("Not Completed")) {
            kyc_verify_status.setText("eKYC Not Verified");
            kyc_verify_logo.setBackgroundResource(R.drawable.kyc_not_done);
        } else {
            kyc_verify_logo.setBackgroundResource(R.drawable.kyc_verified);
            kyc_verify_status.setText("eKYC Verified");

        }
        etmobile_number = findViewById(R.id.et_mobile_number);
        etmobile_number.setEnabled(false);
        etZipcode = findViewById(R.id.et_zipCode);
        etState = findViewById(R.id.et_state);
        etCity = findViewById(R.id.et_city);
        etTaluk = findViewById(R.id.et_taluk);
        etArea = findViewById(R.id.et_area);
        etVillage = findViewById(R.id.et_village);
        etLocalAddress = findViewById(R.id.et_localAddress);
        switchPaymentStatus = findViewById(R.id.et_payment_status);
        et_name = findViewById(R.id.et_name);
        et_name.setEnabled(false);
        et_localAddress = findViewById(R.id.et_localAddress);
        et_address = findViewById(R.id.et_address);
        et_address.setEnabled(false);
        et_date_of_birth = findViewById(R.id.et_date_of_birth);
        et_date_of_birth.setEnabled(false);
        et_alt_mobile_number = findViewById(R.id.et_alt_mobile_number);
        et_alt_mobile_number.setEnabled(false);
        btnInfoSumbit = findViewById(R.id.info_submit);

        //Spinner for Gender
        etGender = findViewById(R.id.et_Gender);
        etGender.setEnabled(false);
        etGender.setOnItemSelectedListener(this);
        // Create an ArrayAdapter using the string array and a default spinner layout
        etGender_adapter = ArrayAdapter.createFromResource(this,
                R.array.Gender, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        etGender_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        etGender.setAdapter(etGender_adapter);

        //Set Error  message to the adapter


        //Spinner for Occupation
        etOccupation = findViewById(R.id.et_occupation);
        etOccupation.setOnItemSelectedListener(this);
        // Create an ArrayAdapter using the string array and a default spinner layout
        etOccupation_adapter = ArrayAdapter.createFromResource(this,
                R.array.Occupation, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        etOccupation_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        etOccupation.setAdapter(etOccupation_adapter);
        //Spinner for Member Type
        etMemberType = findViewById(R.id.et_member_type);
        etMemberType.setEnabled(false);
        etMemberType.setOnItemSelectedListener(this);
        SpmemberTypeGroup = findViewById(R.id.member_type_group);
        SpmemberTypeGroup.setOnItemSelectedListener(this);

        etPrimaryOrgGroup = findViewById(R.id.et_primary_org_group);
        etPrimaryOrgGroup.setOnItemSelectedListener(this);


        // Create an ArrayAdapter using the string array and a default spinner layout

        //Spinner for Coordinator Name
        etCoordinatorName = findViewById(R.id.et_coordinator_supervisor);
        etCoordinatorName.setOnItemSelectedListener(this);


        //Spinner for Coordinator Name
        etPrimaryOrganization = findViewById(R.id.et_primary_organization);
        //etPrimaryOrganization.setEnabled(false);
        etPrimaryOrganization.setOnItemSelectedListener(this);


        //Spinner for documents

        // Create an ArrayAdapter using the string array and a default spinner layout
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.DocumentType, android.R.layout.simple_spinner_item);
//        // Specify the layout to use when the list of choices appears
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        // Apply the adapter to the spinner
//        document_type.setAdapter(adapter);

        getDocumentList();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == PICK_PHOTO_FOR_AVATAR && resultCode == Activity.RESULT_OK) {
                if (data == null) {
                    //Display an error
                    return;
                }
                final Uri uri = data.getData();
                File imageFile = new File(getRealPathFromURI(uri));
                sourceFile = new File(uri.getPath());
                imagePath = imageFile.toString();
                profileImage = imagePath.substring(imagePath.lastIndexOf("/") + 1);

                File source = new File(imagePath);
                String url = Environment.getExternalStorageDirectory().getPath() + "/" + "Phapa/";
                File dest = new File(url, source.getName());

                //Copy Image from one folder to another
                File f = new File(Environment.getExternalStorageDirectory().getPath() + "/" + "Phapa/", source.getName());
                if (!f.exists()) {
                    try {
                        f.createNewFile();
                        copyFile(imageFile, dest);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }


                sourceFile = new File(uri.getPath());
                try {
                    useImage(uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (requestCode == SELECTED_DOCUMENT && resultCode == Activity.RESULT_OK) {
                if (data == null) {
                    //Display an error
                    return;
                }
//                String Fpath = data.getDataString();
                docUri = data.getData();
                try {
                    selectedDocument(docUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                filePathDoc = docUri.getPath();//uri.getLastPathSegment();
                DocumentfileName = filePathDoc.substring(filePathDoc.lastIndexOf("/") + 1);
                DocumentfileName = getFileName(this,docUri,filePathDoc);
                Log.i("DocumentfileName",DocumentfileName+"");
                selectDocument.setVisibility(View.VISIBLE);
                selectDocument.setText(DocumentfileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getRealPathFromURI(Uri contentUri) {

        String[] proj = {MediaStore.Video.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void copyFile(File sourceFile, File destFile) throws IOException {
        if (!sourceFile.exists()) {
            return;
        }

        FileChannel source = null;
        FileChannel destination = null;
        source = new FileInputStream(sourceFile).getChannel();
        destination = new FileOutputStream(destFile).getChannel();
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size());
        }
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            destination.close();
        }


    }

    public void isDBEmpty() {
        noteRepository.getPrimaryOrgGroup().observe(additionalProfile.this, primaryOrgGroupPOJO -> {
            primaryOrgGroupList = new ArrayList<>();
            primaryOrgGroupobj = new ArrayList<>();
            for (PrimaryOrgGroupMaster primaryOrgGroup : primaryOrgGroupPOJO) {
                PrimaryOrgGroupMaster primaryOrgGroupMaster = new PrimaryOrgGroupMaster();
                primaryOrgGroupMaster.setOrganisationGroupType(primaryOrgGroup.getOrganisationGroupType());
                primaryOrgGroupMaster.setPrimaryOrgId(primaryOrgGroup.getPrimaryOrgId());
                primaryOrgGroupobj.add(primaryOrgGroupMaster);
                //List of String For array adapter.
                primaryOrgGroupList.add(primaryOrgGroup.getOrganisationGroupType());

            }
            Collections.sort(primaryOrgGroupList);
            primaryOrgGroupList.add(0, "Select organization group");
            etPrimaryOrgAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, primaryOrgGroupList);
            etPrimaryOrgGroup.setAdapter(etPrimaryOrgAdapter);

        });
        noteRepository.getMemberType().observe(additionalProfile.this, memberTypeMasterPOJOS -> {
            memberTypeList = new ArrayList<>();
            memberTypes = new ArrayList<>();
            memberTypeList.clear();
            for (MemberTypeMasterPOJO memType : memberTypeMasterPOJOS) {
                memberTypes.add(memType);
                memberTypeList.add(memType.getMemberTypeCode());
            }
            Collections.sort(memberTypeList);
            memberTypeList.add(0, "Select member type");
            etMemberType_adapter = new ArrayAdapter<String>(additionalProfile.this, android.R.layout.simple_spinner_dropdown_item, memberTypeList);
            etMemberType.setAdapter(etMemberType_adapter);

        });
        //noteRepository.closeDB();
        noteRepository.getOrgGroup().observe(additionalProfile.this, OrgGroupPOJO -> {
            memGropu = new ArrayList<>();


            for (OrganizationGroupMaster orgGroup : OrgGroupPOJO) {
                OrganizationGroupMaster orgGroupMaster = new OrganizationGroupMaster();
                orgGroupMaster.setMember_group(orgGroup.getMember_group());
                orgGroupMaster.setMember_group_id(orgGroup.getMember_group_id());
                OrgGroup.add(orgGroupMaster);
                //List of String For array adapter.
                memGropu.add(orgGroup.getMember_group());
            }
            Collections.sort(memGropu);
            memGropu.add(0, "Select member group type");
            etMemberTypeGroupAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, memGropu);
            SpmemberTypeGroup.setAdapter(etMemberTypeGroupAdapter);

        });


        //noteRepository.closeDB();


        //noteRepository.closeDB();
        noteRepository.getCoordinator().observe(additionalProfile.this, coordinatorsPOJOS -> {

            coordinatorsPosts = new ArrayList<>();

            for (CoordinatorsPOJO coordinatorDetails : coordinatorsPOJOS) {

                coordinatorNames.add(coordinatorDetails);
                //List of String For array adapter.
                coordinatorsPosts.add(coordinatorDetails.getUser_name());
            }
            Collections.sort(coordinatorsPosts);
            coordinatorsPosts.add(0, "Select Co-ordinator");
            etCoordinatorName_adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, coordinatorsPosts);
            etCoordinatorName.setAdapter(etCoordinatorName_adapter);
        });

        noteRepository.getTasks(common.STATUS_PAYMENT, common.STATUS_APPROVED, common.STATUS_WAITING_PAYMENT, common.STATUS_NOT_JOINED).observe(additionalProfile.this, new Observer<List<OrganizationTypeParam>>() {
            @Override
            public void onChanged(@Nullable List<OrganizationTypeParam> notes) {

                OrgList = new ArrayList<>();
                primaryOrg = new ArrayList<>();
                for (OrganizationTypeParam note : notes) {
                    OrgList.add(note.getOrganization_name());

                    primaryOrg.add(note);
                }
                noteRepository.closeDB();

                etPrimaryOrganization_adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, OrgList);
                etPrimaryOrganization.setAdapter(etPrimaryOrganization_adapter);

                //String spinnerValue = etPrimaryOrgGroup.getSelectedItem().toString();
                String primaryOrgId1 = null;
                for (PrimaryOrgGroupMaster primaryOrgGroup : primaryOrgGroupobj) {
                    if (primaryOrgGroup.getOrganisationGroupType().equalsIgnoreCase(organisationTroupType)) {

                        primaryOrgId1 = primaryOrgGroup.getPrimaryOrgId();
                        etPrimaryOrganization.setEnabled(true);
                    }
                }

                OrgList = new ArrayList<>();
                OrgList.add("Select organization");
                for (OrganizationTypeParam primaryOrg : primaryOrg) {
                    if (primaryOrg.getPrimary_org_idfk().equals(primaryOrgId1)) {
                        OrgList.add(primaryOrg.getOrganization_name());
                    }
                }
                etPrimaryOrganization_adapter = new ArrayAdapter<String>(additionalProfile.this, android.R.layout.simple_spinner_dropdown_item, OrgList);
                etPrimaryOrganization.setAdapter(etPrimaryOrganization_adapter);
                if (orgName != null) {
                    int spinnerPosition = etPrimaryOrganization_adapter.getPosition(orgName);
                    etPrimaryOrganization.setSelection(spinnerPosition);
                }

            }
        });


        noteRepository.closeDB();
        noteRepository.getAdditionalProfile().observe(additionalProfile.this, userProfiledata -> {
            for (UserProfile profile : userProfiledata) {
                UserProfile userProfile = new UserProfile();
                userProfile.setMember_id(profile.getMember_id());
                userProfile.setMember_occupation(profile.getMember_occupation());
                userProfile.setMember_local_add(profile.getMember_local_add());
                userProfile.setVillage_name(profile.getVillage_name());
                userProfile.setArea_name(profile.getArea_name());
                userProfile.setReg_city(profile.getReg_city());
                userProfile.setMember_local_add_taluk(profile.getMember_local_add_taluk());
                userProfile.setReg_state(profile.getReg_state());
                userProfile.setMember_local_add_pincode(profile.getMember_local_add_pincode());
                userProfile.setAlternative_Number(profile.getAlternative_Number());
                userProfile.setMemTypeIDFK(profile.getMemTypeIDFK());
                userProfile.setMemberTypeCode(profile.getMemberTypeCode());
                userProfile.setOrganization_id(profile.getOrganization_id());
                userProfile.setOrganization_name(profile.getOrganization_name());
                userProfile.setAgent_id(profile.getAgent_id());
                userProfile.setAgent_name(profile.getAgent_name());
                userProfile.setMember_dob(profile.getMember_dob());
                userProfile.setMember_permanent_address(profile.getMember_permanent_address());
                userProfile.setMember_sex(profile.getMember_sex());
                userProfile.setMember_fullname(profile.getMember_fullname());
                userProfile.setMember_mobile_no(profile.getMember_mobile_no());
                userProfile.setMemProfileSyncStatus(profile.getMemProfileSyncStatus());
                userProfile.setProfilePicture(profile.getProfilePicture());
                userProfile.setMember_group(profile.getMember_group());
                userProfile.setMember_group_idfk(profile.getMember_group_idfk());
                userProfile.setPrimary_org_id(profile.getPrimary_org_id());
                userProfile.setOrganisation_group_type(profile.getOrganisation_group_type());
                profOccupation.add(profile.getMember_occupation());
                addiProfileData.add(userProfile);
            }
            noteRepository.closeDB();
            runOnUiThread(() -> {
                if (!profOccupation.isEmpty() && !profOccupation.equals(null) && !(profOccupation.get(0)).equals("0")) {
                    for (UserProfile profile : addiProfileData) {
                        String memberId = profile.getMember_id();
                        String occupation = profile.getMember_occupation();
                        String Address = profile.getMember_local_add();
                        String vilageName = profile.getVillage_name();
                        String area = profile.getArea_name();
                        String city = profile.getReg_city();
                        String taluka = profile.getMember_local_add_taluk();
                        String state = profile.getReg_state();
                        String pinCode = profile.getMember_local_add_pincode();
                        String alternateMobileNo = profile.getAlternative_Number();
                        String memberGroupIdfk = profile.getMember_group_idfk();
                        memberGroup = profile.getMember_group();
                        String memberTypeIdfk = profile.getMemTypeIDFK();
                        membberTypeCode = profile.getMemberTypeCode();
                        String orgId = profile.getOrganization_id();
                        orgName = profile.getOrganization_name();
                        String agentId = profile.getAgent_id();
                        String agentName = profile.getAgent_name();
                        Log.d(TAG, "agentName: " + agentName);
                        String dateofBirth = profile.getMember_dob();
                        String pearmanentAddress = profile.getMember_permanent_address();
                        String local_address = profile.getMember_local_add();
                        String memberSex = profile.getMember_sex();
                        String fullName = profile.getMember_fullname();
                        String mobileNo = profile.getMember_mobile_no();
                        String altmobno = profile.getAlternative_Number();
                        String profilePic = profile.getProfilePicture();
                        String primaryOrgId = profile.getPrimary_org_id();
                        organisationTroupType = profile.getOrganisation_group_type();
                        profileImage = profilePic;
                        profile.getMemProfileSyncStatus();

                        if (profilePic != null && !profilePic.isEmpty() && !profilePic.equals("NA")) {

                            Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath() + "/" + "Phapa/" + profilePic);
                            if (bitmap == null) {
                                additionalprofile_photo.setImageResource(R.drawable.userimage);
                            } else {
                                additionalprofile_photo.setImageBitmap(bitmap);
                            }
                        } else {
                            additionalprofile_photo.setImageResource(R.drawable.userimage);
                        }


                        if (memberSex != null && !memberSex.isEmpty() && !memberSex.equals("NA")) {
                            etGender_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            etGender.setAdapter(etGender_adapter);
                            Log.d("memSex", memberSex);
                            if (memberSex != null) {
                                if (memberSex.equalsIgnoreCase("M") || memberSex.equalsIgnoreCase("Male")) {

                                    int spinnerPosition = etGender_adapter.getPosition("Male");
                                    etGender.setSelection(spinnerPosition);
                                } else if (memberSex.equalsIgnoreCase("F") || memberSex.equalsIgnoreCase("Female")) {

                                    int spinnerPosition = etGender_adapter.getPosition("Female");
                                    etGender.setSelection(spinnerPosition);

                                }
                            }
                        }
                        if (occupation != null && !occupation.isEmpty() && !occupation.equals("NA")) {
                            etOccupation_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            etOccupation.setAdapter(etOccupation_adapter);
                            if (occupation != null) {
                                int spinnerPosition = etOccupation_adapter.getPosition(occupation);
                                etOccupation.setSelection(spinnerPosition);
                            }
                        }
                        try {
                            if (agentId != null && !agentId.isEmpty() && !agentId.equals("NA")) {

                                int spinnerPosition = etCoordinatorName_adapter.getPosition(agentName);
                                etCoordinatorName.setSelection(spinnerPosition);
                            }
                        } catch (Exception E) {
                            Log.e("error", "" + E.getMessage());
                        }

                        if (primaryOrgId != null && !primaryOrgId.isEmpty() && !primaryOrgId.equals("NA")) {

                            int spinnerPosition = etPrimaryOrgAdapter.getPosition(organisationTroupType);
                            etPrimaryOrgGroup.setSelection(spinnerPosition);


                            //


                        }


                        if (orgId != null && !orgId.isEmpty() && !orgId.equals("NA") && etPrimaryOrganization_adapter != null) {

                            OrgList = new ArrayList<>();
                            OrgList.add("Select organization");
                            for (OrganizationTypeParam primaryOrg : primaryOrg) {
                                if (primaryOrg.getPrimary_org_idfk().equals(primaryOrgId)) {
                                    OrgList.add(primaryOrg.getOrganization_name());
                                }
                            }
                            etPrimaryOrganization_adapter = new ArrayAdapter<String>(additionalProfile.this, android.R.layout.simple_spinner_dropdown_item, OrgList);
                            etPrimaryOrganization.setAdapter(etPrimaryOrganization_adapter);
                            int spinnerPosition = etPrimaryOrganization_adapter.getPosition(orgName);
                            etPrimaryOrganization.setSelection(spinnerPosition);
                        }
                        if (memberGroupIdfk != null && !memberGroupIdfk.isEmpty() && !memberGroupIdfk.equals("NA")) {


                            if (memberGroup != null) {
                                int spinnerPosition = etMemberTypeGroupAdapter.getPosition(memberGroup);
                                SpmemberTypeGroup.setSelection(spinnerPosition);

                                //Code for displaying drop down for member type


                                String member_group_id = null;
                                for (OrganizationGroupMaster orgMember : OrgGroup) {
                                    if (orgMember.getMember_group().equals(memberGroup)) {
                                        member_group_id = orgMember.getMember_group_id();
                                        etMemberType.setEnabled(true);
                                    }
                                }
                                memberTypeList = new ArrayList<>();
                                memberTypeList.add("Select member type");
                                for (MemberTypeMasterPOJO memTypes : memberTypes) {
                                    if (memTypes.getMemberTypeGroup().equals(member_group_id)) {
                                        memberTypeList.add(memTypes.getMemberTypeCode());
                                    }
                                }
                                etMemberType_adapter = new ArrayAdapter<String>(additionalProfile.this, android.R.layout.simple_spinner_dropdown_item, memberTypeList);
                                etMemberType.setAdapter(etMemberType_adapter);

                            }
                        }

                        if (memberTypeIdfk != null && !memberTypeIdfk.isEmpty() && !memberTypeIdfk.equals("NA")) {
                            if (membberTypeCode != null) {
                                etMemberType.setEnabled(true);
                                int spinnerPosition = etMemberType_adapter.getPosition(membberTypeCode);
                                etMemberType.setSelection(spinnerPosition);
                            }
                        }


                        if (pinCode != null && !pinCode.isEmpty() && !pinCode.equals("NA")) {
                            etZipcode.setText(pinCode);
                        }


                        if (fullName != null && !fullName.isEmpty() && !fullName.equals("NA")) {
                            et_name.setText(fullName);
                        }

                        if (pearmanentAddress != null && !pearmanentAddress.isEmpty() && !pearmanentAddress.equals("NA")) {
                            et_address.setText(pearmanentAddress);
                        }
                        if (local_address != null && !local_address.isEmpty() && !local_address.equals("NA")) {
                            et_localAddress.setText(local_address);
                        }

                        if (dateofBirth != null && !dateofBirth.isEmpty() && !dateofBirth.equals("NA")) {
                            et_date_of_birth.setText(dateofBirth);
                        }

                        if (!alternateMobileNo.equalsIgnoreCase("0") && alternateMobileNo != null && !alternateMobileNo.isEmpty() && !alternateMobileNo.equals("NA")) {
                            et_alt_mobile_number.setText(alternateMobileNo);
                        }

                        if (state != null && !state.isEmpty() && !state.equals("NA")) {
                            etState.setText(state);
                        }

                        if (city != null && !city.isEmpty() && !city.equals("NA")) {
                            etCity.setText(city);
                        }

                        if (taluka != null && !taluka.isEmpty() && !taluka.equals("NA")) {
                            etTaluk.setText(taluka);
                        }

                        if (area != null && !area.isEmpty() && !area.equals("NA")) {
                            etArea.setText(area);
                        }

                        if (vilageName != null && !vilageName.isEmpty() && !vilageName.equals("NA")) {
                            etVillage.setText(vilageName);
                        }

                        if (Address != null && !Address.isEmpty() && !Address.equals("NA")) {
                            etLocalAddress.setText(Address);
                        }

                        if (phoneNumber != null && !phoneNumber.isEmpty() && !phoneNumber.equals("NA")) {
                            et_alt_mobile_number.setText(phoneNumber);
                            etmobile_number.setText(phoneNumber);
                        }
                    }
                }
            });
        });
    }


    private String optString_1(final JSONObject json, final String key) {

        return json.isNull(key) ? "null" : json.optString(key);

    }

    private void selectedDocument(Uri uri) throws IOException {
        byte[] bytes = loadFile(uri);
        base64Document = Base64.encodeToString(bytes, Base64.DEFAULT);
        Bitmap bitmap = null;

        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            //use the bitmap as you like


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private byte[] loadFile(Uri uri) throws IOException {
        InputStream is = getApplicationContext().getContentResolver().openInputStream(uri);
        byte[] bytes = IOUtils.readInputStreamFully(is, false);
        is.close();
        return bytes;
    }

    private void useImage(Uri uri) throws IOException {

        // uploaded profile image in base64 format //

        byte[] bytes = loadFile(uri);
        base64ProfilePic = Base64.encodeToString(bytes, Base64.DEFAULT);

        // uploaded  profile image in base 64 format //

        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            //use the bitmap as you like
            additionalprofile_photo.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        String name = (String) parent.getItemAtPosition(position);
        switch (name) {
            case "Aadhaar Card":
                docTypeId = "20";

                break;
            case "Voter Id":
                docTypeId = "21";
                break;
            case "PAN Card":
                docTypeId = "22";
                break;
            case "DL Card":
                docTypeId = "23";
                break;
            case "Ration Card":
                docTypeId = "24";
                break;
            case "Bank Passbook":
                docTypeId = "25";
                break;
            case "Education Certificate":
                docTypeId = "26";
                break;
            case "Work Experience Certificate":
                docTypeId = "27";
                break;
            case "User Profile Picture":
                docTypeId = "28";
                break;
            case "Other Documents":
                docTypeId = "29";
                break;
        }

    }

    public void sendDoc(){
        VolleyMultiPartRequest multipartRequest = new VolleyMultiPartRequest(com.android.volley.Request.Method.POST, "http://workers.phapa.in/Api/v1/myeid/documentUpload", new com.android.volley.Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                Log.e("resultResponse", resultResponse+"");
                // parse success output
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("document_type_id", "21");
                params.put("name", "TestFile");
                return params;
            }

            /** Passing some request headers* */
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Client-Service", "frontend-client");
                headers.put("Auth-key", "simplerestapi");
                headers.put("User-ID", "7016006");
                headers.put("Authorization", "d5674904d9bdb8678a51c1cdf839095d");
                return headers;
            }


            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView
                InputStream iStream = null;
                try {
                    iStream = getContentResolver().openInputStream(docUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                byte[] inputData = new byte[0];
                try {
                    inputData = getBytes(iStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                params.put("documents", new DataPart(DocumentfileName, inputData, "image/jpeg"));

                return params;
            }
        };

        VolleySingleton.getInstance(getBaseContext()).addToRequestQueue(multipartRequest);
    }

    private void sendUploadedDocument(String docTypeId, String name, String base64Document) {

        OkHttpClient client = new OkHttpClient();

//        final JSONObject jsonBody1 = new JSONObject();
//        try {
//            jsonBody1.put("document_type_id", docTypeId);
//            jsonBody1.put("documents", base64Document);
//            jsonBody1.put("name", name);
//
//            Log.e("jsonBody1", jsonBody1.toString());
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        MediaType mediaType = MediaType.parse("application/json");
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        // put your json here
//        final RequestBody body1 = RequestBody.create(JSON, jsonBody1.toString());
        File file = new File(context.getFilesDir().getAbsolutePath() + "/"+name);
        RequestBody body1 = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("documents", file.getName(),
                        RequestBody.create(MediaType.parse("text/image"), file))
//                .addFormDataPart("documents", "logo-square.png",
//                        RequestBody.create(
//                                new File("docs/images/logo-square.png"),
//                                MEDIA_TYPE_PNG))
                .addFormDataPart("name", name)
                .addFormDataPart("document_type_id", "21")
                .build();

        // put your json here
        final Request request = new Request.Builder()
                .url(common.PHAPAURL + "myeid/documentUpload")
                .addHeader("Client-Service", "frontend-client")
                .addHeader("Auth-key", "simplerestapi")
//                .addHeader("Content-Type", "application/json")
                .addHeader("User-ID", common.ID)
                .addHeader("Authorization", common.TOKEN)
                .addHeader("Accept", "*/*")
                .post(body1)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String myResponse = response.body().string();
                additionalProfile.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(additionalProfile.this, myResponse+"", Toast.LENGTH_LONG).show();
                    }
                });
                Log.e(TAG, "Upload Document = " + myResponse);
            }
        });
    }

    private void getDocumentList() {

        OkHttpClient client = new OkHttpClient();

        // put your json here
        final Request request = new Request.Builder()
                .url(common.PHAPAURL + "myeid/documentList")
                .addHeader("Client-Service", "frontend-client")
                .addHeader("Auth-key", "simplerestapi")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("User-ID", common.ID)
                .addHeader("Authorization", common.TOKEN)
                .addHeader("Accept", "*/*")
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String myResponse = response.body().string();
                documentList.add("Select Document Type");
                try {
                    JSONArray jsonRes = new JSONArray(myResponse);
                    for (int i = 0; i < jsonRes.length(); i++) {
                        JSONObject obj = jsonRes.getJSONObject(i);
                        document_type_name = obj.getString("document_type_name");
                        document_type_id = obj.getString("document_type_id");
                        documentList.add(document_type_name);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                additionalProfile.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // ArrayAdapter<ArrayList<String>> adapter = new ArrayAdapter<ArrayList<String>>(additionalProfile.this, documentList, android.R.layout.simple_spinner_item);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                additionalProfile.this,
                                android.R.layout.simple_spinner_item,
                                documentList
                        );
                        // Specify the layout to use when the list of choices appears
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        // Apply the adapter to the spinner
                        document_type.setAdapter(adapter);
                        //Toast.makeText(additionalProfile.this, "Document List fetched " + myResponse, Toast.LENGTH_LONG).show();
                    }
                });
                Log.i(TAG, "upload document = " + myResponse);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here

                super.onBackPressed();
                Intent intent = new Intent(this, DrawerActivity.class);
                intent.putExtra("menuid", R.id.nav_DashBoard);
                startActivity(intent);
                finish();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void onViewDocuments(View view) {

        Intent intent = new Intent(getApplicationContext(), viewDocumentRecyclerView.class);
        intent.putStringArrayListExtra("docList", DocumentList);
        startActivity(intent);
    }


    // Send user profile data to Phapa server from android app
    private class AsyncTaskRunnerUpdateProfileToServer extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            OkHttpClient client = new OkHttpClient();
            final MediaType MEDIA_TYPE_PNG1 = MediaType.parse("image/*");

            Request request = null;
            RequestBody requestBody = null;

            if (memberTypeId != null && !memberTypeId.isEmpty() && !memberTypeId.equals("null")) {
            } else {
                memberTypeId = "";
            }
            if (memberTypeGroupId != null && !memberTypeGroupId.isEmpty() && !memberTypeGroupId.equals("null")) {
            } else {
                memberTypeGroupId = "";
            }
            if (primaryOrganizationId != null && !primaryOrganizationId.isEmpty() && !primaryOrganizationId.equals("null")) {
            } else {
                primaryOrganizationId = "";
            }
            if (primaryOrganGroupId != null && !primaryOrganGroupId.isEmpty() && !primaryOrganGroupId.equals("null")) {
            } else {
                primaryOrganGroupId = "";
            }
            if (primaryOrganGroupName != null && !primaryOrganGroupName.isEmpty() && !primaryOrganGroupName.equals("null")) {
            } else {
                primaryOrganGroupName = "";
            }
            if (coordinatorId != null && !coordinatorId.isEmpty() && !coordinatorId.equals("null")) {
            } else {
                coordinatorId = "";
            }

            if (params[0] != null) {
                String root = Environment.getExternalStorageDirectory().toString();
                myDir = new File(root + "/Phapa/" + params);
                requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("member_id", memberTypeId)
                        .addFormDataPart("mem_profile_pic", params[0],
                                RequestBody.create(MEDIA_TYPE_PNG1,
                                        new File(root + "/Phapa/" + params[0])))

                        .addFormDataPart("member_local_add", localAddress)
                        .addFormDataPart("member_occupation", Occupation)
                        .addFormDataPart("member_sex", gender)
                        .addFormDataPart("village_name", village)
                        .addFormDataPart("area_name", areaName)
                        .addFormDataPart("reg_city", city)
                        .addFormDataPart("member_local_add_taluk", taluk)
                        .addFormDataPart("member_local_add_pincode", zipcode)
                        .addFormDataPart("reg_state", state)
                        .addFormDataPart("alternative_number", altMobNumber)
                        .addFormDataPart("memTypeIDFK", memberTypeId)
                        .addFormDataPart("member_group_idfk", memberTypeGroupId)
                        .addFormDataPart("Primary_Org_ID", primaryOrganizationId)
                        .addFormDataPart("org_group_idfk", primaryOrganGroupId)
                        .addFormDataPart("organisation_group_type", primaryOrganGroupName)
                        .addFormDataPart("agent_idfk", coordinatorId)
                        .addFormDataPart("member_dob", Dob)
                        .addFormDataPart("member_permanent_address", permanentAdd)
                        .addFormDataPart("member_fullname", fullname)
                        .addFormDataPart("member_mobile_no", phoneNumber)

                        .build();
            } else {
                requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("member_id", memberTypeId)
                        .addFormDataPart("member_local_add", localAddress)
                        .addFormDataPart("member_occupation", Occupation)
                        .addFormDataPart("member_sex", gender)
                        .addFormDataPart("village_name", village)
                        .addFormDataPart("area_name", areaName)
                        .addFormDataPart("reg_city", city)
                        .addFormDataPart("member_local_add_taluk", taluk)
                        .addFormDataPart("member_local_add_pincode", zipcode)
                        .addFormDataPart("reg_state", state)
                        .addFormDataPart("alternative_number", altMobNumber)
                        .addFormDataPart("memTypeIDFK", memberTypeId)
                        .addFormDataPart("member_group_idfk", memberTypeGroupId)
                        .addFormDataPart("Primary_Org_ID", primaryOrganizationId)
                        .addFormDataPart("org_group_idfk", primaryOrganGroupId)
                        .addFormDataPart("organisation_group_type", primaryOrganGroupName)
                        .addFormDataPart("agent_idfk", coordinatorId)
                        .addFormDataPart("member_dob", Dob)
                        .addFormDataPart("member_permanent_address", permanentAdd)
                        .addFormDataPart("member_fullname", fullname)
                        .addFormDataPart("member_mobile_no", phoneNumber)
                        .build();
            }

            request = new Request.Builder()
                    .url(common.PHAPAURL + "myeid/QuickRegistration")
                    .post(requestBody)
                    .addHeader("Client-Service", "frontend-client")
                    .addHeader("Auth-key", "simplerestapi")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("User-ID", common.ID)
                    .addHeader("Authorization", common.TOKEN)
                    .addHeader("Accept", "*")
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String myResponse = response.body().string();
                    Log.e("ProfileUpdated", myResponse + "");
                    try {
                        final JSONObject obj = new JSONObject(myResponse);
                        final String status = obj.optString("status", " ");
                        final String message = obj.optString("message", " ");
                        if (status.equalsIgnoreCase("200")) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(context, message + "", Toast.LENGTH_LONG).show();
                                }
                            });
                            final OrganizationRepository noteRepository = new OrganizationRepository(getApplicationContext());
                            noteRepository.updateUserProfile(common.NOCHANGE);
                            noteRepository.closeDB();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            return null;
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

    // Fetch user profile data from Phapa server to android app
    private class AsyncTaskRunnerUpdateProfileFromServer extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            OkHttpClient client = new OkHttpClient();
            Request request = null;

            request = new Request.Builder()
                    .url(common.PHAPAURL + "myeid/GetQuickRegistration/" + phoneNumber)
                    .get()
                    .addHeader("Client-Service", "frontend-client")
                    .addHeader("Auth-key", "simplerestapi")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("User-ID", common.ID)
                    .addHeader("Authorization", common.TOKEN)
                    .addHeader("Accept", "*")
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String myResponse = response.body().string();
                    Log.e("UpdateProfileFromServer", myResponse + "");
                    try {
                        JSONObject obj = new JSONObject(myResponse);
                        JSONObject data = obj.getJSONObject("data");
                        JSONObject adhar = obj.getJSONObject("adhar");
                        if (data != null) {
                            String member_id = data.optString("member_id", " ");
                            String member_occupation = data.optString("member_occupation", " ");
                            String member_local_add = data.optString("member_local_add", " ");
                            String member_sex = data.optString("member_sex", " ");
                            String village_name = data.optString("village_name", " ");
                            String area_name = data.optString("area_name", " ");
                            String reg_city = data.optString("reg_city", " ");
                            String member_local_add_taluk = data.optString("member_local_add_taluk", " ");
                            String reg_state = data.optString("reg_state", " ");
                            String member_local_add_pincode = data.optString("member_local_add_pincode", " ");
                            String member_mobile_no = data.optString("member_mobile_no", " ");
                            String memTypeIDFK = data.optString("memTypeIDFK", " ");
                            String alternative_Number = data.optString("alternative_Number", " ");
                            String memberTypeCode = data.optString("memberTypeCode", " ");
                            String organization_id = data.optString("organization_id", " ");
                            String organization_name = data.optString("organization_name", " ");
                            String agent_id = data.optString("agent_id", " ");
                            String agent_name = data.optString("agent_name", " ");
                            String Member_group_idfk = data.optString("Member_group_idfk", " ");
                            String member_group = data.optString("member_group", " ");
                            String primary_org_id = data.optString("primary_org_id", " ");
                            String organisation_group_type = data.optString("organisation_group_type", " ");


                            if (adhar != null) {
                                String member_dob = adhar.optString("member_dob", " ");
                                String member_permanent_address = adhar.optString("member_permanent_address", " ");
                                String adhar_member_sex = adhar.optString("member_sex", " ");
                                String member_fullname = adhar.optString("member_fullname", " ");
                                String adhar_member_mobile_no = adhar.optString("member_mobile_no", " ");


                                noteRepository.updateProfile(
                                        member_fullname,
                                        member_permanent_address,
                                        member_local_add,
                                        alternative_Number,
                                        member_dob,
                                        member_occupation,
                                        adhar_member_sex,
                                        village_name,
                                        area_name,
                                        reg_city,
                                        member_local_add_taluk,
                                        reg_state,
                                        member_local_add_pincode,
                                        memberTypeGroupId,
                                        memberTypeGroupName,
                                        memberType,
                                        memberTypeId,
                                        coordinator,
                                        coordinatorId,
                                        primaryOrganization,
                                        primaryOrganizationId,
                                        profileImage,
                                        primaryOrganGroupName,
                                        primaryOrganGroupId,
                                        common.UPDATEDRECORD);

                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        if (member_local_add != null && !member_local_add.isEmpty() && !member_local_add.equals("NA")) {
                                            etLocalAddress.setText(member_local_add);
                                        }
                                        if (village_name != null && !village_name.isEmpty() && !village_name.equals("NA")) {
                                            etVillage.setText(village_name);
                                        }
                                        if (area_name != null && !area_name.isEmpty() && !area_name.equals("NA")) {
                                            etArea.setText(area_name);
                                        }
                                        if (member_local_add_taluk != null && !member_local_add_taluk.isEmpty() && !member_local_add_taluk.equals("NA")) {
                                            etTaluk.setText(member_local_add_taluk);
                                        }
                                        if (reg_city != null && !reg_city.isEmpty() && !reg_city.equals("NA")) {
                                            etCity.setText(reg_city);
                                        }
                                        if (reg_state != null && !reg_state.isEmpty() && !reg_state.equals("NA")) {
                                            etState.setText(reg_state);
                                        }
                                        if (member_local_add_pincode != null && !member_local_add_pincode.isEmpty() && !member_local_add_pincode.equals("NA")) {
                                            etZipcode.setText(member_local_add_pincode);
                                        }
                                    }
                                });
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            return null;
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

    private static String getFileName(@NonNull Context context, Uri uri , String path) {
        String mimeType = context.getContentResolver().getType(uri);
        String filename = null;

        if (mimeType == null) {
            //String path = getPath(context, uri);
            if (path == null) {
                filename = getName(uri.toString());
            } else {
                File file = new File(path);
                filename = file.getName();
            }
        } else {
            Cursor returnCursor = context.getContentResolver().query(uri, null, null, null, null);
            if (returnCursor != null) {
                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                returnCursor.moveToFirst();
                filename = returnCursor.getString(nameIndex);
                returnCursor.close();
            }
        }

        return filename;
    }

    private static String getName(String filename) {
        if (filename == null) {
            return null;
        }
        int index = filename.lastIndexOf('/');
        return filename.substring(index + 1);
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public boolean isNetworkConnected() {
        ConnectivityManager
                connmanager = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connmanager.getActiveNetworkInfo();
        boolean isConnected = (activeNetwork != null) && (activeNetwork.isConnectedOrConnecting());
        return isConnected;
    }



}
