package com.example.mhealthapp;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.AlarmClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;



/**
 * A simple {@link Fragment} subclass.
 */
public class ReminderActivityFrag extends Fragment {

    private static final String TAG = "ReminderActivityFrag";


    DatabaseHelper mdDatabaseHelper;
    EditText medname , medId, mhour1,mmin1 ,mhour2,mmin2, mhour3,mmin3;
    Button set1,set2,set3, save , delmed;
    Spinner dose ,food ;
    String format ,id, food_p, dose_p ,timeset1, timeset2 ,timeset3 ,newEntry;
    TextView Reminder1,Reminder2,Reminder3;

    public ReminderActivityFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((HomeActivity2)getActivity()).setActionBarTitle("Reminder");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reminder_activity, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setUpview();

        mdDatabaseHelper = new DatabaseHelper(getActivity());
        Spinner_Dose();
        Spinner_Food();

        setAlarm1();
        setAlarm2();
        setAlarm3();

        SaveMed();
        DeleteMed();
    }

    //Morning Alarm
    public void setAlarm1(){
        set1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hr = Integer.parseInt(mhour1.getText().toString());
                int mi = Integer.parseInt(mmin1.getText().toString());

                Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
                intent.putExtra(AlarmClock.EXTRA_HOUR,hr);
                intent.putExtra(AlarmClock.EXTRA_MINUTES,mi);
                intent.putExtra(AlarmClock.EXTRA_MESSAGE,"Time to take medicine");
                if(hr <= 24 && mi <= 60){
                    startActivity(intent);
                }
                selectTimeFormat(hr);
                Reminder1.setText(hr + ":" + mi + format);
                timeset1 = Reminder1.getText().toString();
            }
        });
    }

    //Afternoon alarm
    public void setAlarm2(){
        set2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hr = Integer.parseInt(mhour2.getText().toString());
                int mi = Integer.parseInt(mmin2.getText().toString());

                Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
                intent.putExtra(AlarmClock.EXTRA_HOUR,hr);
                intent.putExtra(AlarmClock.EXTRA_MINUTES,mi);
                intent.putExtra(AlarmClock.EXTRA_MESSAGE,"Time to take medicine");
                if(hr <= 24 && mi <= 60){
                    startActivity(intent);
                }
                selectTimeFormat(hr);
                Reminder2.setText(hr + ":" + mi + format);
                timeset2 = Reminder2.getText().toString();

            }
        });
    }

    //Night Alarm
    public void setAlarm3(){
        set3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hr3 = Integer.parseInt(mhour3.getText().toString());
                int mi3 = Integer.parseInt(mmin3.getText().toString());

                Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
                intent.putExtra(AlarmClock.EXTRA_HOUR,hr3);
                intent.putExtra(AlarmClock.EXTRA_MINUTES,mi3);
                intent.putExtra(AlarmClock.EXTRA_MESSAGE,"Time to take medicine");

                if(hr3 <= 24 && mi3 <= 60){
                    startActivity(intent);
                }
                selectTimeFormat(hr3);
                Reminder3.setText(hr3 + ":" + mi3 + format);
                timeset3 = Reminder3.getText().toString();

            }
        });
    }
    //Spinner of dose : Half/full/quarter medicines
    public void Spinner_Dose(){
        ArrayAdapter<CharSequence> adapter =ArrayAdapter.createFromResource(getActivity().getBaseContext(),R.array.dose,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dose.setAdapter(adapter);
        dose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                dose_p = adapterView.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    //Spinner of food : Bf or Af
    public void Spinner_Food(){
        ArrayAdapter<CharSequence> adapter4 =ArrayAdapter.createFromResource(getActivity().getBaseContext(),R.array.food,
                android.R.layout.simple_spinner_item);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        food.setAdapter(adapter4);
        food.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                food_p = adapterView.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void SaveMed(){
        //When All Data are Saved
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                newEntry = medname.getText().toString();
                if (medname.length()!= 0){
                    AddData(newEntry,dose_p,food_p,timeset1,timeset2,timeset3);
                    medname.setText(" ");
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.flMain, new ListViewFragment());
                    ft.commit();
                }else{
                    Toast("Please Enter The Details");
                }
            }
        });

    }

    //Delete Medicine
    public void DeleteMed() {
        delmed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = medId.getText().toString();
                Integer deleteRows = mdDatabaseHelper.deleteData(id);
                if(deleteRows > 0){
                    Toast.makeText(getActivity(), "Data Deleted", Toast.LENGTH_LONG).show();
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.flMain, new ListViewFragment());
                    ft.commit();
                }else{
                    Toast.makeText(getActivity(), "Please Enter Medicine ID", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    //Time Format
    public void selectTimeFormat( int hour){

        if (hour == 0){
            hour += 12;
            format = "AM";
        }else if(hour == 12){
            format = "PM";
        }else if(hour>12){
            hour -= 12;
            format = "PM";
        }else{
            format = "AM";
        }
    }

    //Saving data local storage
    public void AddData(String newEntry, String dose ,String food,String time1,String time2,String time3){
        boolean insertData = mdDatabaseHelper.addData(newEntry,dose,food,time1 ,time2,time3);
        try{
            if(insertData == true){
                Toast("Data Successfully Inserted");
            }else{
                Toast("Something went wrong");
            }
        }catch(Exception ioe)
        {
            ioe.printStackTrace();
        }
    }

    //Toast Method
    public void Toast (String msg){
        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
    }

    //All id's are declared
    public void setUpview(){
        medname = (EditText)getActivity().findViewById(R.id.medicinename);
        dose = (Spinner) getActivity().findViewById(R.id.spinnerDose);
        save = (Button)getActivity().findViewById(R.id.buttonRemSubmit);
        set1 = (Button)getActivity().findViewById(R.id.set1);
        set2 = (Button)getActivity().findViewById(R.id.set2);
        set3 = (Button)getActivity().findViewById(R.id.set3);
        mhour1 = (EditText)getActivity().findViewById(R.id.hr1);
        mmin1 = (EditText)getActivity().findViewById(R.id.mi1);
        mhour2 = (EditText)getActivity().findViewById(R.id.hr2);
        mmin2 = (EditText)getActivity().findViewById(R.id.mi2);
        mhour3 = (EditText)getActivity().findViewById(R.id.hr3);
        mmin3 = (EditText)getActivity().findViewById(R.id.mi3);
        food = (Spinner)getActivity().findViewById(R.id.spinnerfood);
        delmed = (Button) getActivity().findViewById(R.id.del_MED);
        medId = (EditText)getActivity().findViewById(R.id.med_id);
        Reminder1 = (TextView)getActivity().findViewById(R.id.Txttime1);
        Reminder2 = (TextView) getActivity().findViewById(R.id.Txttime2);
        Reminder3 = (TextView) getActivity().findViewById(R.id.Txttime3);

    }
}
