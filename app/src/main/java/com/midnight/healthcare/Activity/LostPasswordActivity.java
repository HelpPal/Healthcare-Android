package com.midnight.healthcare.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.midnight.healthcare.API.API;
import com.midnight.healthcare.API.APIFactory;
import com.midnight.healthcare.R;
import com.midnight.healthcare.SomeRandomeClass;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LostPasswordActivity extends MainActivity {


    EditText email;
    Button recover;
    RelativeLayout done;
    Button ok;
    API api = APIFactory.createAPI();
    RelativeLayout exit;
    RelativeLayout exit2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_password);

        setStatusBarBlue();

        email = (EditText) findViewById(R.id.email_lost);
        recover = (Button) findViewById(R.id.recover);
        done = (RelativeLayout)findViewById(R.id.done);
        ok = (Button) findViewById(R.id.ok);
        exit = (RelativeLayout) findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        exit2 = (RelativeLayout) findViewById(R.id.exit2);
        exit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                done.setVisibility(View.GONE);
            }
        });

        recover.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        recover.setBackground(getResources().getDrawable(R.drawable.round_blue_active));
                        recover.setTextColor(getResources().getColor(R.color.blueHighlighted));
                        break;
                    case MotionEvent.ACTION_UP: {
                        recover.setBackground(getResources().getDrawable(R.drawable.round_blue));
                        recover.setTextColor(0xFFFFFFFF);
                        if (email.getText().toString().equals("")){

                            AlertDialog.Builder builder = new AlertDialog.Builder(LostPasswordActivity.this);
                            builder.setTitle("Error!")
                                    .setMessage("Please submit an email!")

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
                        else {
                            if(isValidEmail(email.getText().toString())) {

                                SomeRandomeClass.AddToList("email", email.getText().toString());
                                Call x = api.recover(SomeRandomeClass.GetData());
                                x.enqueue(new Callback() {
                                    @Override
                                    public void onResponse(Call call, Response response) {
                                        done.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onFailure(Call call, Throwable t) {

                                    }
                                });
                            }
                            else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(LostPasswordActivity.this);
                                builder.setTitle("Error!")
                                        .setMessage("Email is invalid!")

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
                        break;
                    }
                }
                return false;
            }
        });

        ok.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ok.setBackground(getResources().getDrawable(R.drawable.round_blue_active));
                        ok.setTextColor(getResources().getColor(R.color.blueHighlighted));
                        break;
                    case MotionEvent.ACTION_UP: {
                        ok.setBackground(getResources().getDrawable(R.drawable.round_blue));
                        ok.setTextColor(0xFFFFFFFF);
                        finish();
                        break;
                    }
                }
                return false;
            }
        });


    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
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
