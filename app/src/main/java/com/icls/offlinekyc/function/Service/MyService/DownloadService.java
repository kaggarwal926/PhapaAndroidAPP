package com.icls.offlinekyc.function.Service.MyService;

import android.app.NotificationManager;
import android.app.Service;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.StatFs;
import android.support.annotation.NonNull;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.icls.offlinekyc.BuildConfig;
import com.icls.offlinekyc.Database.OrganizationTypeParam;
import com.icls.offlinekyc.R;
import com.icls.offlinekyc.commonshare.common;
import com.icls.offlinekyc.helper.AllNotificationHistoryHelper;
import com.icls.offlinekyc.helper.AllServiceHistoryHelper;
import com.icls.offlinekyc.helper.SupportConversationHistory;
import com.icls.offlinekyc.login.LoginInfo;
import com.icls.offlinekyc.roomdb.OrganizationRepository;
import com.icls.offlinekyc.roomdb.UserProfile;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.icls.offlinekyc.commonshare.common.joinRequestSend;
import static com.icls.offlinekyc.login.LoginInfo.logoPaths1;

public class DownloadService extends Service {

    SharedPreferences preferences;

    private static final String DOCUMENT_VIEW_STATE_PREFERENCES = "DjvuDocumentViewState";

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    private NotificationManager mNM;
    String downloadUrl;
    public static boolean serviceState = false;
    private List<String> logospath = new ArrayList<>(  );

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super( looper );
        }

        @Override
        public void handleMessage(Message msg) {



            stopSelf( msg.arg1 );
        }
    }

    @Override
    public void onCreate() {

        Log.d( "downloadservice started", "onCreate: " );

        logospath = new ArrayList<>(  );
        Log.d( "download servie", "Download service started"+ logoPaths1 );
        //Toast.makeText( DownloadService.this, "service started", Toast.LENGTH_LONG ).show();
        logospath = logoPaths1;
        serviceState = true;
        mNM = (NotificationManager) getSystemService( NOTIFICATION_SERVICE );
        HandlerThread thread = new HandlerThread( "ServiceStartArguments", 1 );
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler( mServiceLooper );
        for (int i=0; i<logospath.size(); i++) {


            downloadFile(  logospath.get( i ));
            if(i == (logospath.size()-1)){
                this.stopSelf();
                Log.d( "downloadservice stopped", "onCreate: " );
            }

        }


    }




    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d( "SERVICE-ONCOMMAND", "onStartCommand" );



        // If we get killed, after returning from here, restart
        return START_STICKY;
    }


    @Override
    public void onDestroy() {

        Log.d( "SERVICE-DESTROY", "DESTORY" );
        serviceState = false;
        //Toast.makeText(this, "service done", Toast.LENGTH_LONG).show();
    }


    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }


    public void downloadFile(String logo) {
        String filename=logo.substring(logo.lastIndexOf("/")+1);
        Log.d( "logourl", "downloadFile: "+common.PHAPHAFILEURL +logo );
        Picasso.get().load(common.PHAPHAFILEURL +logo ).into(getTarget(filename));

    }
    private Bitmap decodeFile(File f) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // The new size we want to scale to
            final int REQUIRED_SIZE=70;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
    }

    private Target getTarget(String profilePicName) {



        Target target = new Target() {

            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + "Phapa/" + profilePicName);
                        if (file.exists())
                            file.delete();
                        try {
                            file.createNewFile();
                            FileOutputStream ostream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream);

                            ostream.flush();
                            ostream.close();



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
    private void SaveImage(Bitmap finalBitmap, String filename) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/Phapa");
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
    public void downloadFile(String fileURL, String fileName) {

        StatFs stat_fs = new StatFs( Environment.getExternalStorageDirectory().getPath() );
        double avail_sd_space = (double) stat_fs.getAvailableBlocksLong() * (double) stat_fs.getBlockSizeLong();
        //double GB_Available = (avail_sd_space / 1073741824);
        double MB_Available = (avail_sd_space / 10485783);
        //System.out.println("Available MB : " + MB_Available);
        Log.d( "MB", "" + MB_Available );
        try {
            File root = new File( Environment.getExternalStorageDirectory() + "/vvveksperten" );
            if (root.exists() && root.isDirectory()) {

            } else {
                root.mkdir();
            }
            Log.d( "CURRENT PATH", root.getPath() );
            URL u = new URL( fileURL );
            HttpURLConnection c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod( "GET" );
            c.setDoOutput( true );
            c.connect();
            int fileSize = c.getContentLength() / 1048576;
            Log.d( "FILESIZE", "" + fileSize );
            if (MB_Available <= fileSize) {
                c.disconnect();
                return;
            }

            FileOutputStream f = new FileOutputStream( new File( root.getPath(), fileName ) );

            InputStream in = c.getInputStream();


            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = in.read( buffer )) > 0) {
                f.write( buffer, 0, len1 );
            }
            f.close();
            File file = new File( root.getAbsolutePath() + "/" + "some.pdf" );
            if (file.exists()) {
                file.delete();
                Log.d( "FILE-DELETE", "YES" );
            } else {
                Log.d( "FILE-DELETE", "NO" );
            }
            File from = new File( root.getAbsolutePath() + "/" + fileName );
            File to = new File( root.getAbsolutePath() + "/" + "some.pdf" );


        } catch (Exception e) {
            Log.d( "Downloader", e.getMessage() );

        }
    }
}