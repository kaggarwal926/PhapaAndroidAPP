package com.icls.offlinekyc.function;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ActionMode;
import android.view.View;
import android.widget.Toast;

import com.icls.offlinekyc.R;
import com.icls.offlinekyc.adapters.AdditionalDocumentAdapter;
import com.icls.offlinekyc.commonshare.common;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class viewDocumentRecyclerView extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static final String TAG = "viewDocumentRecyclerVie";
    private ArrayList<String > docList =  new ArrayList<>(  );
    private ArrayList<String> DocumentList = new ArrayList<>();
    Context context;
    Activity activity;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        context = getApplicationContext();
        setContentView( R.layout.addtional_doc_recyceler_view );
        docList = getIntent().getStringArrayListExtra("docList");
        recyclerView= findViewById( R.id.documentRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        getAdditionalProfile();


    }
    private void getAdditionalProfile() {

        final SharedPreferences prefs = getSharedPreferences("PASSCODEDB", MODE_PRIVATE);
        String user = prefs.getString("user", "9999");// "9999" is the default value.

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String extendedUrl = "myeid/GetQuickRegistration/";

        // put your json here
        final Request request = new Request.Builder()
                .url( common.PHAPAURL + extendedUrl + user)
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

                final String myResponse = response.body().string();

                try {
                    final JSONObject obj = new JSONObject(myResponse);
                    if (response.code() == 200) {

                        runOnUiThread( () -> {
                            try {

                                JSONArray jsonArray = obj.getJSONArray("documents");
                                if (jsonArray.length() > 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        DocumentList.add(jsonArray.getJSONObject(i).optString("documents"));

                                    }
                                    adapter = new AdditionalDocumentAdapter( viewDocumentRecyclerView.this,DocumentList);
                                    recyclerView.setLayoutManager(layoutManager);
                                    recyclerView.setAdapter(adapter);
                                }
                                if (DocumentList.size() <= 0) {
                                    Toast.makeText( context, "No Documents Attached", Toast.LENGTH_SHORT ).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } );
                    } else {
                        Log.d(TAG, "I am in else");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });

    }
}
