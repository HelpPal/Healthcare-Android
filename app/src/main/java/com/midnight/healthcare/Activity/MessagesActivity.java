package com.midnight.healthcare.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.midnight.healthcare.API.API;
import com.midnight.healthcare.API.APIFactory;
import com.midnight.healthcare.API.MessegesResponse;
import com.midnight.healthcare.API.Messegess;
import com.midnight.healthcare.Adapters.MessegesAdapter;
import com.midnight.healthcare.Global;
import com.midnight.healthcare.R;
import com.midnight.healthcare.SomeRandomeClass;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessagesActivity extends MainActivity {

    ListView listView;
    MessegesAdapter adapter;
    TextView noMessages;

    RelativeLayout deleteButton;

    SharedPreferences sPref;

    LinearLayout deleteLayout;
    TextView deleteText;

    Boolean deletedState = false;

    public List<String> unreadMessages = new ArrayList<>();

    API api = APIFactory.createAPI();

    ProgressDialog progress;

    List<Messegess> deleteList = new ArrayList<>();
    List<Messegess> responseFromServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        unreadMessages = new ArrayList<>();

        sPref = this.getSharedPreferences("Notifications", Context.MODE_PRIVATE);
        String wholeString = sPref.getString("messageid","");
        if(!wholeString.equals("")) {
            String[] usersIds = sPref.getString("messageid", "").split(";");
            for(int i = 0; i < usersIds.length; i++){
                unreadMessages.add(usersIds[i]);
            }
        }

        setStatusBarBlue();

        ((RelativeLayout) findViewById(R.id.exit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        deleteButton = (RelativeLayout) findViewById(R.id.deleteSign);
        deleteText = (TextView) findViewById(R.id.deleteText);
        deleteLayout = (LinearLayout) findViewById(R.id.deleteLayout);

        noMessages = (TextView) findViewById(R.id.noMessages);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!deletedState) {
                    deletedState = true;
                    deleteLayout.setVisibility(View.VISIBLE);
                    deleteText.setText("Delete 0 conversations");
                    adapter = new MessegesAdapter(responseFromServer, MessagesActivity.this, 1);
                    listView.setAdapter(adapter);
                }
                else {
                    deletedState = false;
                    deleteLayout.setVisibility(View.GONE);
                    adapter = new MessegesAdapter(responseFromServer, MessagesActivity.this, 0);
                    listView.setAdapter(adapter);
                }
            }
        });

        deleteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(deleteList.size() == 0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MessagesActivity.this);
                    builder.setTitle("Error!")
                            .setMessage("You must choose a conversation you want to delete!")
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
                else{
                    progress = new ProgressDialog(MessagesActivity.this);
                    progress.setMessage("Loading...");
                    progress.show();
                    deleteConversations(0);

                }
            }
        });

        listView = (ListView) findViewById(R.id.messages_list);


        final ProgressDialog dialog = new ProgressDialog(MessagesActivity.this);
        dialog.setMessage("Loading...");
        dialog.show();
        SomeRandomeClass.AddToList("userid" , ((Global)getApplication()).getCurrentUser().getId());
        Call<MessegesResponse> call = api.messages(SomeRandomeClass.GetData());
        call.enqueue(new Callback<MessegesResponse>() {
            @Override
            public void onResponse(Call<MessegesResponse> call, Response<MessegesResponse> response) {
                if(response.body().getResponse().size() == 0){
                    noMessages.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                }
                else {
                    noMessages.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    responseFromServer = response.body().getResponse();
                    adapter = new MessegesAdapter(response.body().getResponse(), MessagesActivity.this, 0);
                    listView.setAdapter(adapter);
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<MessegesResponse> call, Throwable t) {

            }
        });

    }

    public void addToDeleteList(Messegess item){
        deleteList.add(item);
        deleteText.setText("Delete " + String.valueOf(deleteList.size()) + " conversations");
    }

    public void deleteFromDeleteList(Messegess item){
        deleteList.remove(item);
        deleteText.setText("Delete " + String.valueOf(deleteList.size()) + " conversations");
    }

    private void deleteConversations(int i){
        if(i == deleteList.size()){
            deleteLayout.setVisibility(View.GONE);

            deleteList = new ArrayList<>();

            SomeRandomeClass.AddToList("userid" , ((Global)getApplication()).getCurrentUser().getId());
            Call<MessegesResponse> call = api.messages(SomeRandomeClass.GetData());
            call.enqueue(new Callback<MessegesResponse>() {
                @Override
                public void onResponse(Call<MessegesResponse> call, Response<MessegesResponse> response) {
                    if(response.body().getResponse().size() == 0){
                        noMessages.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                    }
                    else {
                        noMessages.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                        responseFromServer = response.body().getResponse();
                        adapter = new MessegesAdapter(response.body().getResponse(), MessagesActivity.this, 0);
                        listView.setAdapter(adapter);
                    }
                    progress.dismiss();
                }

                @Override
                public void onFailure(Call<MessegesResponse> call, Throwable t) {

                }
            });
        }

        else{
            SomeRandomeClass.AddToList("userid" , ((Global)getApplication()).getCurrentUser().getId());
            SomeRandomeClass.AddToList("conversation_id" , deleteList.get(i).getPartner());

            Call<Void> call = api.deleteConversation(SomeRandomeClass.GetData());
            final int finalI = i;
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    deleteConversations(finalI + 1);
                }
                 @Override
                 public void onFailure(Call<Void> call, Throwable t) {

                 }
                });
        }
    }

    public void setStatusBarBlue(){
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(R.color.blueHighlighted));
        }
    }

    public void clearPreferences(){
        sPref = this.getSharedPreferences("Notifications", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString("messageid", "");
        ed.commit();
    }

    public void deleteUser(String userId){
        sPref = this.getSharedPreferences("Notifications", Context.MODE_PRIVATE);
        String bigString = "";
        int position = -1;
        if(unreadMessages.get(0).equals(userId)) position = 0;
        else{
            bigString += unreadMessages.get(0);
        }
        for(int i = 1; i < unreadMessages.size(); i++){
            if(unreadMessages.get(i).equals(userId)) position = i;
            else{
                bigString += ";" + unreadMessages.get(i);
            }
        }

        SharedPreferences.Editor ed = sPref.edit();
        ed.putString("messageid", bigString);
        ed.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        unreadMessages = new ArrayList<>();

        sPref = this.getSharedPreferences("Notifications", Context.MODE_PRIVATE);
        String wholeString = sPref.getString("messageid","");
        if(!wholeString.equals("")) {
            String[] usersIds = sPref.getString("messageid", "").split(";");
            for(int i = 0; i < usersIds.length; i++){
                unreadMessages.add(usersIds[i]);
            }
        }

        sPref = this.getSharedPreferences("Notifications", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putInt("notificationsMessages", 0);
        ed.commit();
    }
}
