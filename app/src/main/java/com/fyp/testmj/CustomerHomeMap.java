package com.fyp.testmj;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.app.Notification;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerHomeMap extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener{

    private Button clogout;
    private Button cSettings;
    private TextView CustomerName;
    private long backPressedTime;
    private Toast backToast;
    private GoogleMap mMap;
    private GoogleApiClient cGoogleApiClient;
    private LocationRequest cLocationRequest;
    private SupportMapFragment mapFragment;
    private FirebaseAuth cAuth;
    private DatabaseReference userDatabase, RequestDB, labs;
    private String LabId, userID, customerUserName, customerEmail, customerNumber, customerProblem;
    private Double latitude, longitude;
    LatLng coordinate;
    Marker markers;
    View mView;
    TextView ShopName, Skills, Email, MobileNumber, LabIDField;
    EditText Problem;
    Button RequestBtn;
    AlertDialog RequestDialogue;
    private List<Marker> addmarkers = new ArrayList<>();        private List<String> LabIDS = new ArrayList<>();
    private List<String> LabUserNames = new ArrayList<>();      private List<String> LabEmails = new ArrayList<>();
    private List<String> LabMobileNumbers = new ArrayList<>();  private List<String> LabSkills = new ArrayList<>();
    private List<Double> LabLatis = new ArrayList<>();          private List<Double> Lablngs = new ArrayList<>();

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
            cAuth =FirebaseAuth.getInstance();
            userID = cAuth.getCurrentUser().getUid();
            userDatabase = FirebaseDatabase.getInstance().getReference().child("Customers").child(userID);
            getUserInfo();
            CustomerName = (TextView) findViewById(R.id.customer_profile_name);
            RequestDB = FirebaseDatabase.getInstance().getReference().child("Requests");
            userDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0)
                    {
                        Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                        if (map.get("UserName") != null)
                        {
                            String customerUserName = map.get("UserName").toString();
                            CustomerName.setText(customerUserName);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            /*Customer Settings Button*/
            cSettings = (Button) findViewById(R.id.Customer_settingsBtn);
            cSettings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CustomerHomeMap.this, CustomerProfile.class);
                    startActivity(intent);
                }
            });

            /*Signing out the current User*/
            clogout = (Button) findViewById(R.id.logout_text);
            clogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(CustomerHomeMap.this, CustomerSignin.class);
                    startActivity(intent);
                    finish();
                }
            });
            labs = FirebaseDatabase.getInstance().getReference().child("LabEngineer");
        }
    }

    /*BackPress Button Action*/
    @Override
    public void onBackPressed() {

        if(backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            return;
        } else {
            backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
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
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) !=  PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CustomerHomeMap.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_REQUEST_CODE);
        }
        buildGoogleApiClient();
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMyLocationEnabled(true);
        mMap.setOnMarkerClickListener(this);

        /*Zooming to current Location*/
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            coordinate = new LatLng(latitude, longitude);
            CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 15);
            mMap.animateCamera(yourLocation);
        }
        //circle of customer range of finding labengineers
        Circle circle = mMap.addCircle(new CircleOptions()
                .center(coordinate)
                .radius(5000)
                .strokeColor(Color.rgb(45, 43, 85))
                .fillColor(Color.argb(20, 88, 84, 130)));
        /*Custom InfoWindow*/
        //InfoWndowAdapter markerInfoWindowAdapter = new InfoWndowAdapter(getApplicationContext());
        //googleMap.setInfoWindowAdapter(markerInfoWindowAdapter);

        /*Request Dialogue Box*/
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(CustomerHomeMap.this);
        mView = getLayoutInflater().inflate(R.layout.request_dialog, null);
        LabIDField = (TextView) mView.findViewById(R.id.LabID_text);
        ShopName = (TextView) mView.findViewById(R.id.shopName_text);
        Skills = (TextView) mView.findViewById(R.id.skills_text);
        Email = (TextView) mView.findViewById(R.id.email_text);
        MobileNumber = (TextView) mView.findViewById(R.id.mobileNumber_text);
        Problem = (EditText) mView.findViewById(R.id.problem_text);
        RequestBtn = (Button) mView.findViewById(R.id.requestBtn);
        mBuilder.setView(mView);
        RequestDialogue = mBuilder.create();

        /* Adding Markers by retrieving LabEngineers data */
        labs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot s: dataSnapshot.getChildren()) {

                    LabId =  s.getKey();
                    String username = (String) s.child("UserName").getValue();
                    String email = String.valueOf(s.child("Email").getValue());
                    String MobileNumber = String.valueOf(s.child("Mobile Number").getValue());
                    String skills = String.valueOf(s.child("Skills").getValue());
                    Double lati = (Double) s.child("Latitude").getValue();
                    Double longi = (Double) s.child("Longitude").getValue();
                    LatLng location = new LatLng(lati, longi);
                    markers = mMap.addMarker(new MarkerOptions().position(location).title(username).visible(false));
                    LabIDS.add(LabId);
                    LabUserNames.add(username);
                    LabEmails.add(email);
                    LabMobileNumbers.add(MobileNumber);
                    LabSkills.add(skills);
                    LabLatis.add(lati);
                    Lablngs.add(longi);
                    addmarkers.add(markers);
                    for (Marker marker : addmarkers) {
                        if (com.google.maps.android.SphericalUtil.computeDistanceBetween(coordinate, marker.getPosition()) < 5000) {
                            marker.setVisible(true);
                        }
                    }
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
        for (int i = 0; i < LabUserNames.size(); i++) {
            if (LabUserNames.get(i).equals(marker.getTitle())) {
                LabIDField.setText(LabIDS.get(i));
                ShopName.setText(LabUserNames.get(i));
                Skills.setText(LabSkills.get(i));
                Email.setText(LabEmails.get(i));
                MobileNumber.setText(LabMobileNumbers.get(i));
            }
        }
        RequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customerProblem = Problem.getText().toString();
                Map NewRequest = new HashMap();
                NewRequest.put("LabEngineerID", LabIDField.getText());
                NewRequest.put("CustomerID", userID);
                NewRequest.put("UserName", customerUserName);
                NewRequest.put("Email", customerEmail);
                NewRequest.put("Mobile Number", customerNumber);
                NewRequest.put("Problem", customerProblem);
                NewRequest.put("Latitude", latitude);
                NewRequest.put("Longitude", longitude);
                RequestDB.push().setValue(NewRequest);
                Toast.makeText(CustomerHomeMap.this, "Request Sent", Toast.LENGTH_SHORT).show();
                Problem.getText().clear();
                RequestDialogue.hide();
            }
        });

        RequestDialogue.show();
        return false;

    }

    /*Getting Customer Information*/
    private void getUserInfo() {
        userDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0)
                {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("UserName") != null)
                    {
                        customerUserName = map.get("UserName").toString();
                    }
                    if (map.get("Email") != null)
                    {
                        customerEmail = map.get("Email").toString();
                    }
                    if (map.get("Mobile Number") != null)
                    {
                        customerNumber = map.get("Mobile Number").toString();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        cLocationRequest = new LocationRequest();
        cLocationRequest.setInterval(1000);
        cLocationRequest.setFastestInterval(5000);
        cLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(CustomerHomeMap.this, new String[]
                            {android.Manifest.permission.ACCESS_FINE_LOCATION},
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


