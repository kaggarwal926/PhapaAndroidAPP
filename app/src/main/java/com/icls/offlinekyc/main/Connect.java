package com.icls.offlinekyc.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Connect extends AsyncTask<String, Void, String> {
    private static final String TAG = "";
    String err;
    public static String XML;
    String printresponseXML;
    Context context;
    public String sendToUIDAIServer(String url, String data) throws Exception {
        String responseXML = null;
        StringBuilder chaine = new StringBuilder("");
        URL url1 = new URL(url);

        HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
        try {
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Cache-Control", "no-cache");
            connection.setRequestProperty("Content-Type", "application/xml;charset=UTF-8");

            try {
                OutputStream os = connection.getOutputStream(); //exception throws here!
                DataOutputStream request = new DataOutputStream(os);
                request.writeBytes(data);
                request.flush();
                request.close();
                connection.connect();
                int respStatus = connection.getResponseCode();
                // Toast.makeText(this,"Entering Meta Data" + String.valueOf(respStatus), Toast.LENGTH_LONG).show();
                Log.d(TAG, "iClimb 11111 " + String.valueOf(respStatus));
                InputStream inputStream = connection.getInputStream();
                Log.d(TAG, "iClimb" + inputStream);
                BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                while ((line = rd.readLine()) != null) {
                    chaine.append(line);
                }
                responseXML = chaine.toString();
                printresponseXML = responseXML;
                XML=responseXML;
                inputStream.close();
                connection.disconnect();

            } catch (Exception e)
            {
                Log.d(TAG, "Pravin " + e.getMessage());
            }
            Log.d(TAG, "responseXML =" + responseXML);
            return responseXML;
        } catch (Exception e) {
            err = String.valueOf(e);
            throw new Exception("Exception in sending request to server -" + e.getMessage());
        }finally {
            if (connection != null) connection.disconnect();
        }

    }


    //Response class return data to main activity
    public String response()
    {
        //  XML=responseXML;
        return XML;
    }


    @Override
    protected String doInBackground(String... params) {
        try{
            String sendurl = params[0];
            String authXML = params[1];
            String respXmL = sendToUIDAIServer(sendurl,authXML);
            return respXmL;
        }
        catch(Exception e)
        {
            Log.d(TAG,e.getMessage());
        }
        return null;
    }
    @Override
    protected void onPostExecute(String responseXML) {
        //process message
         showMessageDialogue("Response XML" + printresponseXML);

        MainActivity mainActivity=new MainActivity();
      //  mainActivity.showConnect(responseXML);
    }
    public void showMessageDialogue(String messageTxt) {
        // MainActivity.this.runOnUiThread(new Runnable() {
        // @Override
        //  public void run() {
        new AlertDialog.Builder(new MainActivity().getApplicationContext())
                .setCancelable(false)
                .setTitle("Message")
                .setMessage(messageTxt)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }
}
