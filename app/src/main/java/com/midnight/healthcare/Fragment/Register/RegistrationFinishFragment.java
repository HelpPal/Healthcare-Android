package com.midnight.healthcare.Fragment.Register;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.midnight.healthcare.API.API;
import com.midnight.healthcare.API.APIFactory;
import com.midnight.healthcare.API.LoginResponse;
import com.midnight.healthcare.Activity.CNAFeedActivity;
import com.midnight.healthcare.Activity.JobDeefActivity;
import com.midnight.healthcare.Activity.LoginActivity;
import com.midnight.healthcare.Activity.RegisterActivity;
import com.midnight.healthcare.Global;
import com.midnight.healthcare.R;
import com.midnight.healthcare.SomeRandomeClass;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegistrationFinishFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegistrationFinishFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrationFinishFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Button okButton;


    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public RegistrationFinishFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistrationFinishFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegistrationFinishFragment newInstance(String param1, String param2) {
        RegistrationFinishFragment fragment = new RegistrationFinishFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration_finish, container, false);

        setAllTheData(view);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteractionRegistrationFinish(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void setAllTheData(View view){
        okButton = (Button) view.findViewById(R.id.okButton);

        sharedPref = getActivity().getSharedPreferences("my", Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        okButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        okButton.setBackground(getResources().getDrawable(R.drawable.round_blue_active));
                        okButton.setTextColor(getResources().getColor(R.color.blueHighlighted));
                        break;
                    case MotionEvent.ACTION_UP: {
                        okButton.setBackground(getResources().getDrawable(R.drawable.round_blue));
                        okButton.setTextColor(0xFFFFFFFF);
                        final ProgressDialog dialog = new ProgressDialog(getActivity());
                        dialog.setMessage("Loading...");
                        dialog.show();
                        API api = APIFactory.createAPI();
                        SomeRandomeClass.AddToList("username", ((Global)getActivity().getApplication()).getRegModel().getEmail());
                        SomeRandomeClass.AddToList("password", ((Global)getActivity().getApplication()).getRegModel().getPassword());
                        Call<LoginResponse> logg = api.login(SomeRandomeClass.GetData());
                        logg.enqueue(new Callback<LoginResponse>() {
                            @Override
                            public void onResponse(Call<LoginResponse> call, final Response<LoginResponse> response) {
                                if (response.body().getUser().getId() != null) {
                                    ((Global) getActivity().getApplication()).setCurrentUser(response.body().getUser());
                                    editor.putString("email", ((Global)getActivity().getApplication()).getRegModel().getEmail());
                                    editor.putString("password", ((Global)getActivity().getApplication()).getRegModel().getPassword());
                                    editor.apply();


                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (response.body().getUser().getType().equals("0")) {
                                                Intent myIntent = new Intent(getActivity(), CNAFeedActivity.class);
                                                startActivity(myIntent);
                                            } else {
                                                Intent myIntent = new Intent(getActivity(), JobDeefActivity.class);
                                                startActivity(myIntent);
                                            }
                                            dialog.dismiss();
                                        }
                                    });
                                }
                                else{
                                    dialog.dismiss();
                                }
                            }

                            @Override
                            public void onFailure(Call<LoginResponse> call, Throwable t) {
                                //SMTH
                                dialog.dismiss();
                            }
                        });
                        break;
                    }
                }
                return false;
            }
        });
    }

    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteractionRegistrationFinish(Uri uri);
    }


}
