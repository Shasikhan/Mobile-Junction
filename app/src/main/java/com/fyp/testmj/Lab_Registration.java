package com.fyp.testmj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Lab_Registration extends AppCompatActivity {
    private TextView LabEngineerText;
    private EditText UserName, Email, MobileNumber, Password, ConfirmPassword;
    private EditText latitude, longitude;
    private Button Register;
    private ProgressBar RegProgressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab__registration);

        LabEngineerText = (TextView) findViewById(R.id.LabEngineer_reg_text);
        UserName = (EditText) findViewById(R.id.customer_name_text);
        Email = (EditText) findViewById(R.id.customer_email_text);
        MobileNumber = (EditText) findViewById(R.id.customer_mobile_text);
        Password = (EditText) findViewById(R.id.customer_pass_text);
        ConfirmPassword = (EditText) findViewById(R.id.customer_conpass_text);
        latitude = (EditText) findViewById(R.id.latitude_text);
        longitude = (EditText) findViewById(R.id.longitude_text);
        Register = (Button) findViewById(R.id.customer_signup_btn);
        RegProgressBar = (ProgressBar) findViewById(R.id.reg_progressBar);
        mAuth = FirebaseAuth.getInstance();

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String labEngineer = LabEngineerText.getText().toString().trim();
                final String username = UserName.getText().toString().trim();
                final String email = Email.getText().toString().trim();
                final String mobileNumber = MobileNumber.getText().toString().trim();
                final String password = Password.getText().toString().trim();
                final String confirmPassword = ConfirmPassword.getText().toString().trim();
                final Double lat = Double.valueOf(latitude.getText().toString());
                final Double longi = Double.valueOf(longitude.getText().toString());
                final String skills = "Skills";

                if(TextUtils.isEmpty(username)) {
                    Toast.makeText(Lab_Registration.this, "Please Enter Username", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(email)) {
                    Toast.makeText(Lab_Registration.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(mobileNumber)) {
                    Toast.makeText(Lab_Registration.this, "Please Enter Mobile Number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)) {
                    Toast.makeText(Lab_Registration.this, "Please Enter Your Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(confirmPassword)) {
                    Toast.makeText(Lab_Registration.this, "Please Confirm Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password.length() < 6) {
                    Toast.makeText(Lab_Registration.this, "Password too short", Toast.LENGTH_SHORT).show();
                }
                if(!password.equals(confirmPassword)) {
                    Toast.makeText(Lab_Registration.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                }

                if(password.equals(confirmPassword)) {
                    RegProgressBar.setVisibility(View.VISIBLE);
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(Lab_Registration.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    RegProgressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                        String CustomerId = mAuth.getCurrentUser().getUid();
                                        DatabaseReference CurrentUserDb = FirebaseDatabase.getInstance().getReference().child("LabEngineer").child(CustomerId);

                                        Map NewUser = new HashMap();
                                        NewUser.put("UserName", username);
                                        NewUser.put("Email", email);
                                        NewUser.put("Mobile Number", mobileNumber);
                                        NewUser.put("User Type", labEngineer);
                                        NewUser.put("Latitude", lat);
                                        NewUser.put("Longitude", longi);
                                        NewUser.put("Skills", skills);
                                        CurrentUserDb.setValue(NewUser);
                                        Toast.makeText(Lab_Registration.this, "Registration Successful", Toast.LENGTH_SHORT).show();

                                    } else {

                                        Toast.makeText(Lab_Registration.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                                    }

                                    // ...
                                }
                            });

                }


            }
        });
    }
}
