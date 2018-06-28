package com.midnight.healthcare.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.actionsheet.ActionSheet;
import com.midnight.healthcare.API.API;
import com.midnight.healthcare.API.APIFactory;
import com.midnight.healthcare.API.AplictaionInd;
import com.midnight.healthcare.API.ApplicationsIndResponse;
import com.midnight.healthcare.API.CheckResponse;
import com.midnight.healthcare.API.Day;
import com.midnight.healthcare.API.Jobs;
import com.midnight.healthcare.API.Messegess;
import com.midnight.healthcare.API.Nurse;
import com.midnight.healthcare.Activity.AplicationsIndividulaActivity;
import com.midnight.healthcare.Activity.CNAActivity;
import com.midnight.healthcare.Activity.ConversationActivity;
import com.midnight.healthcare.Activity.ReviewMyjob;
import com.midnight.healthcare.Global;
import com.midnight.healthcare.R;
import com.midnight.healthcare.SomeRandomeClass;
import com.midnight.healthcare.utils.TextUtils;
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

/**
 * Created by TUSK.ONE on 9/3/16.
 */
public class ApplicationCNAAdapter extends BaseAdapter {
    List<AplictaionInd> list = new ArrayList<>();
    API api = APIFactory.createAPI();
    Context context;
    int pas = 8;
    RelativeLayout accepted;
    LinearLayout acceptButtons;

    ApplicationCNAAdapter adapterContext;

    LayoutInflater inflater;

    public ApplicationCNAAdapter(List<AplictaionInd> list, Context context) {
        this.list = list;
        this.context = context;
        adapterContext = this;

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
            rowView = inflater.inflate(R.layout.aplication_cell_ind_to, viewGroup, false);
        } else {
            rowView = convertView;
        }

        final LinearLayout typezero = (LinearLayout) rowView.findViewById(R.id.typezero);
        final LinearLayout typeone = (LinearLayout) rowView.findViewById(R.id.typeone);


