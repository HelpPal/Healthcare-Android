package com.midnight.healthcare;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.midnight.healthcare.API.JobLocation;
import com.midnight.healthcare.API.Jobs;
import com.midnight.healthcare.API.Messegess;
import com.midnight.healthcare.Activity.ConversationActivity;
import com.midnight.healthcare.Activity.JobForCNA;
import com.midnight.healthcare.Activity.ReviewMyjob;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by TUSK.ONE on 9/19/16.
 */
public class GCMNotificationIntentService extends IntentService {
    // Sets an ID for the notification, so it can be updated
    public static final int notifyID = 9001;
    NotificationCompat.Builder builder;

    SharedPreferences sPref;

    public GCMNotificationIntentService() {
        super("GcmIntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
                    .equals(messageType)) {
//				sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
                    .equals(messageType)) {
//				sendNotification("Deleted messages on server: "
//						+ extras.toString());

            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
                    .equals(messageType)) {
                try {
                    Log.d("extras", String.valueOf(extras));
                    String array;
                    if(extras.get("type").equals("1")){
                        array = ""+extras.get("fromUser");
                    }
                    else{
                        array = ""+extras.get("job");
                    }

                    sendNotification(""+extras.get("message"),""+extras.get("type"), array);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //sendNotification("Message Received from Google GCM Server:\n\n"
                //+ extras.get("m"));
            }
        }
        GcmBroadcastReciever.completeWakefulIntent(intent);
    }

    private int getNotificationIcon() {
        boolean whiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return whiteIcon ? com.midnight.healthcare.R.mipmap.ic_launcher : com.midnight.healthcare.R.mipmap.ic_launcher;
    }

