package com.icls.offlinekyc.function.Service.MyService;

import android.Manifest;
import android.app.Service;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.icls.offlinekyc.Database.OrganizationTypeParam;
import com.icls.offlinekyc.R;
import com.icls.offlinekyc.adapters.NotificationAdapter;
import com.icls.offlinekyc.commonshare.common;
import com.icls.offlinekyc.function.DrawerActivity;
import com.icls.offlinekyc.function.onNotification;
import com.icls.offlinekyc.helper.AllNotificationHistoryHelper;
import com.icls.offlinekyc.helper.AllServiceHistoryHelper;
import com.icls.offlinekyc.helper.MasterNotification;
import com.icls.offlinekyc.helper.SupportConversationHistory;
import com.icls.offlinekyc.roomdb.OrganizationRepository;
import com.icls.offlinekyc.roomdb.UserProfile;
import com.squareup.okhttp.MultipartBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.icls.offlinekyc.commonshare.common.joinRequestSend;
import static com.icls.offlinekyc.login.LoginInfo.downloadFileFromServer;

public class MyService extends Service implements LifecycleOwner {
    // constant
    public static final okhttp3.MediaType JSON
            = okhttp3.MediaType.parse("application/json; charset=utf-8");
    public static final long NOTIFY_INTERVAL = 30 * 1000;// 1 * 1000 = 1seconds
    private static final String TAG = "MyService";
    public NotificationAdapter adapter;
    ArrayList<String> notificationArrayTitle;
    ArrayList<String> notificationArrayNotificationID;
    ArrayList<String> notificationArrayReplyAllowed;
    ArrayList<String> notificationArrayDescription;
    ArrayList<String> notificationOrgIdfk;
    ArrayList<String> notificationMsgTo;
    String remark = null;
    //String orgId = null;
    String userMemberId = null;
    String synStatus = null;

    String memberId, occupation, Address, vilageName, area, city, taluka, state, pinCode, alternateMobileNo,
            memberTypeIdfk, membberTypeCode, orgId, orgName, agentId, agentName, dateofBirth, pearmanentAddress, memberSex,
            fullName, mobileNo, profilePicture, synStaatus, memberGroupIdfk, primaryOrgId, organisationGroupType;
    File myDir;

