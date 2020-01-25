package com.fyp.testmj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class Customer_Registration extends AppCompatActivity {

    private TextView CustomerText;
    private EditText UserName, Email, MobileNumber, Password, ConfirmPassword;
    private Button Register;
    private ProgressBar RegProgressBar;
    private TextView HaveAccount;
    private FirebaseAuth cAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer__registration);

        CustomerText = (TextView) findViewById(R.id.customer_reg_text);
        UserName = (EditText) findViewById(R.id.customer_name_text);
        Email = (EditText) findViewById(R.id.customer_email_text);
        MobileNumber = (EditText) findViewById(R.id.customer_mobile_text);
        Password = (EditText) findViewById(R.id.customer_pass_text);
        ConfirmPassword = (EditText) findViewById(R.id.customer_conpass_text);
        Register = (Button) findViewById(R.id.customer_signup_btn);
        HaveAccount = (TextView) findViewById(R.id.customer_have_acc);
        RegProgressBar = (ProgressBar) findViewById(R.id.reg_progressBar);

        cAuth = FirebaseAuth.getInstance();

        firebaseAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser CurrentUser = FirebaseAuth.getInstance().getCurrentUser();
                if(CurrentUser != null) {
                    Intent i = new Intent(Customer_Registration.this, CustomerHomeMap.class);
                    startActivity(i);
                    finish();
                    return;
                }
            }
        };

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String type = CustomerText.getText().toString().trim();
                final String username = UserName.getText().toString().trim();
                final String email = Email.getText().toString().trim();
                final String mobileNumber = MobileNumber.getText().toString().trim();
                final String password = Password.getText().toString().trim();
                final String confirmPassword = ConfirmPassword.getText().toString().trim();

                if(TextUtils.isEmpty(username)) {
                    Toast.makeText(Customer_Registration.this, "Please Enter Username", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(email)) {
                    Toast.makeText(Customer_Registration.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(mobileNumber)) {
                    Toast.makeText(Customer_Registration.this, "Please Enter Mobile Number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)) {
                    Toast.makeText(Customer_Registration.this, "Please Enter Your Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(confirmPassword)) {
                    Toast.makeText(Customer_Registration.this, "Please Confirm Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password.length() < 6) {
                    Toast.makeText(Customer_Registration.this, "Password too short", Toast.LENGTH_SHORT).show();
                }
                if(!password.equals(confirmPassword)) {
                    Toast.makeText(Customer_Registration.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                }
                if(password.equals(confirmPassword)) {
                    RegProgressBar.setVisibility(View.VISIBLE);
                    cAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(Customer_Registration.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    RegProgressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        startActivity(new Intent(getApplicationContext(), CustomerHomeMap.class));
                                        Toast.makeText(Customer_Registration.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                        String CustomerId = cAuth.getCurrentUser().getUid();
                                        DatabaseReference CurrentUserDb = FirebaseDatabase.getInstance().getReference().child("Customers").child(CustomerId);
                                        Map NewUser = new HashMap();
                                        NewUser.put("UserName", username);
                                        NewUser.put("Email", email);
                                        NewUser.put("Mobile Number", mobileNumber);
                                        NewUser.put("User Type", type);
                                        CurrentUserDb.setValue(NewUser);
                                    } else {

                                        Toast.makeText(Customer_Registration.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        HaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Customer_Registration.this, CustomerSignin.class);
                startActivity(i);
                finish();
                return;
            }
        });
    }
}
