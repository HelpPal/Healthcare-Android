package com.midnight.healthcare.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.midnight.healthcare.API.API;
import com.midnight.healthcare.API.APIFactory;
import com.midnight.healthcare.API.JobResponse;
import com.midnight.healthcare.API.Jobs;
import com.midnight.healthcare.Adapters.MyJobAdapter;
import com.midnight.healthcare.GPSTracker;
import com.midnight.healthcare.Global;
import com.midnight.healthcare.R;
import com.midnight.healthcare.SomeRandomeClass;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyJobFeedActivity extends MainActivity {
    API api = APIFactory.createAPI();
    ListView listView;
    MyJobAdapter adapter;
    Button njob;
    Integer pageCount = 0;
    List<Jobs> listPro = new ArrayList<>();

    TextView noJobs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_job_feed);

        setStatusBarBlue();

        ((RelativeLayout) findViewById(R.id.exit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyJobFeedActivity.this, CNAFeedActivity.class);
                startActivity(intent);
            }
        });

        noJobs = (TextView) findViewById(R.id.noJobs);
        njob = (Button) findViewById(R.id.new_job_btn);
        listView = (ListView) findViewById(R.id.job_feed_list);

        njob.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        njob.setBackground(getResources().getDrawable(R.drawable.round_blue_active));
                        njob.setTextColor(getResources().getColor(R.color.blueHighlighted));
                        break;
                    case MotionEvent.ACTION_UP: {
                        njob.setBackground(getResources().getDrawable(R.drawable.round_blue));
                        njob.setTextColor(0xFFFFFFFF);
                        Intent myIntent = new Intent(MyJobFeedActivity.this, AddJobActivity.class);
                        startActivity(myIntent);
                        break;
                    }
                }
                return false;
            }
        });

        final ProgressDialog dialog = new ProgressDialog(MyJobFeedActivity.this);
        dialog.setMessage("Loading...");
        dialog.show();
        pageCount = 0;
        GPSTracker tracker = new GPSTracker(this);
        if (!tracker.canGetLocation()) {
            tracker.showSettingsAlert();
        } else {
            SomeRandomeClass.AddToList("lat", String.valueOf(tracker.getLatitude()));
            SomeRandomeClass.AddToList("long", String.valueOf(tracker.getLongitude()));
            SomeRandomeClass.AddToList("userid", ((Global) getApplication()).getCurrentUser().getId());
            SomeRandomeClass.AddToList("page" ,  pageCount.toString());

            Call<JobResponse> feed = api.myjobs(SomeRandomeClass.GetData());
            feed.enqueue(new Callback<JobResponse>() {
                @Override
                public void onResponse(Call<JobResponse> call, Response<JobResponse> response) {

                    for (Jobs job : response.body().getList())
                        listPro.add(job);

                    if(listPro.size() == 0){
                        noJobs.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                    }
                    else {
                        noJobs.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                        adapter = new MyJobAdapter(listPro, MyJobFeedActivity.this);
                        listView.setAdapter(adapter);
                    }
                    dialog.dismiss();
                }

                @Override
                public void onFailure(Call<JobResponse> call, Throwable t) {
                    Toast.makeText(MyJobFeedActivity.this, t.toString(), Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            });


        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        final ProgressDialog dialog = new ProgressDialog(MyJobFeedActivity.this);
        dialog.setMessage("Loading...");
        dialog.show();
        GPSTracker tracker = new GPSTracker(this);
        if (!tracker.canGetLocation()) {
            tracker.showSettingsAlert();
        } else {
            SomeRandomeClass.AddToList("lat", String.valueOf(tracker.getLatitude()));
            SomeRandomeClass.AddToList("long", String.valueOf(tracker.getLongitude()));
            SomeRandomeClass.AddToList("userid", ((Global) getApplication()).getCurrentUser().getId());
            SomeRandomeClass.AddToList("page" , "0");
            Call<JobResponse> feed = api.myjobs(SomeRandomeClass.GetData());
            feed.enqueue(new Callback<JobResponse>() {
                @Override
                public void onResponse(Call<JobResponse> call, Response<JobResponse> response) {
                    if(response.body().getList().size() == 0){
                        noJobs.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                    }
                    else {
                        noJobs.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                        adapter = new MyJobAdapter(response.body().getList(), MyJobFeedActivity.this);
                        adapter.notifyDataSetChanged();
                    }
                    dialog.dismiss();
                }

                @Override
                public void onFailure(Call<JobResponse> call, Throwable t) {
                    Toast.makeText(MyJobFeedActivity.this, t.toString(), Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            });


        }
    }
    public void fill(){
        pageCount++;
        GPSTracker tracker = new GPSTracker(this);
        if (!tracker.canGetLocation()) {
            tracker.showSettingsAlert();
        } else {
            SomeRandomeClass.AddToList("lat", String.valueOf(tracker.getLatitude()));
            SomeRandomeClass.AddToList("long", String.valueOf(tracker.getLongitude()));
            SomeRandomeClass.AddToList("userid", ((Global) getApplication()).getCurrentUser().getId());
            SomeRandomeClass.AddToList("page" ,  pageCount.toString());
            Call<JobResponse> feed = api.myjobs(SomeRandomeClass.GetData());
            feed.enqueue(new Callback<JobResponse>() {
                @Override
                public void onResponse(Call<JobResponse> call, Response<JobResponse> response) {
                    for (Jobs job : response.body().getList())
                        listPro.add(job);

                    if(listPro.size() == 0){
                        noJobs.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                    }
                    else {
                        noJobs.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                        adapter = new MyJobAdapter(listPro, MyJobFeedActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<JobResponse> call, Throwable t) {
                    Toast.makeText(MyJobFeedActivity.this, t.toString(), Toast.LENGTH_LONG).show();
                }
            });


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
}
