package com.crimepunch.app.service;

import android.content.Intent;
import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by user-1 on 4/10/15.
 */
public class CpInstanceIdListenerService extends InstanceIDListenerService {

    @Override
    public void onTokenRefresh() {
        // Fetch updated Instance ID token and notify our app's server of any changes (if applicable).
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }

}
