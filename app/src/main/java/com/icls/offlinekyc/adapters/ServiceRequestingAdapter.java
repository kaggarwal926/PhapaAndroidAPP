package com.icls.offlinekyc.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.icls.offlinekyc.R;
import com.icls.offlinekyc.function.Models.ServiceReqMasterHelperModal;

import java.util.ArrayList;
import java.util.List;

public class ServiceRequestingAdapter extends RecyclerView.Adapter<ServiceRequestingAdapter.ViewHolder> {

    private List<ServiceReqMasterHelperModal> titlelist;

    private LayoutInflater mInflater;

    private ItemClickListener mClickListener;
    private Context context;


    // data is passed into the constructor
    public ServiceRequestingAdapter(Context context, ArrayList<ServiceReqMasterHelperModal> ServiceReqMasterListHelperList) {
        this.mInflater = LayoutInflater.from(context);
        this.titlelist =  ServiceReqMasterListHelperList;

        this.context = context;
    }



    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate( R.layout.service_req_card_row, parent, false);

        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String title = titlelist.get( position ).getService_Name();
        holder.title.setText( title );
        holder.cardId.setOnClickListener(view->{

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
            title = itemView.findViewById(R.id.Servicetitle );
            cardId = itemView.findViewById(R.id.cardId_servicereq);
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
}