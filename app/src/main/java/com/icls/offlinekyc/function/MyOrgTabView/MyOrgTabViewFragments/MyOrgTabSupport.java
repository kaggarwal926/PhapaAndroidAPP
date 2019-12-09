package com.icls.offlinekyc.function.MyOrgTabView.MyOrgTabViewFragments;

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
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.icls.offlinekyc.R;
import com.icls.offlinekyc.adapters.SupportChartAdapter;
import com.icls.offlinekyc.commonshare.common;
import com.icls.offlinekyc.helper.SupportConversationHistory;
import com.icls.offlinekyc.roomdb.OrganizationRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.icls.offlinekyc.function.MyOrgTabView.ChatActivity.getDataColumn;
import static com.icls.offlinekyc.function.MyOrgTabView.ChatActivity.isDownloadsDocument;
import static com.icls.offlinekyc.function.MyOrgTabView.ChatActivity.isExternalStorageDocument;
import static com.icls.offlinekyc.function.MyOrgTabView.ChatActivity.isGooglePhotosUri;
import static com.icls.offlinekyc.function.MyOrgTabView.ChatActivity.isMediaDocument;


public class MyOrgTabSupport extends Fragment implements SupportChartAdapter.ItemClickListener {
    private static final String TAG = "MyOrgTabSupport";
    BottomSheetDialog dialog;
    RecyclerView recyclerView;
    EditText InputMesg ;
    private List<Integer> msglistCount;
    private Handler mHandler;
    ImageButton bottomsheetbtn;
    Button SendMesgButton;
    private static final int SELECTED_IMAGE = 0;
    private static final int SELECTED_DOCUMENT = 1;
    ArrayList<SupportConversationHistory> supportMessageList;
    String notificationID;
    String replyAllowed;
    private String selected_attachment_name;
    String notificationOrgId,notificationMsgtO;
    String organizationId;
    LinearLayout sendmsglayout;
    TextView attach_pictures,attach_documents;
    View view;
    private SupportChartAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate( R.layout.fragment_my_org_tab_support, container, false );
        common.PRESENTSCREEN = "supportChat";
        init();

