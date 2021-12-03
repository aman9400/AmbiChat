package com.ambiguous.ambichat.ui.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ambiguous.ambichat.api.ApiVolleyRequest;
import com.ambiguous.ambichat.ui.activities.Signup;
import com.ambiguous.ambichat.utils.Constants;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ambichat.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProfileFragment extends Fragment {

    EditText nameProfile,emailProfile,phoneProfile, userAboutUS;
    ImageView edit_profile, profilepic;
    boolean isClicked = false;
    Button save_btn;
    public static final int PICK_IMAGE = 1;
    Bitmap bitmap;
    String checkPicClicked = "0";
    String encodedImage="a";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);



        /*------ findIds ------*/
        nameProfile = view.findViewById(R.id.nameProfile);
        emailProfile = view.findViewById(R.id.emailProfile);
        phoneProfile = view.findViewById(R.id.phoneProfile);
        edit_profile = view.findViewById(R.id.edit_profile);
        profilepic = view.findViewById(R.id.profilepic);
        userAboutUS = view.findViewById(R.id.userAboutUS);
        save_btn = view.findViewById(R.id.save_btn);


        nameProfile.setEnabled(false);
        emailProfile.setEnabled(false);
        phoneProfile.setEnabled(false);
        userAboutUS.setEnabled(false);
        profilepic.setEnabled(false);



        /*-------- Api for update profile section ---------*/
        apiUpdateProfile();
        Constants.showProgresDialog(getContext());


        /*---------- Edit profile button click ----------*/
        edit_profile.setOnClickListener(v -> {
//                edit_profile.setBackgroundColor(Color.parseColor("#000"));



            if(isClicked){
                isClicked = false;
                nameProfile.setEnabled(true);
//                    emailProfile.setEnabled(true);
//                    phoneProfile.setEnabled(true);
                profilepic.setEnabled(true);
                userAboutUS.setEnabled(true);
                save_btn.setVisibility(View.VISIBLE);

            }else {
                isClicked = true;
                nameProfile.setEnabled(false);
                emailProfile.setEnabled(false);
                phoneProfile.setEnabled(false);
                profilepic.setEnabled(false);
                userAboutUS.setEnabled(false);
                save_btn.setVisibility(View.INVISIBLE);
            }
        });



        /*-------- Profile pic click --------*/

        profilepic.setOnClickListener(v -> imagefromStorage());



        /*------ Save Button click -------*/

        save_btn.setOnClickListener(v -> {
                apiSaveProfile();
                Constants.showProgresDialog(getContext());
        });


        return view;
    }



    /*---- Api for save Profile ----*/

    private void apiSaveProfile() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String username= sharedPreferences.getString("username","");

        String url = ApiVolleyRequest.setProfile;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            try {
                Constants.dismissProgressDialog(getContext());
                JSONObject jsonObject = new JSONObject(response);

                if(jsonObject.getString("msg").equalsIgnoreCase("Profile Updated Successfully")){
                    Toast.makeText(getContext(), ""+jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                }
                apiUpdateProfile();
             /*   JSONObject jsonObject1 = jsonObject.getJSONObject("result");

                String userFullName = jsonObject1.getString("userFullName");
                String userMobNumber = jsonObject1.getString("userMobNumber");
                String userEmail = jsonObject1.getString("userEmail");
                String username = jsonObject1.getString("username");
                String userProfileURL = jsonObject1.getString("userProfileURL");
                String userAboutUs = jsonObject1.getString("userAboutUs");*/

                /*nameProfile.setText(userFullName);
                emailProfile.setText(userEmail);
                phoneProfile.setText(userMobNumber);
                userAboutUS.setText(userAboutUs);
                try {
                    Picasso.get().load(userProfileURL).placeholder(R.drawable.iconprofile).error(R.drawable.iconprofile).into(profilepic);
                }catch (Exception e){
                    e.printStackTrace();
                }*/


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(getContext(), "Server busy", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> map = new HashMap<>();
                map.put("action","updateProfile" );
                map.put("userName",username);
                map.put("userPicUpdate",checkPicClicked);
                map.put("userPicData",encodedImage);
                map.put("userFullName",nameProfile.getText().toString());
                map.put("userAboutUs",userAboutUS.getText().toString());
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
        requestQueue.add(stringRequest);

    }


    /*---- Api for update Profile ----*/

    private void apiUpdateProfile() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String username= sharedPreferences.getString("username","");

            String url = ApiVolleyRequest.setProfile;

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
                try {
                    Constants.dismissProgressDialog(getContext());
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("result");

                    String userFullName = jsonObject1.getString("userFullName");
                    String userMobNumber = jsonObject1.getString("userMobNumber");
                    String userEmail = jsonObject1.getString("userEmail");
                    String username1 = jsonObject1.getString("username");
                    String userProfileURL = jsonObject1.getString("userProfileURL");
                    String userAboutUs = jsonObject1.getString("userAboutUs");

                    nameProfile.setText(userFullName);
                    emailProfile.setText(userEmail);
                    phoneProfile.setText(userMobNumber);
                    userAboutUS.setText(userAboutUs);
                    try {
                        Picasso.get().load(userProfileURL).placeholder(R.drawable.iconprofile).error(R.drawable.iconprofile).into(profilepic);
                    }catch (Exception e){
                        e.printStackTrace();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, error -> Toast.makeText(getContext(), "Server busy", Toast.LENGTH_SHORT).show()) {
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> map = new HashMap<>();
                    map.put("action","getProfile" );
                    map.put("userName",username);
                    return map;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
            requestQueue.add(stringRequest);
        }


        /*-------- Load image from storage ---------*/

        private void imagefromStorage() {
            checkPicClicked = "1";
        Intent intent = new Intent();
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePhotoIntent.putExtra("return-data", true);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_image)), PICK_IMAGE);
    }


    /*-------- Convert Image to String --------*/

    String imageToString(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }


    /*---------  Result for Selected Image -----*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && data != null) {

            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getActivity()).getContentResolver(), path);


                encodedImage = imageToString(bitmap);

                Log.e("a", encodedImage);

                byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                // card_profile.setImageBitmap(decodedByte);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}