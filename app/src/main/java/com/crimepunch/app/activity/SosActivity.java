package com.crimepunch.app.activity;

import android.os.Bundle;
import android.widget.Button;
import com.crimepunch.app.R;
import com.crimepunch.app.base.BaseActivity;
import com.crimepunch.app.fragment.GoogleMapFragment;
import com.crimepunch.app.model.Location;
import com.crimepunch.app.model.UserLocationUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.math.BigDecimal;

/**
 * Created by user-1 on 4/10/15.
 */
public class SosActivity extends BaseActivity implements OnMapReadyCallback {

    private GoogleMapFragment googleMapFragment;
    private Double latitude;
    private Double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        latitude = getIntent().getDoubleExtra("latitude", 0);
        longitude = getIntent().getDoubleExtra("longitude", 0);

        googleMapFragment = (GoogleMapFragment) getSupportFragmentManager().findFragmentById(R.id.ah_map);
        googleMapFragment.setContext(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMapFragment.updateMarkerLocation(latitude, longitude);
    }
}
