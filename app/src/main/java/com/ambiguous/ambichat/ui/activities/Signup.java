package com.ambiguous.ambichat.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ambiguous.ambichat.api.ApiVolleyRequest;
import com.ambiguous.ambichat.api.ServiceHandler;
import com.ambiguous.ambichat.interfaces.WebServiceResponse;
import com.ambiguous.ambichat.utils.ConnectivityUtils;
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
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Signup extends AppCompatActivity {

    private static final String YOUR_API_SITE_KEY = "6LdENccaAAAAAO1Tkk35hN_6MbHpDs9kB0NsruVK";
    EditText reg_name, reg_email, reg_password;
    Button reg_submitBtn;
    TextView reg_signin;

    /*--------  static variables for firebse  --------*/
    public static String userEmailsign;
    public static String verificaionIDsign;
    public static String requestTypesign;


    /*--------  static variables for register  --------*/
    public static String emailsign;
    public static String passwordsign;
    public static String mainsign;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        findIds();
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        String email_pattern = "/^\\w+$/";
        Pattern pattern = Pattern.compile(email_pattern);
//        Pattern pattern1 = Pattern.compile(EMAIL_PATTERN);


        /*--------  Signin Button click  --------*/

        reg_signin = findViewById(R.id.reg_signin);
        reg_signin.setOnClickListener(v -> {
            startActivity(new Intent(Signup.this, LoginActivity.class));
            finish();
        });


        /*--------  Register Button click  --------*/

        reg_submitBtn.setOnClickListener(v -> {

            userEmailsign = reg_email.getText().toString();
            Matcher matcher = pattern.matcher(reg_email.getText().toString());

            if (reg_name.getText().toString().isEmpty()) {
                reg_name.setError("Name can't be empty");
            } else if (reg_password.getText().toString().isEmpty()) {
                reg_password.setError("Password can't be empty");
            } else if (reg_password.getText().toString().length() < 7) {
                reg_password.setError("Password is too short");
            } else if (reg_email.getText().toString().isEmpty()) {
                reg_email.setError("Required Field");
            } else {

                emailsign = reg_email.getText().toString();
                passwordsign = reg_password.getText().toString();
                mainsign = reg_name.getText().toString();

                if (isNumeric(reg_email.getText().toString())) {
                    /*SafetyNet.getClient(Signup.this).verifyWithRecaptcha(YOUR_API_SITE_KEY)
                            .addOnSuccessListener((Executor) Signup.this,
                                    new OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse>() {
                                        @Override
                                        public void onSuccess(SafetyNetApi.RecaptchaTokenResponse response) {
                                            // Indicates communication with reCAPTCHA service was
                                            // successful.
                                            String userResponseToken = response.getTokenResult();
                                            if (!userResponseToken.isEmpty()) {
                                                // Validate the user response token using the
                                                // reCAPTCHA siteverify API.
                                            }
                                        }
                                    })
                            .addOnFailureListener((Executor) Signup.this, new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    if (e instanceof ApiException) {
                                        // An error occurred when communicating with the
                                        // reCAPTCHA service. Refer to the status code to
                                        // handle the error appropriately.
                                        ApiException apiException = (ApiException) e;
                                        int statusCode = apiException.getStatusCode();
                                        Log.d("TAG", "Error: " + CommonStatusCodes
                                                .getStatusCodeString(statusCode));
                                    } else {
                                        // A different, unknown type of error occurred.
                                        Log.d("TAG", "Error: " + e.getMessage());
                                    }
                                }
                            });*/

                    otpFIrebase();    // method for firebase otp send
                    requestTypesign = "number";

                } else {
                    signUpApi();    // api for checking contacts in database
                    requestTypesign = "email";
                }
            }
        });
    }




    /*--------  Method for sending OTP through firebase  --------*/


    private void otpFIrebase() {

        Constants.showProgresDialog(this);

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + reg_email.getText().toString(),
                60,
                TimeUnit.SECONDS,
                Signup.this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

//                        Toast.makeText(Signup.this, ""+phoneAuthCredential, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(Signup.this, "" + e, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        verificaionIDsign = s;
                    }
                }
        );

        startActivity(new Intent(Signup.this, OtpVerification.class));
        Constants.dismissProgressDialog(this);

    }




    /*--------  Api for checking contact available in database or not  --------*/

    private void signUpApi() {
        String url = ApiVolleyRequest.checkContacts;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String checkExistStatus = jsonObject.getString("existsStatus");
                if (checkExistStatus.equals("0")) {
                    sendOtp();
                    Constants.showProgresDialog(Signup.this);
                } else {
                    Toast.makeText(Signup.this, "" + jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                }

//                    Toast.makeText(Signup.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(Signup.this, "Server busy", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> map = new HashMap<>();
                map.put("userContact", reg_email.getText().toString());
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }




    /*--------  OTP email api integration  --------*/

    private void sendOtp() {
        String url = ApiVolleyRequest.mailVerify;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);

                Toast.makeText(Signup.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();

                if (jsonObject.getString("msg").equals("Failed!")) {
                    Toast.makeText(Signup.this, "enter your details again", Toast.LENGTH_SHORT).show();
                } else {
                    Constants.dismissProgressDialog(Signup.this);
                    Intent intent = new Intent(Signup.this, OtpVerification.class);
                    intent.putExtra("emailSignup", reg_email.getText().toString());
                    intent.putExtra("passwordSignup", reg_password.getText().toString());
                    intent.putExtra("nameSignup", reg_name.getText().toString());
                    startActivity(intent);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(Signup.this, "Server busy", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> map = new HashMap<>();
                map.put("action", "sendOtp");
                map.put("userMail", reg_email.getText().toString());
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);
    }



    /*-------- FindIds method --------*/

    private void findIds() {

        reg_email = findViewById(R.id.reg_email);
        reg_name = findViewById(R.id.reg_name);
        reg_password = findViewById(R.id.reg_password);
        reg_submitBtn = findViewById(R.id.reg_submitBtn);
    }
    


    /*-------- Method for matching String is numeric or not --------*/

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }
}
