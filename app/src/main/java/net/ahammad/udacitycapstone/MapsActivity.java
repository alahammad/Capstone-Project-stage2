package net.ahammad.udacitycapstone;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import net.ahammad.udacitycapstone.fragments.ReminderDetailsFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapsActivity extends FragmentActivity {

    @Bind(R.id.save_map)
    Button mSave;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private LatLng mCurrentPos;
    private LatLng mSelectedPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @OnClick (R.id.save_map)
    public void OnClick(View view){
        if (mSelectedPos!=null){
            MainApp.lat = mSelectedPos.latitude;
            MainApp.lon = mSelectedPos.longitude;
            finish();
        }
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                if (mCurrentPos==null) {
                    mCurrentPos = mSelectedPos= new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(mCurrentPos).title(getString(R.string.my_location))).setDraggable(true);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mCurrentPos,16));
                }
            }
        });
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker arg0) {}

            @SuppressWarnings("unchecked")
            @Override
            public void onMarkerDragEnd(Marker arg0) {
                mSelectedPos = arg0.getPosition();
                mMap.animateCamera(CameraUpdateFactory.newLatLng(arg0.getPosition()));
            }

            @Override
            public void onMarkerDrag(Marker arg0) {}
        });
        addMarker ();
    }

    private void addMarker (){
       double lat= getIntent().getDoubleExtra(ReminderDetailsFragment.LAT,-1);
       double lon = getIntent().getDoubleExtra(ReminderDetailsFragment.LON,-1);
       if (lat!=-1 && lon!=-1){
           mMap.addMarker(new MarkerOptions()
                   .position(new LatLng(lat, lon))
                   .title(getString(R.string.pharmacy_location)));
       }
    }
}
