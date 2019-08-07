package com.example.mhealthapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListViewActivity extends AppCompatActivity {

    Button newmed;
    private static final String TAG = "ListViewActivity";
    DatabaseHelper mDatabaseHelper;
    private ListView mListView;
    ArrayList<User> userlist;
    Column_ListAdapter adapter;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        mListView = (ListView) findViewById(R.id.listview);
        newmed = (Button) findViewById(R.id.new_MED);

        mDatabaseHelper = new DatabaseHelper(this);

        userlist = new ArrayList<>();
        Cursor data = mDatabaseHelper.getData();
        int numRows = data.getCount();


        if (numRows == 0) {
            Toast.makeText(this, "The Database is empty  :(.", Toast.LENGTH_LONG).show();
        } else {
            int i = 0;
            while (data.moveToNext()) {
                //Getting the data to the column
                user = new User(data.getString(1), data.getString(2),
                        data.getString(3), data.getString(4));
                userlist.add(user);
            }
            adapter = new Column_ListAdapter(this, R.layout.adapter_view_layout, userlist);
            mListView.setAdapter(adapter);
        }

        newmed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListViewActivity.this, ReminderActivity.class));

            }
        });

    }



}