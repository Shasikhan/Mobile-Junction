package com.fyp.testmj;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.app.Notification;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import static com.fyp.testmj.LabEngineerHome.CHANNEL_1_ID;

public class CustomerHomeMap extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener{

   HashMap<String, String> markerMap = new HashMap<String, String>();
    private GoogleMap mMap;
    GoogleApiClient cGoogleApiClient;
    Location cLastLocation;
    LocationRequest cLocationRequest;
    DatabaseReference labs;
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
            labs = FirebaseDatabase.getInstance().getReference().child("LabEngineer");
        }
    }
       /*--------------------------------- Map specific functions -----------------------------*
       |  Function(s) onMapReady, buildGoogleApiClient, onLocationChanged, onConnected         |
       |                                                                                       |
       |  Purpose:  User location and Registered LabEngineers                                  |
       |                                                                                       |
       |  Note:                                                                                |
       |	   Markers have been added dynamically to show labEngineer's shops                 |
       |       As soon as a new labengineer is registered, marker will be added on his location|
       *---------------------------------------------------------------------------------------*/
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
        labs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot s: dataSnapshot.getChildren())
                {
                    String username = String.valueOf(s.child("UserName").getValue());
                    String email = String.valueOf(s.child("Email").getValue());
                    String MobileNumber = String.valueOf(s.child("Mobile Number").getValue());
                    String skills = String.valueOf(s.child("Skills").getValue());
                    Double lati = (Double) s.child("Latitude").getValue();
                    Double longi = (Double) s.child("Longitude").getValue();
                    LatLng location = new LatLng(lati, longi);

                    mMap.addMarker(new MarkerOptions().position(location).title(username).snippet(skills + "\n\n" + email +
                            "\n\n" + MobileNumber));
                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            AlertDialog.Builder mBuilder = new AlertDialog.Builder(CustomerHomeMap.this);
                            View mView = getLayoutInflater().inflate(R.layout.request_dialog, null);
                            TextView ShopName = (TextView) mView.findViewById(R.id.shopName_text);
                            TextView Skills = (TextView) mView.findViewById(R.id.skills_text);
                            EditText Problem = (EditText) mView.findViewById(R.id.problem_text);
                            Button Request = (Button) mView.findViewById(R.id.requestBtn);

                            ShopName.setText(marker.getTitle());
                            Skills.setText(marker.getSnippet());
                            mBuilder.setView(mView);
                            AlertDialog dialog = mBuilder.create();
                            dialog.show();
                            return false;
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    @Override
    public boolean onMarkerClick(final Marker marker)
    {
        Toast.makeText(this, "Marker Clicked", Toast.LENGTH_SHORT).show();
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
    /*--------------------------- onRequestPermissionsResult -------------------
   |  Function onRequestPermissionsResult
   |
   |  Purpose:  Get permissions for our app if they didn't previously exist.
   |
   |  Note:
   |	requestCode: the number assigned to the request that we've made. Each
   |                request has it's own unique request code.
   |
   *-------------------------------------------------------------------------------*/

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

}
