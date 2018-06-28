package com.midnight.healthcare.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.actionsheet.ActionSheet;
import com.midnight.healthcare.API.API;
import com.midnight.healthcare.API.APIFactory;
import com.midnight.healthcare.API.AplictaionInd;
import com.midnight.healthcare.API.ApplicationsIndResponse;
import com.midnight.healthcare.API.Day;
import com.midnight.healthcare.API.Jobs;
import com.midnight.healthcare.API.Messegess;
import com.midnight.healthcare.Activity.AplicationsCNAActivity;
import com.midnight.healthcare.Activity.ConversationActivity;
import com.midnight.healthcare.Activity.EditProfileActivity;
import com.midnight.healthcare.Global;
import com.midnight.healthcare.R;
import com.midnight.healthcare.SomeRandomeClass;
import com.midnight.healthcare.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by TUSK.ONE on 9/3/16.
 */
public class ApplicationIndAdapter extends BaseAdapter {
    List<AplictaionInd> list = new ArrayList<>();
    Context context;
    int pas = 8;
    ApplicationIndAdapter adapterContext;

    LayoutInflater inflater;

    public ApplicationIndAdapter(List<AplictaionInd> list, Context context) {
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
            rowView = inflater.inflate(R.layout.aplication_can_cell, viewGroup, false);
        } else {
            rowView = convertView;
        }

