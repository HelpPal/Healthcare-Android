package com.midnight.healthcare.Activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.midnight.healthcare.API.API;
import com.midnight.healthcare.API.APIFactory;
import com.midnight.healthcare.Fragment.Register.RegStep1Fragment;
import com.midnight.healthcare.Fragment.Register.RegStep3ProfessionalFragment;
import com.midnight.healthcare.Fragment.Register.RegistrationFinishFragment;
import com.midnight.healthcare.Global;
import com.midnight.healthcare.Models.RegisterModel;
import com.midnight.healthcare.R;
import com.midnight.healthcare.SomeRandomeClass;
import com.soundcloud.android.crop.Crop;

import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setStatusBarBlue();

        addFragmentToBakcStack(new RegStep1Fragment());

    }


    public void addFragmentToBakcStack(Fragment fragment) {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.register_fragment_container, fragment, fragment.getClass().getName())
                .addToBackStack(fragment.getClass().getName())
                .commit();
    }

    @Override
    public void onBackPressed() {
        hideKeyboard();
        if (getFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        } else {
            getFragmentManager().popBackStack();
        }
    }

    public void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void setStatusBarBlue() {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(R.color.blueHighlighted));
        }
    }


    public void afterPurchasement(){
        API api = APIFactory.createAPI();
        RegisterModel mod = ((Global) getApplication()).getRegModel();
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

        String skills = "";
        for (String s : mod.getSkilssid())
            skills = skills + s + ",";

        if (skills.length() == 0) {
            SomeRandomeClass.AddToList("skills", "");
        } else {
            SomeRandomeClass.AddToList("skills", skills.substring(0, skills.length() - 1));
        }

        Call<Void> call = api.register(SomeRandomeClass.GetData());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                addFragmentToBakcStack(new RegistrationFinishFragment());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "AN ERROR HAS OCCURED!", Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1001){
            if(resultCode == Activity.RESULT_OK){
                afterPurchasement();
            }
            else{
                Toast.makeText(this, "AN ERROR HAS OCCURED!", Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == Crop.REQUEST_PICK && resultCode == Activity.RESULT_OK) {
            int index = getFragmentManager().getBackStackEntryCount() - 1;
            FragmentManager.BackStackEntry backEntry = getFragmentManager().getBackStackEntryAt(index);
            String tag = backEntry.getName();
            RegStep3ProfessionalFragment fragment = (RegStep3ProfessionalFragment) getFragmentManager().findFragmentByTag(tag);
            fragment.beginCrop(data.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            int index = getFragmentManager().getBackStackEntryCount() - 1;
            FragmentManager.BackStackEntry backEntry = getFragmentManager().getBackStackEntryAt(index);
            String tag = backEntry.getName();
            RegStep3ProfessionalFragment fragment = (RegStep3ProfessionalFragment) getFragmentManager().findFragmentByTag(tag);
            fragment.handleCrop(resultCode, data);
        }
    }
}
