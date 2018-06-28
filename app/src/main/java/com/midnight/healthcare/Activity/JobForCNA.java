package com.midnight.healthcare.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.actionsheet.ActionSheet;
import com.midnight.healthcare.API.API;
import com.midnight.healthcare.API.APIFactory;
import com.midnight.healthcare.API.ApplicationsIndResponse;
import com.midnight.healthcare.API.CheckResponse;
import com.midnight.healthcare.API.Day;
import com.midnight.healthcare.API.Jobs;
import com.midnight.healthcare.API.Messegess;
import com.midnight.healthcare.Global;
import com.midnight.healthcare.R;
import com.midnight.healthcare.SomeRandomeClass;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobForCNA extends MainActivity {
    Jobs job;
    Button sendMess;
    EditText mess;
    Button apply;
    List<String> jobsApplied;
    RelativeLayout waitingForAnAnswer;
    LinearLayout reportButton;
    Boolean clicked = false;

    ProgressDialog progress;

    API api = APIFactory.createAPI();

    Button accept;
    Button decline;
    Button optionButton;
    Button optionButton2;
    Button sendMessage;
    Button accepted;
    Button rate;

    LinearLayout decision;
    LinearLayout after_answer;
    LinearLayout declinedLayout;

    String applicationId = "";
    private RatingBar ratingBar;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_for_cn);

        setStatusBarBlue();

        ((RelativeLayout) findViewById(R.id.exit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        reportButton = (LinearLayout) findViewById(R.id.reportButton);

        Intent intent = getIntent();
        job = (Jobs) intent.getSerializableExtra("job");
        TextView title = (TextView) findViewById(R.id.title_job);
        TextView price = (TextView) findViewById(R.id.cell_job_price);
        TextView desc = (TextView) findViewById(R.id.cell_job_desc);
        TextView info = (TextView) findViewById(R.id.cell_job_info);
        TextView distance = (TextView) findViewById(R.id.distance_location);
        waitingForAnAnswer = (RelativeLayout) findViewById(R.id.waitingForAnAnswer);
        Button s = (Button) findViewById(R.id.job_cell_s);
        Button m = (Button) findViewById(R.id.job_cell_m);
        Button t = (Button) findViewById(R.id.job_cell_t);
        Button w = (Button) findViewById(R.id.job_cell_w);
        Button th = (Button) findViewById(R.id.job_cell_th);
        Button f = (Button) findViewById(R.id.job_cell_f);
        Button sa = (Button) findViewById(R.id.job_cell_sa);
        TextView location = (TextView) findViewById(R.id.location_job);
        sendMess = (Button) findViewById(R.id.sned_mess);
        mess = (EditText) findViewById(R.id.mess);
        apply = (Button) findViewById(R.id.apply_for_job);
        rate = (Button) findViewById(R.id.rate);
        ratingBar = (RatingBar) findViewById(R.id.rb);

        checkApplied();

        apply.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        apply.setBackground(getResources().getDrawable(R.drawable.round_blue_active));
                        apply.setTextColor(getResources().getColor(R.color.blueHighlighted));
                        break;
                    case MotionEvent.ACTION_UP: {
                        apply.setBackground(getResources().getDrawable(R.drawable.round_blue));
                        apply.setTextColor(0xFFFFFFFF);
                        if (!clicked) {
                            progress = new ProgressDialog(JobForCNA.this);
                            progress.setMessage("Loading, please wait...");
                            progress.setCancelable(false);
                            progress.show();
                            clicked = true;
                            SomeRandomeClass.AddToList("userid", ((Global) getApplication()).getCurrentUser().getId());
                            SomeRandomeClass.AddToList("jobid", job.getId());
                            SomeRandomeClass.AddToList("invite", "0");
                            Call call = api.sendJobTouser(SomeRandomeClass.GetData());
                            call.enqueue(new Callback() {
                                @Override
                                public void onResponse(Call call, Response response) {
                                    checkApplied();
                                    if (progress != null) progress.dismiss();
                                    clicked = false;
                                    finish();
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

        rate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        rate.setBackground(getResources().getDrawable(R.drawable.round_blue_active));
                        rate.setTextColor(getResources().getColor(R.color.blueHighlighted));
                        break;
                    case MotionEvent.ACTION_UP: {
                        rate.setBackground(getResources().getDrawable(R.drawable.round_blue));
                        rate.setTextColor(0xFFFFFFFF);
                        if (!clicked) {
                            progress = new ProgressDialog(JobForCNA.this);
                            progress.setMessage("Loading, please wait...");
                            progress.setCancelable(false);
                            progress.show();
                            clicked = true;
                            SomeRandomeClass.AddToList("type", "1");
                            SomeRandomeClass.AddToList("userid", ((Global) getApplication()).getCurrentUser().getId());
                            SomeRandomeClass.AddToList("toUser", job.getId());
                            SomeRandomeClass.AddToList("rating", String.valueOf(ratingBar.getRating()));
                            Call call = api.sendJobTouser(SomeRandomeClass.GetData());
                            call.enqueue(new Callback() {
                                @Override
                                public void onResponse(Call call, Response response) {
                                    if (progress != null) progress.dismiss();
                                    clicked = false;
                                    Toast.makeText(JobForCNA.this, "Rating added", Toast.LENGTH_LONG).show();

                                }

                                @Override
                                public void onFailure(Call call, Throwable t) {
                                    Toast.makeText(JobForCNA.this, "Error: " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                    Log.d("onFailure", "onFailure: " + t.getLocalizedMessage());
                                }
                            });
                        }
                        break;
                    }
                }
                return false;
            }
        });


        List<Day> list = job.getDays();
        for (Day d : list) {
            if (d.getDay().equals(getString(R.string.sunday))) {
                s.setBackgroundResource(R.drawable.s_normal);
                s.setTextColor(getResources().getColor(R.color.blueHighlighted));
            }
            if (d.getDay().equals(getString(R.string.saturday))) {
                sa.setBackgroundResource(R.drawable.sa_tugle);
                sa.setTextColor(getResources().getColor(R.color.blueHighlighted));
            }
            if (d.getDay().equals(getString(R.string.monday))) {
                m.setBackgroundColor(Color.parseColor("#90bbf2"));
                m.setTextColor(getResources().getColor(R.color.blueHighlighted));
            }
            if (d.getDay().equals(getString(R.string.tuesday))) {
                t.setBackgroundColor(Color.parseColor("#90bbf2"));
                t.setTextColor(getResources().getColor(R.color.blueHighlighted));
            }
            if (d.getDay().equals(getString(R.string.wednesday))) {
                w.setBackgroundColor(Color.parseColor("#90bbf2"));
                w.setTextColor(getResources().getColor(R.color.blueHighlighted));
            }
            if (d.getDay().equals(getString(R.string.thursday))) {
                th.setBackgroundColor(Color.parseColor("#90bbf2"));
                th.setTextColor(getResources().getColor(R.color.blueHighlighted));
            }
            if (d.getDay().equals(getString(R.string.friday))) {
                f.setBackgroundColor(Color.parseColor("#90bbf2"));
                f.setTextColor(getResources().getColor(R.color.blueHighlighted));
            }

        }
        info.setText(job.getTime_desc());
        if (job.getLocation() != null) {
            location.setText(job.getLocation().getCity() + ", " + job.getLocation().getState());
        }

        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SomeRandomeClass.AddToList("userid", ((Global) getApplication()).getCurrentUser().getId());
                SomeRandomeClass.AddToList("jobid", job.getId());
                Call call = api.reportJob(SomeRandomeClass.GetData());
                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        final Animation out = new AlphaAnimation(1.0f, 0.0f);
                        out.setDuration(1000);
                        out.setFillAfter(true);
                        reportButton.startAnimation(out);
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        //TODO: SMTH WENT WRONG
                    }
                });
            }
        });

        title.setText(job.getTitle());
        price.setText("$" + job.getMaxPrice() + "/h");
        desc.setText(job.getInformation());
        distance.setText(job.getDistance() + " mi");

        accept = (Button) findViewById(R.id.accept);
        decline = (Button) findViewById(R.id.decline);

        sendMessage = (Button) findViewById(R.id.sendMessage);
        optionButton = (Button) findViewById(R.id.optionsButton);
        optionButton2 = (Button) findViewById(R.id.optionsButton2);

        decision = (LinearLayout) findViewById(R.id.give_answer);
        accepted = (Button) findViewById(R.id.accepted);
        after_answer = (LinearLayout) findViewById(R.id.after_answer);
        declinedLayout = (LinearLayout) findViewById(R.id.declinedLayout);

        waitingForAnAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(JobForCNA.this);
                builder.setTitle("Warning!")
                        .setMessage("Want to cancel this application?")
                        .setCancelable(false)
                        .setNegativeButton("No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                })
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        SomeRandomeClass.AddToList("userid", ((Global) getApplicationContext()).getCurrentUser().getId());
                                        SomeRandomeClass.AddToList("application_id", applicationId);
                                        final API api = APIFactory.createAPI();
                                        Call<Void> call = api.applicationRemove(SomeRandomeClass.GetData());
                                        call.enqueue(new Callback<Void>() {
                                            @Override
                                            public void onResponse(Call<Void> call, Response<Void> response) {
                                                SomeRandomeClass.AddToList("userid", ((Global) getApplicationContext()).getCurrentUser().getId());
                                                Call<ApplicationsIndResponse> callx = api.myapplications(SomeRandomeClass.GetData());
                                                callx.enqueue(new Callback<ApplicationsIndResponse>() {
                                                    @Override
                                                    public void onResponse(Call<ApplicationsIndResponse> callx, Response<ApplicationsIndResponse> response) {
                                                        checkApplied();
                                                    }

                                                    @Override
                                                    public void onFailure(Call<ApplicationsIndResponse> callx, Throwable t) {


                                                    }
                                                });

                                            }

                                            @Override
                                            public void onFailure(Call<Void> call, Throwable t) {

                                            }
                                        });

                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent conversationIntent = new Intent(JobForCNA.this, ConversationActivity.class);
                Messegess itemMess = new Messegess();
                itemMess.setPartner(job.getByUser());
                conversationIntent.putExtra("mess", itemMess);
                startActivity(conversationIntent);
            }
        });

        optionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: OPTIONS
                setTheme(R.style.ActionSheetStyleiOS7);

                ActionSheet.createBuilder(JobForCNA.this, getSupportFragmentManager())
                        .setCancelButtonTitle("Cancel")
                        .setOtherButtonTitles("Report", "Delete this application")
                        .setCancelableOnTouchOutside(true)
                        .setListener(new ActionSheet.ActionSheetListener() {
                            @Override
                            public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

                            }

                            @Override
                            public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                                /*if(index == 0){
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    intent.setData(Uri.parse("tel:" + global.getUser().getPhone()));
                                    context.startActivity(intent);
                                }*/
                                if (index == 0) {
                                    API api = APIFactory.createAPI();
                                    SomeRandomeClass.AddToList("userid", ((Global) getApplication()).getCurrentUser().getId());
                                    SomeRandomeClass.AddToList("jobid", job.getId());
                                    Call call = api.reportJob(SomeRandomeClass.GetData());
                                    call.enqueue(new Callback() {
                                        @Override
                                        public void onResponse(Call call, Response response) {
                                            Toast.makeText(JobForCNA.this, "Job was succesfully reported.",
                                                    Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onFailure(Call call, Throwable t) {

                                        }
                                    });
                                } else if (index == 1) {
                                    //TODO: DELETE APPLICATION
                                    SomeRandomeClass.AddToList("userid", ((Global) getApplicationContext()).getCurrentUser().getId());
                                    SomeRandomeClass.AddToList("application_id", applicationId);
                                    final API api = APIFactory.createAPI();
                                    Call<Void> call = api.applicationRemove(SomeRandomeClass.GetData());
                                    call.enqueue(new Callback<Void>() {
                                        @Override
                                        public void onResponse(Call<Void> call, Response<Void> response) {
                                            SomeRandomeClass.AddToList("userid", ((Global) getApplicationContext()).getCurrentUser().getId());
                                            Call<ApplicationsIndResponse> callx = api.myapplications(SomeRandomeClass.GetData());
                                            callx.enqueue(new Callback<ApplicationsIndResponse>() {
                                                @Override
                                                public void onResponse(Call<ApplicationsIndResponse> callx, Response<ApplicationsIndResponse> response) {
                                                    checkApplied();
                                                }

                                                @Override
                                                public void onFailure(Call<ApplicationsIndResponse> callx, Throwable t) {


                                                }
                                            });

                                        }

                                        @Override
                                        public void onFailure(Call<Void> call, Throwable t) {

                                        }
                                    });
                                }


                            }
                        }).show();
            }
        });

        optionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTheme(R.style.ActionSheetStyleiOS7);

                ActionSheet.createBuilder(JobForCNA.this, getSupportFragmentManager())
                        .setCancelButtonTitle("Cancel")
                        .setOtherButtonTitles("Report", "Delete this application")
                        .setCancelableOnTouchOutside(true)
                        .setListener(new ActionSheet.ActionSheetListener() {
                            @Override
                            public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

                            }

                            @Override
                            public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                                /*if(index == 0){
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    intent.setData(Uri.parse("tel:" + global.getUser().getPhone()));
                                    context.startActivity(intent);
                                }*/
                                if (index == 0) {
                                    API api = APIFactory.createAPI();
                                    SomeRandomeClass.AddToList("userid", ((Global) getApplication()).getCurrentUser().getId());
                                    SomeRandomeClass.AddToList("jobid", job.getId());
                                    Call call = api.reportJob(SomeRandomeClass.GetData());
                                    call.enqueue(new Callback() {
                                        @Override
                                        public void onResponse(Call call, Response response) {
                                            Toast.makeText(JobForCNA.this, "Job was succesfully reported.",
                                                    Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onFailure(Call call, Throwable t) {

                                        }
                                    });
                                } else if (index == 1) {
                                    //TODO: DELETE APPLICATION

                                    SomeRandomeClass.AddToList("userid", ((Global) getApplicationContext()).getCurrentUser().getId());
                                    SomeRandomeClass.AddToList("application_id", applicationId);
                                    final API api = APIFactory.createAPI();
                                    Call<Void> call = api.applicationRemove(SomeRandomeClass.GetData());
                                    call.enqueue(new Callback<Void>() {
                                        @Override
                                        public void onResponse(Call<Void> call, Response<Void> response) {
                                            SomeRandomeClass.AddToList("userid", ((Global) getApplicationContext()).getCurrentUser().getId());
                                            Call<ApplicationsIndResponse> callx = api.myapplications(SomeRandomeClass.GetData());
                                            callx.enqueue(new Callback<ApplicationsIndResponse>() {
                                                @Override
                                                public void onResponse(Call<ApplicationsIndResponse> callx, Response<ApplicationsIndResponse> response) {
                                                    checkApplied();
                                                }

                                                @Override
                                                public void onFailure(Call<ApplicationsIndResponse> callx, Throwable t) {


                                                }
                                            });

                                        }

                                        @Override
                                        public void onFailure(Call<Void> call, Throwable t) {

                                        }
                                    });
                                }


                            }
                        }).show();
            }
        });

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SomeRandomeClass.AddToList("userid", ((Global) getApplicationContext()).getCurrentUser().getId());
                SomeRandomeClass.AddToList("application_id", applicationId);
                API api = APIFactory.createAPI();
                Call call = api.applicationAccept(SomeRandomeClass.GetData());
                final Animation out = new AlphaAnimation(1.0f, 0.0f);
                out.setDuration(200);
                out.setFillAfter(true);
                decision.startAnimation(out);

                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {

                        decision.setVisibility(View.GONE);
                        accepted.setVisibility(View.VISIBLE);
                        final Animation out = new AlphaAnimation(0.0f, 1.0f);
                        out.setDuration(200);
                        out.setFillAfter(true);
                        out.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                new java.util.Timer().schedule(
                                        new java.util.TimerTask() {
                                            @Override
                                            public void run() {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        final Animation out = new AlphaAnimation(1.0f, 0.0f);
                                                        out.setDuration(200);
                                                        out.setFillAfter(true);
                                                        out.setAnimationListener(new Animation.AnimationListener() {
                                                            @Override
                                                            public void onAnimationStart(Animation animation) {

                                                            }

                                                            @Override
                                                            public void onAnimationEnd(Animation animation) {
                                                                accepted.setVisibility(View.GONE);
                                                                final Animation out = new AlphaAnimation(0.0f, 1.0f);
                                                                out.setDuration(200);
                                                                out.setFillAfter(true);
                                                                out.setAnimationListener(new Animation.AnimationListener() {
                                                                    @Override
                                                                    public void onAnimationStart(Animation animation) {
                                                                        after_answer.setVisibility(View.VISIBLE);
                                                                    }

                                                                    @Override
                                                                    public void onAnimationEnd(Animation animation) {
                                                                    }

                                                                    @Override
                                                                    public void onAnimationRepeat(Animation animation) {

                                                                    }
                                                                });
                                                                after_answer.startAnimation(out);
                                                            }

                                                            @Override
                                                            public void onAnimationRepeat(Animation animation) {

                                                            }
                                                        });

                                                        accepted.startAnimation(out);
                                                    }
                                                });
                                            }
                                        },
                                        3000
                                );
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        accepted.startAnimation(out);

                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {

                    }
                });
            }
        });

        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SomeRandomeClass.AddToList("userid", ((Global) getApplicationContext()).getCurrentUser().getId());
                SomeRandomeClass.AddToList("application_id", applicationId);
                final API api = APIFactory.createAPI();
                Call call = api.applicationRefuse(SomeRandomeClass.GetData());
                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        SomeRandomeClass.AddToList("userid", ((Global) getApplicationContext()).getCurrentUser().getId());
                        Call<ApplicationsIndResponse> callx = api.myapplications(SomeRandomeClass.GetData());
                        callx.enqueue(new Callback<ApplicationsIndResponse>() {
                            @Override
                            public void onResponse(Call<ApplicationsIndResponse> callx, Response<ApplicationsIndResponse> response) {
                                checkApplied();
                            }

                            @Override
                            public void onFailure(Call<ApplicationsIndResponse> callx, Throwable t) {


                            }
                        });

                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {

                    }
                });

            }
        });

        sendMess.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        sendMess.setBackground(getResources().getDrawable(R.drawable.round_blue_active));
                        sendMess.setTextColor(getResources().getColor(R.color.blueHighlighted));
                        break;
                    case MotionEvent.ACTION_UP: {
                        sendMess.setBackground(getResources().getDrawable(R.drawable.round_blue));
                        sendMess.setTextColor(0xFFFFFFFF);
                        if (mess.getText().toString().length() == 0) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(JobForCNA.this);
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
                            SomeRandomeClass.AddToList("toUser", job.getByUser());
                            SomeRandomeClass.AddToList("text", mess.getText().toString());
                            Call call = api.sendmessage(SomeRandomeClass.GetData());
                            call.enqueue(new Callback() {
                                @Override
                                public void onResponse(Call call, Response response) {
                                    mess.setText("");
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

    private void checkApplied() {
        SomeRandomeClass.AddToList("jobid", job.getId());
        SomeRandomeClass.AddToList("user", ((Global) getApplication()).getCurrentUser().getId());

        Call<CheckResponse> call = api.checkCanApply(SomeRandomeClass.GetData());
        call.enqueue(new Callback<CheckResponse>() {
            @Override
            public void onResponse(Call<CheckResponse> call, Response<CheckResponse> response) {
                applicationId = response.body().getError();
                if (response.body().getResponse().equals("5")) {
                    apply.setVisibility(View.VISIBLE);
                    waitingForAnAnswer.setVisibility(View.GONE);
                    decision.setVisibility(View.GONE);
                    declinedLayout.setVisibility(View.GONE);
                    after_answer.setVisibility(View.GONE);
                } else if (response.body().getResponse().equals("0")) {
                    apply.setVisibility(View.GONE);
                    waitingForAnAnswer.setVisibility(View.VISIBLE);
                    decision.setVisibility(View.GONE);
                    declinedLayout.setVisibility(View.GONE);
                    after_answer.setVisibility(View.GONE);
                } else if (response.body().getResponse().equals("1")) {
                    //TODO: ACCEPTED
                    apply.setVisibility(View.GONE);
                    waitingForAnAnswer.setVisibility(View.GONE);
                    decision.setVisibility(View.GONE);
                    declinedLayout.setVisibility(View.GONE);
                    after_answer.setVisibility(View.VISIBLE);
                } else if (response.body().getResponse().equals("2")) {
                    //TODO: DECLINED
                    apply.setVisibility(View.GONE);
                    waitingForAnAnswer.setVisibility(View.GONE);
                    decision.setVisibility(View.GONE);
                    declinedLayout.setVisibility(View.VISIBLE);
                    after_answer.setVisibility(View.GONE);
                } else if (response.body().getResponse().equals("3")) {
                    //TODO: DECISION
                    apply.setVisibility(View.GONE);
                    waitingForAnAnswer.setVisibility(View.GONE);
                    decision.setVisibility(View.VISIBLE);
                    declinedLayout.setVisibility(View.GONE);
                    after_answer.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<CheckResponse> call, Throwable t) {

            }
        });
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
