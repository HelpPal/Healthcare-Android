package com.midnight.healthcare.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.midnight.healthcare.API.Day;
import com.midnight.healthcare.API.Jobs;
import com.midnight.healthcare.R;

import java.util.List;

public class ReviewMyjob extends MainActivity {
    Jobs job;
    Button edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_job);
        ((RelativeLayout) findViewById(R.id.exit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Intent intent = getIntent();
        job = (Jobs) intent.getSerializableExtra("job");
        TextView title = (TextView) findViewById(R.id.title_job);
        TextView price = (TextView) findViewById(R.id.cell_job_price);
        TextView desc = (TextView) findViewById(R.id.cell_job_desc);
        TextView info = (TextView) findViewById(R.id.cell_job_info);
        TextView distance = (TextView) findViewById(R.id.distance_location);
        Button s = (Button) findViewById(R.id.job_cell_s);
        Button m = (Button) findViewById(R.id.job_cell_m);
        Button t = (Button) findViewById(R.id.job_cell_t);
        Button w = (Button) findViewById(R.id.job_cell_w);
        Button th = (Button) findViewById(R.id.job_cell_th);
        Button f = (Button) findViewById(R.id.job_cell_f);
        Button sa = (Button) findViewById(R.id.job_cell_sa);
        TextView location = (TextView) findViewById(R.id.location_job);
        edit = (Button) findViewById(R.id.job_edt);


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReviewMyjob.this , EditJobActivity.class);
                intent.putExtra("job", job);
              startActivity(intent);
                finish();
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
        location.setText(job.getLocation().getCity() + ", "+job.getLocation().getState());

        title.setText(job.getTitle());
        price.setText("$"+job.getMaxPrice()+"/h");
        desc.setText(job.getInformation());
        distance.setText(job.getDistance() + " mi");



    }
}
