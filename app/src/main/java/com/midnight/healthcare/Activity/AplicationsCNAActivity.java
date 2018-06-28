package com.midnight.healthcare.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.midnight.healthcare.API.API;
import com.midnight.healthcare.API.APIFactory;
import com.midnight.healthcare.API.AplictaionInd;
import com.midnight.healthcare.API.ApplicationsIndResponse;
import com.midnight.healthcare.Adapters.ApplicationIndAdapter;
import com.midnight.healthcare.Global;
import com.midnight.healthcare.R;
import com.midnight.healthcare.SomeRandomeClass;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class  AplicationsCNAActivity extends MainActivity{
    API api = APIFactory.createAPI();
    public ListView list;
    ApplicationIndAdapter adapter;
    Integer pageCount = 0;
    List<AplictaionInd> listPro = new ArrayList<>();
    public static final String TAG = AplicationsCNAActivity.class.getSimpleName();
    TextView noApplications;

    public RelativeLayout block;

    SharedPreferences sPref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aplications_cna);

        setStatusBarBlue();

        ((RelativeLayout) findViewById(R.id.exit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        sPref = this.getSharedPreferences("Notifications", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putInt("notificationsOffer", 0);
        ed.commit();

        noApplications = (TextView) findViewById(R.id.noApplicationsText);
        block = (RelativeLayout) findViewById(R.id.block);

        block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        list = (ListView) findViewById(R.id.aplication_list);
        final ProgressDialog dialog = new ProgressDialog(AplicationsCNAActivity.this);
        dialog.setMessage("Loading...");
        dialog.show();
        SomeRandomeClass.AddToList("userid" , ((Global)getApplication()).getCurrentUser().getId());
        SomeRandomeClass.AddToList("page" ,  pageCount.toString());
        Call<ApplicationsIndResponse> call = api.myapplications(SomeRandomeClass.GetData());
        call.enqueue(new Callback<ApplicationsIndResponse>() {
            @Override
            public void onResponse(Call<ApplicationsIndResponse> call, Response<ApplicationsIndResponse> response) {
                listPro.addAll(response.body().getResponse());
                if(listPro.size() == 0){
                    noApplications.setVisibility(View.VISIBLE);
                    list.setVisibility(View.GONE);
                    dialog.dismiss();
                }
                else {
                    noApplications.setVisibility(View.GONE);
                    list.setVisibility(View.VISIBLE);
                    adapter = new ApplicationIndAdapter(listPro, AplicationsCNAActivity.this);
                    list.setAdapter(adapter);
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ApplicationsIndResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: 1" + t.getLocalizedMessage());

                Toast.makeText(AplicationsCNAActivity.this , t.toString() , Toast.LENGTH_LONG).show();
                dialog.dismiss();

            }
        });


    }



    public void fill(){
      pageCount++;
        SomeRandomeClass.AddToList("userid" , ((Global)getApplication()).getCurrentUser().getId());
        SomeRandomeClass.AddToList("page" ,  pageCount.toString());

        Call<ApplicationsIndResponse> call = api.myapplications(SomeRandomeClass.GetData());
        call.enqueue(new Callback<ApplicationsIndResponse>() {
            @Override
            public void onResponse(Call<ApplicationsIndResponse> call, Response<ApplicationsIndResponse> response) {

                listPro.addAll(response.body().getResponse());
                if(listPro.size() == 0){
                    noApplications.setVisibility(View.VISIBLE);
                    list.setVisibility(View.GONE);
                }
                else {
                    noApplications.setVisibility(View.GONE);
                    list.setVisibility(View.VISIBLE);
                    adapter = new ApplicationIndAdapter(listPro, AplicationsCNAActivity.this);
                }
            }

            @Override
            public void onFailure(Call<ApplicationsIndResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
                Toast.makeText(AplicationsCNAActivity.this , t.toString() , Toast.LENGTH_LONG).show();

            }
        });

    }

    public void setTextVisible(){
        noApplications.setVisibility(View.VISIBLE);
    }

    public void setStatusBarBlue(){
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(R.color.blueHighlighted));
        }
    }
}
