package com.crimepunch.app.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import com.crimepunch.app.*;
import com.crimepunch.app.R;
import com.crimepunch.app.application.CrimePunchApplication;
import com.crimepunch.app.model.GoogleMapCluster;
import com.crimepunch.app.model.GridPoint;
import com.crimepunch.app.model.PointEntity;
import com.crimepunch.app.model.PointType;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import de.greenrobot.event.EventBus;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user-1 on 3/10/15.
 */
public class GoogleMapFragment extends SupportMapFragment implements OnMapReadyCallback {

    @Inject
    EventBus eventBus;

    private OnMapReadyCallback callback;
    private GoogleMap.OnCameraChangeListener cameraChangeListener;
    private GoogleMap googleMap;
    private Marker marker;
    private int zoomLevel;
    private MarkerOptions markerOptions;
    private Boolean draggable;
    private Map<String, GridPoint> gridMap = new HashMap<>();
    private Map<String, PointEntity> pointMap = new HashMap<>();
    private HeatmapTileProvider mProvider;
    private TileOverlay mOverlay;

    private Boolean markersDisplayed = false;
    private Boolean heatmapDisplayed = false;
    private Boolean clusterDisplayed = false;

    public GoogleMapFragment() {
        zoomLevel = 14;
        draggable = false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((CrimePunchApplication)getActivity().getApplication()).inject(this);
    }

    public void setContext(OnMapReadyCallback context) {
        this.callback = context;
        super.getMapAsync(this);
    }

    public void addCameraChangeListener(GoogleMap.OnCameraChangeListener context) {
        cameraChangeListener = context;
    }

