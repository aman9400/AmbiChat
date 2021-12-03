package com.ambiguous.ambichat.firebase;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.ambiguous.ambichat.ui.activities.Dashboard;
import com.ambiguous.ambichat.ui.fragments.ChatOriginal;
import com.example.ambichat.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Objects;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    String s, title;
    public static boolean checkStatus = false;
    

    @Override
    public void onNewToken(@NotNull String token) {

//        super.onNewToken(s);
        Log.e("NEW_TOKEN", token);


    }

    @Override
    public void onMessageReceived(@NotNull RemoteMessage remoteMessage) {

        try {

        }catch (Exception e){
            e.printStackTrace();
        }

        Map<String, String> params = remoteMessage.getData();
        JSONObject object = new JSONObject(params);
        try {

             s = object.getString("body");
             title = object.getString("title");
             if(object.getString("type").equalsIgnoreCase("1")){
                 checkStatus = true;
//                 new ChatOriginal().use();
//                Intent intent = new Intent(getApplicationContext(), Dashboard.class);
//                 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                 startActivity(intent);
//                 startLoginFragmemt(getApplicationContext());

//
//                       getSupportFragmentManager().beginTransaction()
//                       .replace(R.id.frame_container,new ChatOriginal()).commit();
             }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("JSON_OBJECT", object.toString());

        String NOTIFICATION_CHANNEL_ID = "0";

        long[] pattern = {0, 1000, 500, 1000};

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Your Notifications",
                    NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.setDescription("Hello");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(pattern);
            notificationChannel.enableVibration(true);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }

        // to diaplay notification in DND Mode
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = mNotificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID);
            channel.canBypassDnd();
        }

        try {

            Intent resultIntent =   new Intent(this, Notification.class);
            resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(this,1,resultIntent,
                    PendingIntent.FLAG_ONE_SHOT);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);


            notificationBuilder.setAutoCancel(true)
                    .setColor(ContextCompat.getColor(this, R.color.colorAccent))
                    .setContentTitle(title)

                    .setContentText(s)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.iconprofile)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);




            mNotificationManager.notify(1000, notificationBuilder.build());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

//    public void startLoginFragmemt(Context context) {
//        Activity activity = (Activity) context;
//        FragmentManager fragmentManager = ((Activity) context).getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
//                .replace(R.id.frame_container, ).commit();
//
//    }
}
