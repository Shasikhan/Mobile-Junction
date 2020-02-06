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
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CustomerSignin extends AppCompatActivity {
    private EditText CustomerEmail, CustomerPass;
    private TextView CustomerNoAccount;
    private ProgressBar SigninProgressBar;
    private Button CustomerSigninBtn;
    private FirebaseAuth CAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_signin);


        CustomerEmail = (EditText) findViewById(R.id.customer_signin_email_text);
        CustomerPass = (EditText) findViewById(R.id.customer_signin_pass_text);
        CustomerSigninBtn = (Button) findViewById(R.id.customer_signin_btn);
        CustomerNoAccount = (TextView) findViewById(R.id.customer_noacc);
        SigninProgressBar = (ProgressBar) findViewById(R.id.signin_progressBar);

        CAuth = FirebaseAuth.getInstance();

        firebaseAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    Intent i = new Intent(CustomerSignin.this, CustomerHomeMap.class);
                    startActivity(i);
                    finish();
                    return;
                }
            }
        };

        CustomerSigninBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = CustomerEmail.getText().toString();
                final String password = CustomerPass.getText().toString();

                if(TextUtils.isEmpty(email)) {
                    Toast.makeText(CustomerSignin.this, "Pleases Enter Email", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(CustomerSignin.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                }
                else if(password.length() < 6) {
                    Toast.makeText(CustomerSignin.this, "Password is too short.", Toast.LENGTH_SHORT).show();
                }
                else {
                    SigninProgressBar.setVisibility(View.VISIBLE);
                    CAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(CustomerSignin.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                SigninProgressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    startActivity(new Intent(getApplicationContext(), CustomerHomeMap.class));
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(CustomerSignin.this, "Login failed or User does not exist.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }); }
            }
        });

        CustomerNoAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CustomerSignin.this, Customer_Registration.class);
                startActivity(i);
            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        CAuth.addAuthStateListener(firebaseAuthListner);
    }
    @Override
    protected void onStop() {
        super.onStop();
        CAuth.removeAuthStateListener(firebaseAuthListner);
    }
}
