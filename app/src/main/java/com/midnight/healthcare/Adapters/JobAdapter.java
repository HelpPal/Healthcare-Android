package com.midnight.healthcare.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.midnight.healthcare.API.Day;
import com.midnight.healthcare.API.Jobs;
import com.midnight.healthcare.Activity.JobDeefActivity;
import com.midnight.healthcare.Activity.JobForCNA;
import com.midnight.healthcare.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TUSK.ONE on 9/3/16.
 */
public class JobAdapter extends BaseAdapter {
    List<Jobs> list = new ArrayList<>();
    Context context;
    int pas = 8;

    LayoutInflater inflater;

    public JobAdapter(List<Jobs> list, Context context) {
        this.list = list;
        this.context = context;


        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        View rowView;
        if (convertView == null) {
            rowView = inflater.inflate(R.layout.job_cell, viewGroup, false);
        } else {
            rowView = convertView;
        }

        if (i > pas) {
            ((JobDeefActivity) context).fill();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms
                    notifyDataSetChanged();
                    pas = pas * 2;
                }
            }, 3000);
        }

        final Jobs item = (Jobs) getItem(i);
        TextView title = (TextView) rowView.findViewById(R.id.cell_job_title);
        TextView price = (TextView) rowView.findViewById(R.id.cell_job_price);
        TextView desc = (TextView) rowView.findViewById(R.id.cell_job_desc);
        TextView info = (TextView) rowView.findViewById(R.id.cell_job_info);
        Button s = (Button) rowView.findViewById(R.id.job_cell_s);
        Button m = (Button) rowView.findViewById(R.id.job_cell_m);
        Button t = (Button) rowView.findViewById(R.id.job_cell_t);
        Button w = (Button) rowView.findViewById(R.id.job_cell_w);
        Button th = (Button) rowView.findViewById(R.id.job_cell_th);
        Button f = (Button) rowView.findViewById(R.id.job_cell_f);
        Button sa = (Button) rowView.findViewById(R.id.job_cell_sa);
        RatingBar ratingBar = (RatingBar) rowView.findViewById(R.id.rb);
        TextView rating = (TextView) rowView.findViewById(R.id.rating_tv);

        TextView location = (TextView) rowView.findViewById(R.id.cell_job_location);
        List<Day> list = item.getDays();
        s.setBackgroundResource(R.drawable.s);
        sa.setBackgroundResource(R.drawable.sa);
        m.setBackgroundColor(0xFFFFFFFF);
        t.setBackgroundColor(0xFFFFFFFF);
        w.setBackgroundColor(0xFFFFFFFF);
        th.setBackgroundColor(0xFFFFFFFF);
        f.setBackgroundColor(0xFFFFFFFF);
        s.setTextColor(context.getResources().getColor(R.color.textColor));
        sa.setTextColor(context.getResources().getColor(R.color.textColor));
        m.setTextColor(context.getResources().getColor(R.color.textColor));
        t.setTextColor(context.getResources().getColor(R.color.textColor));
        w.setTextColor(context.getResources().getColor(R.color.textColor));
        th.setTextColor(context.getResources().getColor(R.color.textColor));
        f.setTextColor(context.getResources().getColor(R.color.textColor));

        for (Day d : list) {
            if (d.getDay().equals(context.getString(R.string.sunday))) {
                s.setBackgroundResource(R.drawable.s_normal);
                s.setTextColor(context.getResources().getColor(R.color.blueHighlighted));
            }
            if (d.getDay().equals(context.getString(R.string.saturday))) {
                sa.setBackgroundResource(R.drawable.sa_tugle);
                sa.setTextColor(context.getResources().getColor(R.color.blueHighlighted));
            }
            if (d.getDay().equals(context.getString(R.string.monday))) {
                m.setBackgroundColor(Color.parseColor("#90bbf2"));
                m.setTextColor(context.getResources().getColor(R.color.blueHighlighted));
            }
            if (d.getDay().equals(context.getString(R.string.tuesday))) {
                t.setBackgroundColor(Color.parseColor("#90bbf2"));
                t.setTextColor(context.getResources().getColor(R.color.blueHighlighted));
            }
            if (d.getDay().equals(context.getString(R.string.wednesday))) {
                w.setBackgroundColor(Color.parseColor("#90bbf2"));
                w.setTextColor(context.getResources().getColor(R.color.blueHighlighted));
            }
            if (d.getDay().equals(context.getString(R.string.thursday))) {
                th.setBackgroundColor(Color.parseColor("#90bbf2"));
                th.setTextColor(context.getResources().getColor(R.color.blueHighlighted));
            }
            if (d.getDay().equals(context.getString(R.string.friday))) {
                f.setBackgroundColor(Color.parseColor("#90bbf2"));
                f.setTextColor(context.getResources().getColor(R.color.blueHighlighted));
            }

        }
        info.setText(item.getTime_desc());
        if (item.getLocation() != null)
            location.setText(item.getLocation().getCity() + ", " + item.getLocation().getState());

        title.setText(item.getTitle());
        price.setText("$" + item.getMaxPrice() + "/h");
        desc.setText(item.getInformation());


//        RATING
        ratingBar.setRating(item.getUserRating());
        rating.setText(String.valueOf(item.getUserRating()));


        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, JobForCNA.class);
                intent.putExtra("job", item);
                context.startActivity(intent);
            }
        });


        return rowView;
    }


}
