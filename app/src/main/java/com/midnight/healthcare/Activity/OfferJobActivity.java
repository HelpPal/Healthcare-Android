package com.midnight.healthcare.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.midnight.healthcare.API.Day;
import com.midnight.healthcare.API.Jobs;
import com.midnight.healthcare.API.Nurse;
import com.midnight.healthcare.Global;
import com.midnight.healthcare.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.ArrayList;
import java.util.List;

public class OfferJobActivity extends MainActivity {
    EditText titlu;
    LinearLayout locationBtn;
    TextView locationTxt;
    EditText priceMin;
    EditText priceMax;
    Button s;
    Button m;
    Button t;
    Button w;
    Button th;
    Button f;
    Button sa;
    //CheckBox repitatly;
    //CheckBox once;
    ImageView minus;
    ImageView plus;
    TextView hours;
    //CheckBox fullTime;
    //CheckBox partTime;
    EditText description;
    int PLACE_PICKER_REQUEST = 1;
    Double lat;
    Double lg;
    Integer hour = 1;
    String s1 = "0";
    String m1 = "0";
    String t1 = "0";
    String w1 = "0";
    String th1 = "0";
    String f1 = "0";
    String sa1 = "0";
    String repeate = "1";
    String avalabil = "1";
    Button send;
    Nurse nurse;
    TextView name;

    TextView hourChangerText;
    RelativeLayout hourChanger;

    LinearLayout repeatedlyButton;
    LinearLayout oneTimeButton;
    LinearLayout fullTimeButton;
    LinearLayout partTimeButton;

    ImageView repeatedlyImage;
    ImageView oneTimeImage;
    ImageView fullTimeImage;
    ImageView partTimeImage;

    TextView repeatedlyText;
    TextView oneTimeText;
    TextView fullTimeText;
    TextView partTimeText;

