package com.icls.offlinekyc.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.icls.offlinekyc.Database.OrganizationTypeParam;
import com.icls.offlinekyc.R;
import com.icls.offlinekyc.commonshare.common;
import com.icls.offlinekyc.function.DrawerActivity;
import com.icls.offlinekyc.function.MyOrgTabView.MyOrgTabViewActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.graphics.BitmapFactory.decodeFile;

public class myOrganizationAdapter extends RecyclerView.Adapter<myOrganizationAdapter.ViewHolder> {
    private List<String> mData;
    private LayoutInflater mInflater;
    List<OrganizationTypeParam> helperOrg = new ArrayList<>();
    private myOrganizationAdapter.ItemClickListener mClickListener;
    private Context context;
    private String orgName;
    private String orgLogo;

    public myOrganizationAdapter(Context context, List<String> data,     List<OrganizationTypeParam> helperOrg ) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.helperOrg = helperOrg;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate( R.layout.myorganizationrow, parent, false);

        return new myOrganizationAdapter.ViewHolder(view);
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;
        ConstraintLayout cardId;
        ImageView myorglogo;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.tvAnimalName);
            cardId = itemView.findViewById(R.id.cardId);
            myorglogo =itemView.findViewById( R.id.myorglogo );
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
    public void setItems(List<OrganizationTypeParam> helperOrg) {
        this.helperOrg = helperOrg;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String animal = mData.get(position);
        holder.myTextView.setText(animal);
        orgName = (String) holder.myTextView.getText();
        Random rnd = new Random();
        int currentColor= 0;
        Bitmap image= null;
        try{
           currentColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            image  = Bitmap.createBitmap(255, rnd.nextInt(256), Bitmap.Config.ARGB_8888);
            image.eraseColor(currentColor);
       }catch(Exception e){
            e.printStackTrace();
        }


        holder.myorglogo.setImageBitmap( image );

        holder.cardId.setOnClickListener(view->{
            Intent intent = new Intent(context  , MyOrgTabViewActivity.class);
            intent.putExtra("TabName",2);
            common.ORG_NAME =helperOrg.get( position ).getOrganization_name() ;
            intent.putExtra("myorganizationName",helperOrg.get( position ).getOrganization_name());
            common.SELECTEDORGID= helperOrg.get( position ).getOrganization_id();

            intent.putExtra( "intentfrom","intentfromMyorganization" );
            intent.putExtra( "replyAllowed","" );
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    // allows clicks events to be caught
    public void setClickListener(myOrganizationAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
}
