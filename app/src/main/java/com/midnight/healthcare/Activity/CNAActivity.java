package com.midnight.healthcare.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.midnight.healthcare.API.API;
import com.midnight.healthcare.API.APIFactory;
import com.midnight.healthcare.API.Messegess;
import com.midnight.healthcare.API.Nurse;
import com.midnight.healthcare.API.Skill;
import com.midnight.healthcare.Global;
import com.midnight.healthcare.R;
import com.midnight.healthcare.SomeRandomeClass;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CNAActivity extends MainActivity {
    ImageView img;
    TextView name;
    TextView info;
    TextView distance;
    TextView exp;
    TextView price;
    TextView time;
    TextView desc;
    TextView reportButton;
    RatingBar ratingBar;
    Button rate;
    Button hire;
    EditText mess;
    Button send;
    ProgressDialog progress;
    boolean clicked;

    API api = APIFactory.createAPI();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cna);

        setStatusBarBlue();

        Intent intent = getIntent();
        ((RelativeLayout) findViewById(R.id.exit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        img = (ImageView) findViewById(R.id.cna_img);
        name = (TextView) findViewById(R.id.cna_name);
        info = (TextView) findViewById(R.id.cna_info);
        distance = (TextView) findViewById(R.id.cna_distance);
        exp = (TextView) findViewById(R.id.cna_exp);
        price = (TextView) findViewById(R.id.cna_price);
        time = (TextView) findViewById(R.id.cna_time);
        desc = (TextView) findViewById(R.id.cna_descritpion);
        hire = (Button) findViewById(R.id.cna_hire);
        send = (Button) findViewById(R.id.cna_send);
        rate = (Button) findViewById(R.id.rate);
        mess = (EditText) findViewById(R.id.cna_mess);
        reportButton = (TextView) findViewById(R.id.reportButton);

        final Nurse nurse = (Nurse) intent.getSerializableExtra("nurse");

        hire.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        hire.setBackground(getResources().getDrawable(R.drawable.round_blue_active));
                        hire.setTextColor(getResources().getColor(R.color.blueHighlighted));
                        break;
                    case MotionEvent.ACTION_UP: {
                        hire.setBackground(getResources().getDrawable(R.drawable.round_blue));
                        hire.setTextColor(0xFFFFFFFF);
                        Intent intent = new Intent(CNAActivity.this, HireCNAActivity.class);
                        intent.putExtra("nurse", nurse);
                        startActivity(intent);
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
                            progress = new ProgressDialog(CNAActivity.this);
                            progress.setMessage("Loading, please wait...");
                            progress.setCancelable(false);
                            progress.show();
                            clicked = true;
                            SomeRandomeClass.AddToList("type", "1");
                            SomeRandomeClass.AddToList("userid", ((Global) getApplication()).getCurrentUser().getId());
                            SomeRandomeClass.AddToList("toUser", nurse.getId());
                            SomeRandomeClass.AddToList("rating", String.valueOf(ratingBar.getRating()));
                            Call call = api.sendJobTouser(SomeRandomeClass.GetData());
                            call.enqueue(new Callback() {
                                @Override
                                public void onResponse(Call call, Response response) {
                                    if (progress != null) progress.dismiss();
                                    clicked = false;
                                    Toast.makeText(CNAActivity.this, "Rating added", Toast.LENGTH_LONG).show();

                                }

                                @Override
                                public void onFailure(Call call, Throwable t) {
                                    Toast.makeText(CNAActivity.this, "Error: " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
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

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                // You can pass your own memory cache implementation
                .discCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .build();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .displayer(new RoundedBitmapDisplayer(350)) //rounded corner bitmap
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
        imageLoader.displayImage("http://104.131.152.91/health-care/" + nurse.getProfileImg(), img, options);
        name.setText(nurse.getFirstName() + " " + nurse.getLastName());
        reportButton.setText("Report " + nurse.getFirstName() + "'s profile");
        if (nurse.getGender().equals("1"))
            info.setText("Male, " + nurse.getYears() + "yo, city, state");
        else
            info.setText("Female, " + nurse.getYears() + "yo, city, state");
        distance.setText(nurse.getDistance() + " mi");
        if (nurse.getExperience().equals("1"))
            exp.setText("Less then 1 year experience");
        if (nurse.getExperience().equals("2"))
            exp.setText("2-5 years experience");
        if (nurse.getExperience().equals("3"))
            exp.setText("5-7 year experience");
        if (nurse.getExperience().equals("4"))
            exp.setText("7-10 year experience");
        if (nurse.getExperience().equals("5"))
            exp.setText("10-15 year experience");
        if (nurse.getExperience().equals("6"))
            exp.setText("15-20 year experience");
        if (nurse.getExperience().equals("7"))
            exp.setText("More than 20 year experience");

        price.setText("$" + nurse.getPriceMin() + "-$" + nurse.getPriceMax());
        if (nurse.getAvailableTime().equals("0"))
            time.setText("Part time");
        else
            time.setText("Full time");


        List<String> skillzzz = new ArrayList<>();
        for (Skill skill : nurse.getSkills())
            skillzzz.add(skill.getName());
        LinearLayout skillContainer = (LinearLayout) findViewById(R.id.skill_container);
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 70);
        llp.setMargins(10, 0, 10, 0);
        LinearLayout.LayoutParams llp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        llp.setMargins(5, 10, 10, 5);
        //  llp2.gravity = Gravity.CENTER_HORIZONTAL;
        LinearLayout line2 = new LinearLayout(this);
        line2.setOrientation(LinearLayout.HORIZONTAL);
        line2.setLayoutParams(llp2);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width3 = size.x;

        int entry = 0;
        String chr = "";
        for (int k = 0; k < skillzzz.size(); k++) {

            TextView skill = new TextView(this);
            skill.setLayoutParams(llp);
            skill.setBackgroundResource(R.drawable.round_blue_skills);
            skill.setTextColor(Color.parseColor("#FFFFFFFF"));
            skill.setText(skillzzz.get(k));
            skill.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
            Rect bounds = new Rect();
            Paint textPaint = skill.getPaint();
            textPaint.getTextBounds(skillzzz.get(k), 0, skillzzz.get(k).length(), bounds);
            int width = bounds.width();
            Rect bounds2 = new Rect();
            Paint textPaint2 = skill.getPaint();
            textPaint2.getTextBounds(chr, 0, chr.length(), bounds2);
            int width2 = bounds2.width();
            if (width3 - width2 * 1.6 > width * 1.6) {
                line2.addView(skill);
                chr = chr + skillzzz.get(k);
            } else {

                skillContainer.addView(line2, entry);
                entry++;
                chr = "";
                line2 = new LinearLayout(this);
                line2.setOrientation(LinearLayout.HORIZONTAL);
                line2.setLayoutParams(llp2);

                line2.addView(skill);
            }

        }
        if (skillContainer.getChildAt(entry) == null)
            skillContainer.addView(line2, entry);

        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SomeRandomeClass.AddToList("userid", ((Global) getApplication()).getCurrentUser().getId());
                SomeRandomeClass.AddToList("reported", nurse.getId());
                Call call = api.reportUser(SomeRandomeClass.GetData());
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


        desc.setText(nurse.getDescription());
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
                        if (mess.getText().toString().length() == 0) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(CNAActivity.this);
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
                            SomeRandomeClass.AddToList("toUser", nurse.getId());
                            SomeRandomeClass.AddToList("text", mess.getText().toString());
                            Call call = api.sendmessage(SomeRandomeClass.GetData());
                            call.enqueue(new Callback() {
                                @Override
                                public void onResponse(Call call, Response response) {
                                    hideKeyboard();
                                    Messegess mess1 = new Messegess();
                                    mess1.setPartner(nurse.getId());
                                    Intent conversation = new Intent(CNAActivity.this, ConversationActivity.class);
                                    conversation.putExtra("mess", mess1);
                                    startActivity(conversation);
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

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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

