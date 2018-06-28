package com.midnight.healthcare.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.crashlytics.android.Crashlytics;
import com.midnight.healthcare.API.API;
import com.midnight.healthcare.API.APIFactory;
import com.midnight.healthcare.API.LoginResponse;
import com.midnight.healthcare.Global;
import com.midnight.healthcare.R;
import com.midnight.healthcare.SomeRandomeClass;

import io.fabric.sdk.android.Fabric;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends MainActivity {
    Button register;
    EditText login;
    EditText pass;
    Button log;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    Button lost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_login);

        setStatusBarBlue();

        checkPermissions();
    }

    private void setAllTheData() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo ni = cm.getActiveNetworkInfo();
        boolean isConnected;
        if (ni == null) {
            // There are no active networks.
            isConnected = false;
        } else {
            isConnected = ni.isConnected();
        }

        if (!isConnected) {
            Intent noNetwork = new Intent(LoginActivity.this, NoInternetConnectionActivity.class);
            startActivity(noNetwork);
        }

        //initialize//
        register = (Button) findViewById(R.id.login_register_btn);
        login = (EditText) findViewById(R.id.login_login_field);
        pass = (EditText) findViewById(R.id.login_pass_field);
        log = (Button) findViewById(R.id.login_login_btn);
        sharedPref = getSharedPreferences("my", Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        if (!sharedPref.getString("email", "").equals("")) {
            ((RelativeLayout) findViewById(R.id.mainLayout)).setVisibility(View.GONE);
            login();
        }


        lost = (Button) findViewById(R.id.lost_pass);
        lost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, LostPasswordActivity.class);
                startActivity(intent);
            }
        });

        log.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        log.setBackground(getResources().getDrawable(R.drawable.round_blue_active));
                        log.setTextColor(getResources().getColor(R.color.blueHighlighted));
                        break;
                    case MotionEvent.ACTION_UP: {
                        log.setBackground(getResources().getDrawable(R.drawable.round_blue));
                        log.setTextColor(0xFFFFFFFF);
                        if (login.getText().toString().equals("") || pass.getText().toString().equals("")) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setTitle("Error!")
                                    .setMessage("All fields are required!")

                                    .setCancelable(false)
                                    .setNegativeButton("ОК",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();


                                                }
                                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        } else {

                            final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
                            dialog.setMessage("Loading...");
                            dialog.show();

                            final String email = login.getText().toString();
                            final String password3 = pass.getText().toString();

                            API api = APIFactory.createAPI();
                            SomeRandomeClass.AddToList("username", email);
                            SomeRandomeClass.AddToList("password", password3);
                            Call<LoginResponse> logg = api.login(SomeRandomeClass.GetData());
                            SomeRandomeClass.AddToList("username", email);
                            SomeRandomeClass.AddToList("password", password3);
                            Log.d("logg", "onTouch: " + SomeRandomeClass.GetData());

                            logg.enqueue(new Callback<LoginResponse>() {
                                @Override
                                public void onResponse(Call<LoginResponse> call, final Response<LoginResponse> response) {
                                    Log.d("onResponse", "onResponse: " + response.body().toString());
                                    if (response.body().getError().equals("")) {

                                        if (response.body().getUser().getId() != null) {
                                            ((Global) getApplication()).setCurrentUser(response.body().getUser());
                                            editor.putString("email", email);
                                            editor.putString("password", password3);
                                            editor.apply();


                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if (response.body().getUser().getType().equals("0")) {
                                                        Intent myIntent = new Intent(LoginActivity.this, CNAFeedActivity.class);
                                                        startActivity(myIntent);
                                                    } else {
                                                        Intent myIntent = new Intent(LoginActivity.this, JobDeefActivity.class);
                                                        startActivity(myIntent);
                                                    }
                                                    dialog.dismiss();
                                                }
                                            });
                                        } else {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                            builder.setTitle("Error!")
                                                    .setMessage("Incorect email or password...")

                                                    .setCancelable(false)
                                                    .setNegativeButton("ОК",
                                                            new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int id) {
                                                                    dialog.cancel();


                                                                }
                                                            });
                                            AlertDialog alert = builder.create();
                                            alert.show();
                                            dialog.dismiss();
                                        }
                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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
                                        dialog.dismiss();
                                    }
                                }

                                @Override
                                public void onFailure(Call<LoginResponse> call, Throwable t) {
                                    Log.d("onFailure", "t.message: " + t.getLocalizedMessage());
                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                    builder.setTitle("Error!")
                                            .setMessage("Incorect email or password...")

                                            .setCancelable(false)
                                            .setNegativeButton("ОК",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.cancel();


                                                        }
                                                    });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                    dialog.dismiss();
                                }
                            });
                            break;
                        }
                    }
                }
                return false;
            }
        });


        //setOnClick//
        register.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        register.setBackground(getResources().getDrawable(R.drawable.round_blue_active));
                        register.setTextColor(getResources().getColor(R.color.blueHighlighted));
                        break;
                    case MotionEvent.ACTION_UP: {
                        register.setBackground(getResources().getDrawable(R.drawable.round_blue));
                        register.setTextColor(0xFFFFFFFF);
                        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                        startActivity(intent);
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


    private void login() {
        final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
        dialog.setMessage("Loading...");
        dialog.show();

        API api = APIFactory.createAPI();
        SomeRandomeClass.AddToList("username", sharedPref.getString("email", "nope"));
        SomeRandomeClass.AddToList("password", sharedPref.getString("password", "nope"));
        Call<LoginResponse> logg = api.login(SomeRandomeClass.GetData());
        logg.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, final Response<LoginResponse> response) {
                if (response.body().getError().equals("")) {
                    if (response.body().getUser().getId() != null) {
                        ((Global) getApplication()).setCurrentUser(response.body().getUser());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (response.body().getUser().getType().equals("0")) {
                                    Intent myIntent = new Intent(LoginActivity.this, CNAFeedActivity.class);
                                    startActivity(myIntent);
                                } else {
                                    Intent myIntent = new Intent(LoginActivity.this, JobDeefActivity.class);
                                    startActivity(myIntent);
                                }
                                dialog.dismiss();
                            }
                        });
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setTitle("Error!")
                                .setMessage("Incorect email or password...")

                                .setCancelable(false)
                                .setNegativeButton("ОК",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();


                                            }
                                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        ((RelativeLayout) findViewById(R.id.mainLayout)).setVisibility(View.VISIBLE);
                        dialog.dismiss();
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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
                    ((RelativeLayout) findViewById(R.id.mainLayout)).setVisibility(View.VISIBLE);
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Error!")
                        .setMessage("Incorect email or password...")

                        .setCancelable(false)
                        .setNegativeButton("ОК",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();


                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
                ((RelativeLayout) findViewById(R.id.mainLayout)).setVisibility(View.VISIBLE);
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {

    }

    public void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_NETWORK_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_NETWORK_STATE},
                    100);
        } else if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    101);
        } else if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    102);
        } else if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    103);
        } else if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET},
                    104);
        } else {
            setAllTheData();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkPermissions();
                } else {
                    finish();
                }
                return;
            }

            case 101: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkPermissions();
                } else {
                    finish();
                }
                return;
            }

            case 102: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkPermissions();
                } else {
                    finish();
                }
                return;
            }

            case 103: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkPermissions();
                } else {
                    finish();
                }
                return;
            }

            case 104: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkPermissions();
                } else {
                    finish();
                }
                return;
            }

        }
    }

    public void setStatusBarBlue() {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(R.color.blueHighlighted));
        }
    }
}
