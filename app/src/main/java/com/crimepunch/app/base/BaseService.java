package com.crimepunch.app.base;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.crimepunch.app.application.CrimePunchApplication;

/**
 * Created by user-1 on 3/10/15.
 */
public class BaseService extends Service {
    public BaseService() {
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
