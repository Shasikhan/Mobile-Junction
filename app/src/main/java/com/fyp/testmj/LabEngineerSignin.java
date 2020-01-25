package com.fyp.testmj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LabEngineerSignin extends AppCompatActivity {

    private EditText EngEmail, EngPassword;
    private Button LabEngineerSigninBtn;
    private FirebaseAuth lAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_engineer_signin);

        EngEmail = (EditText) findViewById(R.id.eng_email_text);
        EngPassword = (EditText) findViewById(R.id.eng_pass_text);
        LabEngineerSigninBtn = (Button) findViewById(R.id.eng_signin_btn);
        lAuth = FirebaseAuth.getInstance();

        firebaseAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (user != null) {

                    Intent i = new Intent(LabEngineerSignin.this, LabEngineerHome.class);
                    startActivity(i);
                    finish();
                    return;

                }
            }
        };

        LabEngineerSigninBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = EngEmail.getText().toString();
                final String password = EngPassword.getText().toString();

                if(TextUtils.isEmpty(email)) {
                    Toast.makeText(LabEngineerSignin.this, "Pleases Enter Email", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(LabEngineerSignin.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                }
                else if(password.length() < 6) {
                    Toast.makeText(LabEngineerSignin.this, "Password is too short.", Toast.LENGTH_SHORT).show();
                }
                else {
                    lAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LabEngineerSignin.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        startActivity(new Intent(getApplicationContext(), LabEngineerHome.class));


                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(LabEngineerSignin.this, "Login failed or User does not exist.", Toast.LENGTH_SHORT).show();
                                    }


                                }
                            }); }
            }
        });


    }
    @Override
    protected void onStart() {
        super.onStart();
        lAuth.addAuthStateListener(firebaseAuthListner);
    }
    @Override
    protected void onStop() {
        super.onStop();
        lAuth.removeAuthStateListener(firebaseAuthListner);
    }

}
