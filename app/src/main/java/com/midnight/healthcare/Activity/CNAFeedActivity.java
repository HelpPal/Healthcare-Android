package com.midnight.healthcare.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.appyvet.rangebar.RangeBar;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.midnight.healthcare.API.API;
import com.midnight.healthcare.API.APIFactory;
import com.midnight.healthcare.API.Nurse;
import com.midnight.healthcare.API.NurseResponse;
import com.midnight.healthcare.API.SubscribeResponse;
import com.midnight.healthcare.Adapters.CNAFeedAdapter;
import com.midnight.healthcare.Config;
import com.midnight.healthcare.GPSTracker;
import com.midnight.healthcare.Global;
import com.midnight.healthcare.R;
import com.midnight.healthcare.SomeRandomeClass;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;

import jp.wasabeef.blurry.Blurry;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CNAFeedActivity extends MainActivity {
    API api = APIFactory.createAPI();
    ListView list;
    RelativeLayout menu;
    RelativeLayout cellMenu;
    CNAFeedAdapter adapter;
    RelativeLayout screen;
    RelativeLayout exit;
    ImageView blur;
    RelativeLayout myjubsBtn;
    RelativeLayout applicationBtn;
    RelativeLayout messages;
    Integer pageCount = 0;

   List<Nurse> listPro = new ArrayList<>();



    RelativeLayout seachBtn;
    RelativeLayout seachScreen;
    SearchView searchView;
    //CheckBox near;
    //CheckBox everywhere;
    RangeBar price;
    //CheckBox fullTime;
    //CheckBox partTime;
    RangeBar years;
    Button backSearch;
    Button doneSeach;
    String range = "0";
    String avalability = "1";
    TextView from;
    TextView to;
    TextView fromy;
    TextView toy;
    TextView noItems;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    SearchView searchViewMain;

    RelativeLayout settingsButton;

    LinearLayout nearMeButton;
    LinearLayout everywhereButton;
    LinearLayout fullTimeButton;
    LinearLayout partTimeButton;

    ImageView nearMeImage;
    ImageView everywhereImage;
    ImageView fullTimeImage;
    ImageView partTimeImage;

    TextView nearMeText;
    TextView everywhereText;
    TextView fullTimeText;
    TextView partTimeText;

    RelativeLayout searchViewBackground;
    RelativeLayout searchViewBackground2;

    TextView applicationsCount;
    TextView messageNumber;

    TextView myJobsText;
    TextView settingsText;
    TextView signOutText;
    TextView messagesText;

    ImageView messageIcon;

    GoogleCloudMessaging gcmObj;
    String regid;
    static final String GOOGLE_PROJ_ID = "1097694396988";
    public static final String PROPERTY_REG_ID = "registration_id";

    SharedPreferences sPref;

    IInAppBillingService mService;

    ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name,
                                       IBinder service) {
            mService = IInAppBillingService.Stub.asInterface(service);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cnafeed);

        setStatusBarBlue();

        Intent serviceIntent =
                new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);

        final Config configClass = new Config();

