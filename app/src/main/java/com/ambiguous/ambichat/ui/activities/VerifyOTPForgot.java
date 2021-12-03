package com.ambiguous.ambichat.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import com.example.ambichat.databinding.ActivityForgotPasswordBinding;
import com.example.ambichat.databinding.ActivityVerifyOTPForgotBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class VerifyOTPForgot extends AppCompatActivity {
    String useremail;
    private ActivityVerifyOTPForgotBinding binding;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerifyOTPForgotBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /*---- Progress Dialog ----*/
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading...");
        progressDialog.setCancelable(false);


        useremail = getIntent().getStringExtra("useremail");


        /*-------- verify button click ---------*/

        binding.verifyOTBtn.setOnClickListener(v -> {
            if (binding.verifyOFEt.getText().toString().isEmpty()) {
                binding.verifyOFEt.setError("Field can't be Empty");
            } else {

                progressDialog.show();
                if (ForgotPassword.requestType.equals("number")) {
                    firebaseVerifyOtp();  // firebase verify otp
                } else {
                    apiVerifyOtp();   // email verify otp
                }
            }
        });
    }



    /*----- Firebase OTP verification -----*/

    private void firebaseVerifyOtp() {
        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(ForgotPassword.verificaionID,
                binding.verifyOFEt.getText().toString());
        Constants.dismissProgressDialog(this);
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(VerifyOTPForgot.this, ResetPassword.class));
                        finish();
                    } else {
                        Toast.makeText(VerifyOTPForgot.this, "Your last entered otp was wrong", Toast.LENGTH_SHORT).show();
                    }
                });
    }




    /*------ verify otp through api -----*/

    private void apiVerifyOtp() {

        String url = ApiVolleyRequest.forgotPassword;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
//                Constants.dismissProgressDialog(VerifyOTPForgot.this);
            progressDialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(response);


                Toast.makeText(VerifyOTPForgot.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();

                if (jsonObject.getString("msg").equals("OTP matched")) {
                    startActivity(new Intent(VerifyOTPForgot.this, ResetPassword.class));
                    finish();
                } else {
                    Toast.makeText(VerifyOTPForgot.this, "Incorrect OTP", Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(VerifyOTPForgot.this, "Server busy", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                map.put("action", "matchOtp");
                map.put("userMail", ForgotPassword.userEmail);
                map.put("userOTP", binding.verifyOFEt.getText().toString());
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}


