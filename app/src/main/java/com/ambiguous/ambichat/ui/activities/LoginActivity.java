package com.ambiguous.ambichat.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ambiguous.ambichat.api.ApiVolleyRequest;
import com.ambiguous.ambichat.utils.Constants;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ambichat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import com.google.firebase.messaging.FirebaseMessaging;

import static android.content.ContentValues.TAG;

public class LoginActivity extends AppCompatActivity {

    TextView registerHere, login_forgotPass;
    EditText login_username, login_password;
    Button loginBtn;
    boolean loggeduserstatus;
    String deviceFirebaseToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);

        loggeduserstatus = preferences.getBoolean("statuslogin", false);
//        checkClassId = preferences.getString("classid", "0");


        if (loggeduserstatus) {

            startActivity(new Intent(LoginActivity.this, Dashboard.class));
            finish();
        }


        findIDs(); // find IDs method calling


        /*------ forgot password button click -----*/
        login_forgotPass.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, ForgotPassword.class)));


        /*------ register button click -----*/
        registerHere = findViewById(R.id.registerHere);
        registerHere.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, Signup.class));
            finish();
        });


        /*------ login button click -----*/
        loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(v -> {
            if (login_username.getText().toString().isEmpty()) {
                login_username.setError("username cant be Empty");
            } else if (login_password.getText().toString().isEmpty()) {
                login_password.setError("password cant be Empty");
            } else {
                logiApi();  // calling login api
            }
        });


        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    // Get new FCM registration token
                    String token = task.getResult();

                    // Log and toast
//                        String msg = getString(R.string.msg_token_fmt, token);
                    deviceFirebaseToken = token;
                    SharedPreferences preferences1 = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                    SharedPreferences.Editor editor = preferences1.edit();
                    editor.putString("firebasetoken", token);
                    editor.apply();
                    Log.d(TAG, token);
//                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                });


    }


    /*------ find ID method -----*/
    private void findIDs() {
        login_password = findViewById(R.id.login_password);
        login_username = findViewById(R.id.login_username);
        login_forgotPass = findViewById(R.id.login_forgotPass);
    }



    /*----------- login api ------------*/

    private void logiApi() {
        String url = ApiVolleyRequest.login;
        String android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);

                JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                Toast.makeText(LoginActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                String username = jsonObject1.getString("username");
                String userFullName = jsonObject1.getString("userFullName");
                String userMobNumber = jsonObject1.getString("userMobNumber");
                String userEmail = jsonObject1.getString("userEmail");
                String userLogID = jsonObject1.getString("userLogID");

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("username", username);

                editor.putBoolean("statuslogin", true);
                editor.putString("userFullName", userFullName);
                editor.putString("userMobNumber", userMobNumber);
                editor.putString("userEmail", userEmail);
                editor.putString("userLogID", userLogID);

                editor.apply();
                Intent intent = new Intent(LoginActivity.this, Dashboard.class);
                startActivity(intent);
                finish();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(LoginActivity.this, "Server busy", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> map = new HashMap<>();
                map.put("username", login_username.getText().toString());
                map.put("password", login_password.getText().toString());
                map.put("userToken", deviceFirebaseToken);
                map.put("userDeviceID", android_id);
                return map;
            }


        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}