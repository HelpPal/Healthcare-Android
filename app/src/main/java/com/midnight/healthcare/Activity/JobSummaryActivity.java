package com.midnight.healthcare.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.midnight.healthcare.API.API;
import com.midnight.healthcare.API.APIFactory;
import com.midnight.healthcare.API.Day;
import com.midnight.healthcare.API.Jobs;
import com.midnight.healthcare.API.Nurse;
import com.midnight.healthcare.Global;
import com.midnight.healthcare.Models.RegisterModel;
import com.midnight.healthcare.R;
import com.midnight.healthcare.SomeRandomeClass;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobSummaryActivity extends MainActivity {
    Jobs item;
    Nurse nurse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_summary);

        setStatusBarBlue();

        ((RelativeLayout) findViewById(R.id.exit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        nurse = (Nurse) intent.getSerializableExtra("nurse");
        final int type = intent.getIntExtra("type", -1);

        item = (Jobs) intent.getSerializableExtra("job");

        TextView title = (TextView) findViewById(R.id.summ_job_title);
        TextView price = (TextView) findViewById(R.id.summ_job_price);
        TextView desc = (TextView) findViewById(R.id.summ_job_desc);
        TextView info = (TextView) findViewById(R.id.summ_job_info);
        Button s = (Button) findViewById(R.id.job_summ_s);
        Button m = (Button) findViewById(R.id.job_summ_m);
        Button t = (Button) findViewById(R.id.job_summ_t);
        Button w = (Button) findViewById(R.id.job_summ_w);
        Button th = (Button) findViewById(R.id.job_summ_th);
        Button f = (Button) findViewById(R.id.job_summ_f);
        Button sa = (Button) findViewById(R.id.job_summ_sa);
        final TextView location = (TextView) findViewById(R.id.summ_job_location);

        List<Day> list = item.getDays();

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
        info.setText(item.getTime_desc());
        if(type == 0)
        location.setText(item.getLocation().getCity() + ", "+item.getLocation().getState());
        else{
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Geocoder gcd = new Geocoder(JobSummaryActivity.this);
                    List<Address> addresses = new ArrayList<>();
                    try {
                        addresses = gcd.getFromLocation(Double.valueOf(item.getLat()), Double.valueOf(item.getLongitude()), 1);
                        final String address = addresses.get(0).getAddressLine(0);
                        final String city = addresses.get(0).getAddressLine(1);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                location.setText(address + ", " + city);
                            }
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        title.setText(item.getTitle());
        if(item.getMinPrice().equals(item.getMaxPrice())) {
            price.setText("$" + item.getMaxPrice() + "/h");
        }
        else{
            price.setText("$" + item.getMinPrice() + "-$" + item.getMaxPrice() + "/h");
        }
        desc.setText(item.getInformation());
        final Button send = (Button) findViewById(R.id.send_offer);

        final Button cancel = (Button) findViewById(R.id.cancel_offer);

        cancel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        cancel.setBackground(getResources().getDrawable(R.drawable.round_blue_active));
                        break;
                    case MotionEvent.ACTION_UP: {
                        cancel.setBackground(getResources().getDrawable(R.drawable.round_blue_light));
                        finish();
                        break;
                    }
                }
                return false;
            }
        });

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

                        final ProgressDialog dialog = ProgressDialog.show(JobSummaryActivity.this, "",
                                "Loading. Please wait...", true);

                        if(type == 0) {


                            API api = APIFactory.createAPI();
                            SomeRandomeClass.AddToList("userid", nurse.getId());
                            SomeRandomeClass.AddToList("jobid", item.getId());
                            SomeRandomeClass.AddToList("invite", "1");
                            Call call = api.sendJobTouser(SomeRandomeClass.GetData());
                            call.enqueue(new Callback() {
                                @Override
                                public void onResponse(Call call, Response response) {
                                    dialog.cancel();
                                    Intent myIntent = new Intent(JobSummaryActivity.this, AplicationsIndividulaActivity.class);
                                    startActivity(myIntent);
                                }

                                @Override
                                public void onFailure(Call call, Throwable t) {

                                }
                            });
                        }

                        else{
                            API api = APIFactory.createAPI();
                            SomeRandomeClass.AddToList("userid", item.getByUser());
                            SomeRandomeClass.AddToList("title", item.getTitle());
                            SomeRandomeClass.AddToList("lat", item.getLat());
                            SomeRandomeClass.AddToList("long", item.getLongitude());
                            SomeRandomeClass.AddToList("min_price", item.getMinPrice());
                            SomeRandomeClass.AddToList("max_price", item.getMaxPrice());

                            String days = "";
                            List<Integer> dayInt = new ArrayList<Integer>();
                            for(Day d : item.getDays()){
                                dayInt.add(Integer.valueOf(d.getDay()));
                            }
                            if(dayInt.contains(7)) days += "1,";
                            else days += "0,";
                            if(dayInt.contains(2)) days += "1,";
                            else days += "0,";
                            if(dayInt.contains(3)) days += "1,";
                            else days += "0,";
                            if(dayInt.contains(4)) days += "1,";
                            else days += "0,";
                            if(dayInt.contains(5)) days += "1,";
                            else days += "0,";
                            if(dayInt.contains(6)) days += "1,";
                            else days += "0,";
                            if(dayInt.contains(1)) days += "1,";
                            else days += "0,";

                            SomeRandomeClass.AddToList("days", days);
                            SomeRandomeClass.AddToList("repeate", item.getRepate());
                            SomeRandomeClass.AddToList("hours", item.getHours());
                            SomeRandomeClass.AddToList("avalabil", item.getAvalable());
                            SomeRandomeClass.AddToList("informations", item.getInformation());
                            SomeRandomeClass.AddToList("private", "1");
                            Call<Jobs> call = api.addjobSpecial(SomeRandomeClass.GetData());
                            call.enqueue(new Callback<Jobs>() {
                                @Override
                                public void onResponse(Call<Jobs> call, Response<Jobs> response) {
                                    if(response != null){
                                        API api = APIFactory.createAPI();
                                        SomeRandomeClass.AddToList("userid", nurse.getId());
                                        SomeRandomeClass.AddToList("jobid", response.body().getId());
                                        SomeRandomeClass.AddToList("invite", "1");
                                        Call call2 = api.sendJobTouser(SomeRandomeClass.GetData());
                                        call2.enqueue(new Callback() {
                                            @Override
                                            public void onResponse(Call call, Response response) {
                                                dialog.cancel();
                                                Intent myIntent = new Intent(JobSummaryActivity.this, AplicationsIndividulaActivity.class);
                                                startActivity(myIntent);
                                            }

                                            @Override
                                            public void onFailure(Call call, Throwable t) {

                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onFailure(Call<Jobs> call, Throwable t) {

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

    public void setStatusBarBlue(){
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(R.color.blueHighlighted));
        }
    }
}
