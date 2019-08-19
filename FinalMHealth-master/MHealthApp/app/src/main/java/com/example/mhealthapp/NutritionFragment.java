package com.example.mhealthapp;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import okhttp3.OkHttpClient;

public class NutritionFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((HomeActivity2) getActivity()).setActionBarTitle("Food & Nutritions");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_nutrition_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Stetho.initializeWithDefaults(getContext());

        new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();

        DBAdapter db = new DBAdapter(getContext());
        db.open();
        int rows = db.CountAllRecords("food");

        if(rows<1) {
            DbFoodInsert dbFoodInsert = new DbFoodInsert(getContext());
            dbFoodInsert.insertAllfood();
        }

        db.close();


    }
}
