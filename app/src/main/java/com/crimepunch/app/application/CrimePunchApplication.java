package com.crimepunch.app.application;

import android.annotation.TargetApi;
import android.app.Application;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;
import com.crimepunch.app.BuildConfig;
import com.crimepunch.app.module.MiddlewareGraph;
import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import dagger.ObjectGraph;
import io.fabric.sdk.android.Fabric;

import java.util.Arrays;
import java.util.Collections;

/**
 * Created by user-1 on 3/10/15.
 */
public class CrimePunchApplication extends Application {

    private static ObjectGraph objectGraph;
    private static CrimePunchApplication instance;

    @Override
    public void onCreate() {
        if (BuildConfig.DEBUG) {
            Log.d("AppHubApplication", "Debug mode enabled");
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()   // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .penaltyDialog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }
        super.onCreate();
        instance = this;
        objectGraph = ObjectGraph.create(getModules());
    }

    private Object[] getModules() {
        return Collections.singletonList(new MiddlewareGraph(this)).toArray();
    }

    public void inject(Object target) {
        objectGraph.inject(target);
    }

    public static CrimePunchApplication getInstance() {
        return instance;
    }
}
