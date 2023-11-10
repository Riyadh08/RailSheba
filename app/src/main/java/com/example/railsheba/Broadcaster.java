package com.example.railsheba;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;


public class Broadcaster extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (!isConnected(context)) {
            Toast.makeText(context.getApplicationContext(), "No Internet Connection ", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Opps!");
            builder.setMessage(" No Internet Connection \n Refresh to continue...");
            builder.setIcon(R.drawable.ic_baseline_add_alert_24);
            builder.setPositiveButton("Refresh", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    onReceive(context, intent);
                }
            });
            AlertDialog al = builder.create();
            al.show();
        }
        else if (isConnected(context)) {
        }
    }

    private boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }

}
