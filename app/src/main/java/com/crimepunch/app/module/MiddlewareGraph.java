package com.crimepunch.app.module;

import android.content.Context;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.crimepunch.app.activity.HomeActivity;
import com.crimepunch.app.activity.ProfileActivity;
import com.crimepunch.app.activity.RegistrationActivity;
import com.crimepunch.app.datastore.Server;
import com.crimepunch.app.datastore.ServerImpl;
import com.crimepunch.app.helper.NotificationHelper;
import com.crimepunch.app.helper.Session;
import com.crimepunch.app.helper.SharedPreferencesHelper;
import com.crimepunch.app.util.LocationUtil;
import com.crimepunch.app.volley.LocationUpdateRequest;
import com.crimepunch.app.volley.SaveUserRequest;
import dagger.Module;
import dagger.Provides;
import de.greenrobot.event.EventBus;

import javax.inject.Singleton;

/**
 * Created by user-1 on 3/10/15.
 */
@Module(
        injects = {
                RegistrationActivity.class,
                ProfileActivity.class,
                HomeActivity.class,
                ServerImpl.class,
                SaveUserRequest.class,
                LocationUtil.class,
                LocationUpdateRequest.class,
                NotificationHelper.class
        },
        library = true,
        complete = true
)
public class MiddlewareGraph {

    private Context applicationContext;

    public MiddlewareGraph(Context context) {
        this.applicationContext = context;
    }

    @Provides
    public Context provideApplicationContext() {
        return applicationContext;
    }

    @Provides
    @Singleton
    EventBus provideEventBus() {
        return EventBus.getDefault();
    }

    @Provides
    @Singleton
    public RequestQueue provideRequestQueue() {
        return Volley.newRequestQueue(this.applicationContext);
    }

    @Provides
    @Singleton
    public Server provideServer() {
        return new ServerImpl();
    }

    @Provides
    @Singleton
    public SharedPreferencesHelper provideSharedPreferencesHelper() {
        return new SharedPreferencesHelper();
    }

    @Provides
    @Singleton
    public Session provideSession() {
        return new Session();
    }

    @Provides
    @Singleton
    public SaveUserRequest provideSaveUserRequest() {
        return new SaveUserRequest();
    }

    @Provides
    @Singleton
    public LocationUtil provideLocationUtil() {
        return new LocationUtil();
    }

    @Provides
    @Singleton
    public LocationUpdateRequest provideLocationUpdateRequest() {
        return new LocationUpdateRequest();
    }

    @Provides
    @Singleton
    public NotificationHelper provideNotificationHelper() {
        return new NotificationHelper();
    }
}
