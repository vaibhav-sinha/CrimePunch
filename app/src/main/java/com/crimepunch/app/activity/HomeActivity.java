package com.crimepunch.app.activity;

import android.os.Bundle;
import android.widget.FrameLayout;
import com.crimepunch.app.R;
import com.crimepunch.app.base.BaseActivity;
import com.crimepunch.app.datastore.Server;
import com.crimepunch.app.event.LocationDataUpdateEvent;
import com.crimepunch.app.fragment.GoogleMapFragment;
import com.crimepunch.app.helper.Session;
import com.crimepunch.app.model.Location;
import com.crimepunch.app.model.UserLocationUpdate;
import com.crimepunch.app.util.LocationUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import de.greenrobot.event.EventBus;

import javax.inject.Inject;
import java.math.BigDecimal;

/**
 * Created by user-1 on 3/10/15.
 */
public class HomeActivity extends BaseActivity implements OnMapReadyCallback {

    @Inject
    LocationUtil locationUtil;

    @Inject
    EventBus eventBus;

    @Inject
    Server server;

    @Inject
    Session session;

    private GoogleMapFragment googleMapFragment;
    private Boolean mapReady = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        googleMapFragment = (GoogleMapFragment) getSupportFragmentManager().findFragmentById(R.id.ah_map);
    }

    @Override
    protected void onStart() {
        super.onStart();
        eventBus.register(this);
        locationUtil.subscribe(this, true);
    }

    @Override
    protected void onStop() {
        locationUtil.unsubscribe();
        eventBus.unregister(this);
        super.onStop();
    }

    public void onEventMainThread(android.location.Location location) {
        UserLocationUpdate userLocationUpdate = new UserLocationUpdate();
        userLocationUpdate.setId(session.getUser(this).getId());
        userLocationUpdate.setLocation(new Location(BigDecimal.valueOf(location.getLatitude()), BigDecimal.valueOf(location.getLongitude())));
        server.sendLocationUpdate(this, userLocationUpdate);
    }

    public void onEventMainThread(LocationDataUpdateEvent event) {
        if (event.getSuccess() && mapReady) {
            googleMapFragment.addMarkers(event.getGridPoints());
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapReady = true;
    }
}
