package com.midnight.healthcare.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.midnight.healthcare.API.Nurse;
import com.midnight.healthcare.API.Skill;
import com.midnight.healthcare.Activity.CNAActivity;
import com.midnight.healthcare.Activity.CNAFeedActivity;
import com.midnight.healthcare.R;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TUSK.ONE on 9/3/16.
 */
public class CNAFeedAdapter extends BaseAdapter {
    List<Nurse> list = new ArrayList<>();
    Context context;
    int pas = 8;
    Integer page;
    ImageLoader imageLoader;
    DisplayImageOptions options;

    LayoutInflater inflater;

    public CNAFeedAdapter(List<Nurse> list, Context context, Integer page) {
        this.list = list;
        this.context = context;
        this.page = page;
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                // You can pass your own memory cache implementation
                .discCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .build();
        options = new DisplayImageOptions.Builder()
                .displayer(new RoundedBitmapDisplayer(150)) //rounded corner bitmap
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);

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
            rowView = inflater.inflate(R.layout.cnafeed_cell, viewGroup, false);
        } else {
            rowView = convertView;
        }

        if (i > pas) {
            ((CNAFeedActivity) context).fill();

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

        final Nurse item = (Nurse) getItem(i);

        ImageView img = (ImageView) rowView.findViewById(R.id.cell_image);
        imageLoader.displayImage("http://104.131.152.91/health-care/" + item.getProfileImg(), img, options);
        TextView name = (TextView) rowView.findViewById(R.id.cell_name);
        TextView descrition = (TextView) rowView.findViewById(R.id.cell_description);
        TextView price = (TextView) rowView.findViewById(R.id.cell_price);
        TextView distance = (TextView) rowView.findViewById(R.id.cell_distance);
        TextView time = (TextView) rowView.findViewById(R.id.cell_time);

        RatingBar ratingBar = (RatingBar) rowView.findViewById(R.id.rb);
        TextView rating = (TextView) rowView.findViewById(R.id.rating_tv);
        name.setText(item.getFirstName() + " " + item.getLastName().substring(0, 1) + ".");
        descrition.setText(item.getDescription());
        price.setText("$" + item.getPriceMin() + "-$" + item.getPriceMax());
        distance.setText(item.getDistance() + " mi");
        if (item.getAvailableTime().equals("0"))
            time.setText("Part time");
        else
            time.setText("Full time");

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CNAActivity.class);
                intent.putExtra("nurse", item);
                context.startActivity(intent);
            }
        });

        TextView location = (TextView) rowView.findViewById(R.id.cell_palce);
        location.setText(item.getLocation().getCity() + ", " + item.getLocation().getState());
        List<String> skillzzz = new ArrayList<>();
        int p = Integer.valueOf(item.getExperience());
        if (p == 1) {
            skillzzz.add("Less than 1 year expirience:");
        }
        if (p == 2) {
            skillzzz.add("2-5 years expirience:");
        }
        if (p == 3) {
            skillzzz.add("5-7 years expirience:");
        }
        if (p == 4) {
            skillzzz.add("7-10 years expirience:");
        }
        if (p == 5) {
            skillzzz.add("10-15 years expirience:");
        }
        if (p == 6) {
            skillzzz.add("15-20 years expirience:");
        }
        if (p == 7) {
            skillzzz.add("More than 20 years expirience:");
        }


        if (item.getSkills().size() > 4) {
            int rest = item.getSkills().size() - 4;
            for (int x = 0; x < 4; x++)
                skillzzz.add(item.getSkills().get(x).getName());
            skillzzz.add(rest + " more...");
        } else
            for (Skill skill : item.getSkills())
                skillzzz.add(skill.getName());


//        RATING
//        ratingBar.setRating(item.getUserRating());
//        rating.setText(String.valueOf(item.getUserRating()));

        LinearLayout skillContainer = (LinearLayout) rowView.findViewById(R.id.skill_container);
        skillContainer.removeAllViews();
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        llp.setMargins(5, 5, 5, 0);
        LinearLayout line2 = new LinearLayout(context);
        line2.setOrientation(LinearLayout.HORIZONTAL);
        line2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        int entry = 0;
        String chr = "";
        for (int k = 0; k < skillzzz.size(); k++) {

            TextView skill = new TextView(context);
            skill.setLayoutParams(llp);
            if (!skillzzz.get(k).contains("...") && !skillzzz.get(k).contains("year")) {
                skill.setBackgroundResource(R.drawable.round_blue_skills);
                skill.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                skill.setTextColor(Color.parseColor("#FFFFFFFF"));
            }
            skill.setText(skillzzz.get(k));
            Rect bounds = new Rect();
            Paint textPaint = skill.getPaint();
            textPaint.getTextBounds(skillzzz.get(k), 0, skillzzz.get(k).length(), bounds);
            int width = bounds.width();
            Rect bounds2 = new Rect();
            Paint textPaint2 = skill.getPaint();
            textPaint2.getTextBounds(chr, 0, chr.length(), bounds2);
            int width2 = bounds2.width();
            if (viewGroup.getWidth() - width2 * 1.5 > width * 1.5) {
                line2.addView(skill);
                chr = chr + skillzzz.get(k);
            } else {

                skillContainer.addView(line2, entry);
                entry++;
                chr = "";
                line2 = new LinearLayout(context);
                line2.setOrientation(LinearLayout.HORIZONTAL);
                line2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                line2.addView(skill);
            }

        }
        if (skillContainer.getChildAt(entry) == null)
            skillContainer.addView(line2, entry);

        return rowView;
    }
}


