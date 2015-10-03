package com.crimepunch.app.datastore;

import android.content.Context;
import com.crimepunch.app.base.BaseClass;
import com.crimepunch.app.model.User;
import com.crimepunch.app.volley.SaveUserRequest;

import javax.inject.Inject;

/**
 * Created by user-1 on 3/10/15.
 */
public class ServerImpl extends BaseClass implements Server {

    @Inject
    SaveUserRequest saveUserRequest;


    @Override
    public void registerUser(Context context, User user) {
        saveUserRequest.processRequest(context, user);
    }
}
