package com.icls.offlinekyc.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.icls.offlinekyc.R;
import com.icls.offlinekyc.commonshare.common;
import com.icls.offlinekyc.function.MyOrgTabView.ChatActivity;
import com.icls.offlinekyc.function.MyOrgTabView.MyOrgTabViewActivity;
import com.icls.offlinekyc.helper.MasterNotification;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private List<String> titlelist;
    private List<String> descriptionlist;
    private List<String>   notificationId;
    private List<String> replyAllowed;
    private LayoutInflater mInflater;
    private List<String>   notificationOrgId;
    private List<String> notificationMsgtO;
    private ItemClickListener mClickListener;
    private Context context;
    private String orgName;

    // data is passed into the constructor
    public NotificationAdapter(Context context,ArrayList<String> notificationArrayTitle, ArrayList<String> notificationArrayDescription, ArrayList<String> notificationArrayNotificationID, ArrayList<String> notificationArrayReplyAllowed, ArrayList<String> notificationOrgIdfk, ArrayList<String> notificationMsgTo) {
        this.mInflater = LayoutInflater.from(context);
        this.titlelist = notificationArrayTitle;
        this.descriptionlist= notificationArrayDescription;
        this.notificationId = notificationArrayNotificationID;
        this.replyAllowed = notificationArrayReplyAllowed;
        this.notificationOrgId=notificationOrgIdfk;
        this.notificationMsgtO=notificationMsgTo;
        this.context = context;
    }


    //private final View.OnClickListener mOnClickListener = new OrganizationProfile();
    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate( R.layout.notification_card_row, parent, false);

        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String title = titlelist.get(position);
        String description = descriptionlist.get(position);
        String notifiID= notificationId.get( position );
        String replyStatus = replyAllowed.get( position );
        holder.title.setText(title);
        holder.Descprition.setText(description);

        orgName = (String) holder.title.getText();
        holder.cardId.setOnClickListener(view->{
            Intent intent = new Intent(context  , ChatActivity.class);
            intent.putExtra("TabName","Notification");
            intent.putExtra( "notificationID", notifiID);
            intent.putExtra( "replyAllowed",replyStatus );
            common.SELECTEDORGID=notificationOrgId.get( position );

            intent.putExtra( "notificationOrgId", notificationOrgId.get( position ));
            intent.putExtra( "notificationMsgtO",notificationMsgtO.get( position ) );
            intent.putExtra( "intentfrom","intentfromNotification" );
            common.ORG_NAME =  titlelist.get( position );
            context.startActivity(intent);
        });


    }
    // total number of rows
    @Override
    public int getItemCount() {
        return titlelist.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title, Descprition;
        CardView cardId;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.notification_title);
            Descprition = itemView.findViewById(R.id.notification_description);
            cardId = itemView.findViewById(R.id.notification_cardId);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return titlelist.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}