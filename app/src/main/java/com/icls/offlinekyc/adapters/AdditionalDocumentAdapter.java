package com.icls.offlinekyc.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.icls.offlinekyc.R;
import com.icls.offlinekyc.commonshare.common;

import java.util.ArrayList;

public class AdditionalDocumentAdapter extends RecyclerView.Adapter<AdditionalDocumentAdapter.myHolder> {

//here the data will be sent and the data type is ArrayList

    private ArrayList<String> myDataList;
    Context context;
    Activity activity;


    public AdditionalDocumentAdapter(Activity activity, ArrayList<String> myDataList) {

        this.myDataList = myDataList;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from( viewGroup.getContext() );
        View view = layoutInflater.inflate( R.layout.additionalprofiledoc, viewGroup, false );

        return new myHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull myHolder myHolder, int position) {

        final String currentItem = myDataList.get( position );
        myHolder.myTitle.setText( currentItem.substring( currentItem.lastIndexOf( "/" ) + 1 ) );
        myHolder.myTitle.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mBaseUrl = common.PHAPHAFILEURL;
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mBaseUrl+currentItem));
                activity.startActivity(browserIntent);

            }
        } );
    }

    @Override
    public int getItemCount() {
        //here we need to pass the length of of our data list

        return myDataList.size();
    }

    //Inner class
    public class myHolder extends RecyclerView.ViewHolder {

        //create constructor
        //here we will use find view by id to acess the id's of our single_row
        TextView myTitle, mySubTitle;
        WebView webView;

        public myHolder(@NonNull View itemView) {
            super( itemView );

            //with the help of itemView we can access it

            myTitle = itemView.findViewById( R.id.additionaldocname );
            webView = itemView.findViewById(R.id.mWebView);


        }


    }
}