    TextView minusText;
    TextView plusText;

    ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_job);

        setStatusBarBlue();

        ((RelativeLayout) findViewById(R.id.exit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Intent intent = getIntent();
        nurse = (Nurse) intent.getSerializableExtra("nurse");
        titlu = (EditText) findViewById(R.id.job_title);
        locationBtn = (LinearLayout) findViewById(R.id.job_location);
        locationTxt = (TextView) findViewById(R.id.job_adr);
        priceMax  = (EditText) findViewById(R.id.job_max);
        priceMin = (EditText) findViewById(R.id.job_min);
        s = (Button) findViewById(R.id.job_s);
        m= (Button) findViewById(R.id.job_m);
        t = (Button) findViewById(R.id.job_t);
        w = (Button) findViewById(R.id.job_w);
        th = (Button) findViewById(R.id.job_th);
        f = (Button) findViewById(R.id.job_f);
        sa = (Button) findViewById(R.id.job_sa);
        //repitatly = (CheckBox) findViewById(R.id.job_repeat);
        //once = (CheckBox) findViewById(R.id.jobe_onetime);
        minus = (ImageView) findViewById(R.id.job_minus);
        plus = (ImageView) findViewById(R.id.job_plus);
        hours = (TextView) findViewById(R.id.job_h);
        //fullTime = (CheckBox) findViewById(R.id.job_full_time);
        //partTime = (CheckBox) findViewById(R.id.job_part_time);
        description = (EditText) findViewById(R.id.job_descript);
        send = (Button) findViewById(R.id.job_add_btn);
        name  = (TextView) findViewById(R.id.cna_name_for_offer);

        hourChangerText = (TextView) findViewById(R.id.hourChangerText);
        hourChanger = (RelativeLayout) findViewById(R.id.hourChanger);

        repeatedlyButton = (LinearLayout) findViewById(R.id.repeatedlyButton);
        oneTimeButton = (LinearLayout) findViewById(R.id.oneTimeButton);
        fullTimeButton = (LinearLayout) findViewById(R.id.fullTimeButton);
        partTimeButton = (LinearLayout) findViewById(R.id.partTimeButton);

        repeatedlyImage = (ImageView) findViewById(R.id.repeatedlyImage);
        oneTimeImage = (ImageView) findViewById(R.id.oneTimeImage);
        fullTimeImage = (ImageView) findViewById(R.id.fullTimeImage);
        partTimeImage = (ImageView) findViewById(R.id.partTimeImage);

        repeatedlyText = (TextView) findViewById(R.id.repeatedlyText);
        oneTimeText = (TextView) findViewById(R.id.oneTimeText);
        fullTimeText = (TextView) findViewById(R.id.fullTimeText);
        partTimeText = (TextView) findViewById(R.id.partTimeText);

        minusText = (TextView) findViewById(R.id.minusText);
        plusText = (TextView) findViewById(R.id.plusText);


        name.setText(nurse.getFirstName()+" "+nurse.getLastName().substring(0 , 1) +".");

        send.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        send.setBackground(getResources().getDrawable(R.drawable.round_blue_active));
                        send.setTextColor(getResources().getColor(R.color.blueHighlighted));
                        break;
                    case MotionEvent.ACTION_UP: {
                        send.setBackground(getResources().getDrawable(R.drawable.round_blue));
                        send.setTextColor(0xFFFFFFFF);
                        if (titlu.getText().toString().length() != 0 && lat.toString().length() != 0 && lg.toString().length() != 0 && priceMin.getText().toString().length() != 0) {
                            if(description.getText().toString().length() != 0) {
                                int price;
                                if(priceMax.getText().toString().length() == 0) price = 0;
                                else price = Integer.valueOf(priceMax.getText().toString());
                                if(Integer.valueOf(priceMin.getText().toString()) <= price || priceMax.getText().toString().length() == 0) {
                                    if(Integer.valueOf(priceMin.getText().toString()) >= 15 && price <= 120) {

                                        ((Global) getApplication()).setJob(new Jobs());
                                        ((Global) getApplication()).getJob().setByUser(((Global) getApplication()).getCurrentUser().getId());
                                        ((Global) getApplication()).getJob().setTitle(titlu.getText().toString());
                                        ((Global) getApplication()).getJob().setLat(lat.toString());
                                        ((Global) getApplication()).getJob().setLongitude(lg.toString());
                                        ((Global) getApplication()).getJob().setMinPrice(priceMin.getText().toString());

                                        if (priceMax.getText().toString().length() == 0) {
                                            ((Global) getApplication()).getJob().setMaxPrice(priceMin.getText().toString());
                                        } else {
                                            ((Global) getApplication()).getJob().setMaxPrice(priceMax.getText().toString());
                                        }

                                        List<Day> newDaysList = new ArrayList<>();
                                        if (s1.equals("1")) {
                                            Day day = new Day();
                                            day.setDay(getString(R.string.saturday));
                                            newDaysList.add(day);
                                        }
                                        if (m1.equals("1")) {
                                            Day day = new Day();
                                            day.setDay(getString(R.string.monday));
                                            newDaysList.add(day);
                                        }
                                        if (t1.equals("1")) {
                                            Day day = new Day();
                                            day.setDay(getString(R.string.tuesday));
                                            newDaysList.add(day);
                                        }
                                        if (w1.equals("1")) {
                                            Day day = new Day();
                                            day.setDay(getString(R.string.wednesday));
                                            newDaysList.add(day);
                                        }
                                        if (th1.equals("1")) {
                                            Day day = new Day();
                                            day.setDay(getString(R.string.thursday));
                                            newDaysList.add(day);
                                        }
                                        if (f1.equals("1")) {
                                            Day day = new Day();
                                            day.setDay(getString(R.string.friday));
                                            newDaysList.add(day);
                                        }
                                        if (sa1.equals("1")) {
                                            Day day = new Day();
                                            day.setDay(getString(R.string.sunday));
                                            newDaysList.add(day);
                                        }


                                        ((Global) getApplication()).getJob().setDays(newDaysList);
                                        ((Global) getApplication()).getJob().setRepate(repeate);
                                        ((Global) getApplication()).getJob().setHours(hour.toString());
                                        ((Global) getApplication()).getJob().setAvalable(avalabil);
                                        ((Global) getApplication()).getJob().setInformation(description.getText().toString());

                                        Intent intent = new Intent(OfferJobActivity.this, JobSummaryActivity.class);
                                        intent.putExtra("nurse", nurse);
                                        intent.putExtra("type", 1);
                                        intent.putExtra("job", ((Global) getApplication()).getJob());
                                        startActivity(intent);

                                    /*API api = APIFactory.createAPI();
                                    SomeRandomeClass.AddToList("userid", ((Global) getApplication()).getCurrentUser().getId());
                                    SomeRandomeClass.AddToList("title", titlu.getText().toString());
                                    SomeRandomeClass.AddToList("lat", lat.toString());
                                    SomeRandomeClass.AddToList("long", lg.toString());
                                    SomeRandomeClass.AddToList("min_price", priceMin.getText().toString());
                                    if(priceMax.getText().toString().length() == 0) {
                                        SomeRandomeClass.AddToList("max_price", priceMin.getText().toString());
                                    }
                                    else {
                                        SomeRandomeClass.AddToList("max_price", priceMax.getText().toString());
                                    }
                                    SomeRandomeClass.AddToList("days", s1 + "," + m1 + "," + t1 + "," + w1 + "," + th1 + "," + f1 + "," + sa1);
                                    SomeRandomeClass.AddToList("repeate", repeate);
                                    SomeRandomeClass.AddToList("hours", hour.toString());
                                    SomeRandomeClass.AddToList("avalabil", avalabil);
                                    SomeRandomeClass.AddToList("informations", description.getText().toString());
                                    SomeRandomeClass.AddToList("private", "1");
                                    Call<Jobs> call = api.addjobSpecial(SomeRandomeClass.GetData());
                                    call.enqueue(new Callback<Jobs>() {
                                        @Override
                                        public void onResponse(Call<Jobs> call, Response<Jobs> response) {
                                            Intent intent = new Intent(OfferJobActivity.this, JobSummaryActivity.class);
                                            intent.putExtra("nurse", nurse);
                                            intent.putExtra("job", response.body());
                                            startActivity(intent);
                                        }

                                        @Override
                                        public void onFailure(Call<Jobs> call, Throwable t) {

                                        }
                                    });*/
                                    }
                                    else{
                                        AlertDialog.Builder builder = new AlertDialog.Builder(OfferJobActivity.this);
                                        builder.setTitle("Error!")
                                                .setMessage("The minimal price can't be lower than 15$ and maximal price can't be higher than 120$!")
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
                                else{
                                    AlertDialog.Builder builder = new AlertDialog.Builder(OfferJobActivity.this);
                                    builder.setTitle("Error!")
                                            .setMessage("The maximal price can't be lower than the minimal price!")
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
                            else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(OfferJobActivity.this);
                                builder.setTitle("Error!")
                                        .setMessage("There must be a description of your offer!")
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
                        else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(OfferJobActivity.this);
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
                        }
                        break;
                    }
                }
                return false;
            }
        });

        repeatedlyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                repeatedlyText.setTextColor(getResources().getColor(R.color.blueHighlighted));
                repeatedlyImage.setImageDrawable(getResources().getDrawable(R.drawable.checked_time));
                oneTimeImage.setImageDrawable(getResources().getDrawable(R.drawable.uncheck));
                oneTimeText.setTextColor(0xFF000000);

                repeate = "1";
            }
        });

        oneTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oneTimeText.setTextColor(getResources().getColor(R.color.blueHighlighted));
                oneTimeImage.setImageDrawable(getResources().getDrawable(R.drawable.checked_time));
                repeatedlyImage.setImageDrawable(getResources().getDrawable(R.drawable.uncheck));
                repeatedlyText.setTextColor(0xFF000000);

                repeate = "0";
            }
        });

        fullTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullTimeImage.setImageDrawable(getResources().getDrawable(R.drawable.checked_time));
                fullTimeText.setTextColor(getResources().getColor(R.color.blueHighlighted));
                partTimeImage.setImageDrawable(getResources().getDrawable(R.drawable.uncheck));
                partTimeText.setTextColor(0xFF000000);

                hourChanger.setVisibility(View.GONE);
                hourChangerText.setVisibility(View.GONE);

                avalabil = "1";
            }
        });

        partTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                partTimeImage.setImageDrawable(getResources().getDrawable(R.drawable.checked_time));
                partTimeText.setTextColor(getResources().getColor(R.color.blueHighlighted));
                fullTimeImage.setImageDrawable(getResources().getDrawable(R.drawable.uncheck));
                fullTimeText.setTextColor(0xFF000000);

                hourChanger.setVisibility(View.VISIBLE);
                hourChangerText.setVisibility(View.VISIBLE);

                avalabil = "0";
            }
        });

        hours.setText(hour.toString());
        minus.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        minus.setImageDrawable(getResources().getDrawable(R.drawable.big_button_white));
                        minusText.setTextColor(getResources().getColor(R.color.blueHighlighted));
                        return true;
                    case MotionEvent.ACTION_UP: {
                        minus.setImageDrawable(getResources().getDrawable(R.drawable.blue_button_background_big));
                        minusText.setTextColor(0xFFFFFFFF);

                        if (hour!=1){
                            hour--;
                            hours.setText(hour.toString());

                        }

                        break;
                    }
                }
                return false;
            }
        });

        plus.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        plus.setImageDrawable(getResources().getDrawable(R.drawable.big_button_white));
                        plusText.setTextColor(getResources().getColor(R.color.blueHighlighted));
                        return true;
                    case MotionEvent.ACTION_UP: {
                        plus.setImageDrawable(getResources().getDrawable(R.drawable.blue_button_background_big));
                        plusText.setTextColor(0xFFFFFFFF);

                        if(hour != 24) {
                            hour++;
                            hours.setText(hour.toString());
                        }

                        break;
                    }
                }
                return false;
            }
        });

        priceMax.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    hideKeyboard();
                }
                return false;
            }
        });


