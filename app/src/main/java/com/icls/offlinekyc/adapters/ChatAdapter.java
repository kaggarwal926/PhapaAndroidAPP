package com.icls.offlinekyc.adapters;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.icls.offlinekyc.BuildConfig;
import com.icls.offlinekyc.R;
import com.icls.offlinekyc.function.Models.ChatModel;
import com.icls.offlinekyc.function.Models.ServiceChatModel;
import com.icls.offlinekyc.main.MainActivity;

import java.io.File;
import java.util.List;



public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private List<ServiceChatModel> mData;


    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;
    private Activity activity;


    // data is passed into the constructor
    public ChatAdapter(Context context, List<ServiceChatModel> data, Activity activity ) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
        this.activity = activity;

    }


    //private final View.OnClickListener mOnClickListener = new OrganizationProfile();
    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate( R.layout.notification_chat_row, parent, false);

        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ServiceChatModel chatModel = mData.get(position);
        activity.runOnUiThread( new Runnable() {
            @Override
            public void run() {

                if(chatModel.getMessage_form().equalsIgnoreCase( chatModel.getMember_id() )){


                    holder.chatLeftMsgLayout.setVisibility( View.GONE );
                    holder.chatRightMsgLayout.setVisibility( View.VISIBLE );
                    holder.rightTimeStamp.setText( chatModel.getCreated_date(  ) );
                    if(!chatModel.getMessage().equalsIgnoreCase( "NULL" )){

                        holder.attatchment_in_chat_row.setVisibility( View.GONE );

                        holder.tvRight.setText(chatModel.getMessage());
                        holder.tvRight.setTextColor( context.getColor( R.color.black ) );
                        holder.tvRight.setCompoundDrawablesWithIntrinsicBounds( 0, 0, 0, 0);
                        holder.tvRight.setTypeface(null, Typeface.NORMAL);

                    }else{
                        holder.tvRight.setVisibility( View.GONE );

                    }

                    if( chatModel.getLocal_filepath()!= null &&!chatModel.getLocal_filepath().equalsIgnoreCase( "" )){

                          if(chatModel.getAttachment_name().toLowerCase().contains( ".jpg")
                                  || chatModel.getAttachment_name().toLowerCase().contains( ".jpeg")
                                  ||chatModel.getAttachment_name().toLowerCase().contains( ".png")){

                              holder.attatchment_in_chat_row.setVisibility( View.VISIBLE );

                              Bitmap bitmap = BitmapFactory.decodeFile( Environment.getExternalStorageDirectory()
                                      .getPath() + chatModel.getLocal_filepath());
                              holder.attatchment_in_chat_row.setImageBitmap( bitmap );
                          } else {
                              /*image view gone*/
                              holder.attatchment_in_chat_row.setVisibility( View.GONE );

                              holder.tvRight.setVisibility( View.VISIBLE );
                              holder.tvRight.setCompoundDrawablesWithIntrinsicBounds( 0, R.drawable.file_icon, 0, 0);
                              holder.tvRight.setCompoundDrawablePadding( 10);
                              holder.tvRight.setTextColor( context.getColor( R.color.green ) );
                              holder.tvRight.setText( chatModel.getAttachment_name() );
                              holder.tvRight.setTypeface(null, Typeface.BOLD);
                          }

                        holder.view_document.setOnClickListener( v -> {


                              String path = "/storage/emulated/0/Phapa/Documents/"+chatModel.getAttachment_name();
                              File file = new File( path );
                              Uri uri = FileProvider.getUriForFile( context,BuildConfig.APPLICATION_ID+".provider",file );
                              showFile(path,uri);


                        } );

                    }

                }else {

                    holder.chatLeftMsgLayout.setVisibility( View.VISIBLE );
                    holder.chatRightMsgLayout.setVisibility( View.GONE );
                    holder.timestamp.setText( chatModel.getCreated_date(  ) );
                    if(!chatModel.getMessage().equalsIgnoreCase( "NULL" )){

                        holder.attatchment_in_chat_row_left.setVisibility( View.GONE );

                        holder.tvLeft.setText(chatModel.getMessage());
                        holder.tvLeft.setTextColor( context.getColor( R.color.black ) );
                        holder.tvLeft.setCompoundDrawablesWithIntrinsicBounds( 0, 0, 0, 0);
                        holder.tvLeft.setTypeface(null, Typeface.NORMAL);

                    }else{
                        holder.tvLeft.setVisibility( View.GONE );

                    }

                    if( chatModel.getLocal_filepath()!= null &&!chatModel.getLocal_filepath().equalsIgnoreCase( "" )){

                        if(chatModel.getAttachment_name().toLowerCase().contains( ".jpg")
                                || chatModel.getAttachment_name().toLowerCase().contains( ".jpeg")
                                ||chatModel.getAttachment_name().toLowerCase().contains( ".png")){

                            holder.attatchment_in_chat_row_left.setVisibility( View.VISIBLE );

                            Bitmap bitmap = BitmapFactory.decodeFile( Environment.getExternalStorageDirectory()
                                    .getPath() + chatModel.getLocal_filepath());
                            holder.attatchment_in_chat_row_left.setImageBitmap( bitmap );
                        } else {
                            /*image view gone*/
                            holder.attatchment_in_chat_row_left.setVisibility( View.GONE );

                            holder.tvLeft.setVisibility( View.VISIBLE );
                            holder.tvLeft.setCompoundDrawablesWithIntrinsicBounds( 0, R.drawable.file_icon, 0, 0);
                            holder.tvLeft.setCompoundDrawablePadding( 10);
                            holder.tvLeft.setTextColor( context.getColor( R.color.green ) );
                            holder.tvLeft.setText( chatModel.getAttachment_name() );
                            holder.tvLeft.setTypeface(null, Typeface.BOLD);
                        }

                        holder.view_document_left.setOnClickListener( v -> {


                            String path = "/storage/emulated/0/Phapa/Documents/"+chatModel.getAttachment_name();
                            File file = new File( path );
                            Uri uri = FileProvider.getUriForFile( context,BuildConfig.APPLICATION_ID+".provider",file );
                            showFile(path,uri);


                        } );

                    }


                }
            }
        } );






    }

    private void showFile(String path,Uri apkURI) {

        String url =path;

        Uri uri = apkURI;
        File file = new File(path);


            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

            if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
                // Word document
                intent.setDataAndType(uri, "application/msword");
            } else if (url.toString().contains(".pdf")) {
                // PDF file
                intent.setDataAndType(uri, "application/pdf");
            } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
                // Powerpoint file
                intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
            } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
                // Excel file
                intent.setDataAndType(uri, "application/vnd.ms-excel");
            } else if (url.toString().contains(".zip") || url.toString().contains(".rar")) {
                // WAV audio file
                intent.setDataAndType(uri, "application/x-wav");
            } else if (url.toString().contains(".rtf")) {
                // RTF file
                intent.setDataAndType(uri, "application/rtf");
            } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
                // WAV audio file
                intent.setDataAndType(uri, "audio/x-wav");
            } else if (url.toString().contains(".gif")) {
                // GIF file
                intent.setDataAndType(uri, "image/gif");
            } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
                // JPG file
                intent.setDataAndType(uri, "image/jpeg");
            } else if (url.toString().contains(".txt")) {
                // Text file
                intent.setDataAndType(uri, "text/plain");
            } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
                // Video files
                intent.setDataAndType(uri, "video/*");
            }
            else {

                intent.setDataAndType(uri, "*/*");
            }
        try{
            context.startActivity(intent);
        }catch(Exception e){
            e.printStackTrace();
            Log.e( "file open error ",e.getMessage()  );

            new AlertDialog.Builder(context)
                    .setTitle("No Application Found ")
                    .setMessage("Download App to view file")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Continue with delete operation
                        }
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    private void openFile(File url) {

        try {

            /*Uri uri = Uri.fromFile(url);*/

            Uri uri = FileProvider.getUriForFile( context, BuildConfig.APPLICATION_ID + ".provider",url);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
                // Word document
                intent.setDataAndType(uri, "application/msword");
            } else if (url.toString().contains(".pdf")) {
                // PDF file
                intent.setDataAndType(uri, "application/pdf");
            } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
                // Powerpoint file
                intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
            } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
                // Excel file
                intent.setDataAndType(uri, "application/vnd.ms-excel");
            } else if (url.toString().contains(".zip")) {
                // ZIP file
                intent.setDataAndType(uri, "application/zip");
            } else if (url.toString().contains(".rar")){
                // RAR file
                intent.setDataAndType(uri, "application/x-rar-compressed");
            } else if (url.toString().contains(".rtf")) {
                // RTF file
                intent.setDataAndType(uri, "application/rtf");
            } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
                // WAV audio file
                intent.setDataAndType(uri, "audio/x-wav");
            } else if (url.toString().contains(".gif")) {
                // GIF file
                intent.setDataAndType(uri, "image/gif");
            } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
                // JPG file
                intent.setDataAndType(uri, "image/jpeg");
            } else if (url.toString().contains(".txt")) {
                // Text file
                intent.setDataAndType(uri, "text/plain");
            } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") ||
                    url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
                // Video files
                intent.setDataAndType(uri, "video/*");
            } else {
                intent.setDataAndType(uri, "*/*");
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "No application found which can open the file", Toast.LENGTH_LONG).show();
        }
    }
    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvLeft,timestamp,tvRight,rightTimeStamp;
        LinearLayout chatLeftMsgLayout,chatRightMsgLayout,view_document_left,view_document;
        ImageView attatchment_in_chat_row,attatchment_in_chat_row_left;

        ViewHolder(View itemView) {
            super(itemView);
            tvLeft = itemView.findViewById(R.id.chat_left_msg_text_view);
            tvRight = itemView.findViewById(R.id.chat_right_msg_text_view);
            timestamp = itemView.findViewById(R.id.timestamp);
            rightTimeStamp = itemView.findViewById(R.id.right_timestamp);

            chatLeftMsgLayout = itemView.findViewById( R.id.chat_left_msg_layout );
            chatRightMsgLayout= itemView.findViewById( R.id.chat_right_msg_layout );
            view_document = itemView.findViewById( R.id.view_document );
            view_document_left= itemView.findViewById( R.id.view_document_left );
            attatchment_in_chat_row =itemView.findViewById( R.id.attatchment_in_chat_row);
            attatchment_in_chat_row_left=itemView.findViewById( R.id.attatchment_in_chat_row_left);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    ServiceChatModel getItem(int id) {
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