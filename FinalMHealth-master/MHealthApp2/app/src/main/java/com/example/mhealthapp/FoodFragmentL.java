package com.example.mhealthapp;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class FoodFragmentL extends Fragment {

    /*- 01 Class Variables -------------------------------------------------------------- */
    private View mainView;
    private Cursor listCursor;

    private String pcs_gram;
    Spinner food_size;

    private MenuItem menuItemDelete;
    private String currentMealNumber;

    // Holder for buttons on toolbar
    private String currentId;
    private String currentName;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((HomeActivity2) getActivity()).setActionBarTitle("Food & Nutritions");
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.activity_food_fragment, container, false);
        return mainView;
    }

    private void setMenuView(int id){

        LayoutInflater layoutInflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainView = layoutInflater.inflate(id,null);
        ViewGroup rootview= (ViewGroup)getView();
        rootview.removeAllViews();
        rootview.addView(mainView);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){

        ((HomeActivity2)getActivity()).getMenuInflater().inflate(R.menu.categories_menu,menu);

    }

    public boolean onOptionsItemSelected(MenuItem menuItem){

        int id = menuItem.getItemId();

        if(id == R.id.action_plus){
            addFood();
        }

        return super.onOptionsItemSelected(menuItem);

    }
    public void populateListFood(){

        /* Database */
        DBAdapter db = new DBAdapter(getActivity());
        db.open();

        // Get categories
        String fields[] = new String[] {
                "_id",
                "food_title",
                "food_cal",
                "food_text",
        };

        listCursor = db.select("food", fields, "", "", "food_title", "ASC");
        // Find ListView to populate
        ListView lvItems = (ListView)getActivity().findViewById(R.id.listViewFood);
        // Setup cursor adapter using cursor from last step
        FoodCursorAdapter continentsAdapter = new FoodCursorAdapter(getActivity(), listCursor);
        // Attach cursor adapter to the ListView
        lvItems.setAdapter(continentsAdapter); // uses ContinensCursorAdapter
        // OnClick
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                listItemClicked(arg2);
            }
        });

        // Close db
        db.close();

    }
    public void listItemClicked(int listItemIDClicked) {

        /* Change layout */
        int id = R.layout.fragment_food_view;
        setMenuView(id);


        // Move cursor to ID clicked
        listCursor.moveToPosition(listItemIDClicked);

        // Get ID and name from cursor
        // Set current name and id
        currentId = listCursor.getString(0);
        currentName = listCursor.getString(1);

        // Change title
        ((HomeActivity2)getActivity()).getSupportActionBar().setTitle(currentName);


        /*  Get data from database */

        // Database
        DBAdapter db = new DBAdapter(getActivity());
        db.open();


        String fields[] = new String[] {
                "_id",
                "food_title",
                "food_text",
                "food_cal",
        };
        String currentIdSQL = db.quoteSmart(currentId);
        Cursor foodCursor = db.select("food", fields, "_id", currentIdSQL);

        // Convert cursor to strings
        String stringId = foodCursor.getString(0);
        String stringName = foodCursor.getString(1);
        String stringtext = foodCursor.getString(2);
        String stringCal = foodCursor.getString(3);


        // Headline
        TextView textViewViewFoodName = (TextView) getView().findViewById(R.id.textViewViewFoodName);
        textViewViewFoodName.setText(stringName);

        TextView textViewViewFoodDescription = (TextView) getView().findViewById(R.id.textViewViewFoodDescription);
        textViewViewFoodDescription.setText(stringtext);

        // Calories table
        TextView textCal = (TextView) getView().findViewById(R.id.Cal_no);
        textCal.setText(stringCal);

        //Portion size
        EditText noOfServings = (EditText)getActivity().findViewById(R.id.edittextPortion);

        food_size = (Spinner)getActivity().findViewById(R.id.spinner_grms_pcs);
        // Portion gram
        EditText Gram_Pcs = (EditText)getActivity().findViewById(R.id.PortionSizeGram_pcs);
        Button savetodiary = (Button)getActivity().findViewById(R.id.save_food_to_diary);


        ArrayAdapter<CharSequence> adapter =ArrayAdapter.createFromResource(getActivity().getBaseContext(),R.array.g_p,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        food_size.setAdapter(adapter);
        food_size.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pcs_gram = adapterView.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        savetodiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFoodToDiary();
            }
        });

        db.close();
    }

    public void addFoodToDiary(){
        // We want to add food
        // Error
        int error = 0;

        // Database
        DBAdapter db = new DBAdapter(getActivity());
        db.open();


        String fields[] = new String[] {
                "_id",
                "food_title",
                "food_text",
                "food_cal",
        };
        String currentIdSQL = db.quoteSmart(currentId);
        Cursor foodCursor = db.select("food", fields, "_id", currentIdSQL);

        String stringId = foodCursor.getString(0);
        String stringName = foodCursor.getString(1);
        String stringtext = foodCursor.getString(2);
        String stringCal = foodCursor.getString(3);

        // Date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Month
        month = month+1; // Month starts with 0
        String stringMonth = "";
        if(month < 10){
            stringMonth = "0" + month;
        }
        else{
            stringMonth = "" + month;
        }
        // Day
        String stringDay = "";
        if(day < 10){
            stringDay = "0" + day;
        }
        else{
            stringDay = "" + day;
        }

        String stringFdDate = year + "-" + stringMonth + "-" + stringDay;
        String stringFdDateSQL = db.quoteSmart(stringFdDate);



        String fdNameSQL = db.quoteSmart(stringName);

        // Meal number
        EditText noOfServings = (EditText)getActivity().findViewById(R.id.edittextPortion);
        String stringFdMealNumber  = noOfServings.getText().toString();
        String stringFdMealNumberSQL = db.quoteSmart(stringFdMealNumber);

        int int_meal_no = 0;
        try{
            int_meal_no = Integer.parseInt(stringFdMealNumber);
        }
        catch (NumberFormatException nfe){
            error = 1;
            Toast.makeText(getActivity(), "Please fill inn a number in gram", Toast.LENGTH_SHORT).show();
        }
        if(stringFdMealNumber.equals("")){
            error = 1;
            Toast.makeText(getActivity(), "Number of servings cannot be empty", Toast.LENGTH_SHORT).show();
        }


        // Food id
        String stringFdFoodId = currentId;
        String StringFdFoodIdSQL = db.quoteSmart(stringFdFoodId);

        // Portion gram
        EditText Gram_Pcs = (EditText)getActivity().findViewById(R.id.PortionSizeGram_pcs);

        String fdServingSize = Gram_Pcs.getText().toString();
        String fdServingSizeGramSQL = db.quoteSmart(fdServingSize);

        String gram_pc_txt = pcs_gram;
        String unitSQL = db.quoteSmart(gram_pc_txt);

        double doublePortionSizeGram = 0;
        try{
            doublePortionSizeGram = Double.parseDouble(fdServingSize);
        }
        catch (NumberFormatException nfe){
            error = 1;
            Toast.makeText(getActivity(), "Please fill in a number in gram or pieces", Toast.LENGTH_SHORT).show();
        }
        if(fdServingSize.equals("")){
            error = 1;
            Toast.makeText(getActivity(), "Gram cannot be empty", Toast.LENGTH_SHORT).show();
        }

        // Energy calcualted
        // energy = myConsumotion*kcal/100

        double doubleCal = Double.parseDouble(stringCal);
        double doubleFdEnergyCalculated = 0;

        if (gram_pc_txt.equals("gram")){
            doubleFdEnergyCalculated = Math.round((doublePortionSizeGram*doubleCal*int_meal_no)/100);
        }else if(gram_pc_txt.equals("pcs")){
            doubleFdEnergyCalculated = Math.round((doublePortionSizeGram*doubleCal*int_meal_no)/100);
        }


        String stringFdEnergyCalcualted = "" + doubleFdEnergyCalculated;
        String stringFdEnergyCalcualtedSQL = db.quoteSmart(stringFdEnergyCalcualted);


        // Insert to SQL
        if(error == 0){
            String inpFields = "_id, fd_id, fd_date,food_title,food_measurement,food_unit, fd_meal_number,fd_cal";

            String inpValues = "NULL, " +StringFdFoodIdSQL + ", "+ stringFdDateSQL + ", " + fdNameSQL + ", " +
                    fdServingSizeGramSQL + ", " + unitSQL + ", " +
                    stringFdMealNumberSQL + ", " +
                    stringFdEnergyCalcualtedSQL;

            db.insertRecord("food_diary_Lunch", inpFields, inpValues);

            Toast.makeText(getActivity(), "Food diary updated", Toast.LENGTH_SHORT).show();

            // Change fragment to HomeFragment
            Fragment fragment = null;
            Class fragmentClass = null;
            fragmentClass = Food_diary.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flMain, fragment).commit();

        }

        // Close db
        db.close();
    }





    public void addFood(){
        /* Database */
        DBAdapter db = new DBAdapter(getActivity());
        db.open();

        /* Change layout */
        int id = R.layout.fragment_food_list_add;
        setMenuView(id);

        // Change title
        ((HomeActivity2)getActivity()).getSupportActionBar().setTitle("Add food");

        /* Main category */
        String spinnerFields[] = new String[] {
                "_id",
                "category_title",
                "category_parent_id"
        };
        Cursor dbCursorMain = db.select("categories", spinnerFields, "category_parent_id",
                "0", "category_title", "ASC");

        // Creating array
        int dbCursorCount = dbCursorMain.getCount();
        String[] arraySpinnerMainCategories = new String[dbCursorCount];

        // Convert Cursor to String
        for(int x=0;x<dbCursorCount;x++){
            arraySpinnerMainCategories[x] = dbCursorMain.getString(1).toString();
            dbCursorMain.moveToNext();
        }

        /* SubmitButton listener */
        Button buttonEditFood = (Button)getActivity().findViewById(R.id.buttonEditFood);
        buttonEditFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonAddFoodSubmitOnClick();
            }
        });


        /* Close db */
        db.close();
    } // addFood

    public void buttonAddFoodSubmitOnClick(){
        /* Database */
        DBAdapter db = new DBAdapter(getActivity());
        db.open();

        // Error?
        int error = 0;

        /* General */

        // Name
        EditText editTextEditFoodName = (EditText)getActivity().findViewById(R.id.editTextEditFoodName);
        String stringName = editTextEditFoodName.getText().toString();
        String stringNameSQL = db.quoteSmart(stringName);
        if(stringName.equals("")){
            Toast.makeText(getActivity(), "Please fill in a name.", Toast.LENGTH_SHORT).show();
            error = 1;
        }

        // Description
        EditText editTextEditFoodDescription = (EditText)getActivity().findViewById(R.id.editTextEditFoodDescription);
        String stringDescription = editTextEditFoodDescription.getText().toString();
        String stringDescriptionSQL = db.quoteSmart(stringDescription);


        EditText editTextCal = (EditText)getActivity().findViewById(R.id.food_cal_Tv);
        String stringFatPerHundred = editTextCal.getText().toString();

        double doubleFatPerHundred = 0;
        if(stringFatPerHundred.equals("")){
            Toast.makeText(getActivity(), "Please fill in Calories.", Toast.LENGTH_SHORT).show();
            error = 1;
        }
        else{
            try {
                doubleFatPerHundred = Double.parseDouble(stringFatPerHundred);
            }
            catch(NumberFormatException nfe) {
                Toast.makeText(getActivity(), "Calorie is not a number.", Toast.LENGTH_SHORT).show();
                error = 1;
            }
        }
        String stringFatPerHundredSQL = db.quoteSmart(stringFatPerHundred);


        /* Insert */
        if(error == 0){

            String fields =
                    "_id, " +
                            "food_title, " +
                            "food_cal, " +
                            "food_text ";
            String values =
                    "NULL, " +
                            stringNameSQL + ", " +
                            stringFatPerHundredSQL + ", " +
                            stringDescriptionSQL;


            db.insertRecord("food", fields, values);

            // Toast
            Toast.makeText(getActivity(), "Food created", Toast.LENGTH_SHORT).show();

            // Move user back to correct design
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flMain, new FoodFragmentB(), FoodFragmentB.class.getName()).commit();

        } // error == 0


        /* Close db */
        db.close();
    } // buttonAddFoodSubmitOnClick








    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if(bundle != null){
            currentMealNumber = bundle.getString("mealNumber");
        }
        populateListFood();

        setHasOptionsMenu(true);

    }




}
