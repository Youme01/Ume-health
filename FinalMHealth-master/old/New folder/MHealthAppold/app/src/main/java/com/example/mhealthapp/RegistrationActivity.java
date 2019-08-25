package com.example.mhealthapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegistrationActivity extends AppCompatActivity {

    private Button signupR, loginR;
    FirebaseAuth firebaseAuth;
    private EditText usernameR, passwordR, emailR, passwordR2, age, height, weight, gender;
    String name, email, userageV, userweightV, pass, pass2, userheightV, usergenderV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setupViews();

        signupR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    firebaseAuth = FirebaseAuth.getInstance();

                    String userEmail = emailR.getText().toString().trim();
                    String userPass = passwordR.getText().toString().trim();


                    firebaseAuth.createUserWithEmailAndPassword(userEmail, userPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                sendemailverification();
                            } else {
                                task.getException().printStackTrace();
                                Toast.makeText(RegistrationActivity.this, "Registration Failed !!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }


        });

        loginR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
            }
        });


    }

    private void sendUserdata() {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myref = firebaseDatabase.getReference(firebaseAuth.getUid());

        UserProfile userProfile = new UserProfile(name, email, userageV, userheightV, userweightV, usergenderV);

        myref.setValue(userProfile);


    }

    private void sendemailverification() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null) {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        sendUserdata();
                        Toast.makeText(RegistrationActivity.this, "Registration Successful! Verification email has been sent", Toast.LENGTH_LONG).show();
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                    } else {
                        Toast.makeText(RegistrationActivity.this, "Verification email is not sent", Toast.LENGTH_LONG).show();

                    }
                }
            });
        }
    }

    public Boolean validate() {

        Boolean res = false;

        name = usernameR.getText().toString();
        pass = passwordR.getText().toString();
        email = emailR.getText().toString();
        pass2 = passwordR2.getText().toString();
        userageV = age.getText().toString();
        usergenderV = gender.getText().toString();
        userheightV = height.getText().toString();
        userweightV = weight.getText().toString();

        if (name.isEmpty() || pass2.isEmpty() || userageV.isEmpty() || userweightV.isEmpty() || userheightV.isEmpty() || usergenderV.isEmpty() || pass.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please Enter All The Details!", Toast.LENGTH_LONG).show();
        } else if (pass.compareTo(pass2) != 0) {
            Toast.makeText(this, "Password doesn't match . Try Again!", Toast.LENGTH_LONG).show();
        } else {
            res = true;
        }
        return res;

    }


    private void setupViews() {
        usernameR = (EditText) findViewById(R.id.usernameR);
        passwordR = (EditText) findViewById(R.id.passR);
        passwordR2 = (EditText) findViewById(R.id.passR2);
        emailR = (EditText) findViewById(R.id.email);
        signupR = (Button) findViewById(R.id.signupR);
        loginR = (Button) findViewById(R.id.loginR);
        age = (EditText) findViewById(R.id.age);
        weight = (EditText) findViewById(R.id.weight);
        height = (EditText) findViewById(R.id.height);
        gender = (EditText) findViewById(R.id.gender);


    }
}
