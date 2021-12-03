package com.ambiguous.ambichat.ui.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ambiguous.ambichat.adapter.AdapterChatList;
import com.ambiguous.ambichat.api.ApiVolleyRequest;
import com.ambiguous.ambichat.interfaces.OnClickINterface;
import com.ambiguous.ambichat.model.ModelChatList;
import com.ambiguous.ambichat.utils.Constants;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ambichat.R;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class ChatFragment extends Fragment implements OnClickINterface {
    RecyclerView recyclerCHatList;
    final List<ModelChatList> modelChatLists = new ArrayList<>();
    public static int chatCLickPosition;
    TextView newGroup;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_chat, container, false);

        recyclerCHatList = view.findViewById(R.id.recyclerCHatList);
        newGroup = view.findViewById(R.id.newGroup);

        newGroup.setOnClickListener(v -> Toast.makeText(getContext(), "We are working on it", Toast.LENGTH_SHORT).show());


                    Constants.showProgresDialog(getContext());

//        modelChatLists.add(new ModelChatList("Aman Shakya","https://image.shutterstock.com/shutterstock/photos/1725825019/display_1500/stock-photo-mountains-under-mist-in-the-morning-amazing-nature-scenery-form-kerala-god-s-own-country-tourism-1725825019.jpg",
//                "Hello"));
//        modelChatLists.add(new ModelChatList("Anurag Goswami","https://images.unsplash.com/photo-1451187580459-43490279c0fa?ixlib=rb-1.2.1&ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&auto=format&fit=crop&w=752&q=80",
//                "hi"));
//        modelChatLists.add(new ModelChatList("Somath Gupta","https://images.unsplash.com/photo-1484589065579-248aad0d8b13?ixlib=rb-1.2.1&ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&auto=format&fit=crop&w=396&q=80",
//                "How are you?"));



        /*--------- Adapter for chatList data ----------*/

        AdapterChatList adapter =
                new AdapterChatList(getContext(),modelChatLists, this);
//        progressDialog1.dismiss();
        apiChatList(this);

        recyclerCHatList.setHasFixedSize(true);
        recyclerCHatList.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerCHatList.setAdapter(adapter);


        return view;

    }


    /*----- api for chat list -------*/

    private void apiChatList(OnClickINterface onClickINterface) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String username= sharedPreferences.getString("username","");

        String url = ApiVolleyRequest.allMessage;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            try {
                Constants.dismissProgressDialog(getContext());
                JSONObject jsonObject = new JSONObject(response);

                JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                modelChatLists.clear();

                JSONArray jsonArray = jsonObject1.getJSONArray("msgList");

                for (int i = 0; i <jsonArray.length() ; i++) {
                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                    modelChatLists.add(new ModelChatList(jsonObject2.getString("userFullName"),
                            jsonObject2.getString("profilePic"),
                            jsonObject2.getString("messages")));
                }

                AdapterChatList adapter =
                        new AdapterChatList(getContext(),modelChatLists,onClickINterface);
//        progressDialog1.dismiss();


                recyclerCHatList.setHasFixedSize(true);
                recyclerCHatList.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
                recyclerCHatList.setAdapter(adapter);

//                    getActivity().getApplicationContext().notifyAll();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(getContext(), "Server busy", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> map = new HashMap<>();

                map.put("username",/*username*/"amanchat81t04405130206");

                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
        requestQueue.add(stringRequest);
    }


    /*--------- get position method ---------*/

    @Override
    public void getPositin(int position) {
        chatCLickPosition = position;
        Objects.requireNonNull(getFragmentManager()).beginTransaction().replace(R.id.frame_container,new ChatOriginal()).commit();

    }


}