    private LifecycleRegistry lifecycleRegistry;
    /*private String gender, mobile, Occupation, zipcode, state, city, taluk,
            areaName, village, referredby, referredContact, memberEmpName,
            memberContactNumber, localAddress, memberType, coordinator, primaryOrganization, memberTypeId
            , agentId, organizationId, profilePicPath;*/
    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();
    // timer handling
    private Timer mTimer = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        lifecycleRegistry = new LifecycleRegistry(this);
        lifecycleRegistry.markState(Lifecycle.State.CREATED);
        // cancel if already existed
        if (mTimer != null) {
            mTimer.cancel();
        } else {
            // recreate new
            mTimer = new Timer();
        }
        // schedule task
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return lifecycleRegistry;
    }


    class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
            // run on another thread
            mHandler.post(() -> {
                // post every 10 sec for notifications
                Log.i(TAG, "run: service ");


                syncOrganization();
                syncAdditionalProfile();
                synAllNotificationHistory();
                synSupportConvHistory();
                synServerData();
                syncAllServiceChat();

            });
        }

        public void syncOrganization() {
            GetTasks gt = new GetTasks();
            gt.execute();
        }

        public void syncAdditionalProfile() {
            SynProfile gt = new SynProfile();
            gt.execute();
        }

        public void synAllNotificationHistory() {
            AllNotification gt = new AllNotification();
            gt.execute();
        }

        public void synSupportConvHistory() {
            SupportConvHistory sConvHistory = new SupportConvHistory();
            sConvHistory.execute();
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public int getRandomNumber(){
            Random r = new Random();
            int a = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                a = r.ints(1, (20000 + 1)).findFirst().getAsInt();
            }
            return a;
        }

        public void synServerData() {
            okhttp3.OkHttpClient client = new okhttp3.OkHttpClient();

            okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/x-www-form-urlencoded");
            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url(common.PHAPAURL + "Syn_cntrl/synchronization")
                    .get()
                    .addHeader("Auth-key", "simplerestapi")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("Authorization", common.TOKEN)
                    .addHeader("User-ID", common.ID)
                    .addHeader("Client-Service", "frontend-client")
                    .build();
            client.newCall(request).enqueue(new okhttp3.Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final OrganizationRepository noteRepository = new OrganizationRepository(getApplicationContext());
                    final String myResponse = response.body().string();

                    try {
                        JSONObject jsn = new JSONObject(myResponse);
                        JSONObject adharDetails = jsn.getJSONObject("adhar_details");
                        JSONArray jsonObject = adharDetails.getJSONArray("data");


                        JSONObject notificationMessage = jsn.getJSONObject("notificationNewMessages");
                        JSONArray notificationMessageArr = notificationMessage.getJSONArray("data");
                        JSONObject notificationMaster = jsn.getJSONObject("notificationMaster");
                        JSONArray notificationMasterArr = notificationMaster.getJSONArray("data");
                        JSONObject ServiceNewMessages = jsn.getJSONObject("ServiceNewMessages");
                        JSONArray ServiceNewMessagesArr = ServiceNewMessages.getJSONArray("data");
                        JSONObject SupporNewMessages = jsn.getJSONObject("SupporNewMessages");
                        JSONArray SupporNewMessagesArr = SupporNewMessages.getJSONArray("data");
                        JSONObject OrganizationTypeParam  = jsn.getJSONObject("OraganizationApplaystatus");
                        JSONArray OrganizationTypeParamArr = OrganizationTypeParam.getJSONArray("data");




                            //Master notification
                            if (notificationMasterArr.length() != 0) {
                                for (int i = 0; i < notificationMasterArr.length(); i++) {
                                    String message_to_id = notificationMasterArr.getJSONObject(i).optString("message_to_id", "");
                                    String notification_id = notificationMasterArr.getJSONObject(i).optString("notification_id", "");

                                    //Test Purpose
                                    String reply_allowed = notificationMasterArr.getJSONObject(i).optString("reply_allowed", "");
                                    String message_subject = notificationMasterArr.getJSONObject(i).optString("message_subject", "");
                                    String parent_messaage_id = notificationMasterArr.getJSONObject(i).optString("parent_messaage_id", "");
                                    String organization_idfk = notificationMasterArr.getJSONObject(i).optString("organization_idfk", "");
                                    String organization_name = notificationMasterArr.getJSONObject(i).optString("organization_name", "");
                                    String message_from = notificationMasterArr.getJSONObject(i).optString("message_from", "");
                                    String message_type_idfk = notificationMasterArr.getJSONObject(i).optString("message_type_idfk", "");
                                    String message = notificationMasterArr.getJSONObject(i).optString("message", "");
                                    String message_to = notificationMasterArr.getJSONObject(i).optString("message_to", "");
                                    String read = notificationMasterArr.getJSONObject(i).optString("read", "");
                                    String syn_status = notificationMasterArr.getJSONObject(i).optString("syn_status", "");
                                    // now inserting data to local db//
                                    noteRepository.insertMasterNotification(message_to_id, notification_id, reply_allowed, message_subject,
                                            parent_messaage_id, organization_idfk, organization_name, message_from, message_type_idfk, message, message_to, read, syn_status);


                                }
                            }

                            //AllNotificationTransactionHistoryHelper
                            if (notificationMessageArr.length() != 0) {
                                for (int i = 0; i < notificationMessageArr.length(); i++) {


                                    String id = notificationMessageArr.getJSONObject(i).optString("id", "");



                                    String notification_idfk = notificationMessageArr.getJSONObject(i).optString("notification_idfk", "");
                                    String organization_id = notificationMessageArr.getJSONObject(i).optString("organization_id", "");
                                    String member_id = notificationMessageArr.getJSONObject(i).optString("member_id", "");
                                    String Message_form = notificationMessageArr.getJSONObject(i).optString("Message_form", "");
                                    String Message_to = notificationMessageArr.getJSONObject(i).optString("Message_to", "");
                                    String Message = notificationMessageArr.getJSONObject(i).optString("Message", "");
                                    String attachment_name = notificationMessageArr.getJSONObject(i).optString("attachment_name", "");
                                    String file_ext = notificationMessageArr.getJSONObject(i).optString("file_ext", "");
                                    String mime_typ = notificationMessageArr.getJSONObject(i).optString("mime_typ", "");
                                    String Parent_id = notificationMessageArr.getJSONObject(i).optString("Parent_id", "");
                                    String read = notificationMessageArr.getJSONObject(i).optString("read", "");
                                    String syn_status = notificationMessageArr.getJSONObject(i).optString("syn_status", "");
                                    String created_date = notificationMessageArr.getJSONObject(i).optString("created_date", "");
                                    String file_path = notificationMessageArr.getJSONObject( i ).optString( "file_path", "" );
                                    String local_filepath = "";
                                    /*downloading the chat images into documents folder*/

                                    String fileUrl = common.PHAPHAFILEURL + file_path+attachment_name;


                                    if (!attachment_name.equalsIgnoreCase( "" )) {
                                        local_filepath= "/Phapa/Documents/"+attachment_name;
                                        downloadFileFromServer(attachment_name,fileUrl);
                                    }





                                    // now inserting data to local db//
                                    noteRepository.insertAllChatHistory(
                                            notification_idfk,
                                            organization_id, member_id,
                                            Message_form, Message_to, Message, attachment_name, file_ext,
                                            mime_typ,
                                            Parent_id, read, syn_status, created_date,file_path,local_filepath);
                                }
                            }

                        //AllServiceHistoryHelper

                            if (ServiceNewMessagesArr.length() != 0) {

                                for (int i = 0; i < ServiceNewMessagesArr.length(); i++) {
                                    String id = ServiceNewMessagesArr.getJSONObject(i).optString("id", "");
                                    String Service_Msg_id = ServiceNewMessagesArr.getJSONObject(i).optString("Service_Msg_id", "");
                                    String Service_id = ServiceNewMessagesArr.getJSONObject(i).optString("Service_id", "");
                                    String organization_id = ServiceNewMessagesArr.getJSONObject(i).optString("organization_id", "");
                                    String member_id = ServiceNewMessagesArr.getJSONObject(i).optString("member_id", "");
                                    String Message_form = ServiceNewMessagesArr.getJSONObject(i).optString("Message_form", "");
                                    String Message_to = ServiceNewMessagesArr.getJSONObject(i).optString("Message_to", "");
                                    String Message = ServiceNewMessagesArr.getJSONObject(i).optString("Message", "");
                                    String attachment_name = ServiceNewMessagesArr.getJSONObject(i).optString("attachment_name", "");
                                    String file_ext = ServiceNewMessagesArr.getJSONObject(i).optString("file_ext", "");
                                    String mime_typ = ServiceNewMessagesArr.getJSONObject(i).optString("mime_typ", "");
                                    String Parent_id = ServiceNewMessagesArr.getJSONObject(i).optString("Parent_id", "");
                                    String read = ServiceNewMessagesArr.getJSONObject(i).optString("read", "");
                                    String syn_status = ServiceNewMessagesArr.getJSONObject(i).optString("syn_status", "");
                                    String created_date = ServiceNewMessagesArr.getJSONObject(i).optString("created_date", "");
                                    String file_path = ServiceNewMessagesArr.getJSONObject( i ).optString( "file_path", "" );
                                    String local_filepath = "";
                                    /*downloading the chat images into documents folder*/


                                    String fileUrl = common.PHAPHAFILEURL + file_path+attachment_name;


                                    if (!attachment_name.equalsIgnoreCase( "" )) {
                                        local_filepath= "/Phapa/Documents/"+attachment_name;
                                        downloadFileFromServer(attachment_name,fileUrl);
                                    }




                                    /*downloading the chat images into documents folder*/

                                    // now inserting data to local db//
                                    noteRepository.insertAllServiceChatHistory(
                                            Service_Msg_id,Service_id,
                                            organization_id, member_id,
                                            Message_form, Message_to, Message, attachment_name, file_ext,
                                            mime_typ,
                                            Parent_id, read, syn_status, created_date,file_path,local_filepath );
                                }
                            }

                            //SupportConversationHistory
                            if (SupporNewMessagesArr.length() != 0) {
                                for (int i = 0; i < SupporNewMessagesArr.length(); i++) {
                                    JSONObject orgGroup = SupporNewMessagesArr.getJSONObject(i);
                                    //String SupportId = orgGroup.getString("Support_id");
                                    String organizationId = orgGroup.getString("organization_id");
                                    String memberId = orgGroup.getString("member_id");
                                    String MessageForm = orgGroup.getString("Message_form");
                                    String MessageTo = orgGroup.getString("Message_to");
                                    String Message = orgGroup.getString("Message");
                                    String attachmentName = orgGroup.getString("attachment_name");
                                    String fileExt = orgGroup.getString("file_ext");
                                    String filePath = orgGroup.getString("file_path");
                                    String mimeTyp = orgGroup.getString("mime_typ");
                                    String ParentId = orgGroup.getString("Parent_id");
                                    String read = orgGroup.getString("read");
                                    String synStatus = orgGroup.getString("syn_status");
                                    String createdDate = orgGroup.getString("created_date");
                                    String file_path = orgGroup.optString( "file_path", "" );
                                    String local_filepath = "";
                                    /*downloading the chat images into documents folder*/

                                    String fileUrl = common.PHAPHAFILEURL + file_path+attachmentName;


                                    if (!attachmentName.equalsIgnoreCase( "" )) {
                                        local_filepath= "/Phapa/Documents/"+attachmentName;
                                        downloadFileFromServer(attachmentName,fileUrl);
                                    }



                                    noteRepository.setSupportConversationHistory( organizationId, memberId,
                                            MessageForm, MessageTo, Message, attachmentName, fileExt, filePath,
                                            mimeTyp, ParentId, read, synStatus, createdDate,local_filepath);
                                }
                            }

                            // Organisation type param status update
                            if (OrganizationTypeParamArr.length() != 0) {
                                for (int i = 0; i < OrganizationTypeParamArr.length(); i++){
                                    JSONObject orgType = OrganizationTypeParamArr.getJSONObject(i);
                                        String organization_id = orgType.getString( "organization_id" );
                                        String primary_org_idfk = orgType.getString( "primary_org_idfk" );
                                        String organization_name = orgType.getString( "organization_name" );
                                        String address = orgType.getString( "address" );
                                        String website = orgType.getString( "website" );
                                        String email = orgType.getString( "email" );
                                        String org_mem_id = orgType.getString( "org_mem_id" );
                                        String member_idfk = orgType.getString( "member_idfk" );
                                        String P_idfk = orgType.getString( "P_idfk" );
                                        String state = orgType.getString( "state" );
                                        String city = orgType.getString( "city" );
                                        String zip = orgType.getString( "zip" );
                                        String country = orgType.getString( "country" );
                                        String orgTypeIDFK = orgType.getString( "orgTypeIDFK" );
                                        String Phone = orgType.getString( "Phone" );
                                        String organization_info = orgType.getString( "organization_info" );
                                        String logo = orgType.getString( "logo" );
                                        String Joining_Remarks = orgType.getString( "Joining_Remarks" );
                                        String status = orgType.getString( "status" );
                                        String syn_status = orgType.getString( "syn_status" );

                                        noteRepository.updateOrgJoinStatus(organization_id,status);


                                }

                            }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        public  void downloadFileFromServer(String filename, String urlString) throws MalformedURLException, IOException
        {
            BufferedInputStream in = null;
            FileOutputStream fout = null;
            try
            {
                URL url = new URL(urlString);

                in = new BufferedInputStream(url.openStream());
                String root = Environment.getExternalStorageDirectory().toString();
                fout = new FileOutputStream(root + "/Phapa/Documents/"+filename);


                byte data[] = new byte[1024];
                int count;
                while ((count = in.read(data, 0, 1024)) != -1)
                {
                    fout.write(data, 0, count);
                    System.out.println(count);
                }
            }
            finally
            {
                if (in != null)
                    in.close();
                if (fout != null)
                    fout.close();
            }
            System.out.println("Done");
        }

        public void syncAllServiceChat() {
            AllServiceChat gt = new AllServiceChat();
            gt.execute();
        }

        class SynProfile extends AsyncTask<Void, Void, List<UserProfile>> {

            @Override
            protected List<UserProfile> doInBackground(Void... voids) {
                final OrganizationRepository noteRepository = new OrganizationRepository(getApplicationContext());
                List<UserProfile> profile = noteRepository.getinstance().daoAccess().syncProfile(common.NEWRECORD, common.UPDATEDRECORD);
                noteRepository.closeDB();
                return profile;
            }

            public String getURLForResource(int resourceId) {
                //use BuildConfig.APPLICATION_ID instead of R.class.getPackage().getName() if both are not same
                return Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + resourceId).toString();
            }

            @Override
            protected void onPostExecute(List<UserProfile> userProfiles) {
                super.onPostExecute(userProfiles);
                if (userProfiles.size() != 0) {
                    for (UserProfile profile : userProfiles) {
                        memberId = profile.getMember_id();
                        occupation = profile.getMember_occupation();
                        Address = profile.getMember_local_add();
                        vilageName = profile.getVillage_name();
                        area = profile.getArea_name();
                        city = profile.getReg_city();
                        taluka = profile.getMember_local_add_taluk();
                        state = profile.getReg_state();
                        pinCode = profile.getMember_local_add_pincode();
                        alternateMobileNo = profile.getAlternative_Number();
                        memberTypeIdfk = profile.getMemTypeIDFK();
                        if (memberTypeIdfk == null) {
                            memberTypeIdfk = "null";
                        }
                        memberGroupIdfk = profile.getMember_group_idfk();
                        if (memberGroupIdfk == null) {
                            memberGroupIdfk = "null";
                        }
                        membberTypeCode = profile.getMemberTypeCode();
                        orgId = profile.getOrganization_id();
                        if (orgId == null) {
                            orgId = "null";
                        }
                        orgName = profile.getOrganization_name();
                        agentId = profile.getAgent_id();
                        if (agentId == null) {
                            agentId = "null";
                        }

                        primaryOrgId = profile.getPrimary_org_id();
                        if (primaryOrgId == null) {
                            primaryOrgId = "null";
                        }

                        organisationGroupType = profile.getOrganisation_group_type();
                        if(organisationGroupType == null) {
                            organisationGroupType = "null";
                        }

                        agentName = profile.getAgent_name();
                        dateofBirth = profile.getMember_dob();
                        pearmanentAddress = profile.getMember_permanent_address();
                        memberSex = profile.getMember_sex();
                        if (memberSex.equals("Select Gender*")) {
                            memberSex = "null";
                        }

                        fullName = profile.getMember_fullname();
                        mobileNo = profile.getMember_mobile_no();
                        profilePicture = profile.getProfilePicture();

                    }

                    if (fullName != null && !fullName.isEmpty() && !fullName.equals("null")) {
                    } else {
                        fullName = "";
                    }
                    if (mobileNo != null && !mobileNo.isEmpty() && !mobileNo.equals("null")) {
                    } else {
                        mobileNo = "";
                    }
                    if (pearmanentAddress != null && !pearmanentAddress.isEmpty() && !pearmanentAddress.equals("null")) {
                    } else {
                        pearmanentAddress = "";
                    }
                    if (dateofBirth != null && !dateofBirth.isEmpty() && !dateofBirth.equals("null")) {
                    } else {
                        dateofBirth = "";
                    }


                    OkHttpClient client = new OkHttpClient();
                    final MediaType MEDIA_TYPE_PNG1 = MediaType.parse("image/*");

                    Request request = null;
                    RequestBody requestBody = null;

                    if (profilePicture != null) {
                        String root = Environment.getExternalStorageDirectory().toString();
                        myDir = new File(root + "/Phapa/" + profilePicture);
                        requestBody = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("member_id", memberId)
                                .addFormDataPart("mem_profile_pic", profilePicture,
                                        RequestBody.create(MEDIA_TYPE_PNG1,
                                                new File(root + "/Phapa/" + profilePicture)))

                                .addFormDataPart("member_local_add", Address)
                                .addFormDataPart("member_occupation", occupation)
                                .addFormDataPart("member_sex", memberSex)
                                .addFormDataPart("village_name", vilageName)
                                .addFormDataPart("area_name", area)
                                .addFormDataPart("reg_city", city)
                                .addFormDataPart("member_local_add_taluk", taluka)
                                .addFormDataPart("member_local_add_pincode", pinCode)
                                .addFormDataPart("reg_state", state)
                                .addFormDataPart("alternative_number", alternateMobileNo)
                                .addFormDataPart("memTypeIDFK", memberTypeIdfk)
                                .addFormDataPart("member_group_idfk", memberGroupIdfk)
                                .addFormDataPart("Primary_Org_ID", orgId)
                                .addFormDataPart("org_group_idfk", primaryOrgId)
                                .addFormDataPart("organisation_group_type", organisationGroupType)
                                .addFormDataPart("agent_idfk", agentId)
                                .addFormDataPart("member_dob", dateofBirth)
                                .addFormDataPart("member_permanent_address", pearmanentAddress)
                                .addFormDataPart("member_fullname", fullName)
                                .addFormDataPart("member_mobile_no", mobileNo)
                                .build();
                    } else {
                        requestBody = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("member_id", memberId)
                                .addFormDataPart("member_local_add", Address)
                                .addFormDataPart("member_occupation", occupation)
                                .addFormDataPart("member_sex", memberSex)
                                .addFormDataPart("village_name", vilageName)
                                .addFormDataPart("area_name", area)
                                .addFormDataPart("reg_city", city)
                                .addFormDataPart("member_local_add_taluk", taluka)
                                .addFormDataPart("member_local_add_pincode", pinCode)
                                .addFormDataPart("reg_state", state)
                                .addFormDataPart("alternative_number", alternateMobileNo)
                                .addFormDataPart("memTypeIDFK", memberTypeIdfk)
                                .addFormDataPart("member_group_idfk", memberGroupIdfk)
                                .addFormDataPart("Primary_Org_ID", orgId)
                                .addFormDataPart("org_group_idfk", primaryOrgId)
                                .addFormDataPart("organisation_group_type", organisationGroupType)
                                .addFormDataPart("agent_idfk", agentId)
                                .addFormDataPart("member_dob", dateofBirth)
                                .addFormDataPart("member_permanent_address", pearmanentAddress)
                                .addFormDataPart("member_fullname", fullName)
                                .addFormDataPart("member_mobile_no", mobileNo)
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
                            try {
                                final JSONObject obj = new JSONObject(myResponse);
                                final String status = obj.optString("status", " ");
                                if (status.equalsIgnoreCase("200")) {
                                    final OrganizationRepository noteRepository = new OrganizationRepository(getApplicationContext());
                                    noteRepository.updateUserProfile(common.NOCHANGE);
                                    noteRepository.closeDB();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }


                    });
                }
            }
        }

        class AllServiceChat extends AsyncTask<Void, Void, List<AllServiceHistoryHelper>> {
            @Override
            protected List<AllServiceHistoryHelper> doInBackground(Void... voids) {
                final OrganizationRepository noteRepository = new OrganizationRepository(getApplicationContext());
                List<AllServiceHistoryHelper> allservicechat = noteRepository.getinstance().daoAccess().syncAllServiceChat(common.NEWRECORD);
                noteRepository.closeDB();
                return allservicechat;
            }

            @Override
            protected void onPostExecute(List<AllServiceHistoryHelper> allServiceHistoryHelpers) {
                super.onPostExecute(allServiceHistoryHelpers);
                if (allServiceHistoryHelpers.size() != 0) {

                    AllServiceHistoryHelper note = allServiceHistoryHelpers.get(0);

                    if(!note.getMessage().equalsIgnoreCase( "NULL" )){

                        OkHttpClient client = new OkHttpClient();

                        RequestBody body = new FormBody.Builder()
                                .add("Service_id", note.getService_id())
                                .add("message", note.getMessage())
                                .add("message_from", common.ID)
                                .add("message_to", note.getMessage_to())
                                .add("organization_id", note.getOrganization_id())
                                .add( "currentTimeStamp",note.getCreated_date() )
                                .build();
                        Request request = new Request.Builder()
                                .url(common.PHAPAURL + "ChatConverstion_cntrl/ConverstionServiceRequestStart")
                                .post(body)
                                .addHeader("Client-Service", "frontend-client")
                                .addHeader("Auth-key", "simplerestapi")
                                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                                .addHeader("User-ID", common.ID)
                                .addHeader("Authorization", common.TOKEN)
                                .addHeader("Accept", "*/*")
                                .build();
                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                final String myResponse = response.body().string();
                                try {
                                    JSONObject jsn = new JSONObject(myResponse);
                                    if (jsn.optString("message").equalsIgnoreCase("Success")) {
                                        final OrganizationRepository noteRepository = new OrganizationRepository(getApplicationContext());
                                        noteRepository.updateSycStatusOfAllChatService(common.NOCHANGE, note.getId());
                                        noteRepository.closeDB();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } else {

                        String filepath = note.getLocal_filepath();
                        String filename = note.getAttachment_name();
                        String input = filepath.replace(" ", "");
                        String mymetype = getMimeType(input);

                        String path = "/storage/emulated/0/Phapa/Documents/"+note.getAttachment_name();
                        File file = new File( path );
                        int size = (int) file.length();
                        Log.d( TAG, "File size for service ++++++++++++++++================>>>"+ size );
                        /*if message is NULL then send attachment */

                        OkHttpClient client = new OkHttpClient();

                        RequestBody body = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("notiOrSer_idfk", note.getService_id())
                                .addFormDataPart("Message_to", note.getMessage_to())
                                .addFormDataPart("Message_form", note.getMessage_form())
                                .addFormDataPart("member_id", note.getMember_id())
                                .addFormDataPart("organization_id", note.getOrganization_id())
                                .addFormDataPart("chatTypeId", "2")
                                .addFormDataPart("attachmentfile", filename,RequestBody.create(MediaType.parse("multipart/form-data"),
                                        file))
                                .build();
                        Request request = new Request.Builder()
                                .url(common.PHAPAURL + "ChatConverstion_cntrl/FileUpload")
                                .post(body)
                                .addHeader("Client-Service", "frontend-client")
                                .addHeader("Auth-key", "simplerestapi")
                                .addHeader("content-type", "multipart/form-data")
                                .addHeader("Content-Type", mymetype)
                                .addHeader("User-ID", common.ID)
                                .addHeader("Authorization", common.TOKEN)
                                .addHeader("Accept", "*/*")
                                .build();
                        client.newCall( request ).enqueue( new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                final String myResponse = response.body().string();
                                try {
                                    JSONObject jsn = new JSONObject(myResponse);
                                            JSONObject jsn2=jsn.getJSONObject( "data" );
                                    if (jsn2.optString("message").equalsIgnoreCase("Success")) {
                                        final OrganizationRepository noteRepository = new OrganizationRepository(getApplicationContext());
                                        noteRepository.updateSycStatusOfAllChatService(common.NOCHANGE, note.getId());
                                        noteRepository.closeDB();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } );

                    }

                }
            }
        }
        public  String getMimeType(String url) {
            String type = null;
            String extension = MimeTypeMap.getFileExtensionFromUrl(url);
            if (extension != null) {
                type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            }
            return type;
        }
		class SupportConvHistory extends AsyncTask<Void, Void, List<SupportConversationHistory>> {

            @Override
            protected List<SupportConversationHistory> doInBackground(Void... voids) {
                final OrganizationRepository noteRepository = new OrganizationRepository(getApplicationContext());
                List<SupportConversationHistory> allservicechat = noteRepository.getinstance().daoAccess().syncSupportChat(common.NEWRECORD);
                noteRepository.closeDB();
                return allservicechat;
            }

            @Override
            protected void onPostExecute(List<SupportConversationHistory> supportConvHistories) {
                super.onPostExecute(supportConvHistories);
                if (supportConvHistories.size() != 0) {
                    SupportConversationHistory note = supportConvHistories.get(supportConvHistories.size() - 1);


                    if(!note.getMessage().equalsIgnoreCase( "NULL" )) {

                        final JSONObject jsonBody1 = new JSONObject();
                        for (SupportConversationHistory ConvHistories : supportConvHistories) {
                            try {
                                jsonBody1.put( "message", ConvHistories.getMessage() );
                                jsonBody1.put( "message_from", common.ID );
                                jsonBody1.put( "message_to", ConvHistories.getMessage_to() );
                                jsonBody1.put( "currentTimeStamp", ConvHistories.getCreated_date() );
                                jsonBody1.put( "organization_id", ConvHistories.getOrganization_id() );
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        OkHttpClient client = new OkHttpClient();

                        MediaType mediaType = MediaType.parse( "application/x-www-form-urlencoded" );
                        RequestBody body = RequestBody.create( JSON, jsonBody1.toString() );
                        Request request = new Request.Builder()
                                .url( common.PHAPAURL + "ChatConverstion_cntrl/SupportConversationStart" )
                                .post( body )
                                .addHeader( "Client-Service", "frontend-client" )
                                .addHeader( "Auth-key", "simplerestapi" )
                                .addHeader( "Content-Type", "application/x-www-form-urlencoded" )
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
                                try {
                                    JSONObject jsn = new JSONObject( myResponse );
                                    if (jsn.optString( "message" ).equalsIgnoreCase( "Success" )) {
                                        final OrganizationRepository noteRepository = new OrganizationRepository( getApplicationContext() );
                                        noteRepository.updateSycSupportConvChatService( common.NOCHANGE, note.getSupport_id() );
                                        noteRepository.closeDB();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } );
                    }else {

                        /*if msg null send attachment*/
                        String filepath = note.getLocal_filepath();
                        String filename = note.getAttachment_name();
                        String input = filepath.replace(" ", "");
                        String mymetype = getMimeType(input);


                        String path = "/storage/emulated/0/Phapa/Documents/"+note.getAttachment_name();
                        File file = new File( path );
                        int size = (int) file.length();
                        Log.d( TAG, "File size in support++++++++++++++++================>>>"+ size );
                        /*if message is NULL then send attachment */

                        OkHttpClient client = new OkHttpClient();

                        RequestBody body = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("notiOrSer_idfk", "")
                                .addFormDataPart("Message_to", note.getMessage_to())
                                .addFormDataPart("Message_form", note.getMessage_form())
                                .addFormDataPart("member_id", note.getMember_id())
                                .addFormDataPart("organization_id", note.getOrganization_id())
                                .addFormDataPart("chatTypeId", "3")
                                .addFormDataPart("attachmentfile", filename,RequestBody.create(MediaType.parse("multipart/form-data"),
                                        file))
                                .build();
                        Request request = new Request.Builder()
                                .url(common.PHAPAURL + "ChatConverstion_cntrl/FileUpload")
                                .post(body)
                                .addHeader("Client-Service", "frontend-client")
                                .addHeader("Auth-key", "simplerestapi")
                                .addHeader("content-type", "multipart/form-data")
                                .addHeader("Content-Type", mymetype)
                                .addHeader("User-ID", common.ID)
                                .addHeader("Authorization", common.TOKEN)
                                .addHeader("Accept", "*/*")
                                .build();
                        client.newCall( request ).enqueue( new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                final String myResponse = response.body().string();
                                try {
                                    JSONObject jsn = new JSONObject(myResponse);
                                    JSONObject jsn2=jsn.getJSONObject( "data" );
                                    if (jsn2.optString("message").equalsIgnoreCase("Success")) {
                                        final OrganizationRepository noteRepository = new OrganizationRepository(getApplicationContext());
                                        noteRepository.updateSycSupportConvChatService(common.NOCHANGE, note.getSupport_id());
                                        noteRepository.closeDB();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } );

                    }
                }
            }
        }
        class AllNotification extends AsyncTask<Void, Void, List<AllNotificationHistoryHelper>> {
            @Override
            protected List<AllNotificationHistoryHelper> doInBackground(Void... voids) {
                final OrganizationRepository noteRepository = new OrganizationRepository(getApplicationContext());
                List<AllNotificationHistoryHelper> allnotihistory = noteRepository.getinstance().daoAccess().syncAllNotificationHistory(common.NEWRECORD);
                noteRepository.closeDB();
                return allnotihistory;
            }

            @Override
            protected void onPostExecute(List<AllNotificationHistoryHelper> allNotificationHistoryHelpers) {
                super.onPostExecute(allNotificationHistoryHelpers);
                if (allNotificationHistoryHelpers.size() != 0) {


                    AllNotificationHistoryHelper note = allNotificationHistoryHelpers.get(0);

                    if(!note.getMessage().equalsIgnoreCase( "NULL" )){

                        OkHttpClient client = new OkHttpClient();

                        RequestBody body = new FormBody.Builder()
                        .add("notification_id", note.getNotification_idfk())
                        .add("message", note.getMessage())
                        .add("message_from", common.ID)
                        .add("message_to", note.getMessage_to())
                        .add("organization_id", note.getOrganization_id())
                        .add( "currentTimeStamp",note.getCreated_date() )
                        .build();

                        Request request = new Request.Builder()
                                .url(common.PHAPAURL + "ChatConverstion_cntrl/ConverstionNotificationStart")
                                .post(body)
                                .addHeader("Client-Service", "frontend-client")
                                .addHeader("Auth-key", "simplerestapi")
                                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                                .addHeader("User-ID", common.ID)
                                .addHeader("Authorization", common.TOKEN)
                                .addHeader("Accept", "*/*")
                                .build();
                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                final String myResponse = response.body().string();
                                try {
                                    JSONObject jsn = new JSONObject(myResponse);
                                    if (jsn.optString("message").equalsIgnoreCase("Success")) {

                                        final OrganizationRepository noteRepository = new OrganizationRepository(getApplicationContext());
                                        noteRepository.updateSycStatusOfAllChatHistory(common.NOCHANGE, note.getId());
                                        noteRepository.closeDB();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } else {

                                /*if attachment there use this*/
                        String filepath = note.getLocal_filepath();
                        String filename = note.getAttachment_name();

                        String input = filepath.replace(" ", "");
                        String mymetype = getMimeType(input);

                        String path = "/storage/emulated/0/Phapa/Documents/"+note.getAttachment_name();
                        File file = new File( path );
                        int size = (int) file.length();
                        Log.d( TAG, "File size in notification ++++++++++++++++================>>>"+ size );
                        /*if message is NULL then send attachment */

                        OkHttpClient client = new OkHttpClient();

                        RequestBody body = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("notiOrSer_idfk", note.getNotification_idfk())
                                .addFormDataPart("Message_to", note.getMessage_to())
                                .addFormDataPart("Message_form", note.getMessage_form())
                                .addFormDataPart("member_id", note.getMember_id())
                                .addFormDataPart("organization_id", note.getOrganization_id())
                                .addFormDataPart("chatTypeId", "1")
                                .addFormDataPart("attachmentfile", filename,RequestBody.create(MediaType.parse("multipart/form-data"),
                                        file))
                                .build();
                        Request request = new Request.Builder()
                                .url(common.PHAPAURL + "ChatConverstion_cntrl/FileUpload")
                                .post(body)
                                .addHeader("Client-Service", "frontend-client")
                                .addHeader("Auth-key", "simplerestapi")
                                .addHeader("content-type", "multipart/form-data")
                                .addHeader("Content-Type", mymetype)
                                .addHeader("User-ID", common.ID)
                                .addHeader("Authorization", common.TOKEN)
                                .addHeader("Accept", "*/*")
                                .build();
                        client.newCall( request ).enqueue( new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                final String myResponse = response.body().string();
                                try {
                                    JSONObject jsn = new JSONObject(myResponse);
                                    JSONObject jsn2=jsn.getJSONObject( "data" );
                                    if (jsn2.optString("message").equalsIgnoreCase("Success")) {
                                        final OrganizationRepository noteRepository = new OrganizationRepository(getApplicationContext());
                                        noteRepository.updateSycStatusOfAllChatHistory(common.NOCHANGE, note.getId());
                                        noteRepository.closeDB();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } );

                    }
                }
            }
        }

        class GetTasks extends AsyncTask<Void, Void, List<OrganizationTypeParam>> {

            @Override
            protected List<OrganizationTypeParam> doInBackground(Void... voids) {
                final OrganizationRepository noteRepository = new OrganizationRepository(getApplicationContext());
                List<OrganizationTypeParam> taskList = noteRepository.getinstance().daoAccess().syncOrganizationNew(common.UPDATEDRECORD);
                noteRepository.closeDB();
                return taskList;
            }

            @Override
            protected void onPostExecute(List<OrganizationTypeParam> tasks) {
                super.onPostExecute(tasks);

                if (tasks.size() != 0) {
                    Log.d("getlist", "" + tasks.size());
                    for (OrganizationTypeParam note : tasks) {
                        remark = note.getJoining_Remarks();
                        orgId = note.getOrganization_id();
                        userMemberId = note.getMember_idfk();
                        synStatus = note.getSyn_status();
                    }
                    OkHttpClient client = new OkHttpClient();
                    /*synStatus = "2";*/
                    final JSONObject jsonBody1 = new JSONObject();
                    try {
                        jsonBody1.put("joining_remarks", remark);
                        jsonBody1.put("organization_id", orgId);
                        jsonBody1.put("member_idfk", userMemberId);
                        jsonBody1.put("syn_status", common.UPDATEDRECORD);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    MediaType mediaType = MediaType.parse("application/json");
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    // put your json here
                    final RequestBody body1 = RequestBody.create(JSON, jsonBody1.toString());


                    String extendedUrl = "OrganizationListjoin/PhapaOrganizationJoin";
                    final SharedPreferences prefs = getSharedPreferences("PASSCODEDB", MODE_PRIVATE);
                    String user = prefs.getString("user", "9999");// "9999" is the default value.
                    final Request request = new Request.Builder()
                            .url(common.PHAPAURL + extendedUrl).post(body1)
                            .addHeader("Client-Service", "frontend-client")
                            .addHeader("Auth-key", "simplerestapi")
                            .addHeader("Content-Type", "application/json")
                            .addHeader("User-ID", common.ID)
                            .addHeader("Authorization", common.TOKEN)
                            .addHeader("Accept", "*/*")
                            .build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                                e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            try {
                                int code = response.code();
                                final OrganizationRepository noteRepository = new OrganizationRepository(getApplicationContext());
                                if (code == 200) {
                                    noteRepository.updateOrganization(common.NOCHANGE, remark, joinRequestSend, orgId);

                                }
                                noteRepository.closeDB();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

            }
        }
    }

    private void SaveImage(Bitmap finalBitmap, String filename) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/Phapa/Documents");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }

        File file = new File (myDir, filename);
        if (file.exists ())
            file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}