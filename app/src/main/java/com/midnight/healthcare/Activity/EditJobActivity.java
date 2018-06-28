package com.midnight.healthcare.Activity;

import android.app.Activity;
import android.app.AlertDialog;
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

import com.midnight.healthcare.API.API;
import com.midnight.healthcare.API.APIFactory;
import com.midnight.healthcare.API.Day;
import com.midnight.healthcare.API.Jobs;
import com.midnight.healthcare.Global;
import com.midnight.healthcare.R;
import com.midnight.healthcare.SomeRandomeClass;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditJobActivity extends MainActivity {
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
    Button del;

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

Jobs job;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_job);

        setStatusBarBlue();

        ((RelativeLayout) findViewById(R.id.exit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Intent intent = getIntent();
        job = (Jobs) intent.getSerializableExtra("job");
        titlu = (EditText) findViewById(R.id.job_title);
        titlu.setText(job.getTitle());
        locationBtn = (LinearLayout) findViewById(R.id.job_location);
        locationTxt = (TextView) findViewById(R.id.job_adr);
        locationTxt.setText(job.getLocation().getCity() + ", "+job.getLocation().getState());
        priceMax  = (EditText) findViewById(R.id.job_max);
        priceMax.setText(job.getMaxPrice());
        priceMin = (EditText) findViewById(R.id.job_min);
        priceMin.setText(job.getMinPrice());
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
        hour = Integer.valueOf(job.getHours());
        lat = Double.valueOf(job.getLat());
        lg = Double.valueOf(job.getLongitude());
        //fullTime = (CheckBox) findViewById(R.id.job_full_time);
        //partTime = (CheckBox) findViewById(R.id.job_part_time);
        description = (EditText) findViewById(R.id.job_descript);
        description.setText(job.getInformation());
        send = (Button) findViewById(R.id.job_done_btn);
        del = (Button) findViewById(R.id.job_delete_btn);

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
                        if(description.getText().toString().length() != 0 && priceMin.getText().toString().length() != 0 && titlu.getText().toString().length() != 0) {
                            int price;
                            if(priceMax.getText().toString().length() == 0) price = 0;
                            else price = Integer.valueOf(priceMax.getText().toString());
                            if (Integer.valueOf(priceMin.getText().toString()) <= price || priceMax.getText().toString().equals("")) {
                                if(Integer.valueOf(priceMin.getText().toString()) >= 15 && price <= 120) {
                                    API api = APIFactory.createAPI();
                                    SomeRandomeClass.AddToList("id", job.getId());
                                    SomeRandomeClass.AddToList("title", titlu.getText().toString());
                                    SomeRandomeClass.AddToList("lat", lat.toString());
                                    SomeRandomeClass.AddToList("long", lg.toString());
                                    SomeRandomeClass.AddToList("min_price", priceMin.getText().toString());
                                    if (priceMax.getText().toString().equals("")) {
                                        SomeRandomeClass.AddToList("max_price", priceMin.getText().toString());
                                    } else {
                                        SomeRandomeClass.AddToList("max_price", priceMax.getText().toString());
                                    }
                                    SomeRandomeClass.AddToList("days", s1 + "," + m1 + "," + t1 + "," + w1 + "," + th1 + "," + f1 + "," + sa1);
                                    SomeRandomeClass.AddToList("repeate", repeate);
                                    SomeRandomeClass.AddToList("hours", hour.toString());
                                    SomeRandomeClass.AddToList("avalabil", avalabil);
                                    SomeRandomeClass.AddToList("informations", description.getText().toString());
                                    Call<Void> call = api.editjob(SomeRandomeClass.GetData());
                                    call.enqueue(new Callback<Void>() {
                                        @Override
                                        public void onResponse(Call<Void> call, Response<Void> response) {
                                            finish();
                                        }

                                        @Override
                                        public void onFailure(Call<Void> call, Throwable t) {

                                        }
                                    });
                                }
                                else{
                                    AlertDialog.Builder builder = new AlertDialog.Builder(EditJobActivity.this);
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
                                AlertDialog.Builder builder = new AlertDialog.Builder(EditJobActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(EditJobActivity.this);
                            builder.setTitle("Error!")
                                    .setMessage("There must be a description of your job!")
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

        del.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        del.setBackground(getResources().getDrawable(R.drawable.round_blue_active));
                        break;
                    case MotionEvent.ACTION_UP: {
                        del.setBackground(getResources().getDrawable(R.drawable.round_blue_light));
                        API api = APIFactory.createAPI();
                        SomeRandomeClass.AddToList("jobid" , job.getId());
                        SomeRandomeClass.AddToList("userid" , ((Global)getApplication()).getCurrentUser().getId());

                        Call<Void> call = api.deletejob(SomeRandomeClass.GetData());
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                finish();
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {

                            }
                        });
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

                hourChangerText.setVisibility(View.GONE);
                hourChanger.setVisibility(View.GONE);

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

                hourChangerText.setVisibility(View.VISIBLE);
                hourChanger.setVisibility(View.VISIBLE);

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
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(EditJobActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        if (job.getAvalable().equals("1"))
        {
            fullTimeImage.setImageDrawable(getResources().getDrawable(R.drawable.checked_time));
            fullTimeText.setTextColor(getResources().getColor(R.color.blueHighlighted));
            partTimeImage.setImageDrawable(getResources().getDrawable(R.drawable.uncheck));
            partTimeText.setTextColor(0xFF000000);
        } else {
            partTimeImage.setImageDrawable(getResources().getDrawable(R.drawable.checked_time));
            partTimeText.setTextColor(getResources().getColor(R.color.blueHighlighted));
            fullTimeImage.setImageDrawable(getResources().getDrawable(R.drawable.uncheck));
            fullTimeText.setTextColor(0xFF000000);
        }


        if (job.getRepate().equals("1")) {
            repeatedlyText.setTextColor(getResources().getColor(R.color.blueHighlighted));
            repeatedlyImage.setImageDrawable(getResources().getDrawable(R.drawable.checked_time));
            oneTimeImage.setImageDrawable(getResources().getDrawable(R.drawable.uncheck));
            oneTimeText.setTextColor(0xFF000000);
        }
        else {
            oneTimeText.setTextColor(getResources().getColor(R.color.blueHighlighted));
            oneTimeImage.setImageDrawable(getResources().getDrawable(R.drawable.checked_time));
            repeatedlyImage.setImageDrawable(getResources().getDrawable(R.drawable.uncheck));
            repeatedlyText.setTextColor(0xFF000000);
        }

        List<Day> list = job.getDays();
        for (Day d : list) {
            if (d.getDay().equals(getString(R.string.sunday))) {
                s.setBackgroundResource(R.drawable.s_normal);
                s1 = "1";
            }
            if (d.getDay().equals(getString(R.string.saturday))){
                sa.setBackgroundResource(R.drawable.sa_tugle);
                sa1 = "1";
            }
            if (d.getDay().equals(getString(R.string.monday))) {
                m.setBackgroundColor(Color.parseColor("#90bbf2"));
                m1= "1";
            }
            if (d.getDay().equals(getString(R.string.tuesday))) {
                t.setBackgroundColor(Color.parseColor("#90bbf2"));
                t1 = "1";
            }
            if (d.getDay().equals(getString(R.string.wednesday))) {
                w.setBackgroundColor(Color.parseColor("#90bbf2"));
                w1 = "1";
            }
            if (d.getDay().equals(getString(R.string.thursday))) {
                th.setBackgroundColor(Color.parseColor("#90bbf2"));
                th1 = "1";
            }
            if (d.getDay().equals(getString(R.string.friday))) {
                f.setBackgroundColor(Color.parseColor("#90bbf2"));
                f1 = "1";
            }

        }


    }




    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
