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
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;

public class CategoriesFragment extends Fragment {

    /*- 01 Class Variables -------------------------------------------------------------- */
    private View mainView;
    private Cursor listCursor;

    // Action buttons on toolbar
    private MenuItem menuItemEdit;
    private MenuItem menuItemDelete;

    // Holder for buttons on toolbar
    private String currentId;
    private String currentName;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((HomeActivity2) getActivity()).setActionBarTitle("Categories");
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.activity_categories_fragment, container, false);
        return mainView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((HomeActivity2)getActivity()).getSupportActionBar().setTitle("Categories");

        populateList("0","");
        setHasOptionsMenu(true);

    }

    private void setMainView(int id){
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainView = inflater.inflate(id, null);
        ViewGroup rootView = (ViewGroup) getView();
        rootView.removeAllViews();
        rootView.addView(mainView);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {


        ((HomeActivity2)getActivity()).getMenuInflater().inflate(R.menu.categories_menu, menu);

        menuItemDelete = menu.findItem(R.id.action_del);
        menuItemDelete.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        int id = menuItem.getItemId();
        if (id == R.id.action_plus) {
            createNewCategory();
        }
        if (id == R.id.action_del) {
            deleteCategory();
        }
        return super.onOptionsItemSelected(menuItem);
    }




    public void createNewCategory(){
        /* Change layout */
        int id = R.layout.fragment_categories_edit;
        setMainView(id);

        /* Database */
        DBAdapter db = new DBAdapter(getActivity());
        db.open();

        /* Fill spinner with categories */
        String fields[] = new String[] {
                "_id",
                "category_title",
                "category_parent_id"
        };
        Cursor dbCursor = db.select("categories", fields, "category_parent_id", "0", "category_title", "ASC");

        // Creating array
        int dbCursorCount = dbCursor.getCount();
        String[] arraySpinnerCategories = new String[dbCursorCount+1];

        // This is parent
        arraySpinnerCategories[0] = "-";

        // Convert Cursor to String
        for(int x=1;x<dbCursorCount+1;x++){
            arraySpinnerCategories[x] = dbCursor.getString(1).toString();
            dbCursor.moveToNext();
        }

        // Populate spinner
        Spinner spinnerParent = (Spinner) getActivity().findViewById(R.id.spinner_cat_id);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, arraySpinnerCategories);
        spinnerParent.setAdapter(adapter);

        /* SubmitButton listener */
        Button buttonHome = (Button)getActivity().findViewById(R.id.btn_cat_save);
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewCategorySubmitOnClick();
            }
        });

        /* Close db */
        db.close();
    }
    public void createNewCategorySubmitOnClick() {
        /* Database */
        DBAdapter db = new DBAdapter(getActivity());
        db.open();
        // Error?
        int error = 0;

        // Name
        EditText editTextName = (EditText)getActivity().findViewById(R.id.name_cat);
        String stringName = editTextName.getText().toString();
        if(stringName.equals("")){
            Toast.makeText(getActivity(), "Please fill in a name.", Toast.LENGTH_SHORT).show();
            error = 1;
        }

        // Parent
        Spinner spinner = (Spinner)getActivity().findViewById(R.id.spinner_cat_id);
        String stringSpinnerCategoryParent = spinner.getSelectedItem().toString();
        String parentID;
        if(stringSpinnerCategoryParent.equals("-")){
            parentID = "0";
        }
        else{
            // Find we want to find parent ID from the text
            String stringSpinnerCategoryParentSQL = db.quoteSmart(stringSpinnerCategoryParent);
            String fields[] = new String[] {
                    "_id",
                    "category_title",
                    "category_parent_id"
            };
            Cursor findParentID = db.select("categories", fields, "category_title", stringSpinnerCategoryParentSQL);
            parentID = findParentID.getString(0).toString();


        }

        if(error == 0){
            // Ready variables
            String stringNameSQL = db.quoteSmart(stringName);
            String parentIDSQL = db.quoteSmart(parentID);

            // Insert into database
            String input = "NULL, " + stringNameSQL + ", " + parentIDSQL;
            db.insertRecord("categories", "_id, category_title, category_parent_id", input);

            // Give feedback
            Toast.makeText(getActivity(), "Category created", Toast.LENGTH_LONG).show();

            // Move user back to correct design
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flMain, new CategoriesFragment(), CategoriesFragment.class.getName()).commit();

        }

        /* Close db */
        db.close();
    } // createNewCategorySubmitOnClick



    public void populateList(String parentID, String parentName){

        /* Database */
        DBAdapter db = new DBAdapter(getActivity());
        db.open();

        // Get categories
        String fields[] = new String[] {
                "_id",
                "category_title",
                "category_parent_id"
        };
        listCursor = db.select("categories", fields, "category_parent_id", parentID, "category_title", "ASC");

        // Createa a array
        ArrayList<String> values = new ArrayList<String>();

        // Convert categories to string
        int categoriesCount = listCursor.getCount();
        for(int x=0;x<categoriesCount;x++){
            values.add(listCursor.getString(listCursor.getColumnIndex("category_title")));

            /* Toast.makeText(getActivity(),
                    "Id: " + categoriesCursor.getString(0) + "\n" +
                            "Name: " + categoriesCursor.getString(1), Toast.LENGTH_SHORT).show();*/
            listCursor.moveToNext();
        }


        // Close cursor
        // categoriesCursor.close();

        // Create adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, values);

        // Set Adapter
        ListView lv = (ListView)getActivity().findViewById(R.id.listview_cat);
        lv.setAdapter(adapter);

        // OnClick
        if(parentID.equals("0")) {
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    listItemClicked(arg2);
                }
            });
        }
        // Close db
        db.close();

        // Remove or show edit button
        if(parentID.equals("0")){
            // Remove edit button
            //Item menuItemEdit = (Item) getActivity().findViewById(R.id.action_edit);
            // menuItemEdit.setVisible(false);
            // menuItemDelete.setVisible(false);
        }
        else{
            // Show edt button
            menuItemEdit.setVisible(true);
            menuItemDelete.setVisible(true);
        }

    } // populateList

    public void listItemClicked(int listItemIDClicked){

        // Move cursor to ID clicked
        listCursor.moveToPosition(listItemIDClicked);

        // Get ID and name from cursor
        String id = listCursor.getString(0);
        String name = listCursor.getString(1);
        String parentID = listCursor.getString(2);

        // Change title
        ((HomeActivity2)getActivity()).getSupportActionBar().setTitle(name);

        // Set current name and id
        currentId = id;
        currentName = name;

        // Move to sub class
        populateList(id, name);
    } // listItemClicked


    public void deleteCategory(){

        /* Change layout */
        int id = R.layout.fragment_categories_delete;
        setMainView(id);

        /* SubmitButton listener */
        Button buttonCancel = (Button)getActivity().findViewById(R.id.buttonCategoriesCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCategoryCancelOnClick();
            }
        });

        Button buttonConfirm = (Button)getActivity().findViewById(R.id.buttonCategoriesConfirmDelete);
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCategoryConfirmOnClick();
            }
        });

    }

    public void deleteCategoryCancelOnClick(){
        // Move user back to correct design
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flMain, new CategoriesFragment(), CategoriesFragment.class.getName()).commit();

    }

    public void deleteCategoryConfirmOnClick(){
        // Delete from SQL

        /* Database */
        DBAdapter db = new DBAdapter(getActivity());
        db.open();

        // Current ID to long
        long longCurrentID = Long.parseLong(currentId);

        // Ready variables
        long currentIDSQL = db.quoteSmart(longCurrentID);

        // Delete
        db.delete("categories", "_id", currentIDSQL);

        // Close db
        db.close();

        // Give message
        Toast.makeText(getActivity(), "Category deleted", Toast.LENGTH_LONG).show();

        // Move user back to correct design
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flMain,
                new CategoriesFragment(), CategoriesFragment.class.getName()).commit();

    }


}

