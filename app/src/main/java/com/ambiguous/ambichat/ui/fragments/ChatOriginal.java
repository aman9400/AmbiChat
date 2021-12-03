package com.ambiguous.ambichat.ui.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.ambiguous.ambichat.adapter.AdapterChat;
import com.ambiguous.ambichat.adapter.AdapterChatList;
import com.ambiguous.ambichat.api.ApiVolleyRequest;
import com.ambiguous.ambichat.firebase.MyFirebaseMessagingService;
import com.ambiguous.ambichat.model.ModelChat;
import com.ambiguous.ambichat.ui.activities.LoginActivity;
import com.ambiguous.ambichat.ui.activities.SplashScreen;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ambichat.R;
import com.google.firebase.messaging.FirebaseMessagingService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class ChatOriginal extends Fragment {

    String userWho ;
    String msg ;
    String time ;
    final List<ModelChat> modelChats = new ArrayList<>();
    RecyclerView originalrecyclerChat;
    public static int splash = 1000;
    private EditText msgBox;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_chat_original, container, false);

        Toast.makeText(getContext(), ""+ChatFragment.chatCLickPosition, Toast.LENGTH_SHORT).show();

        originalrecyclerChat = view.findViewById(R.id.originalrecyclerChat);

        msgBox = view.findViewById(R.id.msgBox);
        ImageView sendBtn = view.findViewById(R.id.sendBtn);
        ImageView attachment = view.findViewById(R.id.attachment);

//            useSplash();
                use();
        apiChatResponse();

        sendBtn.setOnClickListener(v -> apiSendMsg());

        return view;
    }

    private void apiChatResponse() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String username= sharedPreferences.getString("username","");

        String url = ApiVolleyRequest.singleMessage;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
//                useSplash();
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                JSONArray jsonArray = jsonObject1.getJSONArray("msgList");

                modelChats.clear();

                for (int i = 0; i <jsonArray.length() ; i++) {
                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);


                    modelChats.add(new ModelChat(jsonObject2.getString("message"),
                            (jsonObject2.getString("time")),
                            (jsonObject2.getString("user"))));
                }



                AdapterChat adapter =
                        new AdapterChat(getContext(),modelChats);
//        progressDialog1.dismiss();


                originalrecyclerChat.setHasFixedSize(true);

                originalrecyclerChat.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
                originalrecyclerChat.setAdapter(adapter);
                originalrecyclerChat.scrollToPosition(modelChats.size());


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(getContext(), "Server Busy", Toast.LENGTH_SHORT).show()){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("username",/*username*/"amanchat81t04405130206");
                map.put("senderUsername","shivanichat89t121203133705");
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        requestQueue.add(stringRequest);

    }


    /*------ splash method -------*/

    public void use() {

        new Handler().postDelayed(() -> {

            if(MyFirebaseMessagingService.checkStatus){
                apiChatResponse();
            }

        }, 1000);
    }

    /*------ api for send message -------*/

    private void apiSendMsg(){

        String url = ApiVolleyRequest.sendMessage;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response ->{

            try {
                JSONObject jsonObject = new JSONObject(response);
                Toast.makeText(getContext(), ""+jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();

                msgBox.setText("");
                apiChatResponse();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error ->{

        }){
            @Override
            protected Map<String, String> getParams() {
               Map<String, String> map = new HashMap<>();
               map.put("username","amanchat81t04405130206");
               map.put("sendToUsername","shivanichat89t121203133705");
               map.put("msg",msgBox.getText().toString());
               return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
        requestQueue.add(stringRequest);
    }

}