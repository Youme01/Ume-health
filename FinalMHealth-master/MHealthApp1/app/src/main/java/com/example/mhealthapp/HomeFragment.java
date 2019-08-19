package com.example.mhealthapp;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment  extends Fragment   {

    GridView gridView;
    String[] funcNames = {"Activity", "Heart Rate", "Steps", "Food&\nNutrition", "BMI", "Exercise", "Profile", "Reminders", "Sleep", "Settings", "About Us"};
    int[] funcImg = {R.drawable.activity1, R.drawable.heartbeat, R.drawable.steps, R.drawable.mealplan, R.drawable.bmi, R.drawable.exercise, R.drawable.profile, R.drawable.notification
            , R.drawable.sleep, R.drawable.settings, R.drawable.aboutus};

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((HomeActivity2)getActivity()).setActionBarTitle("Home");
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_home, container, false);



    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        gridView = (GridView) getView().findViewById(R.id.allAppGv);

        HomeFragment.CustomAdapter customAdpter = new HomeFragment.CustomAdapter();
        gridView.setAdapter(customAdpter);
        setOnItemclikForGridView();

    }



    public void setOnItemclikForGridView() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), gridviewActivity.class);
                intent.putExtra("name", funcNames[i]);
                intent.putExtra("image", funcImg[i]);
                if (funcNames[i] == "Profile") {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.flMain, new ProfileActivityFrag());
                    ft.commit();                }
                else if (funcNames[i] == "Reminders") {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.flMain, new ReminderActivityFrag());
                    ft.commit();
                }

            }

        });
    }



    private class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return funcImg.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view1 = (View) getLayoutInflater().inflate(R.layout.row_data, null);

            TextView name = view1.findViewById(R.id.activityIconTV);
            ImageView img = view1.findViewById(R.id.activityIconIV);
            name.setText(funcNames[i]);


            img.setImageResource(funcImg[i]);


            return view1;


        }





    }

}





