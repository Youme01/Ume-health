package com.example.mhealthapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText username, userpassword;
    private Button usersignup, userlogin;
    private TextView forgotpassword;
    int counter = 3;
    private FirebaseAuth firebaseAuth;

    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = (EditText) findViewById(R.id.emailP);
        userpassword = (EditText) findViewById(R.id.pass);
        userlogin = (Button) findViewById(R.id.login);
        usersignup = (Button) findViewById(R.id.signup);
        forgotpassword = (TextView) findViewById(R.id.forgotpass);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();


        if (user != null) {
            finish();
            startActivity(new Intent(MainActivity.this, HomeActivity2.class));
        }

        userlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validate();
            }
        });

        usersignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
                startActivity(intent);

            }
        });

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PasswordActivity.class));
            }
        });


    }

    private void validate() {

        firebaseAuth.signInWithEmailAndPassword(username.getText().toString(), userpassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {


            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //Toast.makeText(MainActivity.this, "Login Successful!!", Toast.LENGTH_LONG).show();
                    checkemailverification();
                } else {
                    counter--;
                    Toast.makeText(MainActivity.this, "Number of attempts remaining " + counter, Toast.LENGTH_LONG).show();
                    if (counter == 0) {
                        userlogin.setEnabled(false);
                    }
                }

            }
        });

    }

    private void checkemailverification() {
        FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        Boolean flag = firebaseUser.isEmailVerified();

        if (flag) {
            startActivity(new Intent(MainActivity.this, HomeActivity2.class));
        } else {
            Toast.makeText(this, "Verify Your Email", Toast.LENGTH_LONG).show();
            firebaseAuth.signOut();
        }
    }
}
