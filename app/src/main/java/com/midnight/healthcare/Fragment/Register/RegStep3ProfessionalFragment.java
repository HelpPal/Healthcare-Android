package com.midnight.healthcare.Fragment.Register;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.midnight.healthcare.Activity.RegisterActivity;
import com.midnight.healthcare.Global;
import com.midnight.healthcare.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.midnight.healthcare.utils.TextUtils;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.soundcloud.android.crop.Crop;

import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by TUSK.ONE on 8/25/16.
 */
public class RegStep3ProfessionalFragment extends Fragment {
    RelativeLayout takePicture;
    RelativeLayout uploadPhoto;
    ImageView imageHolder;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    LinearLayout adress;
    int PLACE_PICKER_REQUEST = 1;
    TextView adr;
    ImageView femaleImage;
    ImageView maleImage;
    TextView femaleText;
    TextView maleText;
    LinearLayout femaleButton;
    LinearLayout maleButton;
    ImageView fullTimeImage;
    ImageView partTimeImage;
    LinearLayout fullTimeButton;
    LinearLayout partTimeButton;
    TextView fullTimeText;
    TextView partTimeText;
    TextView exp_text;

    EditText fName;
    EditText lName;
    EditText price1;
    EditText price2;
    EditText phone;

    public EditText addressET;
    public EditText cityET;
    public EditText zipEt;
    public EditText stateET;

    ImageView viewOnMap;
    TextView birthday;
    EditText description;
    TextView stateZip;

    Uri imageUri;

    Bitmap userAvatar;

    ImageView takeAPhotoImage;
    ImageView uploadPhotoImage;
    TextView takeAPhotoText;
    TextView uploadPhotoText;

    public static final int SELECT_PHOTO = 345;

    TextView termsAndConditionsLink;
    ImageView termsChecked;
    ImageView checkSign;

    Button next;
    Boolean camera = false;
    TextView skills;

    Boolean checked = false;

