package com.icls.offlinekyc.function.MyOrgTabView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.icls.offlinekyc.R;
import com.icls.offlinekyc.adapters.ChatAdapter;
import com.icls.offlinekyc.adapters.NotificationChatAdapter;
import com.icls.offlinekyc.commonshare.common;
import com.icls.offlinekyc.function.Models.ChatModel;
import com.icls.offlinekyc.function.Models.ServiceChatModel;
import com.icls.offlinekyc.helper.AllNotificationHistoryHelper;
import com.icls.offlinekyc.helper.AllServiceHistoryHelper;
import com.icls.offlinekyc.roomdb.OrganizationRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.icls.offlinekyc.commonshare.common.ORG_NAME;
import static com.icls.offlinekyc.commonshare.common.SELECTEDORGID;

public class ChatActivity extends AppCompatActivity implements ChatAdapter.ItemClickListener,NotificationChatAdapter.ItemClickListener {
    private Toolbar toolbar;
    private String TAG = "ChatActivity";
    private String orgName = "";
    private static final int SELECTED_IMAGE = 0;
    private static final int SELECTED_DOCUMENT = 1;
    private Handler mHandler;
    private String intetfrom = "";
    String notificationID;
    BottomSheetDialog dialog;
    private ChatAdapter adapter;
    private NotificationChatAdapter notificationadapter;
    RecyclerView recyclerView;
    EditText InputMesg ;
    Button SendMesgButton;
    ImageButton bottomsheetbtn;
    TextView attach_pictures,attach_documents;
    ArrayList<ServiceChatModel> serviceChatModelArrayList;
    String serviceId,TabName;
    String replyAllowed;
    String notificationOrgId,notificationMsgtO;
    LinearLayout sendmsglayout;
    ArrayList<ChatModel> notificationMessageList;
    private String selected_attachment_name;
    private List<Integer> msglistCount;

    @Override
    protected void onDestroy() {
        common.PRESENTSCREEN = "";
        msglistCount.clear();
        /*
        Log.d( TAG, "im stopping to run every 5 sec " );*/
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_chat );

        init();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle(ORG_NAME);

