package com.crimepunch.app.volley;

import android.content.Context;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.crimepunch.app.base.BaseClass;
import com.crimepunch.app.config.Constants;
import com.crimepunch.app.event.LocationDataUpdateEvent;
import com.crimepunch.app.helper.NetworkAccessHelper;
import com.crimepunch.app.model.LocationUpdateResponse;
import com.crimepunch.app.model.UserLocationUpdate;
import com.google.gson.Gson;
import de.greenrobot.event.EventBus;

import javax.inject.Inject;

/**
 * Created by user-1 on 3/10/15.
 */
public class LocationUpdateRequest extends BaseClass {

    @Inject
    EventBus eventBus;

    @Inject
    NetworkAccessHelper networkAccessHelper;

    public void processRequest(Context context, UserLocationUpdate userLocationUpdate) {
        GenericPostVolleyRequest<UserLocationUpdate> request = new GenericPostVolleyRequest<>(Constants.LOCATION_UPDATE_URL, createErrorListener(), createSuccessListener(), userLocationUpdate);
        networkAccessHelper.submitNetworkRequest("UpdateLocation", request);
    }

    private Response.Listener<String> createSuccessListener() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LocationUpdateResponse locationUpdateResponse = new Gson().fromJson(response, LocationUpdateResponse.class);
                LocationDataUpdateEvent event = new LocationDataUpdateEvent();
                if(locationUpdateResponse == null) {
                    event.setSuccess(false);
                }
                else {
                    event.setSuccess(true);
                    event.setCrimeTypes(locationUpdateResponse.getCrimeTypes());
                    event.setGridPoints(locationUpdateResponse.getGridPoints());
                    event.setInterestingPoints(locationUpdateResponse.getInterestingPoints());
                }
                eventBus.post(event);
            }
        };
    }

    private Response.ErrorListener createErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LocationDataUpdateEvent event = new LocationDataUpdateEvent();
                event.setSuccess(false);
                event.setError(error.toString());
                eventBus.post(event);
            }
        };
    }
}