    ProgressDialog progress;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.registration_step3_professional_fragement, null);
        ((RelativeLayout) view.findViewById(R.id.exit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        takePicture = (RelativeLayout) view.findViewById(R.id.takePicture);
        uploadPhoto = (RelativeLayout) view.findViewById(R.id.uploadPhoto);
        imageHolder = (ImageView) view.findViewById(R.id.register_image_holder);
//        adr = (TextView) view.findViewById(R.id.register_step3_adr);
//        adress = (LinearLayout) view.findViewById(R.id.register_step3_address);
        exp_text = (TextView) view.findViewById(R.id.exp_text);
//        stateZip = (TextView) view.findViewById(R.id.register_step3_zip);

        termsAndConditionsLink = (TextView) view.findViewById(R.id.termsAndConditionsLink);
        termsChecked = (ImageView) view.findViewById(R.id.termsChecked);
        checkSign = (ImageView) view.findViewById(R.id.checkSign);

        femaleButton = (LinearLayout) view.findViewById(R.id.femaleButton);
        maleButton = (LinearLayout) view.findViewById(R.id.maleButton);
        femaleText = (TextView) view.findViewById(R.id.femaleText);
        maleText = (TextView) view.findViewById(R.id.maleText);
        femaleImage = (ImageView) view.findViewById(R.id.femaleImage);
        maleImage = (ImageView) view.findViewById(R.id.maleImage);

        fName = (EditText) view.findViewById(R.id.register_step3_first_name);
        lName = (EditText) view.findViewById(R.id.register_step3_second_name);
        price1 = (EditText) view.findViewById(R.id.price_range_1);
        price2 = (EditText) view.findViewById(R.id.price_range_2);
        phone = (EditText) view.findViewById(R.id.register_step3_phone);
        addressET = (EditText) view.findViewById(R.id.register_step3_address_et);
        cityET = (EditText) view.findViewById(R.id.register_step3_city_et);
        stateET = (EditText) view.findViewById(R.id.register_step3_state_et);
        zipEt = (EditText) view.findViewById(R.id.register_step3_zip_et);

        viewOnMap = (ImageView) view.findViewById(R.id.view_on_map);

        description = (EditText) view.findViewById(R.id.register_step3_description);
        birthday = (TextView) view.findViewById(R.id.birthday_text);

        next = (Button) view.findViewById(R.id.next_step_3_prof);
        skills = (TextView) view.findViewById(R.id.skills);

        fullTimeButton = (LinearLayout) view.findViewById(R.id.fullTimeButton);
        partTimeButton = (LinearLayout) view.findViewById(R.id.partTimeButton);
        fullTimeText = (TextView) view.findViewById(R.id.fullTimeText);
        partTimeText = (TextView) view.findViewById(R.id.partTimeText);
        fullTimeImage = (ImageView) view.findViewById(R.id.fullTimeImage);
        partTimeImage = (ImageView) view.findViewById(R.id.partTimeCheck);

        takeAPhotoImage = (ImageView) view.findViewById(R.id.takeAPhotoImage);
        uploadPhotoImage = (ImageView) view.findViewById(R.id.uploadPhotoImage);
        takeAPhotoText = (TextView) view.findViewById(R.id.takeAPhotoText);
        uploadPhotoText = (TextView) view.findViewById(R.id.uploadPhotoText);

        skills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((RegisterActivity) getActivity()).addFragmentToBakcStack(new RegStep3Table());
            }
        });

        next.setEnabled(true);

        termsChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checked) {
                    checked = true;
                    termsChecked.setImageDrawable(getResources().getDrawable(R.drawable.checked_background));
                    checkSign.setVisibility(View.VISIBLE);
                } else {
                    checked = false;
                    termsChecked.setImageDrawable(getResources().getDrawable(R.drawable.uncheck));
                    checkSign.setVisibility(View.GONE);
                }
            }
        });

        termsAndConditionsLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RegisterActivity) getActivity()).addFragmentToBakcStack(new TermsAndConditionsFragment());
            }
        });

        viewOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = new ProgressDialog(getActivity());
                progress.setMessage("Loading, please wait...");
                new MyTask(progress).execute();
            }
        });


        birthday.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                final Date date = new Date();
                date.setMonth(0);
                date.setYear(2000);
                date.setDate(1);
                DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        Date date2 = new Date();
                        date2.setMonth(i1);
                        date2.setYear(i);
                        date2.setDate(i2);
                        if (date.getTime() - date2.getTime() < 0) {
                            //TODO: ALERT
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("Error!")
                                    .setMessage("Your birthday can't be earlier 01.01.2000!")
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
                            String month = "";
                            switch (i1) {
                                case 0:
                                    month = "January";
                                    break;
                                case 1:
                                    month = "February";
                                    break;
                                case 2:
                                    month = "March";
                                    break;
                                case 3:
                                    month = "April";
                                    break;
                                case 4:
                                    month = "May";
                                    break;
                                case 5:
                                    month = "June";
                                    break;
                                case 6:
                                    month = "July";
                                    break;
                                case 7:
                                    month = "August";
                                    break;
                                case 8:
                                    month = "September";
                                    break;
                                case 9:
                                    month = "October";
                                    break;
                                case 10:
                                    month = "November";
                                    break;
                                case 11:
                                    month = "December";
                                    break;
                                default:
                                    break;
                            }
                            birthday.setText(month + " " + i2 + ", " + i);

                            ((Global) getActivity().getApplication()).getRegModel().setBirthday(birthday.getText().toString());
                        }
                    }
                }, 2000, 0, 1);

                dialog.show();

            }
        });


        exp_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((RegisterActivity) getActivity()).addFragmentToBakcStack(new RegStep3Expirience());
            }
        });

        maleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maleImage.setImageDrawable(getResources().getDrawable(R.drawable.checked_time));
                femaleImage.setImageDrawable(getResources().getDrawable(R.drawable.uncheck));
                maleText.setTextColor(getResources().getColor(R.color.blueHighlighted));
                femaleText.setTextColor(0xFF000000);

                ((Global) getActivity().getApplication()).getRegModel().setGender(1);
            }
        });

        femaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                femaleImage.setImageDrawable(getResources().getDrawable(R.drawable.checked_time));
                maleImage.setImageDrawable(getResources().getDrawable(R.drawable.uncheck));
                femaleText.setTextColor(getResources().getColor(R.color.blueHighlighted));
                maleText.setTextColor(0xFF000000);

                ((Global) getActivity().getApplication()).getRegModel().setGender(0);
            }
        });

        fullTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullTimeImage.setImageDrawable(getResources().getDrawable(R.drawable.checked_time));
                partTimeImage.setImageDrawable(getResources().getDrawable(R.drawable.uncheck));
                fullTimeText.setTextColor(getResources().getColor(R.color.blueHighlighted));
                partTimeText.setTextColor(0xFF000000);

                ((Global) getActivity().getApplication()).getRegModel().setAvalable(1);
            }
        });

        partTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                partTimeImage.setImageDrawable(getResources().getDrawable(R.drawable.checked_time));
                fullTimeImage.setImageDrawable(getResources().getDrawable(R.drawable.uncheck));
                partTimeText.setTextColor(getResources().getColor(R.color.blueHighlighted));
                fullTimeText.setTextColor(0xFF000000);

                ((Global) getActivity().getApplication()).getRegModel().setAvalable(0);
            }
        });

        /*takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera = true;
                dispatchTakePictureIntent();
            }
        });*/

        takePicture.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        takeAPhotoText.setTextColor(getResources().getColor(R.color.blueHighlighted));
                        takeAPhotoImage.setImageDrawable(getResources().getDrawable(R.drawable.camera_active));
                        return true;
                    case MotionEvent.ACTION_UP: {
                        takeAPhotoText.setTextColor(0xFF000000);
                        takeAPhotoImage.setImageDrawable(getResources().getDrawable(R.drawable.photo_sign));

                        camera = true;
                        dispatchTakePictureIntent();

                        break;
                    }
                    case MotionEvent.ACTION_CANCEL:
                        takeAPhotoText.setTextColor(0xFF000000);
                        takeAPhotoImage.setImageDrawable(getResources().getDrawable(R.drawable.photo_sign));
                        break;
                }
                return false;
            }
        });

        /*uploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final File root = new File(Environment.getExternalStorageDirectory() + File.separator + "Images" + File.separator);
                root.mkdirs();
                final String fname = getUniqueImageFilename();
                final File sdImageMainDirectory = new File(root, fname);

                Uri outputFileUri = Uri.fromFile(sdImageMainDirectory);

                final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

                final Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                // Create file chooser intent

                // Set camera intent to file chooser

                // On select image call onActivityResult method of activity
                startActivityForResult(galleryIntent, SELECT_PHOTO);
            }
        });*/
