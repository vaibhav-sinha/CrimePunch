package com.crimepunch.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.crimepunch.app.service.WatchdogService;

/**
 * Created by user-1 on 3/10/15.
 */
public class PhoneBootedBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent myIntent = new Intent(context, WatchdogService.class);
        context.startService(myIntent);

    }
}
