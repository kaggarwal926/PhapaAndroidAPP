package com.icls.offlinekyc.function;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.OpenableColumns;

import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.util.IOUtils;
import com.icls.offlinekyc.R;
import com.icls.offlinekyc.commonshare.common;
import com.icls.offlinekyc.login.LoginInfo;
import com.icls.offlinekyc.roomdb.OrganizationRepository;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

@SuppressLint("ValidFragment")
public class OffLineAaadhar extends Fragment  {


    static TextView textCartItemCount,tvXML;
    public static int mCartItemCount = 10;
    RelativeLayout rlDownload,rlShareDoc;
    String path="";
    private boolean bound = false;
    View view=getView();
    String base64=null;
    public Button btnDownloadXML,btnSubmit;
    public EditText etMobile,etAadharDigit,etSharedCode,etFilePath;
    public String phoneNumber;
    CheckBox checkBox;


    public OffLineAaadhar() {

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // TODO Auto-generated method stub

        switch (requestCode) {
            case 7:
                if (resultCode == RESULT_OK) {
                    Uri uri = intent.getData();
                    String displayName = null;
                    String fileName="";
                    if (uri != null)
                    {
                        path = uri.toString();
                        File myFile = new File(uri.getPath());
                        myFile.getAbsolutePath();

                        if (path.startsWith("content://")) {
                            Cursor cursor = null;
                            try {
                                cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
                                if (cursor != null && cursor.moveToFirst()) {
                                    fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                                }
                                base64= encodeFileToBase64Binary(uri);

                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                cursor.close();
                            }
                        } else if (path.startsWith("file://")) {
                            fileName = myFile.getName();
                            File file = new File(path);
                            int size = (int) file.length();
                            byte[] bytes = new byte[size];
                            try {
                                BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
                                buf.read(bytes, 0, bytes.length);
                                buf.close();
                                base64 = Base64.encodeToString( bytes, Base64.DEFAULT );
                            } catch (FileNotFoundException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        }

                    }

                        etFilePath.setText(fileName);
                }
                break;
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle( "KYC Aadhaar" );
        view=inflater.inflate(R.layout.fragment_off_line_aadhar, container, false);
        btnDownloadXML=view.findViewById(R.id.btnDownLoad);
        btnSubmit=view.findViewById(R.id.btnsudmit);
        etMobile=view.findViewById(R.id.mobile_no);
        etAadharDigit=view.findViewById(R.id.aadhaar_last_digit);
        etSharedCode=view.findViewById(R.id.et_shared_code);
        SharedPreferences shared = getActivity().getSharedPreferences("PASSCODEDB", MODE_PRIVATE);
        int passcode = shared.getInt("passcode", 0);
        etSharedCode.setText(""+passcode);
        etFilePath=view.findViewById(R.id.et_file_path);
        checkBox=view.findViewById(R.id.check_box);



        SharedPreferences phoneShare = getActivity().getSharedPreferences("LoginDetails", MODE_PRIVATE);
        phoneNumber = (phoneShare.getString("PhoneNo", ""));
        etMobile.setText(phoneNumber);
        rlDownload=view.findViewById(R.id.rl_download);
        rlShareDoc=view.findViewById(R.id.rl_shareDoc);
        tvXML=view.findViewById(R.id.tvXML);


        tvXML.setOnClickListener( view -> {
            rlDownload.setVisibility(View.GONE);
            rlShareDoc.setVisibility(View.VISIBLE);
        } );
        etFilePath.setOnClickListener( v -> {

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            startActivityForResult(intent, 7);
        } );
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            btnSubmit.setAlpha(1);
                             }
                    });


                    // do something, the isChecked will be
                    // true if the switch is in the On position
                }else{

                    btnSubmit.setAlpha((float) 0.6);

                }
            }
        });
        btnSubmit.setOnClickListener( view -> {

           if(etMobile.getText().toString().isEmpty()){
                Toast.makeText(getContext(), "Please Enter your mobile number Registered with aadhaar", Toast.LENGTH_LONG ).show();
                etMobile.setCursorVisible(true);
            }else if(etAadharDigit.getText().toString().isEmpty()){
                Toast.makeText(getContext(), "Please Enter Last digit of your aadhaar Number", Toast.LENGTH_LONG ).show();
                etAadharDigit.setCursorVisible(true);
            }else if(etSharedCode.getText().toString().isEmpty()){
                Toast.makeText(getContext(), "Please Enter your Share code", Toast.LENGTH_LONG ).show();
                etSharedCode.setCursorVisible(true);
            }else if(etFilePath.getText().toString().isEmpty()){
                Toast.makeText(getContext(), "Please select your aadhaar .zip file ", Toast.LENGTH_LONG ).show();
            }else if (!checkBox.isChecked()){
               Toast.makeText(getContext(), "Please select checkBox ", Toast.LENGTH_LONG ).show();

           }
        } );


        // Inflate the layout for this fragment
        return view;


    }


    private String encodeFileToBase64Binary(Uri uri)
            throws IOException {


        byte[] bytes = loadFile(uri);
        String encoded = Base64.encodeToString(bytes,Base64.DEFAULT );

        return encoded;
    }

    public void sameImage(Bitmap decodedImage, String fname) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/Phapa");
        myDir.mkdirs();



        File file = new File(myDir, fname);


        if (file.exists ())
            file.delete ();

        if (file.exists()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);

            decodedImage.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private byte[] loadFile(Uri uri) throws IOException {
        InputStream is = getContext().getContentResolver().openInputStream(uri);
        byte[] bytes = IOUtils.readInputStreamFully(is, false);

        is.close();
        return bytes;
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



}