//        lName.addTextChangedListener();


//new TextWatcher() {
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//        }
//
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            Geocoder geocoder = new Geocoder(getActivity());
//            List<Address> addresses;
//            try {
//                addresses = geocoder.getFromLocationName(s.toString(), 1);
//
//                if (addresses.size() > 0) {
//                    double latitude = addresses.get(0).getLatitude();
//                    double longitude = addresses.get(0).getLongitude();
//                    Log.d("onTextChanged", "latitude: " + latitude);
//                    Log.d("onTextChanged", "longitude: " + longitude);
//                    List<Address> listAddresses = geocoder.getFromLocation(latitude, longitude, 1);
//                    Log.d("onTextChanged", "address: " + listAddresses.get(0).getAddressLine(0));
//                    Log.d("onTextChanged", "address: " + listAddresses.get(0).getAddressLine(1));
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        public void afterTextChanged(Editable s) {
//
//        }
//    }

        uploadPhoto.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        uploadPhotoText.setTextColor(getResources().getColor(R.color.blueHighlighted));
                        uploadPhotoImage.setImageDrawable(getResources().getDrawable(R.drawable.upload_photo_active));
                        return true;
                    case MotionEvent.ACTION_UP: {
                        uploadPhotoText.setTextColor(0xFF000000);
                        uploadPhotoImage.setImageDrawable(getResources().getDrawable(R.drawable.from_disk));

                        final File root = new File(Environment.getExternalStorageDirectory() + File.separator + "Images" + File.separator);
                        root.mkdirs();
                        final String fname = getUniqueImageFilename();
                        final File sdImageMainDirectory = new File(root, fname);

                        Uri outputFileUri = Uri.fromFile(sdImageMainDirectory);

                        final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

                        final Intent galleryIntent = new Intent();
                        galleryIntent.setType("image/*");
                        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                        // Create file chooser intent

                        // Set camera intent to file chooser

                        // On select image call onActivityResult method of activity
                        startActivityForResult(galleryIntent, SELECT_PHOTO);
                        break;
                    }

                    case MotionEvent.ACTION_CANCEL:
                        uploadPhotoText.setTextColor(0xFF000000);
                        uploadPhotoImage.setImageDrawable(getResources().getDrawable(R.drawable.from_disk));
                        break;
                }
                return false;
            }
        });