m.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        if (m1.equals("0")){
            m1 = "1";
            m.setBackgroundColor(Color.parseColor("#90bbf2"));
            m.setTextColor(getResources().getColor(R.color.blueHighlighted));
        } else {
            m1 = "0";
            m.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
            m.setTextColor(getResources().getColor(R.color.textColor));
        }
    }
});
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (t1.equals("0")){
                    t1 = "1";
                    t.setBackgroundColor(Color.parseColor("#90bbf2"));
                    t.setTextColor(getResources().getColor(R.color.blueHighlighted));
                } else {
                    t1 = "0";
                    t.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                    t.setTextColor(getResources().getColor(R.color.textColor));
                }
            }
        });
        w.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (w1.equals("0")){
                    w1 = "1";
                    w.setBackgroundColor(Color.parseColor("#90bbf2"));
                    w.setTextColor(getResources().getColor(R.color.blueHighlighted));
                } else {
                    w1 = "0";
                    w.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                    w.setTextColor(getResources().getColor(R.color.textColor));
                }
            }
        });
        th.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (th1.equals("0")){
                    th1 = "1";
                    th.setBackgroundColor(Color.parseColor("#90bbf2"));
                    th.setTextColor(getResources().getColor(R.color.blueHighlighted));
                } else {
                    th1 = "0";
                    th.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                    th.setTextColor(getResources().getColor(R.color.textColor));
                }
            }
        });
        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (f1.equals("0")){
                    f1 = "1";
                    f.setBackgroundColor(Color.parseColor("#90bbf2"));
                    f.setTextColor(getResources().getColor(R.color.blueHighlighted));
                } else {
                    f1 = "0";
                    f.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                    f.setTextColor(getResources().getColor(R.color.textColor));
                }
            }
        });
        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (s1.equals("0")){
                    s1 = "1";
                    s.setBackgroundResource(R.drawable.s_normal);
                    s.setTextColor(getResources().getColor(R.color.blueHighlighted));
                } else {
                    s1 = "0";
                    s.setBackgroundResource(R.drawable.s);
                    s.setTextColor(getResources().getColor(R.color.textColor));
                }
            }
        });
        sa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sa1.equals("0")){
                    sa1 = "1";
                    sa.setBackgroundResource(R.drawable.sa_tugle);
                    sa.setTextColor(getResources().getColor(R.color.blueHighlighted));
                } else {
                    sa1 = "0";
                    sa.setBackgroundResource(R.drawable.sa);
                    sa.setTextColor(getResources().getColor(R.color.textColor));
                }
            }
        });
        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress = new ProgressDialog(OfferJobActivity.this);
                progress.setMessage("Loading, please wait...");
                progress.show();
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(OfferJobActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

    }




    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(progress != null) progress.dismiss();
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                locationTxt.setText(place.getAddress().toString());
                lat = place.getLatLng().latitude;
                lg = place.getLatLng().longitude;



            }
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

    public void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


}
