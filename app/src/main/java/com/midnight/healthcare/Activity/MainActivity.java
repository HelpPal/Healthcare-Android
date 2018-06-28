package com.midnight.healthcare.Activity;

import android.support.v7.app.AppCompatActivity;

import com.midnight.healthcare.Global;

/**
 * Created by tolea on 03.11.16.
 */

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onResume() {
        super.onResume();
        ((Global) getApplication()).setAppInBackground(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ((Global) getApplication()).setAppInBackground(true);
    }
}
