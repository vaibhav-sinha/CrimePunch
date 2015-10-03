package com.crimepunch.app.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by user-1 on 3/10/15.
 */
public class GoogleMapCluster implements ClusterItem {

    private final LatLng mPosition;

    public GoogleMapCluster(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }
}