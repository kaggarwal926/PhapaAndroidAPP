package com.icls.offlinekyc.function;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.icls.offlinekyc.R;
import com.icls.offlinekyc.function.Models.IssuedDocumentSelectionModel;

import java.util.ArrayList;
import java.util.List;




public class DocListRecyclerViewAdapter extends RecyclerView.Adapter<DocListRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "DocListRecyclerViewAdap";
    public String pdf;
    private Context context;
    private List<IssuedDocumentSelectionModel> mData;
    /*private List<String> mDataUri;
    private List<String> mDataFiletype;*/
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;



    // temperary created for single document
    public static String tempdocName ;
    public static String tempDocUri ;
    public static String tempDoctype ;





    // data is passed into the constructor
    DocListRecyclerViewAdapter(Context context, ArrayList<IssuedDocumentSelectionModel> docmodelList) {
        this.context=context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = docmodelList;
//        this.mDataUri=dataUri;
//        this.mDataFiletype=datafiletype;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate( R.layout.digi_doc_list_view, parent, false);

        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final IssuedDocumentSelectionModel docModel = mData.get(position);
//        final String docUri = mDataUri.get( position );
//        final String docType = mDataFiletype.get( position );
        holder.myTextView.setText(docModel.getDocName());
        holder.myTextView.setBackgroundColor(docModel.isSelected() ? Color.CYAN : Color.WHITE);
        holder.myTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                docModel.setSelected(!docModel.isSelected());

                holder.myTextView.setBackgroundColor(docModel.isSelected() ? Color.CYAN : Color.WHITE);

                    tempdocName =docModel.getDocName();
                    tempDocUri =docModel.getDocUri();
                    tempDoctype =docModel.getDoctype();


            }
        });


//        holder.myShare.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText( context, "Adding "+docNames+"...", Toast.LENGTH_SHORT ).show();
//                Activity activity = (Activity) context;
//                activity.runOnUiThread( new Runnable() {
//                    @Override
//                    public void run() {
//                        try{
//                            getIsuuedDocument(docNames,docUri,docType);
//                        }catch (Exception e){
//                            e.printStackTrace();
//                        }
//
//                    }
//                } );
//
//
//            }
//        } );
    }





    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;

      //  Button myShare;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.textviewdocname);

           // myShare = itemView.findViewById( R.id.btn_share );
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

//    // convenience method for getting data at click position
//    String getItem(int id) {
//        return mData.get(id);
//    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}