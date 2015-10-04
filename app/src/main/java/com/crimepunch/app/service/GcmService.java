package com.crimepunch.app.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.crimepunch.app.R;
import com.crimepunch.app.activity.SosActivity;
import com.crimepunch.app.base.BaseService;
import com.crimepunch.app.helper.Session;
import com.google.android.gms.gcm.GcmListenerService;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;

/**
 * Created by user-1 on 4/10/15.
 */
public class GcmService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        Double latitude = data.getDouble("latitude");
        Double longitude = data.getDouble("longitude");

        Location location = new Location("");
        location.setLatitude(latitude);
        location.setLongitude(longitude);

        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);

        sendNotification(message, location);
    }

    private void sendNotification(String message, Location location) {
        Intent intent = new Intent(this, SosActivity.class);
        intent.putExtra("location", location);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.bubble_mask)
                .setContentTitle("GCM Message")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
