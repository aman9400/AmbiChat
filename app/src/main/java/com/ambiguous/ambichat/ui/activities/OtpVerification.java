package com.ambiguous.ambichat.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OtpVerification extends AppCompatActivity {

    TextView link_verify, resendOtp;
    Button btnVerifyOtp;
    EditText et_verifyOtp;
    String email, password, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);



        /*--------  getting data from signup activity  --------*/
        email = getIntent().getStringExtra("emailSignup");
        password = getIntent().getStringExtra("passwordSignup");
        name = getIntent().getStringExtra("nameSignup");


// --------------- findIDs -------------
        btnVerifyOtp = findViewById(R.id.btnVerifyOtp);
        et_verifyOtp = findViewById(R.id.verifyOtpet);
        resendOtp = findViewById(R.id.resendOtp);



        /*--------  resend otp   --------*/
        resendOtp.setOnClickListener(v -> verifyOtp());



        /*--------  Fill user detail again  --------*/
        link_verify = findViewById(R.id.link_verify);
        link_verify.setOnClickListener(v -> startActivity(new Intent(OtpVerification.this, Signup.class)));



        /*--------  verify OTP button click  --------*/
        btnVerifyOtp.setOnClickListener(v -> {
            if (Signup.requestTypesign.equals("number")) {
                firebaseVerifyOtp(); // calling method for firebase otp verification
            } else {
                verifyOtp();  // calling method for email otp verification
            }
        });
    }



    /*--------- Email verification method ----------*/

    private void verifyOtp() {
        String url = ApiVolleyRequest.mailVerify;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);

                Toast.makeText(OtpVerification.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();

                if (jsonObject.getString("msg").equals("OTP doesn't matched!")) {
                    resendOtp.setVisibility(View.VISIBLE);
                } else {
                    registrationApi();  // registration api calling method

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(OtpVerification.this, "Server busy", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> map = new HashMap<>();
                map.put("action", "verifyOtp");
                map.put("userMail", email);
                map.put("userOTP", et_verifyOtp.getText().toString());
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }




    /*-------------  Register api method  ----------------*/

    private void registrationApi() {

        String url = ApiVolleyRequest.registration;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);

                Toast.makeText(OtpVerification.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();

                if (jsonObject.getString("msg").equals("Registered Successfully")) {
                    startActivity(new Intent(OtpVerification.this, LoginActivity.class));
                    finish();
                } else {

                    startActivity(new Intent(OtpVerification.this, Signup.class));
                    finish();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(OtpVerification.this, "Server busy", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> map = new HashMap<>();
                map.put("userContact", Signup.emailsign);
                map.put("userPassword", Signup.passwordsign);
                map.put("userName", Signup.mainsign);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }




    /*-------- Firebase OTP verification method --------*/

    private void firebaseVerifyOtp() {
        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(Signup.verificaionIDsign,
                et_verifyOtp.getText().toString());
        Constants.dismissProgressDialog(this);
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        registrationApi();
//                            startActivity(new Intent(OtpVerification.this, ResetPassword.class));
//                            finish();
                    } else {
                        Toast.makeText(OtpVerification.this, "Your last entered otp was wrong", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}