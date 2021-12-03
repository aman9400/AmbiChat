package com.ambiguous.ambichat.ui.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ambiguous.ambichat.api.ApiVolleyRequest;
import com.ambiguous.ambichat.ui.activities.ChangePassword;
import com.ambiguous.ambichat.ui.activities.Dashboard;
import com.ambiguous.ambichat.ui.activities.LoginActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ambichat.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SettingFragment extends Fragment {

    ConstraintLayout clPrivacyPolicy,clAboutUs,clForgotPassword,clLogout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_setting, container, false);

        clAboutUs = view.findViewById(R.id.clAboutUs);
        clPrivacyPolicy = view.findViewById(R.id.clPrivacyPolicy);
        clForgotPassword = view.findViewById(R.id.clForgotPassword);
        clLogout = view.findViewById(R.id.clLogout);

        clLogout.setOnClickListener(v -> logoutApi());

        clAboutUs.setOnClickListener(v -> {
//                Toast.makeText(getContext(), "Coming Soon...", Toast.LENGTH_SHORT).show();
            Uri uri = Uri.parse("https://ambilms.com/about-us/");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

        clForgotPassword.setOnClickListener(v -> {
//                Toast.makeText(getContext(), "Coming Soon...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(),ChangePassword.class);
                startActivity(intent);

        });

        clPrivacyPolicy.setOnClickListener(v -> {
//                Toast.makeText(getContext(), "Coming Soon...", Toast.LENGTH_SHORT).show();
            Uri uri = Uri.parse("https://ambilms.com/privacy-policy-2/");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });


        return view;
    }



    /*------- logout api ---------*/
    private void logoutApi() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String uselogID = sharedPreferences.getString("userLogID", "");


        String url = ApiVolleyRequest.logout;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);

                Toast.makeText(getContext(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("username", "");

                editor.putBoolean("statuslogin", false);
                editor.putString("userFullName", "");
                editor.putString("userMobNumber", "");
                editor.putString("userEmail", "");
                editor.putString("userLogID", "");

                editor.apply();

                startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().finish();


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(getActivity(), "" + error, Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> map = new HashMap<>();
                map.put("userLogID", uselogID);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
        requestQueue.add(stringRequest);
    }

}