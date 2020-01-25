package com.fyp.testmj;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentActivity;

import android.app.Notification;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

import static com.fyp.testmj.LabEngineerHome.CHANNEL_1_ID;

public class CustomerHomeMap extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener, GoogleMap.OnInfoWindowClickListener{

   // ArrayList<LatLng> arrayList = new ArrayList<LatLng>();
   HashMap<String, String> markerMap = new HashMap<String, String>();
    private NotificationManagerCompat notificationManager;
    LatLng iPhoneRepairShop = new LatLng(31.464129, 74.311293);
    LatLng SamsungRepairShop = new LatLng(31.463198, 74.310814);
    LatLng Shah_G_Mobile_RepairingShop = new LatLng(31.460960, 74.315913);
    private GoogleMap mMap;
    GoogleApiClient cGoogleApiClient;
    Location cLastLocation;
    LocationRequest cLocationRequest;

    MarkerOptions iPhone = new MarkerOptions();
    MarkerOptions SamSung = new MarkerOptions();
    MarkerOptions ShahGMobile = new MarkerOptions();


    private SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(CustomerHomeMap.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_REQUEST_CODE);
        }else {
            mapFragment.getMapAsync(this);
            notificationManager = NotificationManagerCompat.from(this);

           /* arrayList.add(iPhoneRepairShop);
            arrayList.add(SamsungRepairShop);*/
             }
    }



    /*-------------------------------------------- Map specific functions -----
       |  Function(s) onMapReady, buildGoogleApiClient, onLocationChanged, onConnected
       |
       |  Purpose:  Find and update user's location.
       |
       |  Note:
       |	   The update interval is set to 1000Ms and the accuracy is set to PRIORITY_HIGH_ACCURACY,
       |      If you're having trouble with battery draining too fast then change these to lower values
       |
       |
       *-------------------------------------------------------------------*/
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Location location;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                !=  PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CustomerHomeMap.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_REQUEST_CODE);
        }

        buildGoogleApiClient();
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMyLocationEnabled(true);

//        MarkerOptions iPhone = new MarkerOptions();
//        MarkerOptions SamSung = new MarkerOptions();
//        MarkerOptions ShahGMobile = new MarkerOptions();

        iPhone.position(iPhoneRepairShop).title("iPhone Repair Shop");
        ShahGMobile.position(Shah_G_Mobile_RepairingShop).title("Shah G Mobile Repairing Shop");
        SamSung.position(SamsungRepairShop).title("SamSung Repair Shop");



        Info_Window_Data iPhone_info = new Info_Window_Data();
        Info_Window_Data SamSung_info = new Info_Window_Data();
        Info_Window_Data ShahG_info = new Info_Window_Data();

        //iPhone_info.setLabRnineerImage("Hello");
        iPhone_info.setLabEngineerName("Ali");
        iPhone_info.setLabEngineerSkills("iPhone Repair Services");
        iPhone_info.setLabEngineerEmail("alirepairserives@gmail.com");
        iPhone_info.setLabEngineerPhone("Phone No: 03049875164");
        iPhone_info.setRatingBar(3.5f);



        //SamSung_info.setLabRnineerImage("Ahmad");
        SamSung_info.setLabEngineerName("Ahmad");
        SamSung_info.setLabEngineerSkills("SamSung Phone Repair Services");
        SamSung_info.setLabEngineerEmail("samsungrepair@yahoo.com");
        SamSung_info.setLabEngineerPhone("03216578941");
        SamSung_info.setRatingBar(4.5f);


       // ShahG_info.setLabRnineerImage("ShahG");
        ShahG_info.setLabEngineerName("Shah G");
        ShahG_info.setLabEngineerSkills("All Phone Company Repair Services");
        ShahG_info.setLabEngineerEmail("shahg@outlook.com");
        ShahG_info.setLabEngineerPhone("03056573947");
        ShahG_info.setRatingBar(2f);


        Map_Custom_Info_Window map_custom_info_window = new Map_Custom_Info_Window(this);
        mMap.setInfoWindowAdapter(map_custom_info_window);
        mMap.setOnInfoWindowClickListener(map_custom_info_window);

        Marker IPHONE = mMap.addMarker(iPhone);
        IPHONE.setTag(iPhone_info);
        IPHONE.showInfoWindow();
//        String idOne = IPHONE.getId();
//        markerMap.put(idOne, "A1");



        Marker SHAHG = mMap.addMarker(ShahGMobile);
        SHAHG.setTag(ShahG_info);
        SHAHG.showInfoWindow();

        Marker SAMSUNG = mMap.addMarker(SamSung);
        SAMSUNG.setTag(SamSung_info);
        SAMSUNG.showInfoWindow();

        //mMap.addMarker(new MarkerOptions().position(iPhoneRepairShop).title("iPhone Repair Shop"));
        //mMap.addMarker(new MarkerOptions().position(SamsungRepairShop).title("Samsung Repair Shop"));
        /*for (int i = 0; i < arrayList.size(); i++) {
            mMap.addMarker(new MarkerOptions().position(arrayList.get(i)).title("Marker"));
        } */

        /*
        * Adding Custom info Windows to the markers
        * */

    }



    @Override
    public boolean onMarkerClick(final Marker marker)
    {

        // Retrieve the data from the marker.
        Integer clickCount = (Integer) marker.getTag();

        // Check if a click count was set, then display the click count.
        if (clickCount != null) {
            clickCount = clickCount + 1;
            marker.setTag(clickCount);
            Toast.makeText(this,
                    marker.getTitle() +
                            " has been clicked " + clickCount + " times.",
                    Toast.LENGTH_SHORT).show();
        }

        return false;

    }

    protected synchronized void buildGoogleApiClient(){
        cGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        cGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        if(getApplicationContext()!=null){
            cLastLocation = location;

            LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());

           //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
           //mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        cLocationRequest = new LocationRequest();
        cLocationRequest.setInterval(1000);
        cLocationRequest.setFastestInterval(5000);
        cLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(CustomerHomeMap.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_REQUEST_CODE);
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(cGoogleApiClient, cLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
    /*-------------------------------------------- onRequestPermissionsResult -----
   |  Function onRequestPermissionsResult
   |
   |  Purpose:  Get permissions for our app if they didn't previously exist.
   |
   |  Note:
   |	requestCode: the number assigned to the request that we've made. Each
   |                request has it's own unique request code.
   |
   *-------------------------------------------------------------------*/

    final int LOCATION_REQUEST_CODE = 1;
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    mapFragment.getMapAsync(this);


                } else {
                    Toast.makeText(getApplicationContext(), "Please provide the permission", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
//        String actionID = markerMap.get(marker.getId());
//        if(actionID.equals("A1")) {
//            Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
//                    .setSmallIcon(R.drawable.ic_one)
//                    .setContentTitle("Repair Request")
//                    .setPriority(NotificationCompat.PRIORITY_HIGH)
//                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
//                    .build();
//            notificationManager.notify(1, notification);
//        }
    }
}
