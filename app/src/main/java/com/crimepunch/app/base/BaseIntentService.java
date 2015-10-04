package com.crimepunch.app.base;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import com.crimepunch.app.application.CrimePunchApplication;

/**
 * Created by user-1 on 4/10/15.
 */
public class BaseIntentService extends IntentService {

    public BaseIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ((CrimePunchApplication)getApplication()).inject(this);
    }
}
