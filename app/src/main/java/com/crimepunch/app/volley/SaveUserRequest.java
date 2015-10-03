package com.crimepunch.app.volley;

import android.content.Context;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.crimepunch.app.base.BaseClass;
import com.crimepunch.app.config.Constants;
import com.crimepunch.app.event.UserRegistrationEvent;
import com.crimepunch.app.helper.NetworkAccessHelper;
import com.crimepunch.app.model.User;
import com.google.gson.Gson;
import de.greenrobot.event.EventBus;

import javax.inject.Inject;

/**
 * Created by user-1 on 3/10/15.
 */
public class SaveUserRequest extends BaseClass {

    @Inject
    EventBus eventBus;
    @Inject
    NetworkAccessHelper networkAccessHelper;

    public void processRequest(Context context, User user) {
        GenericPostVolleyRequest<User> request = new GenericPostVolleyRequest<>(Constants.SAVE_PROFILE_URL, createErrorListener(), createSuccessListener(), user);
        networkAccessHelper.submitNetworkRequest("PostComplaints", request);
    }

    private Response.Listener<String> createSuccessListener() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                User user = new Gson().fromJson(response, User.class);
                UserRegistrationEvent event = new UserRegistrationEvent();
                if(user == null) {
                    event.setSuccess(false);
                }
                else {
                    event.setSuccess(true);
                }
                eventBus.post(event);
            }
        };
    }

    private Response.ErrorListener createErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                UserRegistrationEvent event = new UserRegistrationEvent();
                event.setSuccess(false);
                event.setError(error.toString());
                eventBus.post(event);
            }
        };
    }
}
