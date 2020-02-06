package com.fyp.testmj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.model.Marker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class CustomerProfile extends AppCompatActivity {

    private FirebaseAuth cAuth;
    private DatabaseReference cCustomerDatabase;
    private EditText cName, cEmail, cPhoneNumber;
    private Button cSave, cBack;

    private String customerID, customerUserName, customerEmail, customerNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);

        cName = (EditText) findViewById(R.id.customerinfo_name);
        cEmail = (EditText) findViewById(R.id.customerinfo_email);
        cPhoneNumber = (EditText) findViewById(R.id.customerinfo_number);
        cSave = (Button) findViewById(R.id.customerinfo_save);
        cBack = (Button) findViewById(R.id.customerinfo_back);


        cAuth = FirebaseAuth.getInstance();
        customerID = cAuth.getCurrentUser().getUid();
        cCustomerDatabase = FirebaseDatabase.getInstance().getReference().child("Customers").child(customerID);
        
        getUserInfo();

        cSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInformation();
                Toast.makeText(CustomerProfile.this, "User Information Updated", Toast.LENGTH_SHORT).show();
            }
        });
        cBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerProfile.this, CustomerHomeMap.class);
                startActivity(intent);
                finish();
                return;
            }
        });


    }

//    @Override
//    public void onBackPressed() {
//        Intent intent = new Intent(CustomerProfile.this, CustomerHomeMap.class);
//        startActivity(intent);
//        finish();
//    }

    private void saveUserInformation() {
        customerUserName = cName.getText().toString();
        customerEmail = cEmail.getText().toString();
        customerNumber = cPhoneNumber.getText().toString();
        Map customerInfo = new HashMap();
        customerInfo.put("UserName", customerUserName);
        customerInfo.put("Mobile Number", customerNumber);
        customerInfo.put("Email", customerEmail);
        cCustomerDatabase.updateChildren(customerInfo);

    }

    /*Getting Customer Information*/
    private void getUserInfo() {
        cCustomerDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0)
                {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("UserName") != null)
                        {
                            customerUserName = map.get("UserName").toString();
                            cName.setText(customerUserName);
                        }
                    if (map.get("Email") != null)
                        {
                            customerEmail = map.get("Email").toString();
                            cEmail.setText(customerEmail);
                        }
                    if (map.get("Mobile Number") != null)
                        {
                            customerNumber = map.get("Mobile Number").toString();
                            cPhoneNumber.setText(customerNumber);
                        }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
