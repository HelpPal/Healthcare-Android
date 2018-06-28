package com.midnight.healthcare.Activity;

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
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.midnight.healthcare.API.API;
import com.midnight.healthcare.API.APIFactory;
import com.midnight.healthcare.API.LoginResponse;
import com.midnight.healthcare.API.Skill;
import com.midnight.healthcare.Fragment.Register.RegStep3Expirience;
import com.midnight.healthcare.Fragment.Register.RegStep3Table;
import com.midnight.healthcare.Global;
import com.midnight.healthcare.Models.RegisterModel;
import com.midnight.healthcare.R;
import com.midnight.healthcare.SomeRandomeClass;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends MainActivity {
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
    TextView birthday;
    EditText description;
    TextView stateZip;
    ImageView viewOnMap;
    SimpleTextWatcher myWatcher;
    private boolean visitedSkillsFragment = false;

    Uri imageUri;

    ImageView takeAPhotoImage;
    ImageView uploadPhotoImage;
    TextView takeAPhotoText;
    TextView uploadPhotoText;

    public Boolean backPressed = false;


    String skillString = "";
    String skillsIdString = "";

    public static final int SELECT_PHOTO = 345;

    Boolean photoChanged = false;

    Bitmap imageBitmap;
    Bitmap userAvatar;

    int gender;
    int avalability;

    Button next;
    Boolean camera = false;
    TextView skills;

    ProgressDialog progress;
    private String otherSkills;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        setStatusBarBlue();

        ((RelativeLayout) findViewById(R.id.exit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myProfileIntent = new Intent(EditProfileActivity.this, MyProfileActivity.class);
                startActivity(myProfileIntent);
            }
        });


        final API api = APIFactory.createAPI();

        takePicture = (RelativeLayout) findViewById(R.id.takePicture);
        uploadPhoto = (RelativeLayout) findViewById(R.id.uploadPhoto);
        imageHolder = (ImageView) findViewById(R.id.register_image_holder);

        exp_text = (TextView) findViewById(R.id.exp_text);

        viewOnMap = (ImageView) findViewById(R.id.view_on_map);

        femaleButton = (LinearLayout) findViewById(R.id.femaleButton);
        maleButton = (LinearLayout) findViewById(R.id.maleButton);
        femaleText = (TextView) findViewById(R.id.femaleText);
        maleText = (TextView) findViewById(R.id.maleText);
        femaleImage = (ImageView) findViewById(R.id.femaleImage);
        maleImage = (ImageView) findViewById(R.id.maleImage);

        fName = (EditText) findViewById(R.id.register_step3_first_name);
        lName = (EditText) findViewById(R.id.register_step3_second_name);
        price1 = (EditText) findViewById(R.id.price_range_1);
        price2 = (EditText) findViewById(R.id.price_range_2);
        phone = (EditText) findViewById(R.id.register_step3_phone);
        fName = (EditText) findViewById(R.id.register_step3_first_name);
        description = (EditText) findViewById(R.id.register_step3_description);
        birthday = (TextView) findViewById(R.id.birthday_text);

        next = (Button) findViewById(R.id.saveButton);
        skills = (TextView) findViewById(R.id.skills);


        addressET = (EditText) findViewById(R.id.register_step3_address_et);

        cityET = (EditText) findViewById(R.id.register_step3_city_et);

        stateET = (EditText) findViewById(R.id.register_step3_state_et);

        zipEt = (EditText) findViewById(R.id.register_step3_zip_et);


        fullTimeButton = (LinearLayout) findViewById(R.id.fullTimeButton);
        partTimeButton = (LinearLayout) findViewById(R.id.partTimeButton);
        fullTimeText = (TextView) findViewById(R.id.fullTimeText);
        partTimeText = (TextView) findViewById(R.id.partTimeText);
        fullTimeImage = (ImageView) findViewById(R.id.fullTimeImage);
        partTimeImage = (ImageView) findViewById(R.id.partTimeCheck);

        takeAPhotoImage = (ImageView) findViewById(R.id.takeAPhotoImage);
        uploadPhotoImage = (ImageView) findViewById(R.id.uploadPhotoImage);
        takeAPhotoText = (TextView) findViewById(R.id.takeAPhotoText);
        uploadPhotoText = (TextView) findViewById(R.id.uploadPhotoText);

        ((Global) getApplication()).setRegModel(null);


