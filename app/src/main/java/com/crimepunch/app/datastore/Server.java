package com.crimepunch.app.datastore;

import android.content.Context;
import com.crimepunch.app.base.BaseClass;
import com.crimepunch.app.model.User;

import javax.inject.Inject;

/**
 * Created by user-1 on 3/10/15.
 */
public interface Server {

    void registerUser(Context context, User user);
}