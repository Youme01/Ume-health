package com.example.mhealthapp;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class heartRateWithApi extends Fragment {


    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;

    private FirebaseAuth.AuthStateListener mAuthListner;
    private DatabaseReference myref;
    private String uid;

    TextView uHrtrate , status;

    public heartRateWithApi() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_heart_rate_with_api, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.Measurement).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Heartrate", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(),camActivity.class);
                startActivity(intent);
            }
        });


        uHrtrate = view.findViewById(R.id.BPMcount);


        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myref = mFirebaseDatabase.getReference();
        final FirebaseUser user = mAuth.getCurrentUser();
        uid = user.getUid();

        myref.child("UserHeartRate").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String heartrate = dataSnapshot.child("Last HeartRate").getValue().toString();
                uHrtrate.setText(heartrate);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        String hrtRate = uHrtrate.getText().toString();
        int hrtRateVal = Integer.parseInt(hrtRate);

        if(hrtRateVal> 60 && hrtRateVal<140){

            status.setText(hrtRate);
        }




//        view.findViewById(R.id.buttoninsdb).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String userId = mAuth.getCurrentUser().getUid();
//                DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("UserHeartRate").child(userId);
//
//                String hrt = String.valueOf(hrtratebpm);
//                Map newPost = new HashMap();
//                newPost.put("Last HeartRate",hrt);
//                current_user_db.setValue(newPost);
//            }
//        });
    }
}



