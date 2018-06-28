package com.midnight.healthcare.Activity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.midnight.healthcare.API.API;
import com.midnight.healthcare.API.APIFactory;
import com.midnight.healthcare.API.SubscribeAddResponse;
import com.midnight.healthcare.Config;
import com.midnight.healthcare.Global;
import com.midnight.healthcare.R;
import com.midnight.healthcare.SomeRandomeClass;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubscribtionEndedActivity extends MainActivity {

    RelativeLayout logOutButton;
    Button SubscribeNow;

    RelativeLayout activeState;
    ImageView logOutActive;
    TextView emailText;

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    int BILLING_RESPONSE_RESULT_OK = 0;

    String developerPayload = java.util.UUID.randomUUID().toString();

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
        setContentView(R.layout.activity_subscribtion_ended);

        setStatusBarBlue();

        final Config configClass = new Config();

        Intent serviceIntent =
                new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);

        sharedPref = getSharedPreferences("my", Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        emailText = (TextView) findViewById(R.id.emailText);

        emailText.setText(sharedPref.getString("email", ""));

        activeState = (RelativeLayout) findViewById(R.id.activeState);
        logOutActive = (ImageView) findViewById(R.id.log_out_active);

        SubscribeNow = (Button) findViewById(R.id.subscribeNow);
        logOutButton = (RelativeLayout) findViewById(R.id.logOutButton);

        logOutButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        activeState.setVisibility(View.VISIBLE);
                        logOutActive.setVisibility(View.VISIBLE);
                        return true;
                    case MotionEvent.ACTION_UP: {
                        activeState.setVisibility(View.GONE);
                        logOutActive.setVisibility(View.GONE);

                        editor.putString("email" , "");
                        editor.putString("password" , "");
                        editor.apply();

                        Intent loginIntent = new Intent(SubscribtionEndedActivity.this, LoginActivity.class);
                        startActivity(loginIntent);

                        break;
                    }
                    case MotionEvent.ACTION_CANCEL:
                        activeState.setVisibility(View.GONE);
                        logOutActive.setVisibility(View.GONE);
                        break;
                }
                return false;
            }
        });

        /*logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("email" , "");
                editor.putString("password" , "");
                editor.apply();

                Intent loginIntent = new Intent(SubscribtionEndedActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });*/

        SubscribeNow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        SubscribeNow.setBackground(getResources().getDrawable(R.drawable.round_blue_active));
                        SubscribeNow.setTextColor(getResources().getColor(R.color.blueHighlighted));
                        break;
                    case MotionEvent.ACTION_UP: {
                        SubscribeNow.setBackground(getResources().getDrawable(R.drawable.round_blue));
                        SubscribeNow.setTextColor(0xFFFFFFFF);

                        if((new Config()).getTest() == 0) {
                            try {
                                Bundle bundle = mService.getBuyIntent(3, configClass.getPackageName(),
                                        configClass.getSKU(), "subs", developerPayload);
                                PendingIntent pendingIntent = bundle.getParcelable("BUY_INTENT");
                                if (bundle.getInt("BILLING_RESPONSE_RESULT_OK") == BILLING_RESPONSE_RESULT_OK) {
                                    // Start purchase flow (this brings up the Google Play UI).
                                    // Result will be delivered through onActivityResult().
                                    startIntentSenderForResult(pendingIntent.getIntentSender(), 1001, new Intent(),
                                            Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0));
                                }

                            } catch (RemoteException e) {
                                e.printStackTrace();
                            } catch (IntentSender.SendIntentException e) {
                                e.printStackTrace();
                            }
                        }
                        else{
                            API api = APIFactory.createAPI();
                            SomeRandomeClass.AddToList("userid", ((Global)getApplication()).getCurrentUser().getId());
                            Call<SubscribeAddResponse> call = api.subscribe(SomeRandomeClass.GetData());
                            call.enqueue(new Callback<SubscribeAddResponse>() {
                                @Override
                                public void onResponse(Call<SubscribeAddResponse> call, Response<SubscribeAddResponse> response) {
                                    Log.d("Response", String.valueOf(response.body()));
                                }

                                @Override
                                public void onFailure(Call<SubscribeAddResponse> call, Throwable t) {

                                }
                            });

                            finish();
                        }
                        break;
                    }
                }
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
            API api = APIFactory.createAPI();
            SomeRandomeClass.AddToList("userid", ((Global)getApplication()).getCurrentUser().getId());
            Call<SubscribeAddResponse> call = api.subscribe(SomeRandomeClass.GetData());
            call.enqueue(new Callback<SubscribeAddResponse>() {
                @Override
                public void onResponse(Call<SubscribeAddResponse> call, Response<SubscribeAddResponse> response) {

                }

                @Override
                public void onFailure(Call<SubscribeAddResponse> call, Throwable t) {

                }
            });

            finish();
        }
        else{
            Toast.makeText(SubscribtionEndedActivity.this, "AN ERROR HAS OCCURRED!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        //NOTHING
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