        organizationId = common.SELECTEDORGID;
        if (organizationId != null) {
            getSupportConversationHistory( organizationId );
        }
        if(common.PRESENTSCREEN.equalsIgnoreCase(  "supportChat")){
            runeveryFivesecUntillDestoyed1();
        }
        buttonClick();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );

        try {
            if (requestCode == SELECTED_IMAGE && resultCode == Activity.RESULT_OK) {
                if (data == null) {
                    //Display an error
                    Toast.makeText( getContext(), "Please select again", Toast.LENGTH_LONG ).show();
                    return;
                }
                final Uri uri = data.getData();
                File imageFile = new File(getRealPathFromURI(uri));
                String imagePath = imageFile.toString();
                File source = new File(imagePath);
                String url = Environment.getExternalStorageDirectory().getPath() + "/" + "Phapa/Documents/";
                File dest = new File(url, source.getName() );


                selected_attachment_name = source.getName();
                copyFile(imageFile,dest);

                sendimage();


            }
            else if(requestCode == SELECTED_DOCUMENT && resultCode == Activity.RESULT_OK) {
                /*this is when document is selected*/
                if (data == null) {
                    //Display an error
                    Toast.makeText( getContext(), "Please select again", Toast.LENGTH_LONG ).show();
                    return;
                }
                final Uri uri = data.getData();
                String completePath= getPathFromUri(getContext(),uri);
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

                    // send attachment
                    sendDocument();


                } else if(
                        filetype.equalsIgnoreCase( "image/jpeg" )||
                                filetype.equalsIgnoreCase( "image/png" )){
                    getActivity().runOnUiThread( () -> {
                        Toast.makeText( getContext(), "Invalid file format...", Toast.LENGTH_LONG ).show();
                        new AlertDialog.Builder(getContext())
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
                    getActivity().runOnUiThread( () -> {
                        Toast.makeText( getContext(), "Invalid file format...", Toast.LENGTH_LONG ).show();
                        new AlertDialog.Builder(getContext())
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
    private void runeveryFivesecUntillDestoyed1() {
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            public void run() {
                //Do Something
                Log.d( TAG, "im running every 5 sec in support" );
                // do your stuff
                if(common.PRESENTSCREEN.equalsIgnoreCase("supportChat" )){
                    Log.d( TAG, "im running every 5 sec for supportchat" );
                    if (organizationId != null) {
                        getSupportConversationHistory1( organizationId );
                    }
                }

                mHandler.postDelayed(this, 5000);
            }
        }, 5000);

    }

    @Override
    public void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        common.PRESENTSCREEN = "";
        msglistCount.clear();
        /*
        Log.d( TAG, "im stopping to run every 5 sec " );*/

        super.onDestroy();
    }

    private void sendDocument() {
        final OrganizationRepository noteRepository = new OrganizationRepository(getContext());
        String Parentid = "1";
        if (supportMessageList.size() > 0) {
            String strid = supportMessageList.get( supportMessageList.size()-1 ).getParent_id();
            //Parentid= String.valueOf(Integer.parseInt( strid )+1);
        }

        String organization_id = common.SELECTEDORGID;
        String member_id = common.ID;
        String Message_form = common.ID;
        String Message_to = common.SELECTEDORGID;
        String Message = InputMesg.getText().toString();
        String attachment_name = null;
        String file_ext = null;
        String file_path = null;
        String mime_typ = null;
        String Parent_id = "1";
        String read = "N";
        String syn_status = common.NEWRECORD;
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String dateTime = currentDate+" "+currentTime;
        String created_date = dateTime;

        SupportConversationHistory supportConvHistory =new SupportConversationHistory();
        supportConvHistory.setOrganization_id(organization_id);
        supportConvHistory.setMember_id(member_id);
        supportConvHistory.setMessage_form(Message_form);
        supportConvHistory.setMessage_to(Message_to);
        supportConvHistory.setMessage("NULL");
        supportConvHistory.setAttachment_name(selected_attachment_name);
        supportConvHistory.setFile_ext(file_ext);
        supportConvHistory.setFile_path(file_path);
        supportConvHistory.setMime_typ(mime_typ);
        supportConvHistory.setParent_id(Parent_id);
        supportConvHistory.setRead(read);
        supportConvHistory.setSyn_status(syn_status);
        supportConvHistory.setCreated_date(created_date);
        supportConvHistory.setLocal_filepath( "/Phapa/Documents/"+selected_attachment_name );

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                noteRepository.getinstance().daoAccess().insertSupportConversationHistory(supportConvHistory);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                noteRepository.closeDB();
                getSupportConversationHistory(organization_id);
                InputMesg.getText().clear();
            }
        }.execute();
    }

    private void sendimage() {
        final OrganizationRepository noteRepository = new OrganizationRepository(getContext());
        String Parentid = "1";
        if (supportMessageList.size() > 0) {
            String strid = supportMessageList.get( supportMessageList.size()-1 ).getParent_id();
            //Parentid= String.valueOf(Integer.parseInt( strid )+1);
        }

        String organization_id = common.SELECTEDORGID;
        String member_id = common.ID;
        String Message_form = common.ID;
        String Message_to = common.SELECTEDORGID;
        String file_ext = null;
        String file_path = null;
        String mime_typ = null;
        String Parent_id = "1";
        String read = "N";
        String syn_status = common.NEWRECORD;
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String dateTime = currentDate+" "+currentTime;
        String created_date = dateTime;

        SupportConversationHistory supportConvHistory =new SupportConversationHistory();
        supportConvHistory.setOrganization_id(organization_id);
        supportConvHistory.setMember_id(member_id);
        supportConvHistory.setMessage_form(Message_form);
        supportConvHistory.setMessage_to(Message_to);
        supportConvHistory.setMessage("NULL");
        supportConvHistory.setAttachment_name(selected_attachment_name);
        supportConvHistory.setFile_ext(file_ext);
        supportConvHistory.setFile_path(file_path);
        supportConvHistory.setMime_typ(mime_typ);
        supportConvHistory.setParent_id(Parent_id);
        supportConvHistory.setRead(read);
        supportConvHistory.setSyn_status(syn_status);
        supportConvHistory.setCreated_date(created_date);
        supportConvHistory.setLocal_filepath( "/Phapa/Documents/"+selected_attachment_name );

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                noteRepository.getinstance().daoAccess().insertSupportConversationHistory(supportConvHistory);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                noteRepository.closeDB();
                getSupportConversationHistory(organization_id);
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
        if (uri.getScheme().equals( ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = getActivity().getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
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


    private String getRealPathFromURI(Uri contentUri) {

//        String[] proj = {MediaStore.Video.Media.DATA};
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public void init () {
        msglistCount = new ArrayList<>(  );
        msglistCount.add( 0 );
        msglistCount.add( 0 );
        dialog = new BottomSheetDialog(getContext());
        dialog.setContentView(R.layout.bottom_sheet);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        attach_pictures = dialog.findViewById( R.id.attach_pictures );
        attach_documents= dialog.findViewById( R.id.attach_documents);
        InputMesg = view.findViewById( R.id.input_message );
        SendMesgButton = view.findViewById( R.id.sendbtn );
        bottomsheetbtn = view.findViewById( R.id.bottomsheetbtn );
        recyclerView = (RecyclerView)view.findViewById(R.id.notification_chat_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        sendmsglayout = view.findViewById( R.id.sendmsglayout );
    }

    public void getSupportConversationHistory(String organizationId ) {
        final OrganizationRepository noteRepository = new OrganizationRepository(getContext());

        noteRepository.getSupportConversationHistoryHelper(organizationId).observe( MyOrgTabSupport.this, history -> {
            supportMessageList = new ArrayList<SupportConversationHistory>(  );

            for (SupportConversationHistory orgHistory : history) {
                SupportConversationHistory supportConvHistory = new SupportConversationHistory();
                supportConvHistory.setOrganization_id(orgHistory.getOrganization_id());
                supportConvHistory.setSupport_id(orgHistory.getSupport_id());
                supportConvHistory.setMember_id(orgHistory.getMember_id());
                supportConvHistory.setMessage_form(orgHistory.getMessage_form());
                supportConvHistory.setMessage_to(orgHistory.getMessage_to());
                supportConvHistory.setMessage(orgHistory.getMessage());
                supportConvHistory.setAttachment_name(orgHistory.getAttachment_name());
                supportConvHistory.setFile_ext(orgHistory.getFile_ext());
                supportConvHistory.setFile_path(orgHistory.getFile_path());
                supportConvHistory.setMime_typ(orgHistory.getMime_typ());
                supportConvHistory.setCreated_date(orgHistory.getCreated_date());
                supportConvHistory.setLocal_filepath( orgHistory.getLocal_filepath() );
                supportMessageList.add(supportConvHistory);
            }
            getActivity().runOnUiThread( () -> {

                adapter = new SupportChartAdapter(getContext(),
                        supportMessageList,getActivity());
                adapter.setClickListener(MyOrgTabSupport.this);
                recyclerView.scrollToPosition(supportMessageList.size() - 1);
                recyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);
                recyclerView.setAdapter(adapter);
            } );
            noteRepository.closeDB();
        });
    }
    public void getSupportConversationHistory1(String organizationId ) {
        final OrganizationRepository noteRepository = new OrganizationRepository(getContext());

        noteRepository.getSupportConversationHistoryHelper(organizationId).observe( MyOrgTabSupport.this, history -> {
            supportMessageList = new ArrayList<SupportConversationHistory>(  );

            for (SupportConversationHistory orgHistory : history) {
                SupportConversationHistory supportConvHistory = new SupportConversationHistory();
                supportConvHistory.setOrganization_id(orgHistory.getOrganization_id());
                supportConvHistory.setSupport_id(orgHistory.getSupport_id());
                supportConvHistory.setMember_id(orgHistory.getMember_id());
                supportConvHistory.setMessage_form(orgHistory.getMessage_form());
                supportConvHistory.setMessage_to(orgHistory.getMessage_to());
                supportConvHistory.setMessage(orgHistory.getMessage());
                supportConvHistory.setAttachment_name(orgHistory.getAttachment_name());
                supportConvHistory.setFile_ext(orgHistory.getFile_ext());
                supportConvHistory.setFile_path(orgHistory.getFile_path());
                supportConvHistory.setMime_typ(orgHistory.getMime_typ());
                supportConvHistory.setCreated_date(orgHistory.getCreated_date());
                supportConvHistory.setLocal_filepath( orgHistory.getLocal_filepath() );
                supportMessageList.add(supportConvHistory);
            }


            msglistCount.add( supportMessageList.size() ) ;
            int last =msglistCount.get( msglistCount.size()-1) ;
            int lastbutone =  msglistCount.get(  msglistCount.size()-2);
            Log.d( TAG, "openNotificationChat1: "+" "+supportMessageList.size()+" "+last+" " +lastbutone );
            if (last!=lastbutone) {
                getActivity().runOnUiThread( () -> {

                    adapter = new SupportChartAdapter(getContext(),
                            supportMessageList,getActivity());
                    adapter.setClickListener(MyOrgTabSupport.this);
                    recyclerView.scrollToPosition(supportMessageList.size() - 1);
                    recyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);
                    recyclerView.setAdapter(adapter);
                } );
            }

            noteRepository.closeDB();
        });
    }
    @Override
    public void onItemClick(View view, int position) {

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

        /*attachment btn click upload image */

        attach_pictures.setOnClickListener( v -> {

            dialog.dismiss();

            Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
            getIntent.setType("image/*");

            Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickIntent.setType("image/*");

            Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

            startActivityForResult(chooserIntent, SELECTED_IMAGE);


        } );

        /*btn to upload documents*/
        attach_documents.setOnClickListener( v -> {
            dialog.dismiss();

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            startActivityForResult(intent, SELECTED_DOCUMENT);

        } );



        /*to open bottom sheet modal*/
        bottomsheetbtn.setOnClickListener( v -> {
            dialog.show();
        } );

    }

    private void sendMessage(){

        final OrganizationRepository noteRepository = new OrganizationRepository(getContext());
        String Parentid = "1";
        if (supportMessageList.size() > 0) {
            String strid = supportMessageList.get( supportMessageList.size()-1 ).getParent_id();
            //Parentid= String.valueOf(Integer.parseInt( strid )+1);
        }

        String organization_id = common.SELECTEDORGID;
        String member_id = common.ID;
        String Message_form = common.ID;
        String Message_to = common.SELECTEDORGID;
        String Message = InputMesg.getText().toString();;
        String attachment_name = null;
        String file_ext = null;
        String file_path = null;
        String mime_typ = null;
        String Parent_id = "1";
        String read = "N";
        String syn_status = common.NEWRECORD;
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String dateTime = currentDate+" "+currentTime;
        String created_date = dateTime;

        SupportConversationHistory supportConvHistory =new SupportConversationHistory();
        supportConvHistory.setOrganization_id(organization_id);
        supportConvHistory.setMember_id(member_id);
        supportConvHistory.setMessage_form(Message_form);
        supportConvHistory.setMessage_to(Message_to);
        supportConvHistory.setMessage(Message);
        supportConvHistory.setAttachment_name(attachment_name);
        supportConvHistory.setFile_ext(file_ext);
        supportConvHistory.setFile_path(file_path);
        supportConvHistory.setMime_typ(mime_typ);
        supportConvHistory.setParent_id(Parent_id);
        supportConvHistory.setRead(read);
        supportConvHistory.setSyn_status(syn_status);
        supportConvHistory.setCreated_date(created_date);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                noteRepository.getinstance().daoAccess().insertSupportConversationHistory(supportConvHistory);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                noteRepository.closeDB();
                getSupportConversationHistory(organization_id);
                InputMesg.getText().clear();
            }
        }.execute();
    }

}
