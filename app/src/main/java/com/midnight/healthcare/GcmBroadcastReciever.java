package com.midnight.healthcare;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by TUSK.ONE on 9/19/16.
 */
public class GcmBroadcastReciever extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent intent2 = new Intent("update_by_push");
        // You can also include some extra data.
        intent2.putExtra("message", "This is my message!");
        context.sendBroadcast(intent2);

        ComponentName comp = new ComponentName(context.getPackageName(),
                GCMNotificationIntentService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }
}