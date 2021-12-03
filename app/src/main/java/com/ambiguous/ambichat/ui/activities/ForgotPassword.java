package com.ambiguous.ambichat.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.view.View;
import android.widget.Toast;

import com.ambiguous.ambichat.api.ApiVolleyRequest;
import com.ambiguous.ambichat.utils.Constants;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ambichat.R;
import com.example.ambichat.databinding.ActivityForgotPasswordBinding;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class ForgotPassword extends AppCompatActivity {
    private ActivityForgotPasswordBinding binding;
    public static String userEmail;
    public static String verificaionID;
    public static String requestType;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        /*---- Progress Dialog ----*/
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading...");
        progressDialog.setCancelable(false);




        /*--------- forgot password button click -----------*/

        binding.foergotSend.setOnClickListener(v -> {
            userEmail = binding.forgotEmail.getText().toString();

            if (binding.forgotEmail.getText().toString().isEmpty()) {
                binding.forgotEmail.setError("Please enter email...");
            } else {

                if (isNumeric(binding.forgotEmail.getText().toString())) {
                    checkMobileNumber();  // checking mobile number exist or not
                    requestType = "number";
                } else {
                    requestType = "email";
                    apiForgotPassword();  // api for forgot password
                    progressDialog.show();

                }

            }
        });
    }


    /*--------- Mobile number checking in database ----------*/
    private void checkMobileNumber() {

        String url = ApiVolleyRequest.numberCheck;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            Constants.dismissProgressDialog(ForgotPassword.this);
            try {
                JSONObject jsonObject = new JSONObject(response);

                Toast.makeText(ForgotPassword.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();

                if (jsonObject.getString("msg").equals("Number exists")) {

                    firebaseApi();  //send mobile number

                } else {
                    Toast.makeText(ForgotPassword.this, "Number doesnot match", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(ForgotPassword.this, "Server busy", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
//                map.put("action","sendOtp");
                map.put("userNumber", binding.forgotEmail.getText().toString());
//                map.put("userName",name);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);

    }


    /*-------- Firebase OTP send on mobile number -------*/
    private void firebaseApi() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + ForgotPassword.userEmail,
                60,
                TimeUnit.SECONDS,
                ForgotPassword.this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {


                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(ForgotPassword.this, "" + e, Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        verificaionID = s;
                    }
                }
        );

        String phone = "+91" + binding.forgotEmail.getText().toString();
//        sendVerificationCode(phone);
        Intent intent = new Intent(ForgotPassword.this, VerifyOTPForgot.class);
        intent.putExtra("useremial", binding.forgotEmail.getText().toString());
        startActivity(intent);
        finish();


    }


    /*----------- Forgot password Api ------------*/

    private void apiForgotPassword() {
        String url = ApiVolleyRequest.forgotPassword;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
//                Constants.dismissProgressDialog(ForgotPassword.this);
            progressDialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(response);

                Toast.makeText(ForgotPassword.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
//                    Constants.dismissProgressDialog(ForgotPassword.this);
                if (jsonObject.getString("msg").equals("Please check your mail!")) {
                    Intent intent = new Intent(ForgotPassword.this, VerifyOTPForgot.class);
                    intent.putExtra("useremial", binding.forgotEmail.getText().toString());
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ForgotPassword.this, "Wrong email", Toast.LENGTH_SHORT).show();
                    Constants.dismissProgressDialog(ForgotPassword.this);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(ForgotPassword.this, "Server busy", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                map.put("action", "sendOtp");
                map.put("userMail", binding.forgotEmail.getText().toString());
//                map.put("userName",name);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);
    }


    /*----- checking string is numeric or not -----*/

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }


}