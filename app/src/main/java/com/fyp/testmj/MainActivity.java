package com.fyp.testmj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private Button bCustomer, bLabEngineer;
    private FirebaseAuth cAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bCustomer = (Button) findViewById(R.id.customer);
        bLabEngineer = (Button) findViewById(R.id.labengineer);
        //cAuth = FirebaseAuth.getInstance();

       /* firebaseAuthListner = new FirebaseAuth.AuthStateListener() {
           @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if(user != null) {
                    if(user.equals("Customers")) {
                        Intent intent = new Intent(MainActivity.this, CustomerSignin.class);
                        startActivity(intent);
                        finish();
                        return;
                    } else if(user.equals("LabEngineers")) {
                        Intent intent = new Intent(MainActivity.this, LabEngineerSignin.class);
                        startActivity(intent);
                        finish();
                        return;
                    }
                }
            }
        }; */




        bCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CustomerSignin.class);
                startActivity(i);
                finish();
                return;
            }
        });


        bLabEngineer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, LabEngineerSignin.class);
                startActivity(i);
                finish();
                return;
            }
        });

    }
    /*@Override
    protected void onStart() {
        super.onStart();
        cAuth.addAuthStateListener(firebaseAuthListner);
    }
    @Override
    protected void onStop() {
        super.onStop();
        cAuth.removeAuthStateListener(firebaseAuthListner);
    }*/
}
