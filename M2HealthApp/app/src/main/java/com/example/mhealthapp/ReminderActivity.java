package com.example.mhealthapp;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.util.Calendar;

public class ReminderActivity extends AppCompatActivity {

    private static final String TAG = "ReminderActivity";

    DatabaseHelper mdDatabaseHelper;
    int hour,min;

    EditText medname , medId;
    Button Time1 , Time2 ,Time3 , save , delmed;
    Spinner dose , duration, med ,food ;
    String format ,id, food_p, dose_p , time_p ,timeset, med_p , newEntry;
    Switch TimeSetOn;
    TextView Reminder1,Reminder2,Reminder3;
    Calendar c;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        setUpview();

        mdDatabaseHelper = new DatabaseHelper(this);
        c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        min = c.get(Calendar.MINUTE);

        selectTimeFormat(hour);
        Set_Time1();
        Set_Time2();
        Set_Time3();

        Spinner_Dose();
        Spinner_Duration();
        Spinner_Med_Category();
        Spinner_Food();
        SaveMed();
        DeleteMed();
    }

    //Morning Time
    public void Set_Time1(){
        Time1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(ReminderActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                Reminder1= (TextView)findViewById(R.id.Txttime1);
                                Reminder1.setText(i + ":" + i1 + " " + format);
                                c.set(Calendar.HOUR_OF_DAY, i);
                                c.set(Calendar.MINUTE, i1);
                                c.set(Calendar.SECOND, 0);
                                startAlarm(c);
                            }
                        },hour,min,true);
                timePickerDialog.show();
            }
        });

    }
    //Afternoon Time
    public void Set_Time2(){
        Time2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(ReminderActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        Reminder2 = (TextView)findViewById(R.id.Txttime2);
                        Reminder2.setText(i + ":" + i1 + " " + format);
                        c.set(Calendar.HOUR_OF_DAY, i);
                        c.set(Calendar.MINUTE, i1);
                        c.set(Calendar.SECOND, 0);
                        startAlarm(c);
                    }
                },hour,min,true);
                timePickerDialog.show();
            }
        });

    }

    //Dinner Time
    public void Set_Time3(){
        Time3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(ReminderActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        Reminder3 = (TextView)findViewById(R.id.Txttime3);
                        Reminder3.setText(i + ":" + i1 + " " + format);
                        c.set(Calendar.HOUR_OF_DAY, i);
                        c.set(Calendar.MINUTE, i1);
                        c.set(Calendar.SECOND, 0);
                        startAlarm(c);
                    }
                },hour,min,true);
                timePickerDialog.show();
            }
        });
    }


    //Spinner of dose : Half/full/quarter medicines
    public void Spinner_Dose(){
        ArrayAdapter<CharSequence> adapter =ArrayAdapter.createFromResource(this,R.array.dose,
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

    //Spinner of duration: 3days/10days
    public void Spinner_Duration(){
        ArrayAdapter<CharSequence> adapter2 =ArrayAdapter.createFromResource(this,R.array.duration,
                android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        duration.setAdapter(adapter2);
        duration.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                time_p = adapterView.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    //Spinner of medicine category
    public void Spinner_Med_Category(){
        ArrayAdapter<CharSequence> adapter3 =ArrayAdapter.createFromResource(this,R.array.med,
                android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        med.setAdapter(adapter3);
        med.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                med_p = adapterView.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    //Spinner of food : Bf or Af
    public void Spinner_Food(){
        ArrayAdapter<CharSequence> adapter4 =ArrayAdapter.createFromResource(this,R.array.food,
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
                id = medId.getText().toString();
                newEntry = medname.getText().toString();
                timeset = Reminder1.getText().toString();
                 if (medname.length()!= 0 && timeset.length()!=0){
                    AddData(newEntry,dose_p,food_p,timeset);
                    medname.setText(" ");
                    startActivity(new Intent(ReminderActivity.this,ListViewActivity.class));
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
                    Toast.makeText(ReminderActivity.this, "Data Deleted", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(ReminderActivity.this,ListViewActivity.class));
                }else{
                    Toast.makeText(ReminderActivity.this, "Please Enter Medicine ID", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    //Alarm is initiated here
    public void startAlarm(Calendar c){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        Toast.makeText(ReminderActivity.this, "Done!", Toast.LENGTH_SHORT).show();
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
    public void AddData(String newEntry, String dose , String time,String food){
        boolean insertData = mdDatabaseHelper.addData(newEntry,dose,food,time);
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
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    //All id's are declared
    public void setUpview(){
        medname = (EditText)findViewById(R.id.medicinename);
        dose = (Spinner) findViewById(R.id.spinnerDose);
        duration = (Spinner) findViewById(R.id.spinnerduration);
        med = (Spinner) findViewById(R.id.spinnerMed);
        save = (Button)findViewById(R.id.buttonRemSubmit);
        Time1 = (Button)findViewById(R.id.time1);
        Time2 = (Button)findViewById(R.id.time2);
        Time3 = (Button)findViewById(R.id.time3);
        TimeSetOn = (Switch)findViewById(R.id.switch1);
        food = (Spinner)findViewById(R.id.spinnerfood);
        delmed = (Button) findViewById(R.id.del_MED);
        medId = (EditText)findViewById(R.id.med_id);
    }
}
