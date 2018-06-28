package com.midnight.healthcare;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.midnight.healthcare.Activity.NoInternetConnectionActivity;

import java.util.List;

/**
 * Created by tolea on 23.10.16.
 */

public class NetworkStateReciever extends BroadcastReceiver {
    Boolean opened = false;
    public void onReceive(Context context, Intent intent) {
        Log.d("app", "Network connectivity change");

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> services = activityManager
                .getRunningTasks(Integer.MAX_VALUE);
        boolean isActivityFound = false;

        if (services.get(0).topActivity.getPackageName()
                .equalsIgnoreCase(context.getPackageName())) {
            isActivityFound = true;
        }

        if(isActivityFound) {
            if(!((Global)context.getApplicationContext()).getAppInBackground()) {
                if (intent.getExtras() != null) {
                    if (!((Global) context.getApplicationContext()).getNoNetworkOpened()) {
                        ((Global) context.getApplicationContext()).setNoNetworkOpened(true);
                        NetworkInfo ni = (NetworkInfo) intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);
                        if (ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {
                            //TODO: NOTHING
                            opened = false;
                        } else if (ni != null && ni.getState() == NetworkInfo.State.DISCONNECTED) {
                            //TODO: DISABLE APP
                            if (!opened) {
                                opened = true;
                                Intent noInternet = new Intent(context, NoInternetConnectionActivity.class);
                                noInternet.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(noInternet);
                            }
                        }
                    }
                }
            }
        }
    }
}