        TabName=getIntent().getStringExtra( "TabName" );
        if(TabName.equalsIgnoreCase( "Notification" )){
            common.PRESENTSCREEN = "notificationChat";
            notificationID=getIntent().getStringExtra( "notificationID" );
            notificationMsgtO = getIntent().getStringExtra( "notificationMsgtO" );
            replyAllowed = getIntent().getStringExtra( "replyAllowed" );
            openNotificationChat( notificationID );

            if (replyAllowed.equalsIgnoreCase( "no" )||notificationID.equalsIgnoreCase( "" ) ) {
                sendmsglayout.setVisibility( View.GONE );
            }
        }else{
            common.PRESENTSCREEN = "serviceChat";
            serviceId= getIntent().getStringExtra( "service_id" );

            openServiceChat( serviceId );
        }

    }
    private void sendNotificationMessage(){

        final OrganizationRepository noteRepository = new OrganizationRepository(this);
        String Parentid = "1";
        if (notificationMessageList.size() > 0) {
            String strid = notificationMessageList.get( notificationMessageList.size()-1 ).getParent_id();
            Parentid= String.valueOf(Integer.parseInt( strid )+1);
        }

        String member_id = common.ID;
        String organization_id = SELECTEDORGID ;
        String Message_form = common.ID;
        String Message_to = notificationMsgtO;
        String notification_idfk = notificationID ;
        String Message = InputMesg.getText().toString();
        String attachment_name="", file_ext = "",mime_typ = "",read="N",
                syn_status="0";
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
        allchathistory.setRead( read);
        allchathistory.setSyn_status( syn_status );
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



    }
    private void openNotificationChat(String NOTIFICATIONID) {

        // ===================== chat using local db ======

        final OrganizationRepository noteRepository = new OrganizationRepository(this);

        noteRepository.getAllNotificationHistoryHelper(NOTIFICATIONID).observe( ChatActivity.this, notes -> {
            notificationMessageList = new ArrayList<ChatModel>(  );

            for (AllNotificationHistoryHelper note : notes) {

                 /*chat model is for notification chat */
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
                chatModel.setLocal_filepath( note.getLocal_filepath(  ) );
                chatModel.setFile_path( note.getFile_path() );

                notificationMessageList.add( chatModel);

            }
           runOnUiThread( () -> {

                notificationadapter = new NotificationChatAdapter(this,
                        notificationMessageList,this);
                notificationadapter.setClickListener(  ChatActivity.this );
                recyclerView.scrollToPosition(notificationMessageList.size() - 1);
               recyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);
                recyclerView.setAdapter(notificationadapter);
            } );
            noteRepository.closeDB();
        });



    }
    private void openNotificationChat1(String NOTIFICATIONID) {

        // ===================== chat using local db ======

        final OrganizationRepository noteRepository = new OrganizationRepository(this);

        noteRepository.getAllNotificationHistoryHelper(NOTIFICATIONID).observe( ChatActivity.this, notes -> {
            notificationMessageList = new ArrayList<ChatModel>(  );

            for (AllNotificationHistoryHelper note : notes) {

                /*chat model is for notification chat */
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
                chatModel.setLocal_filepath( note.getLocal_filepath(  ) );
                chatModel.setFile_path( note.getFile_path() );

                notificationMessageList.add( chatModel);

            }

            msglistCount.add( notificationMessageList.size() ) ;
            int last =msglistCount.get( msglistCount.size()-1) ;
            int lastbutone =  msglistCount.get(  msglistCount.size()-2);
            Log.d( TAG, "openNotificationChat1: "+" "+notificationMessageList.size()+" "+last+" " +lastbutone );
            if (last!=lastbutone) {
                runOnUiThread( () -> {
                    notificationadapter = new NotificationChatAdapter(this,
                            notificationMessageList,this);
                    notificationadapter.setClickListener(  ChatActivity.this );
                    recyclerView.scrollToPosition(notificationMessageList.size() - 1);
                    recyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);
                    recyclerView.setAdapter(notificationadapter);
                } );
            }

            noteRepository.closeDB();
        });



    }
    private void init() {
        msglistCount = new ArrayList<>(  );
        msglistCount.add( 0 );
        msglistCount.add( 0 );
        runeveryFivesecUntillDestoyed();

        InputMesg = findViewById( R.id.input_message );
        SendMesgButton = findViewById( R.id.sendbtn );
        bottomsheetbtn = findViewById( R.id.bottomsheetbtn );
        dialog = new BottomSheetDialog(this);
        dialog.setContentView(R.layout.bottom_sheet);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        View bottomSheetView = dialog.getWindow().getDecorView().findViewById(android.support.design.R.id.design_bottom_sheet);
        BottomSheetBehavior.from(bottomSheetView).setHideable(false);

        attach_pictures = dialog.findViewById( R.id.attach_pictures );
        attach_documents= dialog.findViewById( R.id.attach_documents);
        recyclerView = (RecyclerView)findViewById(R.id.notification_chat_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sendmsglayout = findViewById( R.id.sendmsglayout );
        buttonClick();
    }

    private void runeveryFivesecUntillDestoyed() {
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            public void run() {
                //Do Something
                Log.d( TAG, "im running every 5 sec" );
                // do your stuff
                if(common.PRESENTSCREEN.equalsIgnoreCase("notificationChat" )){
                    Log.d( TAG, "im running every 5 sec for notificationchat" );


                    openNotificationChat1( notificationID );

                }else if(common.PRESENTSCREEN.equalsIgnoreCase("serviceChat" )) {
                    Log.d( TAG, "im running every 5 sec for servicechat" );
                    openServiceChat1( serviceId );
                }

                mHandler.postDelayed(this, 5000);
            }
        }, 5000);

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

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void openServiceChat(String NOTIFICATIONID) {

        // ===================== chat using local db ======

        final OrganizationRepository noteRepository = new OrganizationRepository(this);

        noteRepository.getAllServiceHistoryHelper(NOTIFICATIONID, SELECTEDORGID).observe( ChatActivity.this, notes -> {
            serviceChatModelArrayList = new ArrayList<ServiceChatModel>(  );

            for (AllServiceHistoryHelper note : notes) {

                ServiceChatModel chatModel = new ServiceChatModel();

                chatModel.setService_Msg_id( note.getService_Msg_id() );
                chatModel.setService_id( note.getService_id() );
                chatModel.setOrganization_id( note.getOrganization_id() );
                chatModel.setMember_id( note.getMember_id() );
                chatModel.setMessage_form( note.getMessage_form() );
                chatModel.setMessage_to( SELECTEDORGID);
                chatModel.setMessage( note.getMessage());
                chatModel.setAttachment_name( note.getAttachment_name() );
                chatModel.setFile_ext( note.getFile_ext() );
                chatModel.setMime_typ( note.getMime_typ() );
                chatModel.setParent_id( note.getParent_id());
                chatModel.setRead( note.getRead() );
                chatModel.setSyn_status( note.getSyn_status());
                chatModel.setCreated_date( note.getCreated_date() );
                chatModel.setLocal_filepath( note.getLocal_filepath(  ) );
                chatModel.setFile_path( note.getFile_path() );

                serviceChatModelArrayList.add( chatModel);

            }
            runOnUiThread( () -> {

                adapter = new ChatAdapter(this,
                        serviceChatModelArrayList,this);
                adapter.setClickListener(  this );
                recyclerView.scrollToPosition( serviceChatModelArrayList.size() - 1);
                recyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);
                recyclerView.setAdapter(adapter);
            } );
            noteRepository.closeDB();
        });



    }

    private void openServiceChat1(String NOTIFICATIONID) {

        // ===================== chat using local db ======

        final OrganizationRepository noteRepository = new OrganizationRepository(this);

        noteRepository.getAllServiceHistoryHelper(NOTIFICATIONID, SELECTEDORGID).observe( ChatActivity.this, notes -> {
            serviceChatModelArrayList = new ArrayList<ServiceChatModel>(  );

            for (AllServiceHistoryHelper note : notes) {

                ServiceChatModel chatModel = new ServiceChatModel();

                chatModel.setService_Msg_id( note.getService_Msg_id() );
                chatModel.setService_id( note.getService_id() );
                chatModel.setOrganization_id( note.getOrganization_id() );
                chatModel.setMember_id( note.getMember_id() );
                chatModel.setMessage_form( note.getMessage_form() );
                chatModel.setMessage_to( SELECTEDORGID);
                chatModel.setMessage( note.getMessage());
                chatModel.setAttachment_name( note.getAttachment_name() );
                chatModel.setFile_ext( note.getFile_ext() );
                chatModel.setMime_typ( note.getMime_typ() );
                chatModel.setParent_id( note.getParent_id());
                chatModel.setRead( note.getRead() );
                chatModel.setSyn_status( note.getSyn_status());
                chatModel.setCreated_date( note.getCreated_date() );
                chatModel.setLocal_filepath( note.getLocal_filepath(  ) );
                chatModel.setFile_path( note.getFile_path() );

                serviceChatModelArrayList.add( chatModel);

            }

                msglistCount.add( serviceChatModelArrayList.size() );
                int last = msglistCount.get( msglistCount.size() - 1 );
                int lastbutone = msglistCount.get( msglistCount.size() - 2 );
                Log.d( TAG, "openNotificationChat1: " + " " + serviceChatModelArrayList.size() + " " + last + " " + lastbutone );
                if (last != lastbutone) {
                    runOnUiThread( () -> {

                        adapter = new ChatAdapter( this,
                                serviceChatModelArrayList, this );
                        adapter.setClickListener( this );
                        recyclerView.scrollToPosition( serviceChatModelArrayList.size() - 1 );
                        recyclerView.getRecycledViewPool().setMaxRecycledViews( 0, 0 );
                        recyclerView.setAdapter( adapter );
                    } );
                }

            noteRepository.closeDB();
        });



    }

    private void buttonClick() {
        SendMesgButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!InputMesg.getText().toString().isEmpty()) {
                    if(TabName.equalsIgnoreCase( "Notification" )){
                        sendNotificationMessage();
                    }else
                            sendMessage();
                }else {
                    runOnUiThread( () -> {
                        Toast.makeText( ChatActivity.this, "Your message field is empty..", Toast.LENGTH_LONG ).show();
                    } );
                }
            }
        } );

        /*attachment btn click upload image */

        attach_pictures.setOnClickListener( v -> {

            dialog.dismiss();

            if(TabName.equalsIgnoreCase( "Notification" )){
                notification_attachment_btn();
            }else
                service_attachement_btn();

        } );

        /*btn to upload documents*/
        attach_documents.setOnClickListener( v -> {
            dialog.dismiss();

            if(TabName.equalsIgnoreCase( "Notification" )){
                notification_document_attachemnt_btn();
            }else
                service_document_attachement_btn();
        } );



        /*to open bottom sheet modal*/
        bottomsheetbtn.setOnClickListener( v -> {
            dialog.show();
        } );



    }

    private void service_document_attachement_btn() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, SELECTED_DOCUMENT);
    }

    private void notification_document_attachemnt_btn() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, SELECTED_DOCUMENT);
    }


    private void sendMessagewhenImageselected()

        {

            final OrganizationRepository noteRepository = new OrganizationRepository(this);
            String Parentid = "1";
            if (serviceChatModelArrayList.size() > 0) {
                String strid = serviceChatModelArrayList.get( serviceChatModelArrayList.size()-1 ).getParent_id();
                Parentid= String.valueOf(Integer.parseInt( strid )+1);
            }
            String msgid = "1";
            if (serviceChatModelArrayList.size() > 0) {
                String strid = serviceChatModelArrayList.get( serviceChatModelArrayList.size()-1 ).getParent_id();
                msgid= String.valueOf(Integer.parseInt( strid )+1);
            }
            String member_id = common.ID;
            String organization_id = SELECTEDORGID ;
            String Message_form = common.ID;
            String Message_to = SELECTEDORGID;

            String Message = InputMesg.getText().toString();
            String attachment_name="", file_ext = "",mime_typ = "",read="N",syn_status="0";
            String Parent_id = Parentid;
            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
            String dateTime = currentDate+" "+currentTime;
            Log.d( TAG, "onCreateView: "+currentDate+" "+currentTime );
            String created_date = dateTime ;

            AllServiceHistoryHelper allchathistory = new AllServiceHistoryHelper();
            allchathistory.setService_Msg_id(   msgid  );
            allchathistory.setService_id( serviceId );
            allchathistory.setOrganization_id( organization_id);
            allchathistory.setMember_id(member_id );
            allchathistory.setMessage_form(Message_form);
            allchathistory.setMessage_to(Message_to);
            allchathistory.setMessage( "NULL");
            allchathistory.setAttachment_name( selected_attachment_name);
            allchathistory.setFile_ext(file_ext);
            allchathistory.setMime_typ( mime_typ);
            allchathistory.setParent_id( Parent_id);
            allchathistory.setRead( read);
            allchathistory.setSyn_status( syn_status );
            allchathistory.setCreated_date( created_date );
            allchathistory.setLocal_filepath( "/Phapa/Documents/"+selected_attachment_name );


            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    noteRepository.getinstance().daoAccess().insertServiceChat(allchathistory);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    noteRepository.closeDB();
                    openServiceChat( serviceId );
                    InputMesg.getText().clear();
                }
            }.execute();


        }


    private void service_attachement_btn() {
        Intent getIntent = new Intent(Intent.ACTION_VIEW);
        getIntent.setType("image/*");


        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        startActivityForResult(chooserIntent, SELECTED_IMAGE);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data );

        try {
            if (requestCode == SELECTED_IMAGE && resultCode == Activity.RESULT_OK) {
                if (data == null) {
                    //Display an error
                    Toast.makeText( this, "Please select again", Toast.LENGTH_LONG ).show();
                    return;
                }
                final Uri uri = data.getData();
                File imageFile = new File(getRealPathFromURI(uri));
                String imagePath = imageFile.toString();
                Log.d( "source path",uri.toString() );
                File source = new File(imagePath);
                String url =Environment.getExternalStorageDirectory().getPath() + "/" + "Phapa/Documents/";
                File dest = new File(url, source.getName() );


                selected_attachment_name = source.getName();
                copyFile(imageFile,dest);
                if(TabName.equalsIgnoreCase( "Notification" )){
                    sendMessagewhenImageselectedforNotification();
                }else{

                    sendMessagewhenImageselected();
                }

            }
            else if(requestCode == SELECTED_DOCUMENT && resultCode == Activity.RESULT_OK) {
                /*this is when document is selected*/
                if (data == null) {
                    //Display an error
                    Toast.makeText( this, "Please select again", Toast.LENGTH_LONG ).show();
                    return;
                }
                final Uri uri = data.getData();
                String completePath= getPathFromUri(this,uri);
                final String id = DocumentsContract.getDocumentId(uri);
               String  idstr=id.split( ":" )[1];
               String com=Environment.getExternalStorageDirectory()+"/"+idstr;

              // String pathhhhh =cursor.getString(column_index);
                Log.d( TAG, "pathhhhhhhh: "+ completePath );


                File myFile = new File(uri.getPath());
                String Path= myFile.getAbsolutePath();
                File source = new File(Path);
                String filename = myFile.getName();
                String filetype = getMimeType(uri);
                String url =Environment.getExternalStorageDirectory().getPath() + "/" + "Phapa/Documents/";
                File destFile = new File(url, source.getName() );



                /*new method */
                File sourceLocation= new File(completePath);
                selected_attachment_name = sourceLocation.getName();
                File newFile = new File(url, sourceLocation.getName());
                FileChannel outputChannel = null;
                FileChannel inputChannel = null;
                try {
                    outputChannel = new FileOutputStream(newFile).getChannel();
                    inputChannel = new FileInputStream(sourceLocation).getChannel();
                    inputChannel.transferTo(0, inputChannel.size(), outputChannel);
                    inputChannel.close();
                } finally {
                    if (inputChannel != null) inputChannel.close();
                    if (outputChannel != null) outputChannel.close();
                }


                /*new method */

                Log.d( TAG, "filetype=====: " +filetype+"  path ===="+Path);
               if(filetype.equalsIgnoreCase( "text/plain" )||
                       filetype.equalsIgnoreCase( "application/pdf" )||
                       filetype.equalsIgnoreCase( "application/vnd.openxmlformats-officedocument.wordprocessingml.document" )
                       || filetype.equalsIgnoreCase( "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" )||
                       filetype.equalsIgnoreCase( "application/vnd.openxmlformats-officedocument.presentationml.presentation" )||
                       filetype.equalsIgnoreCase( "application/rtf" )
                       ||  filetype.equalsIgnoreCase( "application/vnd.ms-powerpoint" )
                       || filetype.equalsIgnoreCase( "text/rtf" )
               ){

                   if(TabName.equalsIgnoreCase( "Notification" )){
                       sendMessagewhenDocumentselectedforNotification();
                   }else {
                       sendMessagewhenDocumentselected();
                   }


               } else if(
                       filetype.equalsIgnoreCase( "image/jpeg" )||
                       filetype.equalsIgnoreCase( "image/png" )){
                   runOnUiThread( () -> {
                       Toast.makeText( this, "Invalid file format...", Toast.LENGTH_LONG ).show();
                       new AlertDialog.Builder(this)
                               .setTitle("Images")
                               .setMessage("Please upload using Pictures Option...")

                               // Specifying a listener allows you to take an action before dismissing the dialog.
                               // The dialog is automatically dismissed when a dialog button is clicked.
                               .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                   public void onClick(DialogInterface dialog, int which) {
                                       // Continue with delete operation
                                   }
                               })

                               // A null listener allows the button to dismiss the dialog and take no further action.
                               .setNegativeButton(android.R.string.no, null)
                               .setIcon(android.R.drawable.ic_dialog_alert)
                               .show();
                   } );
               } else {
                  runOnUiThread( () -> {
                      Toast.makeText( this, "Invalid file format...", Toast.LENGTH_LONG ).show();
                      new AlertDialog.Builder(this)
                              .setTitle("File Not Supported")
                              .setMessage("upload valid document ...")

                              // Specifying a listener allows you to take an action before dismissing the dialog.
                              // The dialog is automatically dismissed when a dialog button is clicked.
                              .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                  public void onClick(DialogInterface dialog, int which) {
                                      // Continue with delete operation
                                  }
                              })

                              // A null listener allows the button to dismiss the dialog and take no further action.
                              .setNegativeButton(android.R.string.no, null)
                              .setIcon(android.R.drawable.ic_dialog_alert)
                              .show();
                  } );
               }

            }
        } catch (Exception e){
            Log.e( "file path ",e.getMessage() );
            e.printStackTrace();
        }

    }
    public static String getPathFromUri(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                if(id != null) {
                    if (id.startsWith("raw:")) {
                        return id.substring(4);
                    }
                    try {
                        final Uri contentUri = ContentUris.withAppendedId(
                                Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                        return getDataColumn(context, contentUri, null, null);
                    } catch (Exception e) {
                        return null;
                    }
                }

            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
    private void sendMessagewhenDocumentselectedforNotification() {
        final OrganizationRepository noteRepository = new OrganizationRepository(this);
        String Parentid = "1";
        if (notificationMessageList.size() > 0) {
            String strid = notificationMessageList.get( notificationMessageList.size()-1 ).getParent_id();
            Parentid= String.valueOf(Integer.parseInt( strid )+1);
        }

        String member_id = common.ID;
        String organization_id = SELECTEDORGID ;
        String Message_form = common.ID;
        String Message_to = notificationMsgtO;
        String notification_idfk = notificationID ;
        String Message = InputMesg.getText().toString();
        String attachment_name="", file_ext = "",mime_typ = "",read="N",
                syn_status="0";
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
        allchathistory.setMessage( "NULL");
        allchathistory.setAttachment_name( selected_attachment_name);
        allchathistory.setFile_ext(file_ext);
        allchathistory.setMime_typ( mime_typ);
        allchathistory.setParent_id( Parent_id);
        allchathistory.setRead( read);
        allchathistory.setSyn_status( syn_status );
        allchathistory.setCreated_date( created_date );
        allchathistory.setLocal_filepath( "/Phapa/Documents/"+selected_attachment_name );

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
    }

    private void sendMessagewhenImageselectedforNotification() {

        final OrganizationRepository noteRepository = new OrganizationRepository(this);
        String Parentid = "1";
        if (notificationMessageList.size() > 0) {
            String strid = notificationMessageList.get( notificationMessageList.size()-1 ).getParent_id();
            Parentid= String.valueOf(Integer.parseInt( strid )+1);
        }

        String member_id = common.ID;
        String organization_id = SELECTEDORGID ;
        String Message_form = common.ID;
        String Message_to = notificationMsgtO;
        String notification_idfk = notificationID ;
        String Message = InputMesg.getText().toString();
        String attachment_name="", file_ext = "",mime_typ = "",read="N",
                syn_status="0";
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
        allchathistory.setMessage( "NULL");
        allchathistory.setAttachment_name( selected_attachment_name);
        allchathistory.setFile_ext(file_ext);
        allchathistory.setMime_typ( mime_typ);
        allchathistory.setParent_id( Parent_id);
        allchathistory.setRead( read);
        allchathistory.setSyn_status( syn_status );
        allchathistory.setCreated_date( created_date );
        allchathistory.setLocal_filepath( "/Phapa/Documents/"+selected_attachment_name );

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

    }

    public  String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }
    public String getMimeType(Uri uri) {
        String mimeType = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = this.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }
    private void sendMessagewhenDocumentselected() {


        final OrganizationRepository noteRepository = new OrganizationRepository(this);
        String Parentid = "1";
        if (serviceChatModelArrayList.size() > 0) {
            String strid = serviceChatModelArrayList.get( serviceChatModelArrayList.size()-1 ).getParent_id();
            Parentid= String.valueOf(Integer.parseInt( strid )+1);
        }
        String msgid = "1";
        if (serviceChatModelArrayList.size() > 0) {
            String strid = serviceChatModelArrayList.get( serviceChatModelArrayList.size()-1 ).getParent_id();
            msgid= String.valueOf(Integer.parseInt( strid )+1);
        }
        String member_id = common.ID;
        String organization_id = SELECTEDORGID ;
        String Message_form = common.ID;
        String Message_to = SELECTEDORGID;

        String Message = InputMesg.getText().toString();
        String attachment_name="", file_ext = "",mime_typ = "",read="N",syn_status="0";
        String Parent_id = Parentid;
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String dateTime = currentDate+" "+currentTime;
        Log.d( TAG, "onCreateView: "+currentDate+" "+currentTime );
        String created_date = dateTime ;

        AllServiceHistoryHelper allchathistory = new AllServiceHistoryHelper();
        allchathistory.setService_Msg_id(   msgid  );
        allchathistory.setService_id( serviceId );
        allchathistory.setOrganization_id( organization_id);
        allchathistory.setMember_id(member_id );
        allchathistory.setMessage_form(Message_form);
        allchathistory.setMessage_to(Message_to);
        allchathistory.setMessage( "NULL");
        allchathistory.setAttachment_name( selected_attachment_name);
        allchathistory.setFile_ext(file_ext);
        allchathistory.setMime_typ( mime_typ);
        allchathistory.setParent_id( Parent_id);
        allchathistory.setRead( read);
        allchathistory.setSyn_status( syn_status );
        allchathistory.setCreated_date( created_date );
        allchathistory.setLocal_filepath( "/Phapa/Documents/"+selected_attachment_name );


        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                noteRepository.getinstance().daoAccess().insertServiceChat(allchathistory);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                noteRepository.closeDB();
                openServiceChat( serviceId );
                InputMesg.getText().clear();
            }
        }.execute();
    }

    private String getRealPathFromURI(Uri contentUri) {

//        String[] proj = {MediaStore.Video.Media.DATA};
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    private void notification_attachment_btn() {
        // nothing is there for now pls write code here
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        startActivityForResult(chooserIntent, SELECTED_IMAGE);

    }
    private void moveFile(String inputPath, String inputFile, String outputPath) {

        InputStream in = null;
        OutputStream out = null;
        try {

            //create output directory if it doesn't exist
            File dir = new File (outputPath);
            if (!dir.exists())
            {
                dir.mkdirs();
            }


            in = new FileInputStream(inputPath + inputFile);
            out = new FileOutputStream(outputPath + inputFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file
            out.flush();
            out.close();
            out = null;

            // delete the original file
            new File(inputPath + inputFile).delete();


        }

        catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        }
        catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

    }
    public static void copyDocumentFile(File sourceFile, File destFile) throws IOException {


        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();

        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        }catch (Exception e){
            Log.e( "file copy",e.getMessage() );
        }

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


    private void sendMessage(){

        final OrganizationRepository noteRepository = new OrganizationRepository(this);
        String Parentid = "1";
        if (serviceChatModelArrayList.size() > 0) {
            String strid = serviceChatModelArrayList.get( serviceChatModelArrayList.size()-1 ).getParent_id();
            Parentid= String.valueOf(Integer.parseInt( strid )+1);
        }
        String msgid = "1";
        if (serviceChatModelArrayList.size() > 0) {
            String strid = serviceChatModelArrayList.get( serviceChatModelArrayList.size()-1 ).getParent_id();
            msgid= String.valueOf(Integer.parseInt( strid )+1);
        }
        String member_id = common.ID;
        String organization_id = SELECTEDORGID ;
        String Message_form = common.ID;
        String Message_to = SELECTEDORGID;

        String Message = InputMesg.getText().toString();
        String attachment_name="", file_ext = "",mime_typ = "",read="N",syn_status="0";
        String Parent_id = Parentid;
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String dateTime = currentDate+" "+currentTime;
        Log.d( TAG, "onCreateView: "+currentDate+" "+currentTime );
        String created_date = dateTime ;

        AllServiceHistoryHelper allchathistory = new AllServiceHistoryHelper();
        allchathistory.setService_Msg_id(   msgid  );
        allchathistory.setService_id( serviceId );
        allchathistory.setOrganization_id( organization_id);
        allchathistory.setMember_id(member_id );
        allchathistory.setMessage_form(Message_form);
        allchathistory.setMessage_to(Message_to);
        allchathistory.setMessage( Message);
        allchathistory.setAttachment_name( attachment_name);
        allchathistory.setFile_ext(file_ext);
        allchathistory.setMime_typ( mime_typ);
        allchathistory.setParent_id( Parent_id);
        allchathistory.setRead( read);
        allchathistory.setSyn_status( syn_status );
        allchathistory.setCreated_date( created_date );


        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                noteRepository.getinstance().daoAccess().insertServiceChat(allchathistory);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                noteRepository.closeDB();
                openServiceChat( serviceId );
                InputMesg.getText().clear();
            }
        }.execute();


    }


    @Override
    public void onItemClick(View view, int position) {

    }
}
