package com.crimepunch.app.helper;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import com.crimepunch.app.R;

/**
 * Created by user-1 on 3/10/15.
 */
public class NotificationHelper {

    public void sendComplaintUpdateNotification(Context caller, String msg, Bitmap icon, int id) {
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent toLaunch;
        toLaunch = new Intent(caller, null);

        PendingIntent intentBack = PendingIntent.getActivity(caller, 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);

        //PendingIntent intentBack = PendingIntent.getActivity(caller, 0, toLaunch, 0);

        NotificationManager notifier = (NotificationManager) caller.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(caller)
                        .setContentTitle("Crime Punch : Be alert. Entering unsafe zone")
                        .setContentText(msg)
                        .setAutoCancel(true)
                        .setContentIntent(intentBack);

        if(icon != null) {
            mBuilder.setLargeIcon(icon);
        }
        mBuilder.setSound(alarmSound);
        notifier.notify(id, mBuilder.build());
    }
}