        if (i > pas) {
            ((AplicationsCNAActivity) context).fill();

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
//        RatingBar ratingBar = (RatingBar) rowView.findViewById(R.id.rb);
//        final Button rate = (Button) rowView.findViewById(R.id.rate);

//        TextView rating = (TextView) rowView.findViewById(R.id.rating_tv);

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

        final List<Day> listx = item.getDays();
        for (Day d : listx) {
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

        title.setText(global.getUser().getFirstName() + " " + global.getUser().getLastName().substring(0, 1) + ".");
        price.setText("$" + item.getMaxPrice() + "/h");
        desc.setText(item.getInformation());

        //        RATING
//        ratingBar.setRating(TextUtils.rand());
//        rating.setText(String.valueOf(TextUtils.rand()));
        Button accept = (Button) rowView.findViewById(R.id.accept);
        Button decline = (Button) rowView.findViewById(R.id.decline);
        Button wait = (Button) rowView.findViewById(R.id.no_answer);

        final Button sendMessage = (Button) rowView.findViewById(R.id.sendMessage);
        final Button optionButton = (Button) rowView.findViewById(R.id.optionsButton);
        final Button optionButton2 = (Button) rowView.findViewById(R.id.optionsButton2);

        final LinearLayout decision = (LinearLayout) rowView.findViewById(R.id.give_answer);
        final Button accepted = (Button) rowView.findViewById(R.id.accepted);
        final LinearLayout after_answer = (LinearLayout) rowView.findViewById(R.id.after_answer);
        final LinearLayout declinedLayout = (LinearLayout) rowView.findViewById(R.id.declinedLayout);

        final TextView textViewSent = (TextView) rowView.findViewById(R.id.textViewSent);

        if (global.getApplication().getType().equals("0")) {
            textViewSent.setText("You sent an offer to");
        } else {
            textViewSent.setText("You received an offer from");
        }

        if (global.getApplication().getResult().equals("0")) {
            if (global.getApplication().getType().equals("0")) {
                decision.setVisibility(View.GONE);
                declinedLayout.setVisibility(View.GONE);
                after_answer.setVisibility(View.GONE);
                wait.setVisibility(View.VISIBLE);
            } else {
                decision.setVisibility(View.VISIBLE);
                declinedLayout.setVisibility(View.GONE);
                after_answer.setVisibility(View.GONE);
                wait.setVisibility(View.GONE);
            }
        } else if (global.getApplication().getResult().equals("1")) {
            decision.setVisibility(View.GONE);
            wait.setVisibility(View.GONE);
            declinedLayout.setVisibility(View.GONE);
            after_answer.setVisibility(View.VISIBLE);
        } else {
            //TODO:REFUSED
            declinedLayout.setVisibility(View.VISIBLE);
            decision.setVisibility(View.GONE);
            wait.setVisibility(View.GONE);
            after_answer.setVisibility(View.GONE);
        }


//        rate.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        rate.setBackground(context.getResources().getDrawable(R.drawable.round_blue_active));
//                        rate.setTextColor(context.getResources().getColor(R.color.blueHighlighted));
//                        break;
//                    case MotionEvent.ACTION_UP: {
//                        rate.setBackground(context.getResources().getDrawable(R.drawable.round_blue));
//                        rate.setTextColor(0xFFFFFFFF);
//                        Log.d("CLiCK", "onTouch: ");
////                        if (!clicked) {
////                            progress = new ProgressDialog(JobForCNA.this);
////                            progress.setMessage("Loading, please wait...");
////                            progress.setCancelable(false);
////                            progress.show();
////                            clicked = true;
////                            SomeRandomeClass.AddToList("userid", ((Global) getApplication()).getCurrentUser().getId());
////                            SomeRandomeClass.AddToList("jobid", job.getId());
////                            SomeRandomeClass.AddToList("invite", "0");
////                            Call call = api.sendJobTouser(SomeRandomeClass.GetData());
////                            call.enqueue(new Callback() {
////                                @Override
////                                public void onResponse(Call call, Response response) {
////                                    checkApplied();
////                                    if (progress != null) progress.dismiss();
////                                    clicked = false;
////                                    finish();
////                                }
////
////                                @Override
////                                public void onFailure(Call call, Throwable t) {
////
////                                }
////                            });
////                            clicked = false;
////                        }
//                        break;
//                    }
//                }
//                return false;
//            }
//        });

        wait.setOnClickListener(new View.OnClickListener() {
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
                                                            ((AplicationsCNAActivity) context).setTextVisible();
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
                itemMess.setPartner(item.getByUser());
                conversationIntent.putExtra("mess", itemMess);
                context.startActivity(conversationIntent);
            }
        });

        optionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: OPTIONS
                context.setTheme(R.style.ActionSheetStyleiOS7);

                ActionSheet.createBuilder(context, ((AplicationsCNAActivity) context).getSupportFragmentManager())
                        .setCancelButtonTitle("Cancel")
                        .setOtherButtonTitles("Report", "Delete this application")
                        .setCancelableOnTouchOutside(true)
                        .setListener(new ActionSheet.ActionSheetListener() {
                            @Override
                            public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

                            }

                            @Override
                            public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                                /*if(index == 0){
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    intent.setData(Uri.parse("tel:" + global.getUser().getPhone()));
                                    context.startActivity(intent);
                                }*/
                                if (index == 0) {
                                    API api = APIFactory.createAPI();
                                    SomeRandomeClass.AddToList("userid", ((Global) ((AplicationsCNAActivity) context).getApplication()).getCurrentUser().getId());
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
                                                        ((AplicationsCNAActivity) context).setTextVisible();
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

        optionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.setTheme(R.style.ActionSheetStyleiOS7);

                ActionSheet.createBuilder(context, ((AplicationsCNAActivity) context).getSupportFragmentManager())
                        .setCancelButtonTitle("Cancel")
                        .setOtherButtonTitles("Report", "Delete this application")
                        .setCancelableOnTouchOutside(true)
                        .setListener(new ActionSheet.ActionSheetListener() {
                            @Override
                            public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

                            }

                            @Override
                            public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                                /*if(index == 0){
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    intent.setData(Uri.parse("tel:" + global.getUser().getPhone()));
                                    context.startActivity(intent);
                                }*/
                                if (index == 0) {
                                    API api = APIFactory.createAPI();
                                    SomeRandomeClass.AddToList("userid", ((Global) ((AplicationsCNAActivity) context).getApplication()).getCurrentUser().getId());
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
                                                        ((AplicationsCNAActivity) context).setTextVisible();
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

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AplicationsCNAActivity) context).block.setVisibility(View.VISIBLE);
                SomeRandomeClass.AddToList("userid", ((Global) context.getApplicationContext()).getCurrentUser().getId());
                SomeRandomeClass.AddToList("application_id", global.getApplication().getId());
                API api = APIFactory.createAPI();
                Call call = api.applicationAccept(SomeRandomeClass.GetData());
                final Animation out = new AlphaAnimation(1.0f, 0.0f);
                out.setDuration(200);
                out.setFillAfter(true);
                decision.startAnimation(out);

                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {

                        decision.setVisibility(View.GONE);
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
                                                ((AplicationsCNAActivity) context).runOnUiThread(new Runnable() {
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
                                                                        ((AplicationsCNAActivity) context).block.setVisibility(View.GONE);

                                                                        sendMessage.setOnClickListener(new View.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(View v) {
                                                                                Intent conversationIntent = new Intent(context, ConversationActivity.class);
                                                                                Messegess itemMess = new Messegess();
                                                                                itemMess.setPartner(item.getByUser());
                                                                                conversationIntent.putExtra("mess", itemMess);
                                                                                context.startActivity(conversationIntent);
                                                                            }
                                                                        });

                                                                        optionButton.setOnClickListener(new View.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(View v) {
                                                                                //TODO: OPTIONS
                                                                                context.setTheme(R.style.ActionSheetStyleiOS7);

                                                                                ActionSheet.createBuilder(context, ((AplicationsCNAActivity) context).getSupportFragmentManager())
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
                                                                                                    SomeRandomeClass.AddToList("userid", ((Global) ((AplicationsCNAActivity) context).getApplication()).getCurrentUser().getId());
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
                                                                                                                        ((AplicationsCNAActivity) context).setTextVisible();
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
                        Call<ApplicationsIndResponse> callx = api.myapplications(SomeRandomeClass.GetData());
                        callx.enqueue(new Callback<ApplicationsIndResponse>() {
                            @Override
                            public void onResponse(Call<ApplicationsIndResponse> callx, Response<ApplicationsIndResponse> response) {
                                if (response.body() != null) {
                                    list = response.body().getResponse();
                                    notifyDataSetChanged();
                                    synchronized (adapterContext) {
                                        adapterContext.notify();
                                    }
                                    if (list.size() == 0)
                                        ((AplicationsCNAActivity) context).setTextVisible();
                                }
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
