package com.crimepunch.app.datastore;

import android.content.Context;
import com.crimepunch.app.base.BaseClass;
import com.crimepunch.app.model.User;
import com.crimepunch.app.model.UserLocationUpdate;
import com.crimepunch.app.volley.LocationUpdateRequest;
import com.crimepunch.app.volley.SaveUserRequest;
import com.crimepunch.app.volley.SosRequest;

import javax.inject.Inject;

/**
 * Created by user-1 on 3/10/15.
 */
public class ServerImpl extends BaseClass implements Server {

    @Inject
    SaveUserRequest saveUserRequest;

    @Inject
    LocationUpdateRequest locationUpdateRequest;

    @Inject
    SosRequest sosRequest;


    @Override
    public void registerUser(Context context, User user) {
        saveUserRequest.processRequest(context, user);
    }

    @Override
    public void sendLocationUpdate(Context context, UserLocationUpdate userLocationUpdate) {
        locationUpdateRequest.processRequest(context, userLocationUpdate);
    }

    @Override
    public void sendSosRequest(Context context, UserLocationUpdate userLocationUpdate) {
        sosRequest.processRequest(context, userLocationUpdate);
    }
}
