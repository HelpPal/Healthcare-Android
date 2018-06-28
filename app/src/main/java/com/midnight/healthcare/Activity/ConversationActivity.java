package com.midnight.healthcare.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.midnight.healthcare.API.API;
import com.midnight.healthcare.API.APIFactory;
import com.midnight.healthcare.API.ConversationResponse;
import com.midnight.healthcare.API.LastMessage;
import com.midnight.healthcare.API.Messegess;
import com.midnight.healthcare.API.Nurse;
import com.midnight.healthcare.API.ProfileResponse;
import com.midnight.healthcare.Adapters.ConversationAdapter;
import com.midnight.healthcare.Global;
import com.midnight.healthcare.R;
import com.midnight.healthcare.SomeRandomeClass;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConversationActivity extends MainActivity {
    Messegess mess;
    ListView list;
    ConversationAdapter adapter;
    Button send;
    EditText text;
    TextView partnerName;
    Nurse nurse;
    List<LastMessage> listTest = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        setStatusBarBlue();

        ((RelativeLayout) findViewById(R.id.exit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        partnerName = (TextView) findViewById(R.id.partnerName);

        Intent intent = getIntent();
        mess = (Messegess) intent.getSerializableExtra("mess");
        partnerName.setText(intent.getStringExtra("partnerName"));

        if (((Global) getApplication()).getCurrentUser().getType().equals("0")) {
            String userid = mess.getPartner();
            API api = APIFactory.createAPI();
            SomeRandomeClass.AddToList("userid", userid);
            SomeRandomeClass.AddToList("myuserid", ((Global) getApplication()).getCurrentUser().getId());
            Call<ProfileResponse> call = api.profile(SomeRandomeClass.GetData());
            call.enqueue(new Callback<ProfileResponse>() {
                @Override
                public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                    if (response != null) {
                        nurse = response.body().getResponse();

                        partnerName.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent profileIntent = new Intent(ConversationActivity.this, CNAActivity.class);
                                profileIntent.putExtra("nurse", nurse);
                                startActivity(profileIntent);
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<ProfileResponse> call, Throwable t) {

                }
            });
        }

        list = (ListView) findViewById(R.id.conversation_list);
        final ProgressDialog dialog = new ProgressDialog(ConversationActivity.this);
        dialog.setMessage("Loading...");
        dialog.show();
        final API api = APIFactory.createAPI();
        SomeRandomeClass.AddToList("userid", ((Global) getApplication()).getCurrentUser().getId());
        SomeRandomeClass.AddToList("partnerid", mess.getPartner());
        Call<ConversationResponse> call = api.conversation(SomeRandomeClass.GetData());
        call.enqueue(new Callback<ConversationResponse>() {
            @Override
            public void onResponse(Call<ConversationResponse> call, Response<ConversationResponse> response) {
                adapter = new ConversationAdapter(response.body().getResponse(), ConversationActivity.this);
                listTest = response.body().getResponse();
                list.setAdapter(adapter);
                dialog.dismiss();
                loop();
            }

            @Override
            public void onFailure(Call<ConversationResponse> call, Throwable t) {

            }
        });

        send = (Button) findViewById(R.id.send_text_message);
        text = (EditText) findViewById(R.id.text_message);
        send.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        send.setBackground(getResources().getDrawable(R.drawable.round_blue_active));
                        send.setTextColor(getResources().getColor(R.color.blueHighlighted));
                        break;
                    case MotionEvent.ACTION_UP: {
                        send.setBackground(getResources().getDrawable(R.drawable.round_blue));
                        send.setTextColor(0xFFFFFFFF);
                        if (text.getText().toString().equals("")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ConversationActivity.this);
                            builder.setTitle("Error!")
                                    .setMessage("Message must have text!")
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
                            SomeRandomeClass.AddToList("user", ((Global) getApplication()).getCurrentUser().getId());
                            SomeRandomeClass.AddToList("toUser", mess.getPartner());
                            SomeRandomeClass.AddToList("text", text.getText().toString());
                            text.setText("");
                            Call send = api.sendmessage(SomeRandomeClass.GetData());
                            send.enqueue(new Callback() {
                                @Override
                                public void onResponse(Call call, Response response) {
                                    upd();
                                }

                                @Override
                                public void onFailure(Call call, Throwable t) {

                                }
                            });
                        }
                        break;
                    }
                }
                return false;
            }
        });
    }

    void upd() {
        API api = APIFactory.createAPI();
        SomeRandomeClass.AddToList("userid", ((Global) getApplication()).getCurrentUser().getId());
        SomeRandomeClass.AddToList("partnerid", mess.getPartner());

        Call<ConversationResponse> call = api.conversation(SomeRandomeClass.GetData());
        call.enqueue(new Callback<ConversationResponse>() {
            @Override
            public void onResponse(Call<ConversationResponse> call, Response<ConversationResponse> response) {
                if (listTest.size() != response.body().getResponse().size()) {
                    adapter = new ConversationAdapter(response.body().getResponse(), ConversationActivity.this);

                    list.setAdapter(adapter);
                }


            }

            @Override
            public void onFailure(Call<ConversationResponse> call, Throwable t) {

            }
        });
    }

    void loop() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                upd();
                loop();
            }
        }, 5000);
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
