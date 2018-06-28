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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.midnight.healthcare.Activity.LoginActivity;
import com.midnight.healthcare.Activity.RegisterActivity;
import com.midnight.healthcare.Global;
import com.midnight.healthcare.Models.RegisterModel;
import com.midnight.healthcare.R;

/**
 * Created by TUSK.ONE on 8/25/16.
 */
public class RegStep1Fragment extends Fragment {
    ImageView individualImg;
    ImageView profImg;
    TextView individualText;
    TextView proffesionalText;

    TextView regText;
    Button next;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.registration_step1_fragment, null);
        ((RelativeLayout) view.findViewById(R.id.exit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        individualText = (TextView) view.findViewById(R.id.individualText);
        proffesionalText = (TextView) view.findViewById(R.id.professionalText);
        individualImg = (ImageView) view.findViewById(R.id.register_individul);
        profImg = (ImageView) view.findViewById(R.id.register_professional);
        regText = (TextView) view.findViewById(R.id.registration_step1_text);

        next = (Button) view.findViewById(R.id.step1_next);
        ((Global) getActivity().getApplication()).setRegModel(new RegisterModel());


        individualImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                individualImg.setImageResource(R.drawable.individual_selected_img);
                profImg.setImageResource(R.drawable.register_profession);
                individualText.setTextColor(getResources().getColor(R.color.blueHighlighted));
                proffesionalText.setTextColor(0xFF000000);
                next.setVisibility(View.VISIBLE);
                regText.setVisibility(View.GONE);

                next.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch(event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                next.setBackground(getResources().getDrawable(R.drawable.round_blue_active));
                                next.setTextColor(getResources().getColor(R.color.blueHighlighted));
                                break;
                            case MotionEvent.ACTION_UP: {
                                next.setBackground(getResources().getDrawable(R.drawable.round_blue));
                                next.setTextColor(0xFFFFFFFF);
                                ((Global) getActivity().getApplication()).getRegModel().setType(0);
                                ((RegisterActivity) getActivity()).addFragmentToBakcStack(new RegStep2IndividualFragment());
                                break;
                            }
                        }
                        return false;
                    }
                });
            }
        });


        profImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                individualImg.setImageResource(R.drawable.register_individul);
                profImg.setImageResource(R.drawable.professional_selected_img);
                proffesionalText.setTextColor(getResources().getColor(R.color.blueHighlighted));
                individualText.setTextColor(0xFF000000);
                next.setVisibility(View.VISIBLE);
                regText.setVisibility(View.GONE);
                next.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch(event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                next.setBackground(getResources().getDrawable(R.drawable.round_blue_active));
                                next.setTextColor(getResources().getColor(R.color.blueHighlighted));
                                break;
                            case MotionEvent.ACTION_UP: {
                                next.setBackground(getResources().getDrawable(R.drawable.round_blue));
                                next.setTextColor(0xFFFFFFFF);

                                ((Global) getActivity().getApplication()).getRegModel().setType(1);

                                ((RegisterActivity) getActivity()).addFragmentToBakcStack(new RegStep2ProfessionalFragment());
                                break;
                            }
                        }
                        return false;
                    }
                });
            }
        });





        return view;
    }

}
