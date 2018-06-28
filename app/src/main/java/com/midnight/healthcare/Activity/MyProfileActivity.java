package com.midnight.healthcare.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.midnight.healthcare.API.Skill;
import com.midnight.healthcare.API.User;
import com.midnight.healthcare.Global;
import com.midnight.healthcare.R;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyProfileActivity extends MainActivity {
    ImageView img;
    TextView name;
    TextView info;
    TextView exp;
    TextView price;
    TextView time;
    TextView desc;
    Button editBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        setStatusBarBlue();

        ((RelativeLayout) findViewById(R.id.exit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent JobFeedIntent = new Intent(MyProfileActivity.this, JobDeefActivity.class);
                startActivity(JobFeedIntent);
            }
        });

        Intent intent = getIntent();
        img = (ImageView) findViewById(R.id.cna_img);
        name = (TextView) findViewById(R.id.cna_name);
        info = (TextView) findViewById(R.id.cna_info);
        exp = (TextView) findViewById(R.id.cna_exp);
        price = (TextView) findViewById(R.id.cna_price);
        time = (TextView) findViewById(R.id.cna_time);
        desc = (TextView) findViewById(R.id.cna_descritpion);
        editBtn = (Button) findViewById(R.id.edit_btn);

        final User nurse = ((Global) getApplication()).getCurrentUser();

        editBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        editBtn.setBackground(getResources().getDrawable(R.drawable.round_blue_active));
                        editBtn.setTextColor(getResources().getColor(R.color.blueHighlighted));
                        break;
                    case MotionEvent.ACTION_UP: {
                        editBtn.setBackground(getResources().getDrawable(R.drawable.round_blue));
                        editBtn.setTextColor(0xFFFFFFFF);
                        Intent editIntent = new Intent(MyProfileActivity.this, EditProfileActivity.class);
                        startActivity(editIntent);
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

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Geocoder gcd = new Geocoder(MyProfileActivity.this);
//                List<Address> addresses = new ArrayList<>();
//                try {
//                    addresses = gcd.getFromLocation(Double.valueOf(((Global) getApplication()).getCurrentUser().getAddressLat()), Double.valueOf(((Global) getApplication()).getCurrentUser().getAddressLong()), 1);
//                    final String city = addresses.get(0).getAddressLine(0);
//
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (nurse.getGender().equals("1"))
//                                info.setText("Male, " + city);
//                            else
//                                info.setText("Female, " + city);
//                        }
//                    });
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//


        String city = nurse.getAddress() + ", " + nurse.getCity();


        info.setText(nurse.getGender().equals("1") ? "Male, " + city : "Female, " + city);


        name.setText(nurse.getFirstName() + " " + nurse.getLastName());

        if (nurse.getExperience().

                equals("1"))
            exp.setText("Less then 1 year experience");
        if (nurse.getExperience().

                equals("2"))
            exp.setText("2-5 years experience");
        if (nurse.getExperience().

                equals("3"))
            exp.setText("5-7 year experience");
        if (nurse.getExperience().

                equals("4"))
            exp.setText("7-10 year experience");
        if (nurse.getExperience().

                equals("5"))
            exp.setText("10-15 year experience");
        if (nurse.getExperience().

                equals("6"))
            exp.setText("15-20 year experience");
        if (nurse.getExperience().

                equals("7"))
            exp.setText("More than 20 year experience");

        if (nurse.getPriceMin().

                equals(nurse.getPriceMax()))

        {
            price.setText("$" + nurse.getPriceMin());
        } else

        {
            price.setText("$" + nurse.getPriceMin() + "-$" + nurse.getPriceMax());
        }
        if (nurse.getAvailableTime().

                equals("0"))
            time.setText("Part time");
        else
            time.setText("Full time");

        desc.setText(nurse.getDescription());


        List<String> skillzzz = new ArrayList<>();
        for (
                Skill skill : nurse.getSkills())
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
        for (
                int k = 0; k < skillzzz.size(); k++)

        {

            TextView skill = new TextView(this);
            skill.setLayoutParams(llp);
            skill.setBackgroundResource(R.drawable.round_blue);
            skill.setTextColor(Color.parseColor("#FFFFFFFF"));
            skill.setText(skillzzz.get(k));
            skill.setPadding(15, 5, 15, 5);
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


        desc.setText(nurse.getDescription());


    }

    public void setStatusBarBlue() {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(R.color.blueHighlighted));
        }
    }

    @Override
    public void onBackPressed() {
        Intent JobFeedIntent = new Intent(MyProfileActivity.this, JobDeefActivity.class);
        startActivity(JobFeedIntent);
    }
}



