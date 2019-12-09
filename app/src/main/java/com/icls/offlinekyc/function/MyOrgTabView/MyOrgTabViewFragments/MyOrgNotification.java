package com.icls.offlinekyc.function.MyOrgTabView.MyOrgTabViewFragments;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.icls.offlinekyc.R;
import com.icls.offlinekyc.adapters.NotificationAdapter;
import com.icls.offlinekyc.adapters.NotificationChatAdapter;
import com.icls.offlinekyc.commonshare.common;
import com.icls.offlinekyc.function.Models.ChatModel;
import com.icls.offlinekyc.function.onNotification;
import com.icls.offlinekyc.helper.AllNotificationHistoryHelper;
import com.icls.offlinekyc.helper.MasterNotification;
import com.icls.offlinekyc.roomdb.OrganizationRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.msg91.sendotp.library.internal.ApiService.JSON;


public class MyOrgNotification extends Fragment implements NotificationChatAdapter.ItemClickListener {
    private static final String TAG = "MyOrgNotification";

    private NotificationChatAdapter adapter;
    RecyclerView recyclerView;
    EditText InputMesg ;
    Button SendMesgButton;
    ArrayList<ChatModel> notificationMessageList;
    String notificationID;
    String replyAllowed;
    String notificationOrgId,notificationMsgtO;
    LinearLayout sendmsglayout;
    public MyOrgNotification() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_my_org_notification, container, false );
        // Inflate the layout for this fragment
        notificationID=getActivity().getIntent().getStringExtra( "notificationID" );
        replyAllowed = getActivity().getIntent().getStringExtra( "replyAllowed" );
        notificationOrgId =getActivity().getIntent().getStringExtra( "notificationOrgId" );
        notificationMsgtO = getActivity().getIntent().getStringExtra( "notificationMsgtO" );
        InputMesg = view.findViewById( R.id.input_message );
        SendMesgButton = view.findViewById( R.id.sendbtn );
        recyclerView = (RecyclerView)view.findViewById(R.id.notification_chat_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        sendmsglayout = view.findViewById( R.id.sendmsglayout );



        if(!notificationID.equalsIgnoreCase( "" )) {
            openNotificationChat( notificationID );
            if (replyAllowed.equalsIgnoreCase( "no" )||notificationID.equalsIgnoreCase( "" ) ) {
                sendmsglayout.setVisibility( View.GONE );
            }
        }
        buttonClick();

        return view;
    }

    private void buttonClick() {
        SendMesgButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!InputMesg.getText().toString().isEmpty()) {
                    sendMessage();
                }else {
                    getActivity().runOnUiThread( () -> {
                        Toast.makeText( getActivity(), "Your message field is empty..", Toast.LENGTH_LONG ).show();
                    } );
                }
            }
        } );

    }


    private void sendMessage(){
        final OrganizationRepository noteRepository = new OrganizationRepository(getContext());
        String Parentid = "1";
        if (notificationMessageList.size() > 0) {
            String strid = notificationMessageList.get( notificationMessageList.size()-1 ).getParent_id();
            Parentid= String.valueOf(Integer.parseInt( strid )+1);
        }

        String member_id = common.ID;
        String organization_id = notificationOrgId ;
        String Message_form = common.ID;
        String Message_to = notificationMsgtO;
        String notification_idfk = notificationID ;
        String Message = InputMesg.getText().toString();
        String attachment_name="", file_ext = "",mime_typ = "",read="N",syn_status="0";
        String Parent_id = Parentid;
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String dateTime = currentDate+" "+currentTime;
        Log.d( TAG, "onCreateView: "+currentDate+" "+currentTime );
        String created_date = dateTime ;
        AllNotificationHistoryHelper allchathistory = new AllNotificationHistoryHelper();
        allchathistory.setNotification_idfk( notification_idfk);
        allchathistory.setOrganization_id( organization_id);
        allchathistory.setMember_id(member_id );
        allchathistory.setMessage_form(Message_form);
        allchathistory.setMessage_to(Message_to);
        allchathistory.setMessage( Message);
        allchathistory.setAttachment_name( attachment_name);
        allchathistory.setFile_ext(file_ext);
        allchathistory.setMime_typ( mime_typ);
        allchathistory.setParent_id( Parent_id);
        allchathistory.setRead(read);
        allchathistory.setSyn_status(syn_status );
        allchathistory.setCreated_date( created_date );

           new AsyncTask<Void, Void, Void>() {
               @Override
               protected Void doInBackground(Void... voids) {
                   noteRepository.getinstance().daoAccess().insertMasterNotification(allchathistory);
                   return null;
               }

               @Override
               protected void onPostExecute(Void aVoid) {
                   noteRepository.closeDB();
                   openNotificationChat(notificationID);
                   InputMesg.getText().clear();
               }
           }.execute();

       /* noteRepository.insertAllChatHistory(
                notification_idfk,
                organization_id, member_id,
                Message_form, Message_to, Message, attachment_name, file_ext,
                mime_typ,
                Parent_id, read, syn_status, created_date );


*/




//        final JSONObject jsonBody1 = new JSONObject();
//        try {
//
//            jsonBody1.put( "notification_id",notificationID  );
//            jsonBody1.put( "message", InputMesg.getText().toString());
//            jsonBody1.put( "message_from", common.ID);
//            jsonBody1.put( "message_to", notificationMsgtO);
//            jsonBody1.put( "organization_id",notificationOrgId );
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        OkHttpClient client = new OkHttpClient();
//
//        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
//        RequestBody body = RequestBody.create( JSON, jsonBody1.toString() );
//        Request request = new Request.Builder()
//                .url(common.PHAPAURL+"ChatConverstion_cntrl/ConverstionNotificationStart")
//                .post(body)
//                .addHeader("Client-Service", "frontend-client")
//                .addHeader("Auth-key", "simplerestapi")
//                .addHeader("Content-Type", "application/x-www-form-urlencoded")
//                .addHeader("User-ID", common.ID)
//                .addHeader("Authorization", common.TOKEN)
//                .addHeader("Accept", "*/*")
//                .build();
//        client.newCall( request ).enqueue( new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                final String myResponse = response.body().string();
//                Log.w( TAG, "onResponse: "+myResponse  );
//                try {
//                    JSONObject jsn = new  JSONObject(myResponse);
//
//                    if(jsn.optString( "message" ).equalsIgnoreCase( "Success" )){
//                        openNotificationChat(notificationID);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        } );

    }

    @Override
    public void onItemClick(View view, int position) {

    }
    private void openNotificationChat(String NOTIFICATIONID) {

        // ===================== chat using local db ======

        final OrganizationRepository noteRepository = new OrganizationRepository(getContext());

        noteRepository.getAllNotificationHistoryHelper(NOTIFICATIONID).observe( MyOrgNotification.this, notes -> {
                      notificationMessageList = new ArrayList<ChatModel>(  );

                    for (AllNotificationHistoryHelper note : notes) {

                        ChatModel chatModel = new ChatModel();

                        chatModel.setNotification_idfk( note.getNotification_idfk() );
                        chatModel.setOrganization_id( note.getOrganization_id() );
                        chatModel.setMember_id( note.getMember_id() );
                        chatModel.setMessage_form( note.getMessage_form() );
                        chatModel.setMessage_to( note.getMessage_to());
                        chatModel.setMessage( note.getMessage());
                        chatModel.setAttachment_name( note.getAttachment_name() );
                        chatModel.setFile_ext( note.getFile_ext() );
                        chatModel.setMime_typ( note.getMime_typ() );
                        chatModel.setParent_id( note.getParent_id());
                        chatModel.setRead( note.getRead() );
                        chatModel.setSyn_status( note.getSyn_status());
                        chatModel.setCreated_date( note.getCreated_date() );

                        notificationMessageList.add( chatModel);

                    }
                    getActivity().runOnUiThread( () -> {

                        adapter = new NotificationChatAdapter(getContext(),
                                notificationMessageList,getActivity());
                        adapter.setClickListener( MyOrgNotification.this );
                        recyclerView.scrollToPosition(notificationMessageList.size() - 1);
                        recyclerView.setAdapter(adapter);
                    } );
                    noteRepository.closeDB();
                });



    }

    public interface OnFragmentInteractionListener{
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
