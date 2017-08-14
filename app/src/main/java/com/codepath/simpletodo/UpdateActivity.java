package com.codepath.simpletodo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import static com.codepath.simpletodo.MainActivity.ITEM_OBJECT;

public class UpdateActivity extends AppCompatActivity {

    Item mItem;
    ItemsDatabaseHelper mItemsDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        Toolbar tool = (Toolbar)findViewById(R.id.addActivity_layout_toolBar);
        setSupportActionBar(tool);
        tool.setLogo(R.drawable.ic_action_home2);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        showPriority();
        showStatus();
        showCategory();

        //use should not add the past date for dueDate
        DatePicker dueDate = (DatePicker)findViewById(R.id.addActivity_layout_datePicker);
        dueDate.setMinDate(System.currentTimeMillis());

        displayItemData();

    }

    private  void showCategory(){
        Spinner spinner = (Spinner)findViewById(R.id.addActivity_layout_spinner_category);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

    }



    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.menu_save:
                saveWorkItemToDB();
                return true;
            case R.id.menu_close:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed(){

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Confirm Exit...");
        dialog.setMessage("Are you sure you want to go back? ");

        dialog.setPositiveButton("YES", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                finish();
            }
        });

        dialog.setNegativeButton("NO", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                Log.d("AlertDialog", "User selected No");
            }
        });

        dialog.show();

    }

    private void showStatus(){
        Spinner spinner = (Spinner) findViewById(R.id.addActivity_layout_spinner_status);

        //create an Arrayadaptor using the string array and default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.status_array,
                android.R.layout.simple_spinner_item);
        //specify the layout to use
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //apply adapter to spinner
        spinner.setAdapter(adapter);
    }

    private void showPriority(){

        Spinner spinner = (Spinner) findViewById(R.id.addActivity_layout_spinner_priority);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.priority_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

    }

    private void displayItemData(){

        Intent intent = getIntent();

        mItem =  intent.getParcelableExtra(ITEM_OBJECT);
        if(mItem == null)
            return;

        EditText title = (EditText) findViewById(R.id.addActivity_layout_title_edit);
        title.setText(mItem.getTitle());

        setDate();

        EditText notesView = (EditText) findViewById(R.id.addActivity_layout_notes_edit_text);
        notesView.setText(mItem.getNotes());

        Spinner priorityView = (Spinner) findViewById(R.id.addActivity_layout_spinner_priority);
        priorityView.setSelection(getIndex(priorityView,mItem.getPriority()));


        Spinner statusView = (Spinner) findViewById(R.id.addActivity_layout_spinner_status);
        statusView.setSelection(getIndex(statusView,mItem.getStatus()));

        Spinner categoryView = (Spinner) findViewById(R.id.addActivity_layout_spinner_category);
        categoryView.setSelection(getIndex(categoryView,mItem.getCategory()));
    }

    //private method of your class
    private int getIndex(Spinner spinner, String myString)
    {
        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                index = i;
                break;
            }
        }
        return index;
    }


    private void setDate(){

        String date = mItem.getDueDate();
        int month,day,year;
        String stArr[] = date.split("/");
        month = Integer.parseInt(stArr[0]);
        month=month-1;
        day=Integer.parseInt(stArr[1]);
        year= Integer.parseInt(stArr[2]);
        DatePicker dateView = (DatePicker) findViewById(R.id.addActivity_layout_datePicker);
        dateView.updateDate(year,month,day);


    }


    private void saveWorkItemToDB(){

        mItemsDB = ItemsDatabaseHelper.getInstance(this);

        EditText  titleEdit = (EditText) findViewById(R.id.addActivity_layout_title_edit);
        String  title = titleEdit.getText().toString();
        if(title.equals("")){
            promptErrorMessage();
            return;
        }
        mItem.setTitle(title);

        DatePicker datePick = (DatePicker) findViewById(R.id.addActivity_layout_datePicker);
        int day = datePick.getDayOfMonth();
        int month = datePick.getMonth()+1;
        int year = datePick.getYear();

        String date = ""+month+"/"+day+"/"+year;
        mItem.setDueDate(date);

        EditText noteEdit  = (EditText) findViewById(R.id.addActivity_layout_notes_edit_text);
        String notes = noteEdit.getText().toString();
        mItem.setNotes(notes);

        Spinner spinner1 = (Spinner) findViewById(R.id.addActivity_layout_spinner_priority);
        String priority = spinner1.getSelectedItem().toString();
        mItem.setPriority(priority);

        Spinner spinner2 = (Spinner) findViewById(R.id.addActivity_layout_spinner_status);
        String status = spinner2.getSelectedItem().toString();
        mItem.setStatus(status);

        Spinner spinner3 = (Spinner) findViewById(R.id.addActivity_layout_spinner_category);
        String category = spinner3.getSelectedItem().toString();
        mItem.setCategory(category);

        mItemsDB.updateWorkItem(mItem);

        //put the data to pass back to main acitivity and close the activity
        Intent intent = new Intent();
        intent.putExtra(ITEM_OBJECT, (Parcelable)mItem);
        setResult(RESULT_OK, intent);
        finish();
        return;

    }



    private void promptErrorMessage(){

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Error...");
        dialog.setMessage("Title can not be empty...");

        dialog.setNeutralButton("Ok", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                return;
            }
        });

        dialog.show();
    }



}
