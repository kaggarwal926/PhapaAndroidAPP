package com.icls.offlinekyc.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

import com.icls.offlinekyc.Database.OrganizationTypeParam;
import com.icls.offlinekyc.R;
import com.icls.offlinekyc.function.DrawerActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.graphics.BitmapFactory.decodeFile;

public class phapacommunityAdapter extends RecyclerView.Adapter<phapacommunityAdapter.ViewHolder> {

    private List<String> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;
    private String orgName;
    List<OrganizationTypeParam> OrgList =new ArrayList<>(  );
    private String orgLogo;

    // data is passed into the constructor
    public phapacommunityAdapter(Context context, List<String> data,List<OrganizationTypeParam> OrgList) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.OrgList=OrgList;
        this.context = context;
    }

    //private final View.OnClickListener mOnClickListener = new OrganizationProfile();
    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate( R.layout.phapacommunityrow, parent, false);

        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String animal = mData.get(position);
        holder.myTextView.setText(animal);
        Bitmap image = null;
        try {
            Random rnd = new Random();
            int currentColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            image = Bitmap.createBitmap(255, rnd.nextInt(256), Bitmap.Config.ARGB_8888);
            image.eraseColor(currentColor);
        } catch (Exception e) {
            e.printStackTrace();
        }


        holder.phaphaorglogo.setImageBitmap( image );

        orgName = (String) holder.myTextView.getText();
        if( OrgList.get( position ).getStatus().equalsIgnoreCase( "Waiting For Payment" )){
            holder.status.setText( "Waiting For Approval");
        } else {
            holder.status.setText( OrgList.get( position ).getStatus());
        }

        holder.cardId.setOnClickListener(view->{
            Intent intent = new Intent(context  , DrawerActivity.class);
            intent.putExtra("organizationName",holder.myTextView.getText());
            context.startActivity(intent);
        });

        holder.status.setOnClickListener(view->{
            Intent intent = new Intent(context  , DrawerActivity.class);
            intent.putExtra("organizationName",holder.myTextView.getText());
            context.startActivity(intent);
        });


    }
    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;
        Button status;
        ImageView phaphaorglogo;
        ConstraintLayout cardId;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.tvAnimalName);
            status = itemView.findViewById( R.id.org_status );
            cardId = itemView.findViewById(R.id.cardId);
            phaphaorglogo = itemView.findViewById( R.id.phaphaorglogo );
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(id);
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