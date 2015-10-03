package com.crimepunch.app.service;

import android.app.Service;
import android.content.Intent;
import com.crimepunch.app.base.BaseService;
import com.crimepunch.app.datastore.Server;
import com.crimepunch.app.event.LocationDataUpdateEvent;
import com.crimepunch.app.helper.NotificationHelper;
import com.crimepunch.app.helper.Session;
import com.crimepunch.app.model.CrimeType;
import com.crimepunch.app.model.GridPoint;
import com.crimepunch.app.model.Location;
import com.crimepunch.app.model.UserLocationUpdate;
import com.crimepunch.app.util.LocationUtil;
import com.google.android.gms.maps.GoogleMap;
import de.greenrobot.event.EventBus;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by user-1 on 3/10/15.
 */
public class WatchdogService extends BaseService {

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

    private android.location.Location location;
    private Boolean alarmShown = false;

    @Override
    public void onCreate() {
        super.onCreate();
        eventBus.register(this);
        locationUtil.subscribe(this, true);
    }

    @Override
    public void onDestroy() {
        locationUtil.unsubscribe();
        eventBus.unregister(this);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    public void onEventMainThread(android.location.Location location) {
        if(session.getUser(this) == null) {
            stopSelf();
        }
        this.location = location;
        UserLocationUpdate userLocationUpdate = new UserLocationUpdate();
        userLocationUpdate.setId(session.getUser(this).getId());
        userLocationUpdate.setLocation(new Location(BigDecimal.valueOf(location.getLatitude()), BigDecimal.valueOf(location.getLongitude())));
        server.sendLocationUpdate(this, userLocationUpdate);
    }

    public void onEventMainThread(LocationDataUpdateEvent event) {
        if (event.getSuccess()) {
            soundAlarmIfNeeded(event.getGridPoints(), event.getCrimeTypes());
        }
    }

    private void soundAlarmIfNeeded(List<GridPoint> gridPointList, List<CrimeType> crimeTypeList) {
        if(location != null && !alarmShown) {
            for(GridPoint gridPoint : gridPointList) {
                android.location.Location gp = new android.location.Location("");
                gp.setLatitude(gridPoint.getLocation().getLatitude().doubleValue());
                gp.setLongitude(gridPoint.getLocation().getLongitude().doubleValue());

                if (location.distanceTo(gp) < 100) {
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
