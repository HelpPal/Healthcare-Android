package com.midnight.healthcare.Fragment.Register;

import android.app.Activity;
import android.app.Fragment;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.os.IBinder;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.midnight.healthcare.API.API;
import com.midnight.healthcare.API.APIFactory;
import com.midnight.healthcare.Activity.EditProfileActivity;
import com.midnight.healthcare.Activity.LoginActivity;
import com.midnight.healthcare.Activity.RegisterActivity;
import com.midnight.healthcare.Config;
import com.midnight.healthcare.Global;
import com.midnight.healthcare.Models.RegisterModel;
import com.midnight.healthcare.R;
import com.midnight.healthcare.SomeRandomeClass;
import com.midnight.healthcare.utils.TextUtils;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by TUSK.ONE on 9/3/16.
 */

public class RegStep4 extends Fragment {
    Button subs;

    int BILLING_RESPONSE_RESULT_OK = 0;

    ProgressDialog progress;

    String developerPayload = java.util.UUID.randomUUID().toString();

    IInAppBillingService mService;

    ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name,
                                       IBinder service) {
            mService = IInAppBillingService.Stub.asInterface(service);
        }
    };

    Boolean test = false;

    Boolean clicked = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.registration_step4_individual_fragment, null);
        final Config configClass = new Config();
        subs = (Button) view.findViewById(R.id.subscribe);

        Intent serviceIntent =
                new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        getActivity().bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);


        ((RelativeLayout) view.findViewById(R.id.exit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        subs.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        subs.setBackground(getResources().getDrawable(R.drawable.round_blue_active));
                        subs.setTextColor(getResources().getColor(R.color.blueHighlighted));
                        break;
                    case MotionEvent.ACTION_UP: {
                        subs.setBackground(getResources().getDrawable(R.drawable.round_blue));
                        subs.setTextColor(0xFFFFFFFF);
                        progress = new ProgressDialog(getActivity());
                        progress.setMessage("Loading, please wait...");
                        progress.show();

                        if (configClass.getTest() == 1) {
                            test = true;
                        } else {
                            test = false;
                        }

                        Bundle activeSubs = null;
                        try {
                            activeSubs = mService.getPurchases(3, configClass.getPackageName(),
                                    "subs", null);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }

//                        ArrayList<String> subscriptions = activeSubs.getStringArrayList("INAPP_PURCHASE_ITEM_LIST");
//                        if(subscriptions.size() == 0){
//                            test = false;
//                        }
//                        else{
//                            test = true;
//                        }

                        if (!clicked) {
                            clicked = true;
                            if (test) {
                                afterPurchasement();
                            } else {
                                try {
                                    Bundle bundle = mService.getBuyIntent(3, configClass.getPackageName(),
                                            configClass.getSKU(), "subs", developerPayload);
                                    PendingIntent pendingIntent = bundle.getParcelable("BUY_INTENT");
                                    if (bundle.getInt("BILLING_RESPONSE_RESULT_OK") == BILLING_RESPONSE_RESULT_OK) {
                                        // Start purchase flow (this brings up the Google Play UI).
                                        // Result will be delivered through onActivityResult().

                                        progress.dismiss();

                                        getActivity().startIntentSenderForResult(pendingIntent.getIntentSender(), 1001, new Intent(),
                                                Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0));
                                    } else {
                                        progress.dismiss();
                                    }

                                } catch (RemoteException e) {
                                    progress.dismiss();
                                    e.printStackTrace();
                                } catch (IntentSender.SendIntentException e) {
                                    e.printStackTrace();
                                }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        progress.dismiss();

        if (resultCode == Activity.RESULT_OK) {
            afterPurchasement();
        } else {
            Toast.makeText(getActivity(), "AN ERROR HAS OCCURED!", Toast.LENGTH_LONG).show();
            progress.dismiss();
        }
    }

    public void afterPurchasement() {
        API api = APIFactory.createAPI();
        RegisterModel mod = ((Global) getActivity().getApplication()).getRegModel();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (mod.getProfile_img() != null) {
            mod.getProfile_img().compress(Bitmap.CompressFormat.JPEG, 30, baos);
            byte[] imageBytes = baos.toByteArray();
            String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            SomeRandomeClass.AddToList("profile_img", encodedImage);

        }
        SomeRandomeClass.AddToList("type", String.valueOf(mod.getType()));
        SomeRandomeClass.AddToList("password", mod.getPassword());
        SomeRandomeClass.AddToList("email", mod.getEmail());
        SomeRandomeClass.AddToList("first_name", mod.getFirstName());
        SomeRandomeClass.AddToList("last_name", mod.getLastName());
        SomeRandomeClass.AddToList("lat", mod.getLat());
        SomeRandomeClass.AddToList("long", mod.getLongitudine());
        SomeRandomeClass.AddToList("experience", String.valueOf(mod.getExpirience()));
        SomeRandomeClass.AddToList("avalable", String.valueOf(mod.getAvalable()));
        SomeRandomeClass.AddToList("price_min", mod.getPrice_min());
        SomeRandomeClass.AddToList("price_max", mod.getPrice_max());
        SomeRandomeClass.AddToList("phone", mod.getPhone());
        SomeRandomeClass.AddToList("gender", String.valueOf(mod.getGender()));
        SomeRandomeClass.AddToList("description", mod.getDescription());
        SomeRandomeClass.AddToList("birthday", mod.getBirthday());

        SomeRandomeClass.AddToList("zip", mod.getZipToShow());
        Log.d("ZIP ", "afterPurchasement: " + mod.getZipToShow());
        SomeRandomeClass.AddToList("city", mod.getCity());
        SomeRandomeClass.AddToList("state", mod.getState());
        SomeRandomeClass.AddToList("adress", mod.getAddress());

        String skills = "";
        for (String s : mod.getSkilssid()) {
            Log.d("skills", s);
            skills = skills + s + ",";
        }

        if (skills.length() == 0) {
            SomeRandomeClass.AddToList("skills", "");
        } else {
            SomeRandomeClass.AddToList("skills", skills.substring(0, skills.length() - 1));
        }

        String otherSkills = "";
        for (String s : mod.getOtherSkills()) {
            Log.d("otherSkill", s);
            otherSkills = otherSkills + s + ",";
        }

        if (otherSkills.length() == 0) {
            SomeRandomeClass.AddToList("other_skills", "");
        } else {
            SomeRandomeClass.AddToList("other_skills", otherSkills.substring(0, otherSkills.length() - 1));
        }

        Call<Void> call = api.register(SomeRandomeClass.GetData());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("onResponse", "onResponse: ");
                if (response.code() == 500) {
                    Log.d("response", "Status code: 500");
                    clicked = false;
                    Toast.makeText(getActivity(), "AN ERROR HAS OCCURRED!", Toast.LENGTH_LONG).show();
                    progress.dismiss();
                } else {
                    ((RegisterActivity) getActivity()).addFragmentToBakcStack(new RegistrationFinishFragment());
                    clicked = false;
                    progress.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                clicked = false;
                Toast.makeText(getActivity(), "AN ERROR HAS OCCURRED!", Toast.LENGTH_LONG).show();
                progress.dismiss();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mService != null) {
            getActivity().unbindService(mServiceConn);
        }
    }
}
