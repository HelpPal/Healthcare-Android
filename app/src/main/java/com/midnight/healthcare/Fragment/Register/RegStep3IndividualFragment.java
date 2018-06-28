package com.midnight.healthcare.Fragment.Register;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.midnight.healthcare.Activity.LoginActivity;
import com.midnight.healthcare.Activity.RegisterActivity;
import com.midnight.healthcare.Global;
import com.midnight.healthcare.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TUSK.ONE on 8/25/16.
 */
public class RegStep3IndividualFragment extends Fragment {
    EditText fname;
    EditText lName;
    ImageView unchecked;
    ImageView checkSign;
    TextView termsAndConditions;
    LinearLayout adress;
    int PLACE_PICKER_REQUEST = 1;
    Button next;
    TextView adr;
    TextView stateZip;

    public EditText addressET;
    public EditText cityET;
    public EditText zipEt;
    public EditText stateET;

    ImageView viewOnMap;

    Boolean checked = false;

    ProgressDialog dialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.registration_step3_individual_fragement, null);
        ((RelativeLayout) view.findViewById(R.id.exit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        fname = (EditText) view.findViewById(R.id.register_step3_first_name);
        lName = (EditText) view.findViewById(R.id.register_step3_second_name);
//        adress = (Edi) view.findViewById(R.id.register_step3_address);
        next = (Button) view.findViewById(R.id.next_step_3);

        addressET = (EditText) view.findViewById(R.id.register_step3_address_et);
        cityET = (EditText) view.findViewById(R.id.register_step3_city_et);
        stateET = (EditText) view.findViewById(R.id.register_step3_state_et);
        zipEt = (EditText) view.findViewById(R.id.register_step3_zip_et);
//        adr = (TextView) view.findViewById(R.id.register_step3_adr);
//        stateZip = (TextView) view.findViewById(R.id.register_step3_zip);

        viewOnMap = (ImageView) view.findViewById(R.id.view_on_map);

        termsAndConditions = (TextView) view.findViewById(R.id.termsAndConditionsLink);
        unchecked = (ImageView) view.findViewById(R.id.termsChecked);
        checkSign = (ImageView) view.findViewById(R.id.checkSign);

        next.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        next.setBackground(getResources().getDrawable(R.drawable.round_blue_active));
                        next.setTextColor(getResources().getColor(R.color.blueHighlighted));
                        break;
                    case MotionEvent.ACTION_UP: {
                        next.setBackground(getResources().getDrawable(R.drawable.round_blue));
                        next.setTextColor(0xFFFFFFFF);
                        if (fname.getText().toString().equals("") ||
                                lName.getText().toString().equals("") ||
                                cityET.getText().toString().equals("") ||
                                addressET.getText().toString().equals("") ||
                                stateET.getText().toString().equals("")) {
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
                            if (!checked) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setTitle("Terms and Conditions!")
                                        .setMessage("Please read and accept terms and conditions to continue.")
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

                                hideKeyboard();
                                ((Global) getActivity().getApplication()).getRegModel().setFirstName(fname.getText().toString());
                                ((Global) getActivity().getApplication()).getRegModel().setLastName(lName.getText().toString());
                                ((RegisterActivity) getActivity()).addFragmentToBakcStack(new RegStep4());
                                getCoordinatesFromAddress();
                            }

                        }
                        break;
                    }
                }
                return false;
            }
        });

//        adress.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//            }
//        });

        viewOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new ProgressDialog(getActivity());
                dialog.setMessage("Loading, please wait...");
                new MyTask(dialog).execute();
            }
        });

        unchecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checked) {
                    checked = true;
                    unchecked.setImageDrawable(getResources().getDrawable(R.drawable.checked_background));
                    checkSign.setVisibility(View.VISIBLE);
                } else {
                    checked = false;
                    unchecked.setImageDrawable(getResources().getDrawable(R.drawable.uncheck));
                    checkSign.setVisibility(View.GONE);
                }
            }
        });

        termsAndConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RegisterActivity) getActivity()).addFragmentToBakcStack(new TermsAndConditionsFragment());
            }
        });


        return view;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                //Place place = PlacePicker.getPlace(data, getActivity());
                final Place place = PlacePicker.getPlace(getActivity(), data);
                //adr.setText(place.getAddress().toString());
                ((Global) getActivity().getApplication()).getRegModel().setLat(String.valueOf(place.getLatLng().latitude));
                ((Global) getActivity().getApplication()).getRegModel().setLongitudine(String.valueOf(place.getLatLng().longitude));

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Geocoder gcd = new Geocoder(getActivity());
                        List<Address> addresses = new ArrayList<>();
                        try {
                            addresses = gcd.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        final String getThoroughfare = addresses.get(0).getThoroughfare();
                        final String getSubThoroughfare = addresses.get(0).getSubThoroughfare();
                        final String getLocality = addresses.get(0).getLocality();
                        final String getPostalCode = addresses.get(0).getPostalCode();
                        final String getAdminArea = addresses.get(0).getAdminArea();

                        final String adddressToShow = addresses.get(0).getAddressLine(0);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ((Global) getActivity().getApplication()).getRegModel().setAddressToShow(adddressToShow);
                                ((Global) getActivity().getApplication()).getRegModel().setAddress(getThoroughfare + ", " + getSubThoroughfare);
                                ((Global) getActivity().getApplication()).getRegModel().setZipToShow(getPostalCode);
                                ((Global) getActivity().getApplication()).getRegModel().setCity(getLocality);
                                ((Global) getActivity().getApplication()).getRegModel().setState(getAdminArea);

                                zipEt.setText(getPostalCode);
                                addressET.setText(getThoroughfare + ", " + getSubThoroughfare);
                                cityET.setText(getLocality);
                                stateET.setText(getAdminArea);
                            }
                        });
                    }
                }).start();
            }
        }
    }

    public class MyTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog progress;

        public MyTask(ProgressDialog progress) {
            this.progress = progress;
        }

        public void onPreExecute() {
            progress.show();
        }

        public Void doInBackground(Void... unused) {

            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            try {
                startActivityForResult(builder.build((getActivity())), PLACE_PICKER_REQUEST);
            } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }

            return null;
        }

        public void onPostExecute(Void unused) {

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (dialog != null) dialog.dismiss();
    }

    public void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void getCoordinatesFromAddress() {
        Geocoder geocoder = new Geocoder(getActivity());
        double latitude;
        double longitude;
        String addressFromEts = addressET.getText().toString().trim() + ", "
                + cityET.getText().toString().trim() + ", "
                + stateET.getText().toString().trim() + ", "
                + zipEt.getText().toString().trim();
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocationName(
                    addressFromEts, 1);
            longitude = addresses.get(0).getLongitude();
            latitude = addresses.get(0).getLatitude();

            ((Global) getActivity().getApplication()).getRegModel().setAddress(addressFromEts);
            ((Global) getActivity().getApplication()).getRegModel().setLat(String.valueOf(latitude));
            ((Global) getActivity().getApplication()).getRegModel().setLongitudine(String.valueOf(longitude));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
