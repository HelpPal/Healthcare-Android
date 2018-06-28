package com.midnight.healthcare.Fragment.Register;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.midnight.healthcare.API.API;
import com.midnight.healthcare.API.APIFactory;
import com.midnight.healthcare.API.passResponse;
import com.midnight.healthcare.Activity.LoginActivity;
import com.midnight.healthcare.Activity.RegisterActivity;
import com.midnight.healthcare.Global;
import com.midnight.healthcare.R;
import com.midnight.healthcare.SomeRandomeClass;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by TUSK.ONE on 8/25/16.
 */
public class RegStep2ProfessionalFragment extends Fragment {
    Button next;
    EditText email;
    EditText pass;
    EditText repeatpass;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.registration_step2_proffesional_fragment, null);
        ((RelativeLayout) view.findViewById(R.id.exit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        next = (Button) view.findViewById(R.id.step2_next_prof);
        email = (EditText) view.findViewById(R.id.register_step2_email2);
        pass = (EditText) view.findViewById(R.id.register_step2_pass2);
        repeatpass =  (EditText) view.findViewById(R.id.register_step2_pass_repeat);

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
                        if (email.getText().toString().equals("") || pass.getText().toString().equals("")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("Error!")
                                    .setMessage("Please fill all the fields!")
                                    .setCancelable(false)
                                    .setNegativeButton("ОК",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        } else {
                            if(isValidEmail(email.getText().toString())) {
                                if(pass.getText().toString().length() > 4) {
                                    if (pass.getText().toString().equals(repeatpass.getText().toString())) {
                                        API api = APIFactory.createAPI();
                                        SomeRandomeClass.AddToList("email", email.getText().toString());
                                        Call<passResponse> call = api.checkEmail(SomeRandomeClass.GetData());
                                        call.enqueue(new Callback<passResponse>() {
                                            @Override
                                            public void onResponse(Call<passResponse> call, Response<passResponse> response) {
                                                if(response.body() != null){
                                                    if(response.body().getResponse().equals("0")){
                                                        ((Global) getActivity().getApplication()).getRegModel().setEmail(email.getText().toString());
                                                        ((Global) getActivity().getApplication()).getRegModel().setPassword(pass.getText().toString());
                                                        ((RegisterActivity) getActivity()).addFragmentToBakcStack(new RegStep3ProfessionalFragment());
                                                    }
                                                    else{
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                                        builder.setTitle("Error!")
                                                                .setMessage("This email is already used!")
                                                                .setCancelable(false)
                                                                .setNegativeButton("ОК",
                                                                        new DialogInterface.OnClickListener() {
                                                                            public void onClick(DialogInterface dialog, int id) {
                                                                                dialog.cancel();
                                                                            }
                                                                        });
                                                        AlertDialog alert = builder.create();
                                                        alert.show();
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<passResponse> call, Throwable t) {

                                            }
                                        });
                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                        builder.setTitle("Error!")
                                                .setMessage("Passwords do not match!")
                                                .setCancelable(false)
                                                .setNegativeButton("ОК",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                dialog.cancel();
                                                            }
                                                        });
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                    }
                                }
                                else{
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setTitle("Error!")
                                            .setMessage("Password is too short!")
                                            .setCancelable(false)
                                            .setNegativeButton("ОК",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.cancel();
                                                        }
                                                    });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                }
                            }
                            else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setTitle("Error!")
                                        .setMessage("Email is invalid!")
                                        .setCancelable(false)
                                        .setNegativeButton("ОК",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                    }
                                                });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        }
                        break;
                    }
                }
                return false;
            }
        });



        return view;
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