//        FIRST START SET TEXT AND RECREATING REGMODEL
        setTextOnCreate();
        createNewRegModel();


        List<Skill> skillsList = ((Global) getApplication()).getCurrentUser().getSkills();
        //TODO: SKILLS
        skillString = "";
        for (int i = 0; i < skillsList.size(); i++) {
            if (i == skillsList.size() - 1) {
                skillString += skillsList.get(i).getName();
            } else {
                skillString += skillsList.get(i).getName() + ", ";
            }
        }

        otherSkills = "";

        skillsIdString = "";
        for (int i = 0; i < skillsList.size(); i++) {
            if (i == skillsList.size() - 1) {
                skillsIdString += skillsList.get(i).getId();
            } else {
                skillsIdString += skillsList.get(i).getId() + ",";
            }
        }


        if (((Global) getApplication()).getCurrentUser().getAvailable().equals("1")) {
            avalability = 1;
            fullTimeImage.setImageDrawable(getResources().getDrawable(R.drawable.checked_time));
            partTimeImage.setImageDrawable(getResources().getDrawable(R.drawable.uncheck));
            fullTimeText.setTextColor(getResources().getColor(R.color.blueHighlighted));
            partTimeText.setTextColor(0xFF000000);
        } else {
            avalability = 0;
            partTimeImage.setImageDrawable(getResources().getDrawable(R.drawable.checked_time));
            fullTimeImage.setImageDrawable(getResources().getDrawable(R.drawable.uncheck));
            partTimeText.setTextColor(getResources().getColor(R.color.blueHighlighted));
            fullTimeText.setTextColor(0xFF000000);
        }

        if (((Global) getApplication()).getCurrentUser().getGender().equals("1")) {
            maleImage.setImageDrawable(getResources().getDrawable(R.drawable.checked_time));
            femaleImage.setImageDrawable(getResources().getDrawable(R.drawable.uncheck));
            maleText.setTextColor(getResources().getColor(R.color.blueHighlighted));
            femaleText.setTextColor(0xFF000000);

            gender = 1;
        } else {
            femaleImage.setImageDrawable(getResources().getDrawable(R.drawable.checked_time));
            maleImage.setImageDrawable(getResources().getDrawable(R.drawable.uncheck));
            femaleText.setTextColor(getResources().getColor(R.color.blueHighlighted));
            maleText.setTextColor(0xFF000000);

            gender = 0;
        }


        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
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
        imageLoader.displayImage("http://104.131.152.91/health-care/" + ((Global) getApplication()).getCurrentUser().getProfileImg(), imageHolder, options);


        skills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((Global) getApplication()).getRegModel().getSkilss().size() == 0) {
                    List<Skill> skillsList = ((Global) getApplication()).getCurrentUser().getSkills();
                    for (int i = 0; i < skillsList.size(); i++) {
                        ((Global) getApplication()).getRegModel().getSkilss().add(skillsList.get(i).getName());
                        ((Global) getApplication()).getRegModel().getSkilssid().add(skillsList.get(i).getId());
                    }
                }
                visitedSkillsFragment = true;
                addFragmentToBakcStack(new RegStep3Table());
            }
        });

        next.setEnabled(true);

        birthday.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(EditProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
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
                        Log.d("onDateSet", "Clicklistener: " + birthday.getText().toString());

                        ((Global) getApplication()).getRegModel().setBirthday(birthday.getText().toString());
                    }
                }, 2000, Calendar.MONTH, Calendar.DAY_OF_MONTH);

                dialog.show();

            }
        });


        exp_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFragmentToBakcStack(new RegStep3Expirience());
            }
        });

        maleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maleImage.setImageDrawable(getResources().getDrawable(R.drawable.checked_time));
                femaleImage.setImageDrawable(getResources().getDrawable(R.drawable.uncheck));
                maleText.setTextColor(getResources().getColor(R.color.blueHighlighted));
                femaleText.setTextColor(0xFF000000);

                gender = 1;
            }
        });

        femaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                femaleImage.setImageDrawable(getResources().getDrawable(R.drawable.checked_time));
                maleImage.setImageDrawable(getResources().getDrawable(R.drawable.uncheck));
                femaleText.setTextColor(getResources().getColor(R.color.blueHighlighted));
                maleText.setTextColor(0xFF000000);

                gender = 0;
            }
        });

        fullTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullTimeImage.setImageDrawable(ContextCompat.getDrawable(EditProfileActivity.this, R.drawable.checked_time));
                partTimeImage.setImageDrawable(getResources().getDrawable(R.drawable.uncheck));
                fullTimeText.setTextColor(getResources().getColor(R.color.blueHighlighted));
                partTimeText.setTextColor(0xFF000000);

                avalability = 1;
            }
        });

        viewOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = new ProgressDialog(EditProfileActivity.this);
                progress.setMessage("Loading, please wait...");
                new MyTask(progress).execute();
            }
        });

        partTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                partTimeImage.setImageDrawable(getResources().getDrawable(R.drawable.checked_time));
                fullTimeImage.setImageDrawable(getResources().getDrawable(R.drawable.uncheck));
                partTimeText.setTextColor(getResources().getColor(R.color.blueHighlighted));
                fullTimeText.setTextColor(0xFF000000);

                avalability = 0;
            }
        });

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
                        if (fName.getText().toString().equals("") ||
                                lName.getText().toString().equals("") ||
                                phone.getText().toString().equals("") ||
                                price1.getText().toString().equals("") ||
                                cityET.getText().toString().equals("") ||
                                addressET.getText().toString().equals("") ||
                                skills.getText().equals("") ||
                                skills.getText().equals("None...")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
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
                            if (!validCellPhone(phone.getText().toString())) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
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
                                int price;
                                if (price2.getText().toString().equals("")) price = 0;
                                else price = Integer.valueOf(price2.getText().toString());
                                if (Integer.valueOf(price1.getText().toString()) <= price || price2.getText().toString().length() == 0) {
                                    if (getCoordinatesFromAddress()) {
                                        progress = new ProgressDialog(EditProfileActivity.this);
                                        progress.setMessage("Loading, please wait...");
                                        progress.show();

                                        ((Global) getApplication()).getRegModel().setFirstName(fName.getText().toString());
                                        ((Global) getApplication()).getRegModel().setLastName(lName.getText().toString());
                                        ((Global) getApplication()).getRegModel().setBirthday(birthday.getText().toString());
                                        ((Global) getApplication()).getRegModel().setDescription(description.getText().toString());
                                        ((Global) getApplication()).getRegModel().setPhone(phone.getText().toString());
                                        ((Global) getApplication()).getRegModel().setPrice_min(price1.getText().toString());
                                        ((Global) getApplication()).getRegModel().setPrice_max(price2.getText().toString());
                                        ((Global) getApplication()).getRegModel().setAvalable(avalability);
                                        ((Global) getApplication()).getRegModel().setGender(gender);
                                        ((Global) getApplication()).getRegModel().setProfile_img(imageBitmap);

                                        ((Global) getApplication()).getRegModel().setAddress(TextUtils.editTextValidation(addressET));
                                        ((Global) getApplication()).getRegModel().setCity(TextUtils.editTextValidation(cityET));
                                        ((Global) getApplication()).getRegModel().setZipToShow(TextUtils.editTextValidation(zipEt));
                                        ((Global) getApplication()).getRegModel().setState(TextUtils.editTextValidation(stateET));


                                        if (photoChanged) {
                                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                                        Bitmap bitmap = ((BitmapDrawable) imageHolder.getDrawable()).getBitmap();

                                            Bitmap bitmap = userAvatar;

                                            ((Global) getApplication()).getRegModel().setProfile_img(bitmap);

                                            if (bitmap != null) {
                                                bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);
                                                byte[] imageBytes = baos.toByteArray();
                                                String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                                                SomeRandomeClass.AddToList("profile_img", encodedImage);
                                            }

                                            SomeRandomeClass.AddToList("first_name", fName.getText().toString());
                                            SomeRandomeClass.AddToList("last_name", lName.getText().toString());
                                            SomeRandomeClass.AddToList("lat", ((Global) getApplication()).getRegModel().getLat());
                                            SomeRandomeClass.AddToList("long", ((Global) getApplication()).getRegModel().getLongitudine());
                                            SomeRandomeClass.AddToList("experience", String.valueOf(((Global) getApplication()).getRegModel().getExpirience()));
                                            SomeRandomeClass.AddToList("avalable", String.valueOf(avalability));
                                            SomeRandomeClass.AddToList("skills", skillsIdString);
                                            SomeRandomeClass.AddToList("price_min", price1.getText().toString());
                                            if (price2.getText().toString().length() == 0) {
                                                SomeRandomeClass.AddToList("price_max", price1.getText().toString());
                                            } else {
                                                SomeRandomeClass.AddToList("price_max", price2.getText().toString());
                                            }
                                            SomeRandomeClass.AddToList("phone", phone.getText().toString());
                                            SomeRandomeClass.AddToList("gender", String.valueOf(gender));
                                            SomeRandomeClass.AddToList("description", description.getText().toString());
                                            SomeRandomeClass.AddToList("birthday", birthday.getText().toString());
                                            SomeRandomeClass.AddToList("userid", ((Global) getApplication()).getCurrentUser().getId());
                                            SomeRandomeClass.AddToList("other_skills", otherSkills);

                                            SomeRandomeClass.AddToList("city", TextUtils.editTextValidation(cityET));
                                            SomeRandomeClass.AddToList("state", TextUtils.editTextValidation(stateET));
                                            Log.d("REGISTER", "onTouch: " + TextUtils.editTextValidation(stateET));

                                            SomeRandomeClass.AddToList("adress", TextUtils.editTextValidation(addressET));
                                            SomeRandomeClass.AddToList("zip", TextUtils.editTextValidation(zipEt));


                                            //TODO: EDIT
                                            Call<LoginResponse> call = api.editprofile(SomeRandomeClass.GetData());
                                            call.enqueue(new Callback<LoginResponse>() {
                                                @Override
                                                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                                                    if (progress != null) progress.dismiss();
                                                    if (response.body() != null) {
                                                        ((Global) getApplication()).setCurrentUser(response.body().getUser());

                                                        Intent myProfileIntent = new Intent(EditProfileActivity.this, MyProfileActivity.class);
                                                        startActivity(myProfileIntent);

                                                    } else {
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
                                                        builder.setTitle("Error!")
                                                                .setMessage("Something went wrong!!")
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

                                                @Override
                                                public void onFailure(Call<LoginResponse> call, Throwable t) {
                                                    if (progress != null) progress.dismiss();
                                                }
                                            });

                                        } else {

                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Bitmap bit = null;
                                                    try {
                                                        URL url = new URL("http://104.131.152.91/health-care/" + ((Global) getApplication()).getCurrentUser().getProfileImg());
                                                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                                        connection.setDoInput(true);
                                                        connection.connect();
                                                        InputStream input = connection.getInputStream();
                                                        Bitmap myBitmap = BitmapFactory.decodeStream(input);
                                                        if (myBitmap != null) {
                                                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                                            myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                                            byte[] imageBytes = baos.toByteArray();
                                                            String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                                                        }
                                                        //SomeRandomeClass.AddToList("profile_img", encodedImage);

                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                progress = new ProgressDialog(EditProfileActivity.this);
                                                                progress.setMessage("Loading, please wait...");
                                                                progress.show();

                                                                SomeRandomeClass.AddToList("first_name", fName.getText().toString());
                                                                SomeRandomeClass.AddToList("last_name", lName.getText().toString());
                                                                SomeRandomeClass.AddToList("lat", ((Global) getApplication()).getRegModel().getLat());
                                                                SomeRandomeClass.AddToList("long", ((Global) getApplication()).getRegModel().getLongitudine());
                                                                SomeRandomeClass.AddToList("experience", String.valueOf(((Global) getApplication()).getRegModel().getExpirience()));
                                                                SomeRandomeClass.AddToList("skills", skillsIdString);
                                                                SomeRandomeClass.AddToList("avalable", String.valueOf(avalability));
                                                                SomeRandomeClass.AddToList("price_min", price1.getText().toString());
                                                                SomeRandomeClass.AddToList("price_min", price1.getText().toString());

                                                                SomeRandomeClass.AddToList("zip", TextUtils.editTextValidation(zipEt));
                                                                SomeRandomeClass.AddToList("city", TextUtils.editTextValidation(cityET));
                                                                SomeRandomeClass.AddToList("state", TextUtils.editTextValidation(stateET));
                                                                SomeRandomeClass.AddToList("adress", TextUtils.editTextValidation(addressET));
                                                                Log.d("REGISTER", "PHOTOCHANGED]: " + TextUtils.editTextValidation(stateET));

                                                                if (price2.getText().toString().length() == 0) {
                                                                    SomeRandomeClass.AddToList("price_max", price1.getText().toString());
                                                                } else {
                                                                    SomeRandomeClass.AddToList("price_max", price2.getText().toString());
                                                                }
                                                                SomeRandomeClass.AddToList("phone", phone.getText().toString());
                                                                SomeRandomeClass.AddToList("gender", String.valueOf(gender));
                                                                SomeRandomeClass.AddToList("description", description.getText().toString());
                                                                SomeRandomeClass.AddToList("birthday", birthday.getText().toString());
                                                                SomeRandomeClass.AddToList("userid", ((Global) getApplication()).getCurrentUser().getId());
                                                                SomeRandomeClass.AddToList("other_skills", otherSkills);

                                                                //TODO: EDIT
                                                                Call<LoginResponse> call = api.editprofile(SomeRandomeClass.GetData());
                                                                call.enqueue(new Callback<LoginResponse>() {
                                                                    @Override
                                                                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                                                                        if (progress != null)
                                                                            progress.dismiss();
                                                                        if (response.body() != null) {
                                                                            ((Global) getApplication()).setCurrentUser(response.body().getUser());

                                                                            Intent myProfileIntent = new Intent(EditProfileActivity.this, MyProfileActivity.class);
                                                                            startActivity(myProfileIntent);
                                                                        } else {
                                                                            Log.d("EDIT", "onResponse: " + response.body().toString());
                                                                            AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
                                                                            builder.setTitle("Error!")
                                                                                    .setMessage("Something went wrong!!")
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

                                                                    @Override
                                                                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                                                                        if (progress != null)
                                                                            progress.dismiss();
                                                                        Log.d("EDIT", "onFailure: " + t.getLocalizedMessage());
                                                                        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
                                                                        builder.setTitle("Error!")
                                                                                .setMessage("Something went wrong!")
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
                                                                });
                                                            }
                                                        });
                                                    } catch (IOException e) {
                                                    }

                                                }
                                            }).start();

                                        }
                                    } else {
                                        Toast.makeText(EditProfileActivity.this, "Please verify your address", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
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
                        break;
                    }
                }
                return false;
            }
        });
        addTextWatchers();
    }

    private void addTextWatchers() {

        myWatcher = new SimpleTextWatcher();

        addressET.addTextChangedListener(myWatcher);
        cityET.addTextChangedListener(myWatcher);
        stateET.addTextChangedListener(myWatcher);
        zipEt.addTextChangedListener(myWatcher);
    }

    private void createNewRegModel() {
        ((Global) getApplication()).setRegModel(new RegisterModel());

        ((Global) getApplication()).getRegModel().setExpirience(Integer.valueOf(((Global) getApplication()).getCurrentUser().getExperience()));
        ((Global) getApplication()).getRegModel().setAddress(((Global) getApplication()).getCurrentUser().getAddress());
        ((Global) getApplication()).getRegModel().setZipToShow(((Global) getApplication()).getCurrentUser().getZip());
        ((Global) getApplication()).getRegModel().setState(((Global) getApplication()).getCurrentUser().getState());
        ((Global) getApplication()).getRegModel().setCity(((Global) getApplication()).getCurrentUser().getCity());
        ((Global) getApplication()).getRegModel().setBirthday(((Global) getApplication()).getCurrentUser().getBirthday());
        ((Global) getApplication()).getRegModel().setDescription(((Global) getApplication()).getCurrentUser().getDescription());


        ((Global) getApplication()).getRegModel().setFirstName(((Global) getApplication()).getCurrentUser().getFirstName());
        ((Global) getApplication()).getRegModel().setLastName(((Global) getApplication()).getCurrentUser().getLastName());
        ((Global) getApplication()).getRegModel().setPhone(((Global) getApplication()).getCurrentUser().getPhone());
        ((Global) getApplication()).getRegModel().setPrice_min(((Global) getApplication()).getCurrentUser().getPriceMin());
        ((Global) getApplication()).getRegModel().setPrice_max(((Global) getApplication()).getCurrentUser().getPriceMax());
        ((Global) getApplication()).getRegModel().setAvalable(avalability);
        ((Global) getApplication()).getRegModel().setGender(gender);
        ((Global) getApplication()).getRegModel().setProfile_img(imageBitmap);

    }

    private void saveDataBeforeActivityChange() {

        ((Global) getApplication()).getRegModel().setFirstName(fName.getText().toString());
        ((Global) getApplication()).getRegModel().setLastName(lName.getText().toString());

        ((Global) getApplication()).getRegModel().setAddress(addressET.getText().toString());
        ((Global) getApplication()).getRegModel().setZipToShow(zipEt.getText().toString());
        ((Global) getApplication()).getRegModel().setCity(cityET.getText().toString());
        ((Global) getApplication()).getRegModel().setState(stateET.getText().toString());

        ((Global) getApplication()).getRegModel().setBirthday(birthday.getText().toString());

        ((Global) getApplication()).getRegModel().setDrawableCache(imageHolder.getDrawable());

    }

    private void restoreData() {
        if (progress != null) progress.dismiss();


        birthday.setText(((Global) getApplication()).getRegModel().getBirthday());

        zipEt.setText(((Global) getApplication()).getRegModel().getZipToShow());
        addressET.setText(((Global) getApplication()).getRegModel().getAddress());
        cityET.setText(((Global) getApplication()).getRegModel().getCity());
        stateET.setText(((Global) getApplication()).getRegModel().getState());

        skillString = "";

        List<String> skillsList = ((Global) getApplication()).getRegModel().getSkilss();

        List<String> allSkills = new ArrayList<>();

        allSkills.addAll(skillsList);

//        for (String skill : skillsList) {
//            Log.d("restoreData", "skillsList: " + skill);
//            Log.d("restoreData", "|||||||||||||||||");
//
//        }
//
//        Log.d("restoreData", "otherskillSize: " + ((Global) getApplication()).getRegModel().getOtherSkills());

        List<String> otherSkill = ((Global) getApplication()).getRegModel().getOtherSkills();

        allSkills.addAll(otherSkill);

//        for (String skill : otherSkill) {
//            Log.d("restoreData", "otherSkill: " + skill);
//            Log.d("restoreData", "|||||||||||||||||");
//        }

        if (allSkills.size() > 0) {
            for (int i = 0; i < allSkills.size(); i++) {
                if (i == allSkills.size() - 1) {
                    skillString += allSkills.get(i);
                } else {
                    skillString = skillString + allSkills.get(i) + ", ";
                }
            }
            this.skills.setText(skillString);
        } else if (visitedSkillsFragment && allSkills.size() == 0) {
            this.skills.setText("None...");
        } else {
            for (int i = 0; i < ((Global) getApplication()).getCurrentUser().getSkills().size(); i++) {
                if (i == ((Global) getApplication()).getCurrentUser().getSkills().size() - 1) {
                    skillString += ((Global) getApplication()).getCurrentUser().getSkills().get(i).getName();
                } else {
                    skillString = skillString + ((Global) getApplication()).getCurrentUser().getSkills().get(i).getName() + ", ";
                }
            }
            this.skills.setText(skillString);
        }

        skillsIdString = "";
        List<String> skillsIdList = ((Global) getApplication()).getRegModel().getSkilssid();
//        Log.d("restoreData", "restoreData: " + skillsIdList.size());
        if (skillsIdList.size() == 0) skillsIdString = "";
        else {
            for (int i = 0; i < skillsIdList.size(); i++) {
                if (i == skillsIdList.size() - 1) {
                    skillsIdString += skillsIdList.get(i);
                } else {
                    skillsIdString += skillsIdList.get(i) + ",";
                }
            }
        }


        List<String> otherSkillz = ((Global) getApplication()).getRegModel().getOtherSkills();
        otherSkills = "";
        if (otherSkillz.size() > 0) {
            for (int i = 0; i < otherSkillz.size(); i++) {
                if (i == otherSkillz.size() - 1) {
                    otherSkills += otherSkillz.get(i);
                } else {
                    otherSkills = otherSkills + otherSkillz.get(i) + ", ";
                }
            }


            int i = ((Global) getApplication()).getRegModel().getExpirience();
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

        if (((Global) getApplication()).getRegModel() != null && (((Global) getApplication()).getRegModel().getDrawableCache() != null)) {
            imageHolder.setImageDrawable(((Global) getApplication()).getRegModel().getDrawableCache());
        }
        visitedSkillsFragment = false;
    }

    private void setTextOnCreate() {
        fName.setText(((Global) getApplication()).getCurrentUser().getFirstName());
        lName.setText(((Global) getApplication()).getCurrentUser().getLastName());

        skills.setText(skillString);

        price1.setText(((Global) getApplication()).getCurrentUser().getPriceMin());
        price2.setText(((Global) getApplication()).getCurrentUser().getPriceMax());

        phone.setText(((Global) getApplication()).getCurrentUser().getPhone());
        birthday.setText(((Global) getApplication()).getCurrentUser().getBirthday());

        description.setText(((Global) getApplication()).getCurrentUser().getDescription());

        cityET.setText(((Global) getApplication()).getCurrentUser().getCity());
        addressET.setText(((Global) getApplication()).getCurrentUser().getAddress());
        zipEt.setText(((Global) getApplication()).getCurrentUser().getZip());
        stateET.setText(((Global) getApplication()).getCurrentUser().getState());
    }


    private void dispatchTakePictureIntent() {
        //Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //if (takePictureIntent.resolveActivity(EditProfileActivity.this.getPackageManager()) != null) {
        //    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        //}
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_PHOTO && resultCode == Activity.RESULT_OK) {
            Bitmap pickedImage;
            Uri selectedImageUri;
            selectedImageUri = data.getData();

            try {
                pickedImage = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);

                ((Global) getApplication()).getRegModel().setProfile_img(pickedImage);

                beginCrop(selectedImageUri);

                photoChanged = true;

                //Bitmap thumbnail2 = Bitmap.createScaledBitmap(pickedImage, 1024, 1024, false);
                //imageHolder.setImageBitmap(thumbnail2);

                //imageHolder.setImageBitmap(thumbnail2);
            } catch (IOException e) {
                e.printStackTrace();
            }


            //cursor.close();
        } else if (requestCode == Crop.REQUEST_PICK && resultCode == Activity.RESULT_OK) {
            beginCrop(data.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);
        } else {
            if (camera && requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
                try {
                    Bitmap thumbnail = MediaStore.Images.Media.getBitmap(
                            getContentResolver(), imageUri);

                    ((Global) getApplication()).getRegModel().setProfile_img(thumbnail);

                    //Bitmap thumbnail2 = Bitmap.createScaledBitmap(thumbnail, 1024, 1024, false);
                    //imageHolder.setImageBitmap(thumbnail2);

                    beginCrop(imageUri);

                    photoChanged = true;
                    camera = false;

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                if (resultCode == Activity.RESULT_OK) {
                    final Place place = PlacePicker.getPlace(data, EditProfileActivity.this);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Geocoder gcd = new Geocoder(EditProfileActivity.this);
                            List<Address> addresses = new ArrayList<>();
                            try {
                                addresses = gcd.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1);

                                final String getThoroughfare = addresses.get(0).getThoroughfare();
                                final String getSubThoroughfare = addresses.get(0).getSubThoroughfare();
                                final String getLocality = addresses.get(0).getLocality();
                                final String getPostalCode = addresses.get(0).getPostalCode();
                                final String getAdminArea = addresses.get(0).getAdminArea();


//                                final String adddressToShow = addresses.get(0).getAddressLine(0);
                                runOnUiThread(new Runnable() {
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
    protected void onPause() {
        super.onPause();
    }


    @Override
    public void onResume() {
        super.onResume();
        restoreData();
    }


    public boolean validCellPhone(String phoneNumber) {
        if (!phoneNumber.contains("+")) {
            phoneNumber = "+" + phoneNumber;
        }
        return android.util.Patterns.PHONE.matcher(phoneNumber).matches();
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
                startActivityForResult(builder.build(EditProfileActivity.this), PLACE_PICKER_REQUEST);
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }

            return null;
        }

        public void onPostExecute(Void unused) {

        }

    }

    public void setMainVisible() {
        RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);
        mainLayout.setVisibility(View.VISIBLE);
    }

    public void setMainGone() {
        RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);
        mainLayout.setVisibility(View.GONE);
    }

    public void addFragmentToBakcStack(Fragment fragment) {
        hideKeyboard();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.block, fragment, fragment.getClass().getName())
                .addToBackStack(fragment.getClass().getName())
                .commit();
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public String getUniqueImageFilename() {

        return "img_" + System.currentTimeMillis() + ".jpg";
    }

    public void setStatusBarBlue() {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(R.color.blueHighlighted));
        }
    }

    public void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    public void handleCrop(int resultCode, Intent result) {
        if (resultCode == Activity.RESULT_OK) {


            InputStream is;
            Bitmap image;
            try {
                is = getContentResolver().openInputStream(Crop.getOutput(result));
                image = BitmapFactory.decodeStream(is);
                userAvatar = image;

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                userAvatar.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(getContentResolver(), userAvatar, "Title", null);
                Uri address = Uri.parse(path);

                ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
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
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private boolean getCoordinatesFromAddress() {
        Geocoder geocoder = new Geocoder(this);
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

                ((Global) getApplication()).getRegModel().setLat(String.valueOf(latitude));
                ((Global) getApplication()).getRegModel().setLongitudine(String.valueOf(longitude));
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
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