    /*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mapView = super.onCreateView(inflater, container, savedInstanceState);
        // Get the button view
        View locationButton = ((View) mapView.findViewById(1).getParent()).findViewById(2);
        // and next place it, for example, on bottom right (as Google Maps app)
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        // position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp.setMargins(0, 0, 30, 30);
        return mapView;
    }
    */

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setMyLocationEnabled(true);
        if(callback != null) {
            callback.onMapReady(googleMap);
        }
        if(cameraChangeListener != null) {
            googleMap.setOnCameraChangeListener(cameraChangeListener);
        }
        googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker arg0) {
            }

            @SuppressWarnings("unchecked")
            @Override
            public void onMarkerDragEnd(Marker arg0) {
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(arg0.getPosition()));
            }

            @Override
            public void onMarkerDrag(Marker arg0) {
            }
        });
    }

    public void setZoomLevel(int zoomLevel) {
        this.zoomLevel = zoomLevel;
        if(googleMap != null) {
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(this.zoomLevel);
            googleMap.animateCamera(zoom);
        }
    }

    public Boolean centreMapAt(double lat, double lng) {
        if(googleMap != null) {
            LatLng location = new LatLng(lat, lng);
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(location)
                    .zoom(zoomLevel)
                    .bearing(0)
                    .tilt(45)
                    .build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            return true;
        }
        return false;
    }

    public void showMyLocationButton() {
        if(googleMap != null) {
            UiSettings uiSettings = googleMap.getUiSettings();
            googleMap.setMyLocationEnabled(true);
            uiSettings.setMyLocationButtonEnabled(true);
        }
    }

    public void updateMarkerLocation(double lat, double lng) {
        LatLng location = new LatLng(lat, lng);

        if(marker == null) {
            markerOptions = new MarkerOptions();
            markerOptions.visible(true);
            markerOptions.position(location);
            markerOptions.draggable(draggable);
            marker = googleMap.addMarker(markerOptions);
        }

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(location)
                .zoom(zoomLevel)
                .bearing(0)
                .tilt(45)
                .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        marker.setPosition(location);
    }

    public void makeMarkerDraggable() {
        draggable = true;
        if(marker != null) {
            marker.setDraggable(true);
        }
    }

    public double getMarkerLatitude() {
        return marker.getPosition().latitude;
    }

    public double getMarkerLongitude() {
        return marker.getPosition().longitude;
    }

    public double getMarkedLatitude() {
        return googleMap.getCameraPosition().target.latitude;
    }

    public double getMarkedLongitude() {
        return googleMap.getCameraPosition().target.longitude;
    }

    public void disableGestures() {
        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setMyLocationButtonEnabled(false);
        uiSettings.setTiltGesturesEnabled(false);
        uiSettings.setZoomGesturesEnabled(false);
        uiSettings.setZoomControlsEnabled(false);
        uiSettings.setRotateGesturesEnabled(false);
        uiSettings.setMyLocationButtonEnabled(false);
        uiSettings.setScrollGesturesEnabled(false);
        uiSettings.setCompassEnabled(false);
    }

    public void addMarkers(List<GridPoint> gridPointList, List<PointEntity> pointEntityList) {
        clearMap();
        Marker m;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for(GridPoint gridPoint : gridPointList) {
            Bitmap icon = gridPoint.getScore() > 10 ? gridPoint.getScore() > 20 ? BitmapFactory.decodeResource(getResources(), R.drawable.red_dot) : BitmapFactory.decodeResource(getResources(), R.drawable.yellow_dot) : BitmapFactory.decodeResource(getResources(), R.drawable.green_dot);
            float hue = gridPoint.getScore() > 10 ? gridPoint.getScore() > 20 ? BitmapDescriptorFactory.HUE_RED : BitmapDescriptorFactory.HUE_YELLOW : BitmapDescriptorFactory.HUE_GREEN;

            markerOptions = new MarkerOptions();
            markerOptions.visible(true);
            markerOptions.position(new LatLng(gridPoint.getLocation().getLatitude().doubleValue(), gridPoint.getLocation().getLongitude().doubleValue()));
            markerOptions.draggable(false);
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
            //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(hue));

            m = googleMap.addMarker(markerOptions);
            gridMap.put(m.getId(), gridPoint);
            builder.include(m.getPosition());
        }

        for (PointEntity pointEntity : pointEntityList) {
            Bitmap icon = pointEntity.getPointType().equals(PointType.PERSON) ? BitmapFactory.decodeResource(getResources(), R.drawable.man) : pointEntity.getPointType().equals(PointType.POLICE_STATION) ? BitmapFactory.decodeResource(getResources(), R.drawable.police) : BitmapFactory.decodeResource(getResources(), R.drawable.hospital);
            markerOptions = new MarkerOptions();
            markerOptions.visible(true);
            markerOptions.position(new LatLng(pointEntity.getLocation().getLatitude().doubleValue(), pointEntity.getLocation().getLongitude().doubleValue()));
            markerOptions.draggable(false);
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));

            m = googleMap.addMarker(markerOptions);
            pointMap.put(m.getId(), pointEntity);
            builder.include(m.getPosition());
        }

        if(gridPointList.size() > 1) {
            LatLngBounds bounds = builder.build();
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 0);
            googleMap.animateCamera(cu);
        }
        else if(gridPointList.size() > 0) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(gridPointList.get(0).getLocation().getLatitude().doubleValue(), gridPointList.get(0).getLocation().getLongitude().doubleValue()))
                    .zoom(zoomLevel)
                    .bearing(0)
                    .tilt(45)
                    .build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }

        markersDisplayed = true;
        heatmapDisplayed = false;
        clusterDisplayed = false;
    }

    public void removeMarkers() {
        googleMap.clear();
        gridMap.clear();
    }

    public void addHeatMap(List<GridPoint> gridPointList) {
        clearMap();
        List<LatLng> list = new ArrayList<LatLng>();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        LatLng latLng = null;

        for(GridPoint gridPoint : gridPointList) {
            latLng = new LatLng(gridPoint.getLocation().getLatitude().doubleValue(), gridPoint.getLocation().getLongitude().doubleValue());
            list.add(latLng);
            builder.include(latLng);
        }

        if(list.size() < 1) {
            return;
        }

        mProvider = new HeatmapTileProvider.Builder()
                .data(list)
                .build();
        mOverlay = googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));

        if(gridPointList.size() > 1) {
            LatLngBounds bounds = builder.build();
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 0);
            googleMap.animateCamera(cu);
        }
        else if(gridPointList.size() > 0) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng)
                    .zoom(zoomLevel)
                    .bearing(0)
                    .tilt(45)
                    .build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
        markersDisplayed = false;
        heatmapDisplayed = true;
        clusterDisplayed = false;
    }

    public void removeHeatMap() {
        mOverlay.remove();
    }

    public void addCluster(List<GridPoint> complaintDtos) {
        clearMap();
        GoogleMapCluster googleMapCluster;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        ClusterManager<GoogleMapCluster> mClusterManager;
        mClusterManager = new ClusterManager<GoogleMapCluster>(getActivity(), googleMap);
        googleMap.setOnCameraChangeListener(mClusterManager);
        googleMap.setOnMarkerClickListener(mClusterManager);
        for(GridPoint complaintDto : complaintDtos) {
            googleMapCluster = new GoogleMapCluster(complaintDto.getLocation().getLatitude().doubleValue(), complaintDto.getLocation().getLongitude().doubleValue());
            mClusterManager.addItem(googleMapCluster);
            builder.include(googleMapCluster.getPosition());
        }

        if(complaintDtos.size() > 1) {
            LatLngBounds bounds = builder.build();
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 0);
            googleMap.animateCamera(cu);
        }
        else if(complaintDtos.size() > 0) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(complaintDtos.get(0).getLocation().getLatitude().doubleValue(), complaintDtos.get(0).getLocation().getLongitude().doubleValue()))
                    .zoom(zoomLevel)
                    .bearing(0)
                    .tilt(45)
                    .build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
        markersDisplayed = false;
        heatmapDisplayed = false;
        clusterDisplayed = true;
    }

    public void removeCluster() {
        googleMap.clear();
        googleMap.setOnCameraChangeListener(null);
        googleMap.setOnMarkerClickListener(null);
    }

    public void clearMap() {
        if(markersDisplayed) {
            removeMarkers();
        }
        else if(clusterDisplayed) {
            removeCluster();
        }
        else if(heatmapDisplayed) {
            removeHeatMap();
        }
    }

}
