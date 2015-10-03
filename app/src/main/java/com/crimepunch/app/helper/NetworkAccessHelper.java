package com.crimepunch.app.helper;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.crimepunch.app.base.BaseClass;

import javax.inject.Inject;

/**
 * Created by user-1 on 3/10/15.
 */
public class NetworkAccessHelper extends BaseClass {

    @Inject
    Context applicationContext;
    @Inject
    RequestQueue requestQueue;

    public void submitNetworkRequest(String requestTag, Request request) {
        requestQueue.cancelAll(requestTag);
        requestQueue.add(request);
        request.setTag(requestTag);
    }
}
