package com.midnight.healthcare.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.midnight.healthcare.API.API;
import com.midnight.healthcare.API.APIFactory;
import com.midnight.healthcare.API.passResponse;
import com.midnight.healthcare.Global;
import com.midnight.healthcare.R;
import com.midnight.healthcare.SomeRandomeClass;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends MainActivity {

    RelativeLayout exitBtn;
    Button save;
    EditText newPass;
    EditText newPass2;
    EditText oldPass;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setStatusBarBlue();

        sharedPref = getSharedPreferences("my", Context.MODE_PRIVATE);
        editor = sharedPref.edit();


        final API api = APIFactory.createAPI();

        exitBtn = (RelativeLayout) findViewById(R.id.exit);
        save = (Button) findViewById(R.id.saveButton);
        newPass = (EditText) findViewById(R.id.newPass);
        newPass2 = (EditText) findViewById(R.id.newPassRepeat);
        oldPass = (EditText) findViewById(R.id.oldPass);

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        save.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        save.setBackground(getResources().getDrawable(R.drawable.round_blue_active));
                        save.setTextColor(getResources().getColor(R.color.blueHighlighted));
                        break;
                    case MotionEvent.ACTION_UP: {
                        save.setBackground(getResources().getDrawable(R.drawable.round_blue));
                        save.setTextColor(0xFFFFFFFF);
                        String newPassString = newPass.getText().toString();
                        String newPass2String = newPass2.getText().toString();
                        String oldPassString = oldPass.getText().toString();

                        if(newPassString.length() <= 4){
                            AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                            builder.setTitle("Error!")
                                    .setMessage("Password is too short!")
                                    .setCancelable(false)
                                    .setNegativeButton("ОК",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                        else{
                            if(!newPassString.equals(newPass2String)){
                                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                                builder.setTitle("Error!")
                                        .setMessage("Passwords do not match!")
                                        .setCancelable(false)
                                        .setNegativeButton("ОК",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                    }
                                                });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                            else{
                                //TODO: REQUEST
                                progress = new ProgressDialog(SettingsActivity.this);
                                progress.setMessage("Loading, please wait...");
                                progress.show();

                                SomeRandomeClass.AddToList("newpass", md5(newPass.getText().toString()));
                                SomeRandomeClass.AddToList("oldpass", md5(oldPass.getText().toString()));
                                SomeRandomeClass.AddToList("id", ((Global) getApplication()).getCurrentUser().getId());

                                Call<passResponse> call = api.changepass(SomeRandomeClass.GetData());
                                call.enqueue(new Callback<passResponse>() {
                                    @Override
                                    public void onResponse(Call<passResponse> call, Response<passResponse> response) {
                                        //TODO: PASS IN SHARED PREFERENCES
                                        if(progress != null) progress.dismiss();
                                        if(response.body() != null){
                                            if(response.body().getError() == null){
                                                editor.putString("password", md5(newPass.getText().toString()));

                                                finish();
                                            }
                                            else{
                                                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                                                builder.setTitle("Error!")
                                                        .setMessage(response.body().getError())
                                                        .setCancelable(false)
                                                        .setNegativeButton("ОК",
                                                                new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int id) {
                                                                        dialog.cancel();
                                                                    }
                                                                });
                                                AlertDialog alert = builder.create();
                                                alert.show();
                                            }
                                        }

                                    }

                                    @Override
                                    public void onFailure(Call<passResponse> call, Throwable t) {
                                        if(progress != null) progress.dismiss();
                                        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                                        builder.setTitle("Error!")
                                                .setMessage("Old Password is incorrect!")
                                                .setCancelable(false)
                                                .setNegativeButton("ОК",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                dialog.cancel();
                                                            }
                                                        });
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                    }
                                });
                            }
                        }
                        break;
                    }
                }
                return false;
            }
        });
    }

    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
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
