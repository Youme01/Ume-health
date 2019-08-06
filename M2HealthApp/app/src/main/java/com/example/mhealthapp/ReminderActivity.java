package com.example.mhealthapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

public class ReminderActivity extends AppCompatActivity {

    private static final String TAG = "ReminderActivity";

    DatabaseHelper mdDatabaseHelper;

    private int notificationid = 1;
    int hour,min;
    public TimePicker timePicker;

    RadioGroup radioGroup;
    RadioButton radioButton;
    EditText medname;
    Button Time1 , Time2 ,Time3 , save;
    Spinner dose , duration, med ;
    String format , food , dose_p , time_p , med_p , newEntry;
    Switch TimeSetOn;
    TextView Reminder1;
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
        Time1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(ReminderActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {

                        TextView Reminder3 = (TextView)findViewById(R.id.Txttime3);
                        Reminder3.setText(i + ":" + i1 + " " + format);
                        c.set(Calendar.HOUR_OF_DAY, i);
                        c.set(Calendar.MINUTE, i1);
                        c.set(Calendar.SECOND, 0);
                        selectTimeFormat(hour);
                        startAlarm(c);


                    }
                },hour,min,true);
                timePickerDialog.show();
            }
            });


        Time2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimePickerDialog timePickerDialog = new TimePickerDialog(ReminderActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        TextView Reminder2 = (TextView)findViewById(R.id.Txttime2);
                        Reminder2.setText(i + ":" + i1 + " " + format);

                        c.set(Calendar.HOUR_OF_DAY, i);
                        c.set(Calendar.MINUTE, i1);
                        c.set(Calendar.SECOND, 0);
                        selectTimeFormat(hour);
                        startAlarm(c);

                    }
                },hour,min,true);
                timePickerDialog.show();
            }
        });
        Time3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimePickerDialog timePickerDialog = new TimePickerDialog(ReminderActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        TextView Reminder3 = (TextView)findViewById(R.id.Txttime3);
                        Reminder3.setText(i + ":" + i1 + " " + format);

                        c.set(Calendar.HOUR_OF_DAY, i);
                        c.set(Calendar.MINUTE, i1);
                        c.set(Calendar.SECOND, 0);
                        selectTimeFormat(hour);
                        startAlarm(c);

                    }
                },hour,min,true);
                timePickerDialog.show();
            }
        });

        //Spinner of dose : Half/full/quarter medicines
        ArrayAdapter<CharSequence> adapter =ArrayAdapter.createFromResource(this,R.array.dose, android.R.layout.simple_spinner_item);
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


        ArrayAdapter<CharSequence> adapter2 =ArrayAdapter.createFromResource(this,R.array.duration, android.R.layout.simple_spinner_item);
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

        ArrayAdapter<CharSequence> adapter3 =ArrayAdapter.createFromResource(this,R.array.med, android.R.layout.simple_spinner_item);
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




        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //For radiobutton to be checked
                int radioId = radioGroup.getCheckedRadioButtonId();
                radioButton =(RadioButton) findViewById(radioId);

               // food = radioButton.getText().toString();

                newEntry = medname.getText().toString();

                if (medname.length()!= 0){
                    AddData(newEntry,dose_p,time_p);
                    medname.setText(" ");
                    startActivity(new Intent(ReminderActivity.this,ListViewActivity.class));

                }else{
                    Toast("Please Enter The Details");
                }
            }
        });

        TimeSetOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    /*public void updateTimeText(Calendar c){
        String time_text = "Alarm set for: ";
        time_text += DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
        Reminder1.setText(time_text);
    }*/

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
    public void AddData(String newEntry, String dose , String time){
        boolean insertData = mdDatabaseHelper.addData(newEntry,dose,time);
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
        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        TimeSetOn = (Switch)findViewById(R.id.switch1);
    }

  /*  @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);

        updateTimeText(c);
        startAlarm(c);

    }*/
}
