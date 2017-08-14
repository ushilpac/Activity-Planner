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
import android.widget.TextView;

import static com.codepath.simpletodo.MainActivity.ITEM_OBJECT;

public class DisplayItemActivity extends AppCompatActivity {

    private Item mItemObj;
    private ItemsDatabaseHelper mItemDb;
    private static final int DELETE_OPERATION_CODE = 2;
    private static final int UPDATE_OPERATION_CODE = 3;
    private static final int UPDATE_ACTIVITY_REQ_CODE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_item);

        Toolbar tool = (Toolbar)findViewById(R.id.displayActivity_layout_toolBar);
        tool.setNavigationIcon(R.drawable.ic_action_arrow1);
        setSupportActionBar(tool);
        tool.setLogo(R.drawable.ic_action_home2);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mItemDb = ItemsDatabaseHelper.getInstance(getApplicationContext());

        displayItemData();


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.menu_edit:
                updateWorkItem();
                return true;
            case R.id.menu_delete:
                deleteWorkItem();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteWorkItem(){

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Confirm Delete...");
        dialog.setMessage("Are you sure you want to Delete? ");
        dialog.setIcon(R.drawable.ic_action_delete1);

        dialog.setPositiveButton("YES", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                deleteItemFromDB();
            }
        });

        dialog.setNegativeButton("NO", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                Log.d("AlertDialog", "User selected No");

            }
        });

        dialog.show();

    }

    private void deleteItemFromDB(){

        mItemDb.deleteWorkItems(mItemObj.getItemId());

        //put the data to pass back to main activity and close the activity
        Intent intent = new Intent();
        intent.putExtra(ITEM_OBJECT,(Parcelable)mItemObj);
        intent.putExtra("Operation",DELETE_OPERATION_CODE);
        setResult(RESULT_OK, intent);
        finish();

    }

    private void updateWorkItem(){

        Intent intent = new Intent(this, UpdateActivity.class);
        intent.putExtra(ITEM_OBJECT, (Parcelable)mItemObj);
        startActivityForResult(intent,UPDATE_ACTIVITY_REQ_CODE);
    }

    private void displayItemData(){

        Intent intent = getIntent();

        mItemObj =  intent.getParcelableExtra(ITEM_OBJECT);

        TextView titleView = (TextView) findViewById(R.id.displayActivity_layout_title_value);
        titleView.setText(mItemObj.getTitle());

        TextView dateView = (TextView) findViewById(R.id.displayActivity_layout_date_value);
        dateView.setText(mItemObj.getDueDate());

        TextView notesView = (TextView) findViewById(R.id.displayActivity_layout_notes_value);
        notesView.setText(mItemObj.getNotes());

        TextView priorityView = (TextView) findViewById(R.id.displayActivity_layout_priority_value);
        priorityView.setText(mItemObj.getPriority());

        TextView statusView = (TextView) findViewById(R.id.displayActivity_layout_status_value);
        statusView.setText(mItemObj.getStatus());

        TextView categoryView = (TextView) findViewById(R.id.displayActivity_layout_category_value);
        categoryView.setText(mItemObj.getCategory());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // check that it is the SecondActivity with an OK result
        if (requestCode == UPDATE_ACTIVITY_REQ_CODE) {
            if (resultCode == RESULT_OK) {

                Item item = intent.getParcelableExtra(ITEM_OBJECT);

                //put the data to pass back to main activity and close the activity
                Intent intentNew = new Intent();
                intentNew.putExtra("Operation",UPDATE_OPERATION_CODE);
                intentNew.putExtra(ITEM_OBJECT,(Parcelable)item);
                setResult(RESULT_OK, intentNew);
                finish();

            }
        }
    }


}
