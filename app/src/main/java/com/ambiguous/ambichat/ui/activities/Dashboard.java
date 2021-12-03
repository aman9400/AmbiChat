package com.ambiguous.ambichat.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.ambiguous.ambichat.api.ApiVolleyRequest;
import com.ambiguous.ambichat.ui.fragments.ChatFragment;
import com.ambiguous.ambichat.ui.fragments.ProfileFragment;
import com.ambiguous.ambichat.ui.fragments.SettingFragment;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ambichat.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Dashboard extends AppCompatActivity {
    private ActionBar toolbar;
    Button logout_button;
    Fragment selectedFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorAccent));

//        getSupportActionBar()/* or getSupportActionBar() */.setTitle(Html.fromHtml("<font color=\"#ffffff\">" + " Profile " + "</font>"));
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.back);
//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimaryDark)));




        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Dashboard.this);

//        toolbar = getSupportActionBar();



        /*------- logout button click ------*/

        logout_button = findViewById(R.id.logout_button);
        logout_button.setOnClickListener(v -> {
//                logoutApi();
        });



        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,new ChatFragment()).commit();


        /*------ bottom navigation view -----*/

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.profile_nav: {
                    selectedFragment = new ProfileFragment();
                    break;
                }
                case R.id.setting: {
                    selectedFragment = new SettingFragment();
                    break;
                }
                case R.id.chat: {
                    selectedFragment = new ChatFragment();
                    break;
                }

            }
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,selectedFragment).commit();
            return false;
        });

//        toolbar.setTitle("Shop");
    }



}