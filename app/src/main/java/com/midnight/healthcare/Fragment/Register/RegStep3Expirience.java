package com.midnight.healthcare.Fragment.Register;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.midnight.healthcare.Activity.EditProfileActivity;
import com.midnight.healthcare.Activity.LoginActivity;
import com.midnight.healthcare.Activity.RegisterActivity;
import com.midnight.healthcare.Global;
import com.midnight.healthcare.R;

/**
 * Created by TUSK.ONE on 9/2/16.
 */
public class RegStep3Expirience extends Fragment {

    Button done;

    LinearLayout less1Button;
    LinearLayout years25Button;
    LinearLayout years57Button;
    LinearLayout years710Button;
    LinearLayout years1015Button;
    LinearLayout years1520Button;
    LinearLayout more20Button;

    ImageView less1Image;
    ImageView years25Image;
    ImageView years57Image;
    ImageView years710Image;
    ImageView years1015Image;
    ImageView years1520Image;
    ImageView more20Image;

    TextView less1Text;
    TextView years25Text;
    TextView years57Text;
    TextView years710Text;
    TextView years1015Text;
    TextView years1520Text;
    TextView more20Text;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.registration_step3_exp, null);
        ((RelativeLayout) view.findViewById(R.id.exit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity().findViewById(R.id.mainLayout) != null){
                    ((EditProfileActivity)getActivity()).setMainVisible();
                }
                getFragmentManager().popBackStack();
            }
        });
        done = (Button) view.findViewById(R.id.exp_done);

        less1Button = (LinearLayout) view.findViewById(R.id.less1Button);
        years25Button = (LinearLayout) view.findViewById(R.id.years25Button);
        years57Button = (LinearLayout) view.findViewById(R.id.years57Button);
        years710Button = (LinearLayout) view.findViewById(R.id.years710Button);
        years1015Button = (LinearLayout) view.findViewById(R.id.years1015Button);
        years1520Button = (LinearLayout) view.findViewById(R.id.years1520Button);
        more20Button = (LinearLayout) view.findViewById(R.id.more20Button);

        less1Image = (ImageView) view.findViewById(R.id.less1Image);
        years25Image = (ImageView) view.findViewById(R.id.years25Image);
        years57Image = (ImageView) view.findViewById(R.id.years57Image);
        years710Image = (ImageView) view.findViewById(R.id.years710Image);
        years1015Image = (ImageView) view.findViewById(R.id.years1015Image);
        years1520Image = (ImageView) view.findViewById(R.id.years1520Image);
        more20Image = (ImageView) view.findViewById(R.id.more20Image);

        less1Text = (TextView) view.findViewById(R.id.less1Text);
        years25Text = (TextView) view.findViewById(R.id.years25Text);
        years57Text = (TextView) view.findViewById(R.id.years57Text);
        years710Text = (TextView) view.findViewById(R.id.years710Text);
        years1015Text = (TextView) view.findViewById(R.id.years1015Text);
        years1520Text = (TextView) view.findViewById(R.id.years1520Text);
        more20Text = (TextView) view.findViewById(R.id.more20Text);

        if(getActivity().findViewById(R.id.mainLayout) != null){
            ((EditProfileActivity)getActivity()).setMainGone();
        }

        uncheckEverything();

        int i  = ((Global) getActivity().getApplication()).getRegModel().getExpirience();
        if(i == 1){
            less1Image.setImageDrawable(getResources().getDrawable(R.drawable.checked_time));
            less1Text.setTextColor(getResources().getColor(R.color.blueHighlighted));
        }
        else if(i == 2){
            years25Image.setImageDrawable(getResources().getDrawable(R.drawable.checked_time));
            years25Text.setTextColor(getResources().getColor(R.color.blueHighlighted));
        }
        else if(i == 3){
            years57Image.setImageDrawable(getResources().getDrawable(R.drawable.checked_time));
            years57Text.setTextColor(getResources().getColor(R.color.blueHighlighted));
        }
        else if(i == 4){
            years710Image.setImageDrawable(getResources().getDrawable(R.drawable.checked_time));
            years710Text.setTextColor(getResources().getColor(R.color.blueHighlighted));
        }
        else if(i == 5){
            years1015Image.setImageDrawable(getResources().getDrawable(R.drawable.checked_time));
            years1015Text.setTextColor(getResources().getColor(R.color.blueHighlighted));
        }
        else if(i == 6){
            years1520Image.setImageDrawable(getResources().getDrawable(R.drawable.checked_time));
            years1520Text.setTextColor(getResources().getColor(R.color.blueHighlighted));
        }
        else if(i == 7){
            more20Image.setImageDrawable(getResources().getDrawable(R.drawable.checked_time));
            more20Text.setTextColor(getResources().getColor(R.color.blueHighlighted));
        }


        less1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uncheckEverything();
                less1Text.setTextColor(getResources().getColor(R.color.blueHighlighted));
                less1Image.setImageDrawable(getResources().getDrawable(R.drawable.checked_time));

                ((Global) getActivity().getApplication()).getRegModel().setExpirience(1);
            }
        });

        years25Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uncheckEverything();
                years25Image.setImageDrawable(getResources().getDrawable(R.drawable.checked_time));
                years25Text.setTextColor(getResources().getColor(R.color.blueHighlighted));

                ((Global) getActivity().getApplication()).getRegModel().setExpirience(2);
            }
        });

        years57Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uncheckEverything();
                years57Image.setImageDrawable(getResources().getDrawable(R.drawable.checked_time));
                years57Text.setTextColor(getResources().getColor(R.color.blueHighlighted));

                ((Global) getActivity().getApplication()).getRegModel().setExpirience(3);
            }
        });

        years710Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uncheckEverything();
                years710Image.setImageDrawable(getResources().getDrawable(R.drawable.checked_time));
                years710Text.setTextColor(getResources().getColor(R.color.blueHighlighted));

                ((Global) getActivity().getApplication()).getRegModel().setExpirience(4);
            }
        });

        years1015Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uncheckEverything();
                years1015Image.setImageDrawable(getResources().getDrawable(R.drawable.checked_time));
                years1015Text.setTextColor(getResources().getColor(R.color.blueHighlighted));

                ((Global) getActivity().getApplication()).getRegModel().setExpirience(5);
            }
        });

        years1520Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uncheckEverything();
                years1520Image.setImageDrawable(getResources().getDrawable(R.drawable.checked_time));
                years1520Text.setTextColor(getResources().getColor(R.color.blueHighlighted));

                ((Global) getActivity().getApplication()).getRegModel().setExpirience(6);
            }
        });

        more20Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uncheckEverything();
                more20Image.setImageDrawable(getResources().getDrawable(R.drawable.checked_time));
                more20Text.setTextColor(getResources().getColor(R.color.blueHighlighted));

                ((Global) getActivity().getApplication()).getRegModel().setExpirience(7);
            }
        });

        done.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        done.setBackground(getResources().getDrawable(R.drawable.round_blue_active));
                        done.setTextColor(getResources().getColor(R.color.blueHighlighted));
                        break;
                    case MotionEvent.ACTION_UP: {
                        done.setBackground(getResources().getDrawable(R.drawable.round_blue));
                        done.setTextColor(0xFFFFFFFF);
                        if(getActivity().findViewById(R.id.mainLayout) != null){
                            ((EditProfileActivity)getActivity()).setMainVisible();
                            ((EditProfileActivity)getActivity()).onResume();
                        }
                        getFragmentManager().popBackStack();
                        break;
                    }
                }
                return false;
            }
        });



        return view;
    }

    private void uncheckEverything(){
        less1Image.setImageDrawable(getResources().getDrawable(R.drawable.uncheck));
        years25Image.setImageDrawable(getResources().getDrawable(R.drawable.uncheck));
        years57Image.setImageDrawable(getResources().getDrawable(R.drawable.uncheck));
        years710Image.setImageDrawable(getResources().getDrawable(R.drawable.uncheck));
        years1015Image.setImageDrawable(getResources().getDrawable(R.drawable.uncheck));
        years1520Image.setImageDrawable(getResources().getDrawable(R.drawable.uncheck));
        more20Image.setImageDrawable(getResources().getDrawable(R.drawable.uncheck));

        less1Text.setTextColor(0xFF000000);
        years25Text.setTextColor(0xFF000000);
        years57Text.setTextColor(0xFF000000);
        years710Text.setTextColor(0xFF000000);
        years1015Text.setTextColor(0xFF000000);
        years1520Text.setTextColor(0xFF000000);
        more20Text.setTextColor(0xFF000000);
    }

}