    private void sendNotification(String msg, String page, String array) throws JSONException {


        JSONObject user = new JSONObject(array);

        SharedPreferences preferences = getBaseContext().getSharedPreferences("Notifications", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        if(page.equals("1")) {
            int myvar = preferences.getInt("notificationsMessages", 0) + 1;

            String previousData = preferences.getString("messageid", "");
            if(previousData.equals("")) editor.putString("messageid", user.getString("id"));
            else editor.putString("messageid", previousData + ";" + user.getString("id"));

            editor.putInt("notificationsMessages", myvar);
            editor.commit();
        }
        else{
            int myvar = preferences.getInt("notificationsOffer", 0) + 1;

            editor.putInt("notificationsOffer", myvar);
            editor.commit();
        }





        NotificationCompat.Builder mNotifyBuilder;
        NotificationManager mNotificationManager;

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

//TODO icon
        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                com.midnight.healthcare.R.mipmap.ic_launcher);

        String finalMessage;

        if(page.equals("1")) {
            finalMessage = user.getString("first_name") + " " + user.getString("last_name") + " " + msg;
        }
        else{
            finalMessage = msg;
        }

        mNotifyBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("Heath Care")
                .setContentText(finalMessage)
                .setSmallIcon(getNotificationIcon())
                .setColor(Color.WHITE);



        //R.drawable.ic_launcher

        // Set pending intent

        Intent resultIntent = null;
        if(!page.equals("null")) {
            if (page.equals("1")) {
                //TODO: MESSAGE TYPE

                resultIntent = new Intent(getBaseContext() , ConversationActivity.class);
                Messegess item = new Messegess();
                item.setPartner(user.getString("id"));
                resultIntent.putExtra("mess", item);

                //TODO: PARTNER NAME
                resultIntent.putExtra("partnerName", user.getString("first_name") + " " + user.getString("last_name"));

            } else if(page.equals("2")){
                //TODO: OFFER ACCEPT
                if(((Global) getApplication()).getCurrentUser().getType().equals("0")){
                    resultIntent = new Intent(getBaseContext(), ReviewMyjob.class);
                }
                else{
                    resultIntent = new Intent(getBaseContext(), JobForCNA.class);
                }

                Jobs job = new Jobs();
                job.setAvalable(user.getString("avalable"));
                job.setByUser(user.getString("byUser"));
                job.setDate(user.getString("date"));
                if(user.has("distance")) job.setDistance(user.getString("distance"));
                job.setHidden(user.getString("hidden"));
                job.setHours(user.getString("hours"));
                job.setId(user.getString("id"));
                job.setInformation(user.getString("information"));
                job.setLat(user.getString("lat"));
                job.setLongitude(user.getString("longitude"));
                job.setMaxPrice(user.getString("max_price"));
                job.setMinPrice(user.getString("min_price"));
                job.setMaxYear(user.getString("max_year"));
                job.setMinYear(user.getString("min_year"));
                job.setTitle(user.getString("title"));
                job.setRepate(user.getString("repate"));

                JSONObject locationJSON = user.getJSONObject("location");
                JobLocation location = new JobLocation();
                location.setCity(locationJSON.getString("city"));
                location.setState(locationJSON.getString("state"));

                job.setLocation(location);

                if(user.has("time_desc")) job.setTime_desc(user.getString("time_desc"));

                resultIntent.putExtra("job",  job);

                //resultIntent.putExtra("job",  user);
            }
            else if(page.equals("3")){
                //TODO: OFFER REFUSE
                if(((Global) getApplication()).getCurrentUser().getType().equals("0")){
                    resultIntent = new Intent(getBaseContext(), ReviewMyjob.class);
                }
                else{
                    resultIntent = new Intent(getBaseContext(), JobForCNA.class);
                }

                Jobs job = new Jobs();
                job.setAvalable(user.getString("avalable"));
                job.setByUser(user.getString("byUser"));
                job.setDate(user.getString("date"));
                if(user.has("distance")) job.setDistance(user.getString("distance"));
                job.setHidden(user.getString("hidden"));
                job.setHours(user.getString("hours"));
                job.setId(user.getString("id"));
                job.setInformation(user.getString("information"));
                job.setLat(user.getString("lat"));
                job.setLongitude(user.getString("longitude"));
                job.setMaxPrice(user.getString("max_price"));
                job.setMinPrice(user.getString("min_price"));
                job.setMaxYear(user.getString("max_year"));
                job.setMinYear(user.getString("min_year"));
                job.setTitle(user.getString("title"));
                job.setRepate(user.getString("repate"));

                JSONObject locationJSON = user.getJSONObject("location");
                JobLocation location = new JobLocation();
                location.setCity(locationJSON.getString("city"));
                location.setState(locationJSON.getString("state"));

                job.setLocation(location);

                if(user.has("time_desc")) job.setTime_desc(user.getString("time_desc"));

                resultIntent.putExtra("job",  job);

                //resultIntent.putExtra("job",  user);
            }

            else if(page.equals("4")){
                //TODO: OFFER RECIEVED
                if(((Global) getApplication()).getCurrentUser().getType().equals("0")){
                    resultIntent = new Intent(getBaseContext(), ReviewMyjob.class);
                }
                else{
                    resultIntent = new Intent(getBaseContext(), JobForCNA.class);
                }

                Jobs job = new Jobs();
                job.setAvalable(user.getString("avalable"));
                job.setByUser(user.getString("byUser"));
                job.setDate(user.getString("date"));
                if(user.has("distance")) job.setDistance(user.getString("distance"));
                job.setHidden(user.getString("hidden"));
                job.setHours(user.getString("hours"));
                job.setId(user.getString("id"));
                job.setInformation(user.getString("information"));
                job.setLat(user.getString("lat"));
                job.setLongitude(user.getString("longitude"));
                job.setMaxPrice(user.getString("max_price"));
                job.setMinPrice(user.getString("min_price"));
                job.setMaxYear(user.getString("max_year"));
                job.setMinYear(user.getString("min_year"));
                job.setTitle(user.getString("title"));
                job.setRepate(user.getString("repate"));

                JSONObject locationJSON = user.getJSONObject("location");
                JobLocation location = new JobLocation();
                location.setCity(locationJSON.getString("city"));
                location.setState(locationJSON.getString("state"));

                job.setLocation(location);

                if(user.has("time_desc")) job.setTime_desc(user.getString("time_desc"));

                resultIntent.putExtra("job",  job);
            }
        }
        PendingIntent resultPendingIntent = PendingIntent.getActivity(getBaseContext(), 0,
                resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        mNotifyBuilder.setContentIntent(resultPendingIntent);




        // Set Vibrate, Sound and Light
        int defaults = 0;
        defaults = defaults | Notification.DEFAULT_LIGHTS;
        defaults = defaults | Notification.DEFAULT_VIBRATE;
        defaults = defaults | Notification.DEFAULT_SOUND;

        mNotifyBuilder.setDefaults(defaults);
        // Set the content for Notification
        mNotifyBuilder.setContentText(finalMessage);
        // Set autocancel
        mNotifyBuilder.setAutoCancel(true);
        // Post a notification



        mNotificationManager.notify(notifyID, mNotifyBuilder.build());
    }
}