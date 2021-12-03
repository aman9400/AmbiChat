package com.ambiguous.ambichat.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.example.ambichat.databinding.ActivityResetPasswordBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ResetPassword extends AppCompatActivity {
    private ActivityResetPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        /*-------- save button click  ---------*/

        binding.saveBtnReset.setOnClickListener(v -> {
            if (binding.firstPassword.getText().toString().isEmpty()) {
                binding.firstPassword.setError("Field can't be Empty");
            } else if (binding.firstPassword.getText().toString().length() < 6) {
                binding.firstPassword.setError("Password length must greater than 6");
            } else if (!binding.firstPassword.getText().toString().equals(binding.secondPassword.getText().toString())) {
                Toast.makeText(ResetPassword.this, "Password doesnot match", Toast.LENGTH_SHORT).show();
            } else {
                apiResetPassword();  // api reset password

                Constants.showProgresDialog(ResetPassword.this); // show progress dialog
            }
        });
    }



    /*---- api intergration for reset password ----*/

    private void apiResetPassword() {

        String url = ApiVolleyRequest.forgotPassword;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            Constants.dismissProgressDialog(ResetPassword.this);
            try {
                JSONObject jsonObject = new JSONObject(response);


                Toast.makeText(ResetPassword.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();

                if (jsonObject.getString("msg").equals("Password changed successfully!")) {
                    startActivity(new Intent(ResetPassword.this, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(ResetPassword.this, "Error", Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(ResetPassword.this, "Server_busy", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                map.put("action", "chngPsd");
                map.put("userMail", ForgotPassword.userEmail);
                map.put("newPassword", binding.firstPassword.getText().toString());
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}