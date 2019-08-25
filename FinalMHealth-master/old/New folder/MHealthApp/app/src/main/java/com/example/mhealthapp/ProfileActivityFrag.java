package com.example.mhealthapp;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileActivityFrag extends Fragment {

    private TextView uName, uEmail, uAge, uHeight, uWeight, uGender;
    private ImageView profile_pic;
    private DatabaseReference ref;
    private FirebaseAuth mAuth;
    private String curUid;

    public ProfileActivityFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((HomeActivity2)getActivity()).setActionBarTitle("Profile");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_activity, container, false);

    }

    public void onViewCreated(View view, Bundle savedInstanceState){

        mAuth = FirebaseAuth.getInstance();
        curUid = mAuth.getCurrentUser().getUid();

        ref = FirebaseDatabase.getInstance().getReference().child(curUid);
        uName = (TextView) getActivity().findViewById(R.id.userNametv);
        uAge = (TextView) getActivity().findViewById(R.id.ageTv);
        uWeight = (TextView) getActivity().findViewById(R.id.weightTv);
        uHeight = (TextView) getActivity().findViewById(R.id.heightTv);
        uGender = (TextView) getActivity().findViewById(R.id.gendertv);
        uEmail = (TextView) getActivity().findViewById(R.id.proNametv);
        //profile_pic = (ImageView) getActivity().findViewById(R.id.profile_pic);

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
                    Toast.makeText(getActivity(), "Error or no data in database", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
