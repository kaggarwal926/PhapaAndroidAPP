package com.icls.offlinekyc.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.icls.offlinekyc.R;
import com.icls.offlinekyc.commonshare.common;
import com.icls.offlinekyc.function.Models.MytabServiceReqModal;
import com.icls.offlinekyc.function.MyOrgTabView.ChatActivity;
import com.icls.offlinekyc.function.MyOrgTabView.MyOrgTabViewActivity;
import com.icls.offlinekyc.roomdb.OrganizationRepository;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OrgSpecificServiceRequestingAdapter extends RecyclerView.Adapter<OrgSpecificServiceRequestingAdapter.ViewHolder> {

    private List<MytabServiceReqModal> List;

    private LayoutInflater mInflater;

    private ItemClickListener mClickListener;
    private Context context;
    private String myResponse;
    private Activity activity;


    // data is passed into the constructor
    public OrgSpecificServiceRequestingAdapter(Context context, Activity activity, ArrayList<MytabServiceReqModal> orgspecificSerReqObjList) {
        this.mInflater = LayoutInflater.from(context);
        this.List = orgspecificSerReqObjList;

        this.activity = activity;
        this.context = context;
    }


    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.mytab_service_req_card_row, parent, false);

        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String Servicename = List.get(position).getService_Name();
        Log.d("durationn", "msg = " + Servicename);

        String Duration = List.get(position).getDuration_To_Complete();
        Log.d("durationn", "msg = " + Duration);
        String description = List.get(position).getService_Description();
        String servicefee = List.get(position).getServiceFee();

        holder.servicename.setText(Servicename);
        holder.duration.setText(Duration);
        holder.description.setText(description);
        holder.fee.setText(servicefee);
        holder.service_req_btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("service_id", List.get(position).getService_ID());
                intent.putExtra("TabName", "ServiceRequest");

                common.SELECTEDORGID = List.get(position).getOrganization_idfk();
                context.startActivity(intent);
            }
        });
        holder.cardId.setOnClickListener(view -> {

        });
        holder.reqBtn.setOnClickListener(view -> {
            //new AsyncTaskRunner().execute(Integer.toString(position));
            applyRequest(position, holder);
        });


    }

    // total number of rows
    @Override
    public int getItemCount() {
        return List.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView servicename, duration, description, fee, apply_status;
        Button reqBtn;
        CardView cardId;
        ImageButton service_req_btn_chat;

        ViewHolder(View itemView) {
            super(itemView);
            servicename = itemView.findViewById(R.id.ServiceName);
            duration = itemView.findViewById(R.id.Duration);
            apply_status = itemView.findViewById(R.id.apply_status);
            service_req_btn_chat = itemView.findViewById(R.id.service_req_btn_chat);

            description = itemView.findViewById(R.id.Description);
            fee = itemView.findViewById(R.id.fee);
            reqBtn = itemView.findViewById(R.id.MytabServReqBtn);
            cardId = itemView.findViewById(R.id.myTabServiceCard);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    private void applyRequest(int position , ViewHolder holder){
        OkHttpClient client = new OkHttpClient();
        Request request = null;
        RequestBody requestBody = null;

        requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("Service_ID", List.get(position).getService_ID())
                .addFormDataPart("organization_idfk", List.get(position).getOrganization_idfk())
                .addFormDataPart("syn_status", "1")
                .build();


        request = new Request.Builder()
                .url(common.PHAPAURL + "ChatConverstion_cntrl/ServiceRequestforOrganizationServices")
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
                myResponse = response.body().string();
                Log.i("RequestApplyResponse", myResponse + "");
                try {
                    JSONObject obj = new JSONObject(myResponse);
                    String status = obj.optString("status", " ");
                    String message = obj.optString("message", " ");
                    if (status.equalsIgnoreCase("200")) {
                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                holder.reqBtn.setEnabled(false);
                                holder.apply_status.setVisibility(View.VISIBLE);

                                Toast.makeText(context, message+"", Toast.LENGTH_LONG).show();
                            }
                        });
//                        final OrganizationRepository noteRepository = new OrganizationRepository(context);
//                        noteRepository.updateUserProfile(common.NOCHANGE);
//                        noteRepository.closeDB();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}