//        stateZip.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                progress = new ProgressDialog(getActivity());
//                progress.setMessage("Loading, please wait...");
//                new MyTask(progress).execute();
//            }
//        });

//        adress.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                progress = new ProgressDialog(getActivity());
//                progress.setMessage("Loading, please wait...");
//                new MyTask(progress).execute();
//            }
//        });

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
//                        birthday.getText().toString().equals("") ||
                        if (fName.getText().toString().equals("") ||
                                lName.getText().toString().equals("") ||
                                phone.getText().toString().equals("") ||
                                price1.getText().toString().equals("") ||
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
                                if (!validCellPhone(phone.getText().toString())) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setTitle("Error!")
                                            .setMessage("Phone number is invalid!")
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
                                    if (((Global) getActivity().getApplication()).getRegModel().getSkilss().size() == 0 && ((Global) getActivity().getApplication()).getRegModel().getOtherSkills().size() == 0) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                        builder.setTitle("Error!")
                                                .setMessage("You must specify at least 1 skill!")
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
                                        int price;
                                        if (price2.getText().toString().length() == 0) price = 0;
                                        else price = Integer.valueOf(price2.getText().toString());
                                        if (Integer.valueOf(price1.getText().toString()) <= price || price2.getText().toString().length() == 0) {
                                            if (getCoordinatesFromAddress()) {
                                                ((Global) getActivity().getApplication()).getRegModel().setFirstName(fName.getText().toString());
                                                ((Global) getActivity().getApplication()).getRegModel().setLastName(lName.getText().toString());
                                                ((Global) getActivity().getApplication()).getRegModel().setBirthday(birthday.getText().toString());
                                                ((Global) getActivity().getApplication()).getRegModel().setDescription(description.getText().toString());
                                                ((Global) getActivity().getApplication()).getRegModel().setPhone(phone.getText().toString());
                                                ((Global) getActivity().getApplication()).getRegModel().setPrice_min(price1.getText().toString());
                                                if (price2.getText().toString().equals("")) {
                                                    ((Global) getActivity().getApplication()).getRegModel().setPrice_max(price1.getText().toString());
                                                } else {
                                                    ((Global) getActivity().getApplication()).getRegModel().setPrice_max(price2.getText().toString());
                                                }
                                                hideKeyboard();

                                                ((Global) getActivity().getApplication()).getRegModel().setAddress(TextUtils.editTextValidation(addressET));
                                                Log.d("ZIP", "BEFORE SAVING: edittext" + TextUtils.editTextValidation(zipEt));
                                                ((Global) getActivity().getApplication()).getRegModel().setZipToShow(TextUtils.editTextValidation(zipEt));
                                                Log.d("ZIP", "AFTER SAVING: edittext" + TextUtils.editTextValidation(zipEt));
                                                Log.d("ZIP", "AFTER SAVING: edittext" + ((Global) getActivity().getApplication()).getRegModel().getZipToShow());

                                                ((Global) getActivity().getApplication()).getRegModel().setCity(TextUtils.editTextValidation(cityET));
                                                ((Global) getActivity().getApplication()).getRegModel().setState(TextUtils.editTextValidation(stateET));

                                                ((RegisterActivity) getActivity()).addFragmentToBakcStack(new RegStep4());
                                            } else {
                                                Toast.makeText(getActivity(), "Please verify your address", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                            builder.setTitle("Error!")
                                                    .setMessage("The maximal price can't be lower than the minimal price!")
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
                            }
                        }
                        break;
                    }

                    case MotionEvent.ACTION_CANCEL:
                        next.setBackground(getResources().getDrawable(R.drawable.round_blue));
                        next.setTextColor(0xFFFFFFFF);
                        break;
                }
                return false;
            }
        });
