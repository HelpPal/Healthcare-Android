package com.midnight.healthcare.Fragment.Register;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.midnight.healthcare.API.API;
import com.midnight.healthcare.API.APIFactory;
import com.midnight.healthcare.API.Skill;
import com.midnight.healthcare.API.SkillsResponse;
import com.midnight.healthcare.Activity.EditProfileActivity;
import com.midnight.healthcare.Adapters.RegStep3TableAdapter;
import com.midnight.healthcare.Global;
import com.midnight.healthcare.R;
import com.midnight.healthcare.SomeRandomeClass;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by TUSK.ONE on 9/2/16.
 */
public class RegStep3Table extends Fragment {
    RecyclerView listView;
    RegStep3TableAdapter adapter;
    Button done;
    Button otherSkill;
    AlertDialog.Builder dialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.registration_step3_table, null);
        ((RelativeLayout) view.findViewById(R.id.exit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity().findViewById(R.id.mainLayout) != null) {
                    ((EditProfileActivity) getActivity()).setMainVisible();
                    ((EditProfileActivity) getActivity()).onResume();
                }
                getFragmentManager().popBackStack();
            }
        });

        if (getActivity().findViewById(R.id.mainLayout) != null) {
            ((EditProfileActivity) getActivity()).setMainGone();
        }

        done = (Button) view.findViewById(R.id.skill_done);
        listView = (RecyclerView) view.findViewById(R.id.skill_list);
        otherSkill = (Button) view.findViewById(R.id.skill_other);

        done.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        done.setBackground(getResources().getDrawable(R.drawable.round_blue_active));
                        done.setTextColor(getResources().getColor(R.color.blueHighlighted));
                        break;
                    case MotionEvent.ACTION_UP: {
                        done.setBackground(getResources().getDrawable(R.drawable.round_blue));
                        done.setTextColor(0xFFFFFFFF);
                        if (getActivity().findViewById(R.id.mainLayout) != null) {
                            ((EditProfileActivity) getActivity()).setMainVisible();
                            ((EditProfileActivity) getActivity()).onResume();
                        }
                        getFragmentManager().popBackStack();
                        break;
                    }
                }
                return false;
            }
        });
        otherSkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildDialog();
            }
        });

        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.show();
        API api = APIFactory.createAPI();
        if (getActivity() instanceof EditProfileActivity) {
            SomeRandomeClass.AddToList("userid", ((Global) getActivity().getApplication()).getCurrentUser().getId());
            Log.d("onCreateView", "userId: " + ((Global) getActivity().getApplication()).getCurrentUser().getId());
        }
        Call<SkillsResponse> skill = api.getSkills(SomeRandomeClass.GetData());
        skill.enqueue(new Callback<SkillsResponse>() {
            @Override
            public void onResponse(Call<SkillsResponse> call, Response<SkillsResponse> response) {
                if (response != null) {
                    adapter = new RegStep3TableAdapter(response.body().getResponse(), getActivity());
                    for (int i = 0; i < response.body().getResponse().size(); i++) {
                        Log.d("getResponse", "skill Id: " + response.body().getResponse().get(i).getId());
                        Log.d("getResponse", "skill Name: " + response.body().getResponse().get(i).getName());
                        Log.d("getResponse", "||||||||||||||||||||||||||||||||");
                    }
                    listView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    listView.setAdapter(adapter);
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<SkillsResponse> call, Throwable t) {

            }
        });
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void buildDialog() {
        dialog = new AlertDialog.Builder(getActivity());
        final EditText et = new EditText(getActivity());
        et.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        dialog.setTitle("Add your skill");
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        et.setLayoutParams(lp);
        dialog.setView(et);
        dialog.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!et.getText().equals("")) {
                    Skill newSkill = new Skill();

                    newSkill.setName(et.getText().toString());
                    newSkill.setId("35");
                    newSkill.setOtherSkill(true);

                    adapter.addSkill(newSkill);
                    listView.smoothScrollToPosition(adapter.getItemCount() - 1);
                } else {
                    Toast.makeText(getActivity(), "Type something first", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.create();
        dialog.show();
    }
}
