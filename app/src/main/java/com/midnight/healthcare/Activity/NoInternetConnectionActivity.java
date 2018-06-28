package com.midnight.healthcare.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import com.midnight.healthcare.Global;
import com.midnight.healthcare.R;

public class NoInternetConnectionActivity extends MainActivity {

    Button tryAgain;
    ImageView phoneImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet_connection);

        tryAgain = (Button) findViewById(R.id.tryAgain);
        phoneImage = (ImageView) findViewById(R.id.phoneImage);

        tryAgain.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        tryAgain.setBackground(getResources().getDrawable(R.drawable.round_blue_active));
                        tryAgain.setTextColor(getResources().getColor(R.color.blueHighlighted));
                        break;
                    case MotionEvent.ACTION_UP: {
                        tryAgain.setBackground(getResources().getDrawable(R.drawable.round_blue));
                        tryAgain.setTextColor(0xFFFFFFFF);
                        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                        NetworkInfo ni = cm.getActiveNetworkInfo();
                        boolean isConnected;
                        if (ni == null) {
                            // There are no active networks.
                            isConnected = false;
                        }
                        else{
                            isConnected = ni.isConnected();
                        }


                        if(isConnected){
                            ((Global) getApplication()).setNoNetworkOpened(false);
                            finish();
                        }
                        else{
                            //TODO: ANIMATION
                            TranslateAnimation anim = new TranslateAnimation(-30,30,0,0);
                            anim.setDuration(150);
                            anim.setRepeatCount(4);
                            anim.setRepeatMode(Animation.REVERSE);
                            phoneImage.startAnimation(anim);
                        }
                        break;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {

    }
}
