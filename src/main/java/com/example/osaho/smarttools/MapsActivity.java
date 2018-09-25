package com.example.osaho.smarttools;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import com.google.android.gms.maps.model.Marker;
import java.util.List;


/**
 * Program to get the location of the device and pin it to google map using device GPS sensor
 */

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    private Marker currentLocationMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        getLocation();

    }

    /**
     * Function to get the location of the device using device GPS sensor
     */
    public void getLocation() {


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        //Granting permnission to use device location
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            return;

        }

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double setLatitude = location.getLatitude();
                double setLongitude = location.getLongitude();

                //get the location name from latitude and longitude
                Geocoder geocoder = new Geocoder(getApplicationContext());
                try {

                    //fetching data about the current location such as the city, zipcode, state and the country
                    List<Address> addresses =
                            geocoder.getFromLocation(setLatitude, setLongitude, 1);
                    String result = addresses.get(0).getLocality() + ", " + addresses.get(0).getPostalCode() + ", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName();
                    LatLng latLng = new LatLng(setLatitude, setLongitude);

                    //remove current marker if it is not null. This prevent having multiple markers on the map when location changes
                    if (currentLocationMarker != null) {

                        currentLocationMarker.remove();
                    }


                    MarkerOptions markerOptions = new MarkerOptions();

                    //setting the markers location
                    markerOptions.position(latLng);

                    //pinning information about the current location to the marker
                    markerOptions.title(result);
                    //MarkerOptions markerOptions = new MarkerOptions();
                    currentLocationMarker = mMap.addMarker(markerOptions);
                    // mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10.0f));

                    /**
                     * Catch IO exceptions
                     */
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };


        //Using Internet and GPS for greater accuracy to get device location
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 0, locationListener);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

    }

}
