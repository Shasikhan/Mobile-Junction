package com.fyp.testmj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LabEngineerProfile extends AppCompatActivity {

    private TextView LabEngineerName, LabEngineerSkills, LabEngineerLocation, LabEngineerEmail, LabEngineerPhone, LabEngineerRating;
    private ImageView ProfileImageView;
    private RatingBar ratingBar;
    FirebaseAuth mAuth;
    FirebaseUser labUser;
    FirebaseDatabase Database;
    DatabaseReference UserReference;
    private String email;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_engineer_profile);

        LabEngineerName = (TextView) findViewById(R.id.LabEngineer_name_textview);
        LabEngineerSkills = (TextView) findViewById(R.id.skill_textview);
        LabEngineerLocation = (TextView) findViewById(R.id.location_textview);
        LabEngineerEmail = (TextView) findViewById(R.id.email_textview);
        LabEngineerPhone = (TextView) findViewById(R.id.phone_no_textview);
        ratingBar = (RatingBar) findViewById(R.id.star_rating);

        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        mAuth = FirebaseAuth.getInstance();
        labUser = mAuth.getCurrentUser();
        UserReference = FirebaseDatabase.getInstance().getReference().child(labUser.getUid());

        UserReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                LabEngineerEmail.setText(dataSnapshot.child("LabEngineers").child("Email").getValue(String.class));
                LabEngineerName.setText(dataSnapshot.child("LabEngineers").child("UserName").getValue(String.class));
                LabEngineerSkills.setText(dataSnapshot.child("LabEngineers").child("Skill").getValue(String.class));
                LabEngineerPhone.setText(dataSnapshot.child("LabEngineers").child("Mobile Number").getValue(String.class));
                LabEngineerLocation.setText(dataSnapshot.child("LabEngineers").child("Location").getValue(String.class));
                //LabEngineerRating.setText(dataSnapshot.child("LabEngineers").child("Rating").getValue(String.class));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
