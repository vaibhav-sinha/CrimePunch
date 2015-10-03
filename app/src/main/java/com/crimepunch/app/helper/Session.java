package com.crimepunch.app.helper;

import android.content.Context;
import com.crimepunch.app.base.BaseClass;
import com.crimepunch.app.model.User;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import javax.inject.Inject;

/**
 * Created by user-1 on 3/10/15.
 */
public class Session extends BaseClass {

    @Inject
    SharedPreferencesHelper sharedPreferencesHelper;


    public User getUser(Context context) {
        String json = sharedPreferencesHelper.getString(context, "ServerData", "User", null);
        if(json == null) {
            return null;
        }
        Gson gson = new Gson();
        try {
            return gson.fromJson(json, User.class);
        } catch (JsonParseException e) {
            return null;
        }
    }

    public void setUser(Context context, User user) {
        Gson gson = new Gson();
        sharedPreferencesHelper.putString(context, "ServerData", "User", gson.toJson(user));
    }
}
