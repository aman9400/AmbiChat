package com.ambiguous.ambichat.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ambiguous.ambichat.api.ApiVolleyRequest;
import com.ambiguous.ambichat.ui.fragments.ChatFragment;
import com.ambiguous.ambichat.ui.fragments.SettingFragment;
import com.ambiguous.ambichat.utils.Constants;
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

public class ChangePassword extends AppCompatActivity {
    EditText currentPassword,newPassword,confirmPassword;
    Button saveBtnChagePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        currentPassword = findViewById(R.id.currentPassword);
        newPassword = findViewById(R.id.newPassword);
        confirmPassword = findViewById(R.id.confirmPassword);
        saveBtnChagePassword = findViewById(R.id.saveBtnChagePassword);

        saveBtnChagePassword.setOnClickListener(v -> {
            if(currentPassword.getText().toString().isEmpty()){
                currentPassword.setError("Field cant be Empty");
            }else if(newPassword.getText().toString().isEmpty()){
                newPassword.setError("Field cant be Empty");
            }else if(!newPassword.getText().toString().equalsIgnoreCase(confirmPassword.getText().toString())){
                Toast.makeText(ChangePassword.this, "Password doesnot match", Toast.LENGTH_SHORT).show();
            }else{

                Constants.showProgresDialog(ChangePassword.this);
                apiChangePassword();
            }
        });

    }

    private void apiChangePassword() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ChangePassword.this);
        String username = sharedPreferences.getString("username", "");


        String url = ApiVolleyRequest.changePassword;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            Constants.dismissProgressDialog(ChangePassword.this);
            try {
                JSONObject jsonObject = new JSONObject(response);

                String msg = jsonObject.getString("msg");
                if(msg.equalsIgnoreCase("Password Changed Successfully!")){
                    Toast.makeText(ChangePassword.this, ""+msg, Toast.LENGTH_SHORT).show();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,new ChatFragment()).commit();
                }else {
                    Toast.makeText(ChangePassword.this, ""+msg, Toast.LENGTH_SHORT).show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(ChangePassword.this, "" + error, Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> map = new HashMap<>();
                map.put("username", username);
                map.put("oldPassword",currentPassword.getText().toString());
                map.put("newPassword",newPassword.getText().toString());
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

}