package com.icls.offlinekyc.firebaseService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String message=intent.getStringExtra("toastmessage");
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }
}