//        new java.util.Timer().schedule(new TimerTask() {
//            @Override
//            public void run() {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if(configClass.getTest() == 0){
//                            Bundle activeSubs = null;
//                            try {
//                                activeSubs = mService.getPurchases(3, configClass.getPackageName(),
//                                        "subs", null);
//                            } catch (RemoteException e) {
//                                e.printStackTrace();
//                            }
//
//                            ArrayList<String> subs = activeSubs.getStringArrayList("INAPP_PURCHASE_ITEM_LIST");
//                            if(subs.size() == 0){
//                                Intent subscribtionEnded = new Intent(CNAFeedActivity.this, SubscribtionEndedActivity.class);
//                                startActivity(subscribtionEnded);
//                            }
//                        }
//                    }
//                });
//            }
//        }, 1000);

        gcmObj = GoogleCloudMessaging.getInstance(CNAFeedActivity.this);
        regid = getRegistrationId(CNAFeedActivity.this);

        if (regid.isEmpty()) {
            if(((Global)getApplication()).getCurrentUser() != null) {
                registerInBackground(((Global) getApplication()).getCurrentUser().getId());
            }
        }

        sharedPref = getSharedPreferences("my", Context.MODE_PRIVATE);

        if(((Global)getApplication()).getCurrentUser() == null){
            Intent loginIntent = new Intent(CNAFeedActivity.this, LoginActivity.class);
            startActivity(loginIntent);
        }

        applicationsCount = (TextView) findViewById(R.id.applicationsNumber);
        messageNumber = (TextView) findViewById(R.id.messageNumber);
        messageIcon = (ImageView) findViewById(R.id.mess_icon);

        editor = sharedPref.edit();
        final RelativeLayout sign_out = (RelativeLayout) findViewById(R.id.sign_out);
        signOutText = (TextView) findViewById(R.id.signOutText);

        sign_out.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        sign_out.setBackground(getResources().getDrawable(R.drawable.round_blue_active_white));
                        signOutText.setTextColor(getResources().getColor(R.color.blueHighlighted));
                        return true;
                    case MotionEvent.ACTION_UP: {
                        sign_out.setBackground(getResources().getDrawable(R.drawable.round_blue_rotund));
                        signOutText.setTextColor(0xFFFFFFFF);

                        AlertDialog.Builder builder = new AlertDialog.Builder(CNAFeedActivity.this);
                        builder.setTitle("Warning!")
                                .setMessage("Are you sure you want to sign out of your account?")
                                .setCancelable(false)
                                .setPositiveButton("Yes",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();

                                                editor.putString("email" , "");
                                                editor.putString("password" , "");
                                                editor.apply();
                                                SomeRandomeClass.AddToList("userid" , ((Global)getApplication()).getCurrentUser().getId());
                                                Call delet = api.deletePushAndroid(SomeRandomeClass.GetData());
                                                delet.enqueue(new Callback() {
                                                    @Override
                                                    public void onResponse(Call call, Response response) {

                                                    }

                                                    @Override
                                                    public void onFailure(Call call, Throwable t) {

                                                    }
                                                });
                                                Intent loginIntent = new Intent(CNAFeedActivity.this, LoginActivity.class);
                                                startActivity(loginIntent);
                                            }
                                        })
                                .setNegativeButton("No",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                        AlertDialog alert = builder.create();
                        alert.show();

                        break;
                    }
                    case MotionEvent.ACTION_CANCEL:
                        sign_out.setBackground(getResources().getDrawable(R.drawable.round_blue_rotund));
                        signOutText.setTextColor(0xFFFFFFFF);
                        break;
                }
                return false;
            }
        });


        /*sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CNAFeedActivity.this);
                builder.setTitle("Warning!")
                        .setMessage("Are you sure you want to sign out of your account?")
                        .setCancelable(false)
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();

                                        editor.putString("email" , "");
                                        editor.putString("password" , "");
                                        editor.apply();
                                        SomeRandomeClass.AddToList("userid" , ((Global)getApplication()).getCurrentUser().getId());
                                        Call delet = api.deletePushAndroid(SomeRandomeClass.GetData());
                                        delet.enqueue(new Callback() {
                                            @Override
                                            public void onResponse(Call call, Response response) {

                                            }

                                            @Override
                                            public void onFailure(Call call, Throwable t) {

                                            }
                                        });
                                        Intent loginIntent = new Intent(CNAFeedActivity.this, LoginActivity.class);
                                        startActivity(loginIntent);
                                    }
                                })
                        .setNegativeButton("No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });*/


        from = (TextView) findViewById(R.id.from);
        to = (TextView) findViewById(R.id.to);
        fromy = (TextView) findViewById(R.id.fromy);
        toy = (TextView) findViewById(R.id.toy);
        seachBtn = (RelativeLayout) findViewById(R.id.search_btn);
        seachScreen = (RelativeLayout) findViewById(R.id.seach_screen);
        searchView = (SearchView) findViewById(R.id.search_seach);
        price = (RangeBar) findViewById(R.id.price_range);
        years = (RangeBar) findViewById(R.id.years_range);
        backSearch = (Button) findViewById(R.id.back_search);
        doneSeach = (Button) findViewById(R.id.done_search);
        searchViewMain = (SearchView) findViewById(R.id.searchViewMain);
        noItems = (TextView) findViewById(R.id.noJobsText);

        settingsButton = (RelativeLayout) findViewById(R.id.settingsButton);

        searchViewBackground = (RelativeLayout) findViewById(R.id.searchViewBackground);
        searchViewBackground2 = (RelativeLayout) findViewById(R.id.searchViewBackground2);

        nearMeButton = (LinearLayout) findViewById(R.id.nearMeButton);
        everywhereButton = (LinearLayout) findViewById(R.id.everywhereButton);
        fullTimeButton = (LinearLayout) findViewById(R.id.fullTimeButton);
        partTimeButton = (LinearLayout) findViewById(R.id.partTimeButton);

        nearMeImage = (ImageView) findViewById(R.id.nearMeImage);
        everywhereImage = (ImageView) findViewById(R.id.everywhereImage);
        fullTimeImage = (ImageView) findViewById(R.id.fullTimeImage);
        partTimeImage = (ImageView) findViewById(R.id.partTimeImage);

        nearMeText = (TextView) findViewById(R.id.nearMeText);
        everywhereText = (TextView) findViewById(R.id.everywhereText);
        fullTimeText = (TextView) findViewById(R.id.fullTimeText);
        partTimeText = (TextView) findViewById(R.id.partTimeText);

        myJobsText = (TextView) findViewById(R.id.myJobsText);
        settingsText = (TextView) findViewById(R.id.settingsText);


        settingsButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        settingsButton.setBackground(getResources().getDrawable(R.drawable.round_blue_active_white));
                        settingsText.setTextColor(getResources().getColor(R.color.blueHighlighted));
                        return true;
                    case MotionEvent.ACTION_UP: {
                        settingsButton.setBackground(getResources().getDrawable(R.drawable.round_blue_rotund));
                        settingsText.setTextColor(0xFFFFFFFF);

                        Intent settingsIntent = new Intent(CNAFeedActivity.this, SettingsActivity.class);
                        startActivity(settingsIntent);

                        break;
                    }
                    case MotionEvent.ACTION_CANCEL:
                        settingsButton.setBackground(getResources().getDrawable(R.drawable.round_blue_rotund));
                        settingsText.setTextColor(0xFFFFFFFF);
                        break;
                }
                return false;
            }
        });

        /*settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(CNAFeedActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
            }
        });*/

        price.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                from.setText("$"+leftPinValue);
                to.setText("$"+rightPinValue);
            }
        });
        years.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                fromy.setText(leftPinValue+" years");
                toy.setText(rightPinValue+" years");
            }
        });

        nearMeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nearMeImage.setImageDrawable(getResources().getDrawable(R.drawable.checked_time));
                nearMeText.setTextColor(getResources().getColor(R.color.blueHighlighted));
                everywhereImage.setImageDrawable(getResources().getDrawable(R.drawable.uncheck));
                everywhereText.setTextColor(0xFF000000);

                range = "1";
            }
        });

        everywhereButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                everywhereImage.setImageDrawable(getResources().getDrawable(R.drawable.checked_time));
                everywhereText.setTextColor(getResources().getColor(R.color.blueHighlighted));
                nearMeImage.setImageDrawable(getResources().getDrawable(R.drawable.uncheck));
                nearMeText.setTextColor(0xFF000000);

                range = "0";
            }
        });

        fullTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullTimeImage.setImageDrawable(getResources().getDrawable(R.drawable.checked_time));
                fullTimeText.setTextColor(getResources().getColor(R.color.blueHighlighted));
                partTimeImage.setImageDrawable(getResources().getDrawable(R.drawable.uncheck));
                partTimeText.setTextColor(0xFF000000);

                avalability = "1";
            }
        });

        partTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                partTimeImage.setImageDrawable(getResources().getDrawable(R.drawable.checked_time));
                partTimeText.setTextColor(getResources().getColor(R.color.blueHighlighted));
                fullTimeImage.setImageDrawable(getResources().getDrawable(R.drawable.uncheck));
                fullTimeText.setTextColor(0xFF000000);

                avalability = "0";
            }
        });


        seachBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchViewMain.clearFocus();
                seachScreen.setVisibility(View.VISIBLE);
            }
        });
        backSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seachScreen.setVisibility(View.GONE);
            }
        });
        seachScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        searchViewBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchViewMain.setIconified(false);
            }
        });

        searchViewBackground2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });

        searchViewMain.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                final ProgressDialog dialog = new ProgressDialog(CNAFeedActivity.this);
                dialog.setMessage("Loading...");
                dialog.show();
                pageCount = 0;
                GPSTracker tracker = new GPSTracker(CNAFeedActivity.this);
                if (!tracker.canGetLocation()) {
                    tracker.showSettingsAlert();
                } else {
                    SomeRandomeClass.AddToList("id", ((Global) getApplication()).getCurrentUser().getId());
                    SomeRandomeClass.AddToList("lat", String.valueOf(tracker.getLatitude()));
                    SomeRandomeClass.AddToList("long", String.valueOf(tracker.getLongitude()));
                    SomeRandomeClass.AddToList("range", range);
                    SomeRandomeClass.AddToList("avalability", avalability);
                    SomeRandomeClass.AddToList("min_price", price.getLeftPinValue());
                    SomeRandomeClass.AddToList("max_price", price.getRightPinValue());
                    SomeRandomeClass.AddToList("min_age", years.getLeftPinValue());
                    SomeRandomeClass.AddToList("max_age", years.getRightPinValue());
                    SomeRandomeClass.AddToList("skills", searchViewMain.getQuery().toString());
                    SomeRandomeClass.AddToList("page", pageCount.toString());
                    SomeRandomeClass.AddToList("userid", ((Global) getApplication()).getCurrentUser().getId());
                    Call<NurseResponse> feed = api.cnafeed(SomeRandomeClass.GetData());
                    feed.enqueue(new Callback<NurseResponse>() {
                        @Override
                        public void onResponse(Call<NurseResponse> call, Response<NurseResponse> response) {
                            if(response.body().getResponse().size() == 0){
                                noItems.setVisibility(View.VISIBLE);
                                list.setVisibility(View.GONE);
                            }
                            else {
                                list.setVisibility(View.VISIBLE);
                                noItems.setVisibility(View.GONE);
                                adapter = new CNAFeedAdapter(response.body().getResponse(), CNAFeedActivity.this, pageCount);
                                list.setAdapter(adapter);
                            }
                            seachScreen.setVisibility(View.GONE);
                            dialog.dismiss();
                        }

                        @Override
                        public void onFailure(Call<NurseResponse> call, Throwable t) {
                            Toast.makeText(CNAFeedActivity.this, t.toString(), Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                            Log.d("onFailure", "onFailure: " + t.getLocalizedMessage());
                        }
                    });

                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.equals("")){
                    final ProgressDialog dialog = new ProgressDialog(CNAFeedActivity.this);
                    dialog.setMessage("Loading...");
                    dialog.show();
                    pageCount = 0;
                    GPSTracker tracker = new GPSTracker(CNAFeedActivity.this);
                    if (!tracker.canGetLocation()) {
                        tracker.showSettingsAlert();
                    } else {
                        SomeRandomeClass.AddToList("id", ((Global) getApplication()).getCurrentUser().getId());
                        SomeRandomeClass.AddToList("lat", String.valueOf(tracker.getLatitude()));
                        SomeRandomeClass.AddToList("long", String.valueOf(tracker.getLongitude()));
                        SomeRandomeClass.AddToList("range", range);
                        SomeRandomeClass.AddToList("avalability", avalability);
                        SomeRandomeClass.AddToList("min_price", price.getLeftPinValue());
                        SomeRandomeClass.AddToList("max_price", price.getRightPinValue());
                        SomeRandomeClass.AddToList("min_age", years.getLeftPinValue());
                        SomeRandomeClass.AddToList("max_age", years.getRightPinValue());
                        SomeRandomeClass.AddToList("skills", searchViewMain.getQuery().toString());
                        SomeRandomeClass.AddToList("page", pageCount.toString());
                        SomeRandomeClass.AddToList("userid", ((Global) getApplication()).getCurrentUser().getId());
                        Call<NurseResponse> feed = api.cnafeed(SomeRandomeClass.GetData());
                        feed.enqueue(new Callback<NurseResponse>() {
                            @Override
                            public void onResponse(Call<NurseResponse> call, Response<NurseResponse> response) {
                                if(response.body().getResponse().size() == 0){
                                    noItems.setVisibility(View.VISIBLE);
                                    list.setVisibility(View.GONE);
                                }
                                else {
                                    list.setVisibility(View.VISIBLE);
                                    noItems.setVisibility(View.GONE);
                                    adapter = new CNAFeedAdapter(response.body().getResponse(), CNAFeedActivity.this, pageCount);
                                    list.setAdapter(adapter);
                                }
                                seachScreen.setVisibility(View.GONE);
                                dialog.dismiss();
                            }

                            @Override
                            public void onFailure(Call<NurseResponse> call, Throwable t) {
                                Toast.makeText(CNAFeedActivity.this, t.toString(), Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                        });

                    }
                }
                return false;
            }
        });

        doneSeach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialog dialog = new ProgressDialog(CNAFeedActivity.this);
                dialog.setMessage("Loading...");
                dialog.show();
                pageCount = 0;
                GPSTracker tracker = new GPSTracker(CNAFeedActivity.this);
                if (!tracker.canGetLocation()) {
                    tracker.showSettingsAlert();
                } else {
                    SomeRandomeClass.AddToList("id", ((Global) getApplication()).getCurrentUser().getId());
                    SomeRandomeClass.AddToList("lat", String.valueOf(tracker.getLatitude()));
                    SomeRandomeClass.AddToList("long", String.valueOf(tracker.getLongitude()));
                    SomeRandomeClass.AddToList("range", range);
                    SomeRandomeClass.AddToList("avalability", avalability);
                    SomeRandomeClass.AddToList("min_price", price.getLeftPinValue());
                    SomeRandomeClass.AddToList("max_price", price.getRightPinValue());
                    SomeRandomeClass.AddToList("min_age", years.getLeftPinValue());
                    SomeRandomeClass.AddToList("max_age", years.getRightPinValue());
                    SomeRandomeClass.AddToList("skills", searchView.getQuery().toString());
                    SomeRandomeClass.AddToList("page", pageCount.toString());
                    SomeRandomeClass.AddToList("userid", ((Global) getApplication()).getCurrentUser().getId());
                    Call<NurseResponse> feed = api.cnafeed(SomeRandomeClass.GetData());
                    feed.enqueue(new Callback<NurseResponse>() {
                        @Override
                        public void onResponse(Call<NurseResponse> call, Response<NurseResponse> response) {
                            if(response.body().getResponse().size() == 0){
                                noItems.setVisibility(View.VISIBLE);
                                list.setVisibility(View.GONE);
                            }
                            else {
                                list.setVisibility(View.VISIBLE);
                                noItems.setVisibility(View.GONE);
                                adapter = new CNAFeedAdapter(response.body().getResponse(), CNAFeedActivity.this, pageCount);
                                list.setAdapter(adapter);
                            }
                            seachScreen.setVisibility(View.GONE);
                            dialog.dismiss();
                        }

                        @Override
                        public void onFailure(Call<NurseResponse> call, Throwable t) {
                            Toast.makeText(CNAFeedActivity.this, t.toString(), Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                    });

                }


            }
        });


        list = (ListView) findViewById(R.id.cnaList);
        menu = (RelativeLayout) findViewById(R.id.side_menu);
        screen = (RelativeLayout) findViewById(R.id.main_screen);
        cellMenu = (RelativeLayout) findViewById(R.id.call_menu);
        exit = (RelativeLayout) findViewById(R.id.exit_menu);
        blur = (ImageView) findViewById(R.id.blur);
        myjubsBtn = (RelativeLayout) findViewById(R.id.mjobsbtn);
        applicationBtn = (RelativeLayout) findViewById(R.id.applications_btn);
        messages = (RelativeLayout) findViewById(R.id.messages);
        messagesText = (TextView) findViewById(R.id.messagesText);

        messages.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        messages.setBackground(getResources().getDrawable(R.drawable.round_blue_active_white));
                        messagesText.setTextColor(getResources().getColor(R.color.blueHighlighted));
                        return true;
                    case MotionEvent.ACTION_UP: {
                        messages.setBackground(getResources().getDrawable(R.drawable.round_blue_rotund));
                        messagesText.setTextColor(0xFFFFFFFF);

                        hideKeyboard();
                        Intent myIntent = new Intent(CNAFeedActivity.this, MessagesActivity.class);
                        startActivity(myIntent);

                        break;
                    }
                    case MotionEvent.ACTION_CANCEL:
                        messages.setBackground(getResources().getDrawable(R.drawable.round_blue_rotund));
                        messagesText.setTextColor(0xFFFFFFFF);
                        break;
                }
                return false;
            }
        });

        /*messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                Intent myIntent = new Intent(CNAFeedActivity.this, MessagesActivity.class);
                startActivity(myIntent);
            }
        });*/

        sPref = this.getSharedPreferences("Notifications", Context.MODE_PRIVATE);
        int unread = sPref.getInt("notificationsOffer", 0);
        if (unread == 0) {
            applicationsCount.setVisibility(View.GONE);
        } else {
            applicationsCount.setVisibility(View.VISIBLE);
            applicationsCount.setText(String.valueOf(unread));
        }

        applicationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                Intent myIntent = new Intent(CNAFeedActivity.this, AplicationsIndividulaActivity.class);
                startActivity(myIntent);
            }
        });

        myjubsBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        myjubsBtn.setBackground(getResources().getDrawable(R.drawable.round_blue_active_white));
                        myJobsText.setTextColor(getResources().getColor(R.color.blueHighlighted));
                        return true;
                    case MotionEvent.ACTION_UP: {
                        myjubsBtn.setBackground(getResources().getDrawable(R.drawable.round_blue_rotund));
                        myJobsText.setTextColor(0xFFFFFFFF);

                        hideKeyboard();
                        Intent myIntent = new Intent(CNAFeedActivity.this, MyJobFeedActivity.class);
                        startActivity(myIntent);

                        break;
                    }
                    case MotionEvent.ACTION_CANCEL:
                        myjubsBtn.setBackground(getResources().getDrawable(R.drawable.round_blue_rotund));
                        myJobsText.setTextColor(0xFFFFFFFF);
                        break;
                }
                return false;
            }
        });

        /*myjubsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                Intent myIntent = new Intent(CNAFeedActivity.this, MyJobFeedActivity.class);
                startActivity(myIntent);
            }
        });*/

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menu.setVisibility(View.GONE);

            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        final ProgressDialog dialog = new ProgressDialog(CNAFeedActivity.this);
        dialog.setMessage("Loading...");
        dialog.show();
        GPSTracker tracker = new GPSTracker(this);
        if (!tracker.canGetLocation()) {
            tracker.showSettingsAlert();
        } else {
            if(((Global) getApplication()).getCurrentUser() != null) {
                SomeRandomeClass.AddToList("id", ((Global) getApplication()).getCurrentUser().getId());
                SomeRandomeClass.AddToList("lat", String.valueOf(tracker.getLatitude()));
                SomeRandomeClass.AddToList("long", String.valueOf(tracker.getLongitude()));
                SomeRandomeClass.AddToList("page", String.valueOf(0));
                SomeRandomeClass.AddToList("userid", ((Global) getApplication()).getCurrentUser().getId());
            }


            Call<NurseResponse> feed = api.cnafeed(SomeRandomeClass.GetData());
            feed.enqueue(new Callback<NurseResponse>() {
                @Override
                public void onResponse(Call<NurseResponse> call, Response<NurseResponse> response) {
                    if(response.body() != null) {
                        listPro.addAll(response.body().getResponse());
                        if (listPro.size() == 0) {
                            noItems.setVisibility(View.VISIBLE);
                            list.setVisibility(View.GONE);
                        } else {
                            list.setVisibility(View.VISIBLE);
                            noItems.setVisibility(View.GONE);
                            adapter = new CNAFeedAdapter(listPro, CNAFeedActivity.this, 0);
                            list.setAdapter(adapter);
                        }
                        dialog.dismiss();

                    }
                    dialog.dismiss();
                }

                @Override
                public void onFailure(Call<NurseResponse> call, Throwable t) {
                    Toast.makeText(CNAFeedActivity.this, t.toString(), Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                    Log.d("onFailure1", "onFailure: " + t.toString());
                }
            });

        }


        cellMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sPref = getBaseContext().getSharedPreferences("Notifications", Context.MODE_PRIVATE);
                int unread = sPref.getInt("notificationsMessages", 0);
                if (unread == 0) {
                    messageIcon.setImageDrawable(getResources().getDrawable(R.drawable.message_inactive));
                    messageNumber.setVisibility(View.GONE);
                } else {
                    messageIcon.setImageDrawable(getResources().getDrawable(R.drawable.mess_icon_standart));
                    messageNumber.setVisibility(View.VISIBLE);
                    messageNumber.setText(String.valueOf(unread));
                }

                if(!searchViewMain.isIconified()){
                    searchViewMain.clearFocus();
                    new java.util.Timer().schedule(
                            new java.util.TimerTask() {
                                @Override
                                public void run() {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            menu.setVisibility(View.VISIBLE);
                                            Blurry.with(CNAFeedActivity.this).radius(20).color(Color.argb(180, 255, 255, 255)).async().animate(200).capture(screen).into(blur);
                                        }
                                    });
                                }
                            },
                            200
                    );
                }
                else {

                    menu.setVisibility(View.VISIBLE);
                    Blurry.with(CNAFeedActivity.this).radius(20).color(Color.argb(180, 255, 255, 255)).async().animate(200).capture(screen).into(blur);
                }



            }
        });
    }

    @Override
    public void onBackPressed() {

    }

    public void fill() {


       pageCount++;
        GPSTracker tracker = new GPSTracker(this);
        if (!tracker.canGetLocation()) {
            tracker.showSettingsAlert();
        } else {
            SomeRandomeClass.AddToList("id", ((Global) getApplication()).getCurrentUser().getId());
            SomeRandomeClass.AddToList("lat", String.valueOf(tracker.getLatitude()));
            SomeRandomeClass.AddToList("long", String.valueOf(tracker.getLongitude()));
            SomeRandomeClass.AddToList("page", pageCount.toString());
            SomeRandomeClass.AddToList("userid", ((Global) getApplication()).getCurrentUser().getId());
            Call<NurseResponse> feed = api.cnafeed(SomeRandomeClass.GetData());
            feed.enqueue(new Callback<NurseResponse>() {
                @Override
                public void onResponse(Call<NurseResponse> call, Response<NurseResponse> response) {
                  for (Nurse nurse : response.body().getResponse())
                    listPro.add(nurse);

                    if(listPro.size() == 0){
                        noItems.setVisibility(View.VISIBLE);
                        list.setVisibility(View.GONE);
                    }
                    else {
                        noItems.setVisibility(View.GONE);
                        list.setVisibility(View.VISIBLE);
                        adapter = new CNAFeedAdapter(listPro, CNAFeedActivity.this, pageCount);
                    }



                }

                @Override
                public void onFailure(Call<NurseResponse> call, Throwable t) {
                    Toast.makeText(CNAFeedActivity.this, t.toString(), Toast.LENGTH_LONG).show();
                    Log.d("onFailure", "onFailure: " + t.getLocalizedMessage());
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        SomeRandomeClass.AddToList("userid", ((Global) getApplication()).getCurrentUser().getId());
        Call<SubscribeResponse> call = api.checksubscribe(SomeRandomeClass.GetData());
        call.enqueue(new Callback<SubscribeResponse>() {
            @Override
            public void onResponse(Call<SubscribeResponse> call, Response<SubscribeResponse> response) {
                if(response.body().getError() == null){
                    if(response.body().getUser().equals("0")){
                        Intent subscribtionEnded = new Intent(CNAFeedActivity.this, SubscribtionEndedActivity.class);
                        startActivity(subscribtionEnded);
                    }
                }
                else{
                    Intent newIntent = new Intent(CNAFeedActivity.this, LoginActivity.class);
                    startActivity(newIntent);
                }
            }

            @Override
            public void onFailure(Call<SubscribeResponse> call, Throwable t) {

            }
        });

        sPref = this.getSharedPreferences("Notifications", Context.MODE_PRIVATE);
        int unread = sPref.getInt("notificationsOffer", 0);
        if (unread == 0) {
            applicationsCount.setVisibility(View.GONE);
        } else {
            applicationsCount.setVisibility(View.VISIBLE);
            applicationsCount.setText(String.valueOf(unread));
        }

        sPref = getBaseContext().getSharedPreferences("Notifications", Context.MODE_PRIVATE);
        int unreadMess = sPref.getInt("notificationsMessages", 0);
        if (unreadMess == 0) {
            messageNumber.setVisibility(View.GONE);
            messageIcon.setImageDrawable(getResources().getDrawable(R.drawable.message_inactive));
        } else {
            messageNumber.setVisibility(View.VISIBLE);
            messageIcon.setImageDrawable(getResources().getDrawable(R.drawable.mess_icon_standart));
            messageNumber.setText(String.valueOf(unreadMess));
        }
    }

    public void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void registerInBackground(final String id) {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object... params) {
                String msg = "";
                try {
                    if (gcmObj == null) {
                        gcmObj = GoogleCloudMessaging.getInstance(CNAFeedActivity.this);
                    }
                    regid = gcmObj.register(GOOGLE_PROJ_ID);
                    Log.d("TAG", "########################################");
                    Log.d("TAG", "Current Device's Registration ID is: " + regid);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SomeRandomeClass.AddToList("userid" , id);
                            SomeRandomeClass.AddToList("push" , regid);
                            Call push = api.pusAndroid(SomeRandomeClass.GetData());
                            push.enqueue(new Callback() {
                                @Override
                                public void onResponse(Call call, Response response) {

                                }

                                @Override
                                public void onFailure(Call call, Throwable t) {

                                }
                            });
                            //Connect.getInstance(context).setPushToken(regid);
                        }
                    });

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return null;
            }

            protected void onPostExecute(Object result) { //to do here };
            }
        }.execute(null,null,null);
    }

    private String getRegistrationId(Context context)
    {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.d("TAG", "Registration ID not found.");
            return "";
        }
        return registrationId;
    }

    private SharedPreferences getGCMPreferences(Context context)
    {
        return getSharedPreferences(LoginActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mService != null) {
            unbindService(mServiceConn);
        }
    }

    public void setStatusBarBlue(){
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(R.color.blueHighlighted));
        }
    }

}
