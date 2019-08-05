package com.example.mhealthapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    private TextView uName, uEmail, uAge, uHeight, uWeight, uGender;
    private ImageView profile_pic;
    private DatabaseReference ref;
    private FirebaseAuth mAuth;
    private String curUid;

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAuth = FirebaseAuth.getInstance();
        curUid = mAuth.getCurrentUser().getUid();

        ref = FirebaseDatabase.getInstance().getReference().child(curUid);
        uName = (TextView) findViewById(R.id.userNametv);
        uAge = (TextView) findViewById(R.id.ageTv);
        uWeight = (TextView) findViewById(R.id.weightTv);
        uHeight = (TextView) findViewById(R.id.heightTv);
        uGender = (TextView) findViewById(R.id.gendertv);
        uEmail = (TextView) findViewById(R.id.proNametv);
        profile_pic = (ImageView) findViewById(R.id.profile_pic);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("username").getValue().toString();
                    String age = dataSnapshot.child("userage").getValue().toString();
                    String height = dataSnapshot.child("userheight").getValue().toString();
                    String weight = dataSnapshot.child("userweight").getValue().toString();
                    String gender = dataSnapshot.child("usergender").getValue().toString();
                    String email = dataSnapshot.child("useremail").getValue().toString();

                    uName.setText(name);
                    uAge.setText(age);
                    uEmail.setText(email);
                    uGender.setText(gender);
                    uHeight.setText(height);
                    uWeight.setText(weight);


                } else {
                    Toast.makeText(ProfileActivity.this, "Error or no data in database", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