        if (i > pas) {
            ((AplicationsIndividulaActivity) context).fill();

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

        final AplictaionInd global = (AplictaionInd) getItem(i);


        if (global.getApplication().getType().equals("1")) {

            typeone.setVisibility(View.VISIBLE);
            typezero.setVisibility(View.GONE);


            final Jobs item = (Jobs) global.getJob();
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
            TextView location = (TextView) rowView.findViewById(R.id.cell_job_location);
//            RatingBar ratingBar = (RatingBar) rowView.findViewById(R.id.rb);
//            TextView rating = (TextView) rowView.findViewById(R.id.rating_tv);
//            Button rate = (Button) rowView.findViewById(R.id.rate);

            List<Day> listDays = item.getDays();
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

            for (Day d : listDays) {
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
            location.setText(item.getLocation().getCity() + ", " + item.getLocation().getState());

            title.setText(global.getUser().getFirstName() + " " + global.getUser().getLastName().substring(0, 1) + ".");
            price.setText("$" + item.getMaxPrice() + "/h");
            desc.setText(item.getInformation());

            //        RATING
//            ratingBar.setRating(TextUtils.rand());
//            rating.setText(String.valueOf(TextUtils.rand()));

            final Button waitButton = (Button) rowView.findViewById(R.id.waitButton);
            final LinearLayout declinedLayout = (LinearLayout) rowView.findViewById(R.id.declinedLayout);
            final Button options2 = (Button) rowView.findViewById(R.id.optionsButton2TypeOne);
            final LinearLayout after_answer = (LinearLayout) rowView.findViewById(R.id.after_answer);
            final Button sendMessage = (Button) rowView.findViewById(R.id.sendMessageTypeOne);
            final Button options = (Button) rowView.findViewById(R.id.optionsButtonTypeOne);

            if (global.getApplication().getResult().equals("0")) {
                waitButton.setVisibility(View.VISIBLE);
                declinedLayout.setVisibility(View.GONE);
                after_answer.setVisibility(View.GONE);
            } else if (global.getApplication().getResult().equals("1")) {
                //TODO: ACCEPTED
                declinedLayout.setVisibility(View.GONE);
                waitButton.setVisibility(View.GONE);
                after_answer.setVisibility(View.VISIBLE);
            } else {
                declinedLayout.setVisibility(View.VISIBLE);
                waitButton.setVisibility(View.GONE);
                after_answer.setVisibility(View.GONE);
            }

            waitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Warning!")
                            .setMessage("Want to cancel this application?")
                            .setCancelable(false)
                            .setNegativeButton("No",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    })
                            .setPositiveButton("Yes",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            SomeRandomeClass.AddToList("userid", ((Global) context.getApplicationContext()).getCurrentUser().getId());
                                            SomeRandomeClass.AddToList("application_id", global.getApplication().getId());
                                            final API api = APIFactory.createAPI();
                                            Call<Void> call = api.applicationRemove(SomeRandomeClass.GetData());
                                            call.enqueue(new Callback<Void>() {
                                                @Override
                                                public void onResponse(Call<Void> call, Response<Void> response) {
                                                    SomeRandomeClass.AddToList("userid", ((Global) context.getApplicationContext()).getCurrentUser().getId());
                                                    Call<ApplicationsIndResponse> callx = api.myapplications(SomeRandomeClass.GetData());
                                                    callx.enqueue(new Callback<ApplicationsIndResponse>() {
                                                        @Override
                                                        public void onResponse(Call<ApplicationsIndResponse> callx, Response<ApplicationsIndResponse> response) {
                                                            list = response.body().getResponse();
                                                            notifyDataSetChanged();
                                                            synchronized (adapterContext) {
                                                                adapterContext.notify();
                                                            }
                                                            if (list.size() == 0)
                                                                ((AplicationsIndividulaActivity) context).setTextVisible();
                                                        }

                                                        @Override
                                                        public void onFailure(Call<ApplicationsIndResponse> callx, Throwable t) {


                                                        }
                                                    });

                                                }

                                                @Override
                                                public void onFailure(Call<Void> call, Throwable t) {

                                                }
                                            });

                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });

            sendMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent conversationIntent = new Intent(context, ConversationActivity.class);
                    Messegess itemMess = new Messegess();
                    itemMess.setPartner(global.getUser().getId());
                    conversationIntent.putExtra("mess", itemMess);
                    context.startActivity(conversationIntent);
                }
            });

            options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    optionShow(global);
                }
            });

            options2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    optionShow(global);
                }
            });


            return rowView;

        } else {
            typezero.setVisibility(View.VISIBLE);
            typeone.setVisibility(View.GONE);

            accepted = (RelativeLayout) rowView.findViewById(R.id.accepted);
            acceptButtons = (LinearLayout) rowView.findViewById(R.id.acceptButtons);

            acceptButtons.setVisibility(View.VISIBLE);
            accepted.setVisibility(View.GONE);


            final Nurse item = (Nurse) global.getUser();
            ImageView img = (ImageView) rowView.findViewById(R.id.cell_image);
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                    // You can pass your own memory cache implementation
                    .discCacheFileNameGenerator(new HashCodeFileNameGenerator())
                    .build();
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .displayer(new RoundedBitmapDisplayer(150)) //rounded corner bitmap
                    .cacheInMemory(true)
                    .cacheOnDisc(true)
                    .build();
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.init(config);
            imageLoader.displayImage("http://104.131.152.91/health-care/" + item.getProfileImg(), img, options);
            TextView name = (TextView) rowView.findViewById(R.id.cell_name);
            TextView descrition = (TextView) rowView.findViewById(R.id.cell_description);
            TextView price = (TextView) rowView.findViewById(R.id.cell_price);
            TextView distance = (TextView) rowView.findViewById(R.id.cell_distance);
            TextView time = (TextView) rowView.findViewById(R.id.cell_time);
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
            if (item.getLocation() != null)
                location.setText(item.getLocation().getCity() + ", " + item.getLocation().getState());

            final Button accept = (Button) rowView.findViewById(R.id.accept);
            final Button decline = (Button) rowView.findViewById(R.id.decline);
            final RelativeLayout accepted = (RelativeLayout) rowView.findViewById(R.id.accepted);
            final LinearLayout after_answer = (LinearLayout) rowView.findViewById(R.id.after_answerTypeZero);
            final LinearLayout declinedLayout = (LinearLayout) rowView.findViewById(R.id.declinedLayout);
            final Button option2 = (Button) rowView.findViewById(R.id.optionsButton2);
            final Button sendMessage = (Button) rowView.findViewById(R.id.sendMessageTypeZero);
            final Button option = (Button) rowView.findViewById(R.id.optionsButtonTypeZero);


            if (global.getApplication().getResult().equals("0")) {
                acceptButtons.setVisibility(View.VISIBLE);
                declinedLayout.setVisibility(View.GONE);
                after_answer.setVisibility(View.GONE);
            } else if (global.getApplication().getResult().equals("1")) {
                //TODO: ACCEPTED
                declinedLayout.setVisibility(View.GONE);
                acceptButtons.setVisibility(View.GONE);
                after_answer.setVisibility(View.VISIBLE);
            } else {
                declinedLayout.setVisibility(View.VISIBLE);
                acceptButtons.setVisibility(View.GONE);
                after_answer.setVisibility(View.GONE);
            }

            TextView job = (TextView) rowView.findViewById(R.id.job);
            job.setText(global.getJob().getTitle());
            job.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ReviewMyjob.class);
                    intent.putExtra("job", global.getJob());
                    context.startActivity(intent);
                }
            });

            option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    optionShow(global);
                }
            });

            option2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    optionShow(global);
                }
            });

            sendMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent conversationIntent = new Intent(context, ConversationActivity.class);
                    Messegess itemMess = new Messegess();
                    itemMess.setPartner(global.getUser().getId());
                    conversationIntent.putExtra("mess", itemMess);
                    context.startActivity(conversationIntent);
                }
            });


            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((AplicationsIndividulaActivity) context).block.setVisibility(View.VISIBLE);
                    SomeRandomeClass.AddToList("userid", ((Global) context.getApplicationContext()).getCurrentUser().getId());
                    SomeRandomeClass.AddToList("application_id", global.getApplication().getId());
                    API api = APIFactory.createAPI();
                    Call call = api.applicationAccept(SomeRandomeClass.GetData());
                    final Animation out = new AlphaAnimation(1.0f, 0.0f);
                    out.setDuration(200);
                    out.setFillAfter(true);
                    acceptButtons.startAnimation(out);

                    call.enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {

                            acceptButtons.setVisibility(View.GONE);
                            accepted.setVisibility(View.VISIBLE);
                            final Animation out = new AlphaAnimation(0.0f, 1.0f);
                            out.setDuration(200);
                            out.setFillAfter(true);
                            out.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {
                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    new java.util.Timer().schedule(
                                            new java.util.TimerTask() {
                                                @Override
                                                public void run() {
                                                    ((AplicationsIndividulaActivity) context).runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            final Animation out = new AlphaAnimation(1.0f, 0.0f);
                                                            out.setDuration(200);
                                                            out.setFillAfter(true);
                                                            out.setAnimationListener(new Animation.AnimationListener() {
                                                                @Override
                                                                public void onAnimationStart(Animation animation) {

                                                                }

                                                                @Override
                                                                public void onAnimationEnd(Animation animation) {
                                                                    accepted.setVisibility(View.GONE);
                                                                    final Animation out = new AlphaAnimation(0.0f, 1.0f);
                                                                    out.setDuration(200);
                                                                    out.setFillAfter(true);
                                                                    out.setAnimationListener(new Animation.AnimationListener() {
                                                                        @Override
                                                                        public void onAnimationStart(Animation animation) {
                                                                            after_answer.setVisibility(View.VISIBLE);
                                                                        }

                                                                        @Override
                                                                        public void onAnimationEnd(Animation animation) {
                                                                            ((AplicationsIndividulaActivity) context).block.setVisibility(View.GONE);
                                                                            sendMessage.setOnClickListener(new View.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(View v) {
                                                                                    Intent conversationIntent = new Intent(context, ConversationActivity.class);
                                                                                    Messegess itemMess = new Messegess();
                                                                                    itemMess.setPartner(item.getId());
                                                                                    conversationIntent.putExtra("mess", itemMess);
                                                                                    context.startActivity(conversationIntent);
                                                                                }
                                                                            });

                                                                            option.setOnClickListener(new View.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(View v) {
                                                                                    //TODO: OPTIONS
                                                                                    context.setTheme(R.style.ActionSheetStyleiOS7);

                                                                                    ActionSheet.createBuilder(context, ((AplicationsIndividulaActivity) context).getSupportFragmentManager())
                                                                                            .setCancelButtonTitle("Cancel")
                                                                                            .setOtherButtonTitles("Report", "Delete this application")
                                                                                            .setCancelableOnTouchOutside(true)
                                                                                            .setListener(new ActionSheet.ActionSheetListener() {
                                                                                                @Override
                                                                                                public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

                                                                                                }

                                                                                                @Override
                                                                                                public void onOtherButtonClick(ActionSheet actionSheet, int index) {

                                                                                                    if (index == 0) {
                                                                                                        API api = APIFactory.createAPI();
                                                                                                        SomeRandomeClass.AddToList("userid", ((Global) ((AplicationsIndividulaActivity) context).getApplication()).getCurrentUser().getId());
                                                                                                        SomeRandomeClass.AddToList("jobid", global.getJob().getId());
                                                                                                        Call call = api.reportJob(SomeRandomeClass.GetData());
                                                                                                        call.enqueue(new Callback() {
                                                                                                            @Override
                                                                                                            public void onResponse(Call call, Response response) {
                                                                                                                Toast.makeText(context.getApplicationContext(), "Job was succesfully reported.",
                                                                                                                        Toast.LENGTH_SHORT).show();
                                                                                                            }

                                                                                                            @Override
                                                                                                            public void onFailure(Call call, Throwable t) {

                                                                                                            }
                                                                                                        });
                                                                                                    } else if (index == 1) {
                                                                                                        //TODO: DELETE APPLICATION
                                                                                                        SomeRandomeClass.AddToList("userid", ((Global) context.getApplicationContext()).getCurrentUser().getId());
                                                                                                        SomeRandomeClass.AddToList("application_id", global.getApplication().getId());
                                                                                                        final API api = APIFactory.createAPI();
                                                                                                        Call<Void> call = api.applicationRemove(SomeRandomeClass.GetData());
                                                                                                        call.enqueue(new Callback<Void>() {
                                                                                                            @Override
                                                                                                            public void onResponse(Call<Void> call, Response<Void> response) {
                                                                                                                SomeRandomeClass.AddToList("userid", ((Global) context.getApplicationContext()).getCurrentUser().getId());
                                                                                                                Call<ApplicationsIndResponse> callx = api.myapplications(SomeRandomeClass.GetData());
                                                                                                                callx.enqueue(new Callback<ApplicationsIndResponse>() {
                                                                                                                    @Override
                                                                                                                    public void onResponse(Call<ApplicationsIndResponse> callx, Response<ApplicationsIndResponse> response) {
                                                                                                                        list = response.body().getResponse();
                                                                                                                        notifyDataSetChanged();
                                                                                                                        synchronized (adapterContext) {
                                                                                                                            adapterContext.notify();
                                                                                                                        }
                                                                                                                        if (list.size() == 0)
                                                                                                                            ((AplicationsIndividulaActivity) context).setTextVisible();
                                                                                                                    }

                                                                                                                    @Override
                                                                                                                    public void onFailure(Call<ApplicationsIndResponse> callx, Throwable t) {


                                                                                                                    }
                                                                                                                });

                                                                                                            }

                                                                                                            @Override
                                                                                                            public void onFailure(Call<Void> call, Throwable t) {

                                                                                                            }
                                                                                                        });
                                                                                                    }


                                                                                                }
                                                                                            })
                                                                                            .show();
                                                                                }
                                                                            });
                                                                        }

                                                                        @Override
                                                                        public void onAnimationRepeat(Animation animation) {

                                                                        }
                                                                    });
                                                                    after_answer.startAnimation(out);
                                                                }

                                                                @Override
                                                                public void onAnimationRepeat(Animation animation) {

                                                                }
                                                            });

                                                            accepted.startAnimation(out);
                                                        }
                                                    });
                                                }
                                            },
                                            3000
                                    );
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });
                            accepted.startAnimation(out);

                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {

                        }
                    });
                }
            });

            decline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SomeRandomeClass.AddToList("userid", ((Global) context.getApplicationContext()).getCurrentUser().getId());
                    SomeRandomeClass.AddToList("application_id", global.getApplication().getId());
                    final API api = APIFactory.createAPI();
                    Call call = api.applicationRefuse(SomeRandomeClass.GetData());
                    call.enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {
                            SomeRandomeClass.AddToList("userid", ((Global) context.getApplicationContext()).getCurrentUser().getId());
                            Call<ApplicationsIndResponse> callx = api.applications(SomeRandomeClass.GetData());
                            callx.enqueue(new Callback<ApplicationsIndResponse>() {
                                @Override
                                public void onResponse(Call<ApplicationsIndResponse> callx, Response<ApplicationsIndResponse> response) {
                                    list = response.body().getResponse();
                                    notifyDataSetChanged();
                                    synchronized (adapterContext) {
                                        adapterContext.notify();
                                    }
                                    if (list.size() == 0)
                                        ((AplicationsIndividulaActivity) context).setTextVisible();
                                }

                                @Override
                                public void onFailure(Call<ApplicationsIndResponse> callx, Throwable t) {


                                }
                            });

                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {

                        }
                    });

                }
            });


            return rowView;

        }
    }

    /*private void checkOffer(AplictaionInd item){
        final RelativeLayout acceptedLocal = accepted;
        final LinearLayout acceptButtonsLocal = acceptButtons;

        SomeRandomeClass.AddToList("jobid", item.getJob().getId());
        SomeRandomeClass.AddToList("user", item.getUser().getId());

        Call<CheckResponse> callCheck = api.checkCanApply(SomeRandomeClass.GetData());
        callCheck.enqueue(new Callback<CheckResponse>() {
            @Override
            public void onResponse(Call<CheckResponse> call, Response<CheckResponse> response) {
                if (response.body().getResponse().equals("0")) {
                    acceptedLocal.setVisibility(View.GONE);
                    acceptButtonsLocal.setVisibility(View.VISIBLE);
                }
                else if(response.body().getResponse().equals("1")){
                    acceptedLocal.setVisibility(View.VISIBLE);
                    acceptButtonsLocal.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<CheckResponse> call, Throwable t) {

            }
        });
    }*/

    private void optionShow(final AplictaionInd global) {
        context.setTheme(R.style.ActionSheetStyleiOS7);

        ActionSheet.createBuilder(context, ((AplicationsIndividulaActivity) context).getSupportFragmentManager())
                .setCancelButtonTitle("Cancel")
                .setOtherButtonTitles("Call " + global.getUser().getPhone(), "Report", "Delete this application")
                .setCancelableOnTouchOutside(true)
                .setListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

                    }

                    @Override
                    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                        if (index == 0) {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + global.getUser().getPhone()));
                            context.startActivity(intent);
                        }
                        if (index == 1) {
                            API api = APIFactory.createAPI();
                            SomeRandomeClass.AddToList("userid", ((Global) ((AplicationsIndividulaActivity) context).getApplication()).getCurrentUser().getId());
                            SomeRandomeClass.AddToList("jobid", global.getJob().getId());
                            Call call = api.reportJob(SomeRandomeClass.GetData());
                            call.enqueue(new Callback() {
                                @Override
                                public void onResponse(Call call, Response response) {
                                    Toast.makeText(context.getApplicationContext(), "Job was succesfully reported.",
                                            Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call call, Throwable t) {

                                }
                            });
                        } else if (index == 2) {
                            //TODO: DELETE APPLICATION
                            SomeRandomeClass.AddToList("userid", ((Global) context.getApplicationContext()).getCurrentUser().getId());
                            SomeRandomeClass.AddToList("application_id", global.getApplication().getId());
                            final API api = APIFactory.createAPI();
                            Call<Void> call = api.applicationRemove(SomeRandomeClass.GetData());
                            call.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    SomeRandomeClass.AddToList("userid", ((Global) context.getApplicationContext()).getCurrentUser().getId());
                                    Call<ApplicationsIndResponse> callx = api.myapplications(SomeRandomeClass.GetData());
                                    callx.enqueue(new Callback<ApplicationsIndResponse>() {
                                        @Override
                                        public void onResponse(Call<ApplicationsIndResponse> callx, Response<ApplicationsIndResponse> response) {
                                            list = response.body().getResponse();
                                            notifyDataSetChanged();
                                            synchronized (adapterContext) {
                                                adapterContext.notify();
                                            }
                                            if (list.size() == 0)
                                                ((AplicationsIndividulaActivity) context).setTextVisible();
                                        }

                                        @Override
                                        public void onFailure(Call<ApplicationsIndResponse> callx, Throwable t) {


                                        }
                                    });

                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {

                                }
                            });
                        }


                    }
                })
                .show();
    }
}
