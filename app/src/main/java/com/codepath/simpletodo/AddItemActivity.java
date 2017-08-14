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

import static android.R.attr.prompt;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static com.codepath.simpletodo.MainActivity.ITEM_OBJECT;

public class AddItemActivity extends AppCompatActivity {

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

        //instantiate DbHelper class
        mItemsDB = ItemsDatabaseHelper.getInstance(getApplicationContext());

        showPriority();
        showStatus();
        showCategory();

        //use should not add the past date for dueDate
        DatePicker dueDate = (DatePicker)findViewById(R.id.addActivity_layout_datePicker);
        dueDate.setMinDate(System.currentTimeMillis());
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

    private  void showCategory(){
        Spinner spinner = (Spinner)findViewById(R.id.addActivity_layout_spinner_category);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

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

    private void saveWorkItemToDB(){

        Item item = new Item();

        EditText  titleEdit = (EditText) findViewById(R.id.addActivity_layout_title_edit);
        String  title = titleEdit.getText().toString();
        if(title.equals("")){
            promptErrorMessage();
            return;
        }
        item.setTitle(title);

        DatePicker datePick = (DatePicker) findViewById(R.id.addActivity_layout_datePicker);
        int day = datePick.getDayOfMonth();
        int month = datePick.getMonth()+1;
        int year = datePick.getYear();

        String date = ""+month+"/"+day+"/"+year;
        item.setDueDate(date);

        EditText noteEdit  = (EditText) findViewById(R.id.addActivity_layout_notes_edit_text);
        String notes = noteEdit.getText().toString();
        item.setNotes(notes);

        Spinner spinner1 = (Spinner) findViewById(R.id.addActivity_layout_spinner_priority);
        String priority = spinner1.getSelectedItem().toString();
        item.setPriority(priority);

        Spinner spinner2 = (Spinner) findViewById(R.id.addActivity_layout_spinner_status);
        String status = spinner2.getSelectedItem().toString();
        item.setStatus(status);

        Spinner spinner3 = (Spinner) findViewById(R.id.addActivity_layout_spinner_category);
        String category = spinner3.getSelectedItem().toString();
        item.setCategory(category);


        mItemsDB.addItems(item);

        //put the data to pass back to main acitivity and close the activity
        Intent intent = new Intent();
        intent.putExtra(ITEM_OBJECT, (Parcelable)item);
        setResult(RESULT_OK, intent);
        finish();

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
