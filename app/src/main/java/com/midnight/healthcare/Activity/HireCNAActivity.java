package com.midnight.healthcare.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.midnight.healthcare.API.API;
import com.midnight.healthcare.API.APIFactory;
import com.midnight.healthcare.API.CheckResponse;
import com.midnight.healthcare.API.JobResponse;
import com.midnight.healthcare.API.Jobs;
import com.midnight.healthcare.API.Nurse;
import com.midnight.healthcare.Adapters.HireJobAdapter;
import com.midnight.healthcare.GPSTracker;
import com.midnight.healthcare.Global;
import com.midnight.healthcare.R;
import com.midnight.healthcare.SomeRandomeClass;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HireCNAActivity extends MainActivity {
    API api = APIFactory.createAPI();
    ListView list;
    TextView hireText;
    TextView mainText;
    RelativeLayout offer;
    HireJobAdapter  adapter;
    Nurse nurse;

    TextView noJobs;

    List<Jobs> filteredJobs = new ArrayList<>();
    List<Jobs> notFilteredJobs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hire_cna);

        setStatusBarBlue();

        ((RelativeLayout) findViewById(R.id.exit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        noJobs = (TextView) findViewById(R.id.noExistingJob);
        Intent intent = getIntent();
        offer = (RelativeLayout) findViewById(R.id.creeate_offer_btn);
        list = (ListView) findViewById(R.id.offering_list);
        hireText = (TextView) findViewById(R.id.hire_text);
        mainText = (TextView) findViewById(R.id.offer_text);
        nurse = (Nurse) intent.getSerializableExtra("nurse");
        offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HireCNAActivity.this , OfferJobActivity.class);
                intent.putExtra("nurse", nurse);
                startActivity(intent);
            }
        });
        hireText.setText("Hire "+nurse.getFirstName()+" "+nurse.getLastName().substring(0 ,1 )+".");
        mainText.setText("To hire "+nurse.getFirstName()+" "+nurse.getLastName().substring(0 ,1 )+". you need to send her an offer. If she accepts your offer - you hired her.");
        final ProgressDialog dialog = new ProgressDialog(HireCNAActivity.this);
        dialog.setMessage("Loading...");
        dialog.show();
        GPSTracker tracker = new GPSTracker(this);
        if (!tracker.canGetLocation()) {
            tracker.showSettingsAlert();
        } else {
            SomeRandomeClass.AddToList("lat", String.valueOf(tracker.getLatitude()));
            SomeRandomeClass.AddToList("long", String.valueOf(tracker.getLongitude()));
            SomeRandomeClass.AddToList("userid", ((Global) getApplication()).getCurrentUser().getId());
            Call<JobResponse> feed = api.myjobs(SomeRandomeClass.GetData());
            feed.enqueue(new Callback<JobResponse>() {
                @Override
                public void onResponse(Call<JobResponse> call, Response<JobResponse> response) {
                    if(response.body().getList().size() == 0){
                        noJobs.setVisibility(View.VISIBLE);
                        list.setVisibility(View.GONE);
                    }
                    else {
                        noJobs.setVisibility(View.GONE);
                        list.setVisibility(View.VISIBLE);
                        filteredJobs = new ArrayList<>();
                        notFilteredJobs = response.body().getList();

                        checkJob(0);
                    }
                    dialog.dismiss();
                }

                @Override
                public void onFailure(Call<JobResponse> call, Throwable t) {
                    Toast.makeText(HireCNAActivity.this, t.toString(), Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            });


        }

    }
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (view == null)
                return;
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    private void checkJob(final int i) {
        if (i == notFilteredJobs.size()) {
            adapter = new HireJobAdapter(filteredJobs, HireCNAActivity.this, nurse);
            list.setAdapter(adapter);
            setListViewHeightBasedOnChildren(list);
        } else {
            SomeRandomeClass.AddToList("jobid", notFilteredJobs.get(i).getId());
            SomeRandomeClass.AddToList("user", nurse.getId());

            Call<CheckResponse> callCheck = api.checkCanApply(SomeRandomeClass.GetData());
            callCheck.enqueue(new Callback<CheckResponse>() {
                @Override
                public void onResponse(Call<CheckResponse> call, Response<CheckResponse> response) {
                    if (response.body().getResponse().equals("5")) {
                        filteredJobs.add(notFilteredJobs.get(i));
                    }
                    if (i < notFilteredJobs.size()) checkJob(i + 1);

                }

                @Override
                public void onFailure(Call<CheckResponse> call, Throwable t) {

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