//
//
//        next.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("SKILLS", "onClick: " + ((Global) getActivity().getApplication()).getRegModel().getSkilss().size());
//                Log.d("SKILLS", "onClick: " + ((Global) getActivity().getApplication()).getRegModel().getOtherSkills().size());
//            }
//        });

        return view;
    }

    private void dispatchTakePictureIntent() {
        //Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = getActivity().getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);

        //if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
        //   startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        //}
    }

    private boolean getCoordinatesFromAddress() {
        Geocoder geocoder = new Geocoder(getActivity());
        double latitude = 0;
        double longitude = 0;
        String addressFromEts = addressET.getText().toString().trim() + ", "
                + cityET.getText().toString().trim() + ", "
                + stateET.getText().toString().trim() + ", "
                + zipEt.getText().toString().trim();
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocationName(
                    addressFromEts, 1);

            if (addresses.size() > 0) {
                longitude = addresses.get(0).getLongitude();
                latitude = addresses.get(0).getLatitude();

                ((Global) getActivity().getApplication()).getRegModel().setLat(String.valueOf(latitude));
                ((Global) getActivity().getApplication()).getRegModel().setLongitudine(
                        String.valueOf(longitude));
                return true;

            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PHOTO && resultCode == Activity.RESULT_OK) {
            Bitmap pickedImage;
            Uri selectedImageUri;
            selectedImageUri = data.getData();
            try {
                pickedImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);

                ((Global) getActivity().getApplication()).getRegModel().setProfile_img(pickedImage);

                beginCrop(selectedImageUri);

                //Bitmap thumbnail2 = Bitmap.createScaledBitmap(pickedImage, 1024, 1024, false);

                //imageHolder.setImageBitmap(thumbnail2);
            } catch (IOException e) {
                e.printStackTrace();
            }


            //cursor.close();
        } else {
            if (camera && requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
                //Bundle extras = data.getExtras();
                //Bitmap imageBitmap = (Bitmap) extras.get("data");

                try {
                    Bitmap thumbnail = MediaStore.Images.Media.getBitmap(
                            getActivity().getContentResolver(), imageUri);

                    ((Global) getActivity().getApplication()).getRegModel().setProfile_img(thumbnail);

                    beginCrop(imageUri);

                    //Bitmap thumbnail2 = Bitmap.createScaledBitmap(thumbnail, 1024, 1024, false);

                    //imageHolder.setImageBitmap(thumbnail2);
                    camera = false;
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //((Global) getActivity().getApplication()).getRegModel().setProfile_img(imageBitmap);
                //imageHolder.setImageBitmap(imageBitmap);
                //camera = false;
            } else {
                if (resultCode == Activity.RESULT_OK) {
                    final Place place = PlacePicker.getPlace(data, getActivity());
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

                                final String getThoroughfare = addresses.get(0).getThoroughfare();
                                final String getSubThoroughfare = addresses.get(0).getSubThoroughfare();
                                final String getLocality = addresses.get(0).getLocality();
                                final String getPostalCode = addresses.get(0).getPostalCode();
                                final String getAdminArea = addresses.get(0).getAdminArea();


                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {


                                        zipEt.setText(getPostalCode);
                                        addressET.setText(getThoroughfare + ", " + getSubThoroughfare);
                                        cityET.setText(getLocality);
                                        stateET.setText(getAdminArea);
                                    }
                                });

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        restoreInfo();
    }

    public boolean validCellPhone(String phoneNumber) {
        if (!phoneNumber.contains("+")) {
            phoneNumber = "+" + phoneNumber;
        }
        return android.util.Patterns.PHONE.matcher(phoneNumber).matches();
    }

    /*public Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();

        final Rect rect = new Rect(0, 0, imageHolder.getWidth() / 2 - 10, imageHolder.getHeight() / 2);
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }*/
    private void restoreInfo() {
        if (progress != null) progress.dismiss();

        zipEt.setText(((Global) getActivity().getApplication()).getRegModel().getZipToShow());
        addressET.setText(((Global) getActivity().getApplication()).getRegModel().getAddress());
        cityET.setText(((Global) getActivity().getApplication()).getRegModel().getCity());
        stateET.setText(((Global) getActivity().getApplication()).getRegModel().getState());
        birthday.setText(((Global) getActivity().getApplication()).getRegModel().getBirthday());

        if (((Global) getActivity().getApplication()).getRegModel() != null) {
            if (((Global) getActivity().getApplication()).getRegModel().getDrawableCache() != null) {
                imageHolder.setImageDrawable(((Global) getActivity().getApplication()).getRegModel().getDrawableCache());
            }
        }

        String skills = "";

        List<String> skillsList = ((Global) getActivity().getApplication()).getRegModel().getSkilss();

        for (String skill : skillsList) {
            Log.d(this.getClass().getSimpleName(), "skill: " + skill);
            Log.d(this.getClass().getSimpleName(), "|||||||||||||");
        }


        List<String> otherSkills = ((Global) getActivity().getApplication()).getRegModel().getOtherSkills();

        for (String skill : otherSkills) {
            Log.d(this.getClass().getSimpleName(), "other: " + skill);
            Log.d(this.getClass().getSimpleName(), "|||||||||||||");
        }


        List<String> allSkills = new ArrayList<>();

        allSkills.addAll(skillsList);
        allSkills.addAll(otherSkills);


        if (allSkills.size() == 0) this.skills.setText("None...");
        else {
            for (int i = 0; i < allSkills.size(); i++) {
                if (i == allSkills.size() - 1) {
                    skills += allSkills.get(i);
                } else {
                    skills = skills + allSkills.get(i) + ", ";
                }
            }

            this.skills.setText(skills);
        }

        int i = ((Global) getActivity().getApplication()).getRegModel().getExpirience();
        if (i == 1) {
            exp_text.setText("Less than 1 year");
        }
        if (i == 2) {
            exp_text.setText("2-5 years");
        }
        if (i == 3) {
            exp_text.setText("5-7 years");
        }
        if (i == 4) {
            exp_text.setText("7-10 years");
        }
        if (i == 5) {
            exp_text.setText("10-15 years");
        }
        if (i == 6) {
            exp_text.setText("15-20 years");
        }
        if (i == 7) {
            exp_text.setText("More than 20 years");
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        saveDataBeforeActivityChange();
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
                startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
            } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }

            return null;
        }

        public void onPostExecute(Void unused) {

        }
    }

    public void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public String getUniqueImageFilename() {

        return "img_" + System.currentTimeMillis() + ".jpg";
    }

    public void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getActivity().getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(getActivity());
    }

    public void handleCrop(int resultCode, Intent result) {
        if (resultCode == Activity.RESULT_OK) {


            InputStream is;
            Bitmap image;
            try {
                is = getActivity().getContentResolver().openInputStream(Crop.getOutput(result));
                image = BitmapFactory.decodeStream(is);
                userAvatar = image;

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                userAvatar.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), userAvatar, "Title", null);
                Uri address = Uri.parse(path);

                ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity())
                        // You can pass your own memory cache implementation
                        .discCacheFileNameGenerator(new HashCodeFileNameGenerator())
                        .build();

                DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .displayer(new RoundedBitmapDisplayer(360)) //rounded corner bitmap
                        .cacheInMemory(true)
                        .cacheOnDisc(true)
                        .build();

                ImageLoader imageLoader = ImageLoader.getInstance();
                imageLoader.init(config);
                imageLoader.displayImage(String.valueOf(address), imageHolder, options);

                is.close();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            //mImageViewUserAvatar.setImageURI(Crop.getOutput(result));
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(getActivity(), Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void saveDataBeforeActivityChange() {

        ((Global) getActivity().getApplication()).getRegModel().setFirstName(fName.getText().toString());
        ((Global) getActivity().getApplication()).getRegModel().setLastName(lName.getText().toString());

        ((Global) getActivity().getApplication()).getRegModel().setAddress(addressET.getText().toString());
        ((Global) getActivity().getApplication()).getRegModel().setZipToShow(zipEt.getText().toString());
        ((Global) getActivity().getApplication()).getRegModel().setCity(cityET.getText().toString());
        ((Global) getActivity().getApplication()).getRegModel().setState(stateET.getText().toString());

        ((Global) getActivity().getApplication()).getRegModel().setBirthday(birthday.getText().toString());

        ((Global) getActivity().getApplication()).getRegModel().setDrawableCache(imageHolder.getDrawable());

    }


    private class SimpleTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            saveDataBeforeActivityChange();
        }
    }
}



