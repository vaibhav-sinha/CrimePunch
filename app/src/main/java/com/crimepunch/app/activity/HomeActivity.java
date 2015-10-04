package com.crimepunch.app.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.crimepunch.app.R;
import com.crimepunch.app.base.BaseActivity;
import com.crimepunch.app.datastore.Server;
import com.crimepunch.app.event.LocationDataUpdateEvent;
import com.crimepunch.app.event.SosSentEvent;
import com.crimepunch.app.fragment.GoogleMapFragment;
import com.crimepunch.app.helper.NotificationHelper;
import com.crimepunch.app.helper.Session;
import com.crimepunch.app.model.CrimeType;
import com.crimepunch.app.model.GridPoint;
import com.crimepunch.app.model.Location;
import com.crimepunch.app.model.UserLocationUpdate;
import com.crimepunch.app.util.LocationUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import de.greenrobot.event.EventBus;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;

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

    @Inject
    NotificationHelper notificationHelper;

    private GoogleMapFragment googleMapFragment;
    private Button sos;
    private Boolean mapReady = false;
    private android.location.Location location;
    private Boolean alarmShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        googleMapFragment = (GoogleMapFragment) getSupportFragmentManager().findFragmentById(R.id.ah_map);
        googleMapFragment.setContext(this);

        sos = (Button) findViewById(R.id.ah_sos);
        sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (location != null) {
                    UserLocationUpdate userLocationUpdate = new UserLocationUpdate();
                    userLocationUpdate.setId(session.getUser(getBaseContext()).getId());
                    userLocationUpdate.setLocation(new Location(BigDecimal.valueOf(location.getLatitude()), BigDecimal.valueOf(location.getLongitude())));
                    server.sendSosRequest(getBaseContext(), userLocationUpdate);
                }
            }
        });
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
        this.location = location;
        UserLocationUpdate userLocationUpdate = new UserLocationUpdate();
        userLocationUpdate.setId(session.getUser(this).getId());
        userLocationUpdate.setLocation(new Location(BigDecimal.valueOf(location.getLatitude()), BigDecimal.valueOf(location.getLongitude())));
        server.sendLocationUpdate(this, userLocationUpdate);
    }

    public void onEventMainThread(LocationDataUpdateEvent event) {
        if (event.getSuccess() && mapReady) {
            googleMapFragment.addMarkers(event.getGridPoints(), event.getInterestingPoints());
            soundAlarmIfNeeded(event.getGridPoints(), event.getCrimeTypes());
        }
    }

    public void onEventMainThread(SosSentEvent event) {
        if(event.getSuccess()) {
            Toast.makeText(this, "SOS signal sent successfully.", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this, "SOS signal failed. Please try again", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapReady = true;
    }

    private void soundAlarmIfNeeded(List<GridPoint> gridPointList, List<CrimeType> crimeTypeList) {
        if(location != null && !alarmShown) {
            for(GridPoint gridPoint : gridPointList) {
                android.location.Location gp = new android.location.Location("");
                gp.setLatitude(gridPoint.getLocation().getLatitude().doubleValue());
                gp.setLongitude(gridPoint.getLocation().getLongitude().doubleValue());

                if (location.distanceTo(gp) < 100 && gridPoint.getScore() > 20) {
                    String message = "Be careful. This area has seen multiple instances of";
                    for(CrimeType crimeType : crimeTypeList) {
                        message = message + ", " + crimeType;
                    }
                    notificationHelper.sendComplaintUpdateNotification(this, message, null, 101);
                    alarmShown = true;
                    break;
                }
            }
        }
    }

}
