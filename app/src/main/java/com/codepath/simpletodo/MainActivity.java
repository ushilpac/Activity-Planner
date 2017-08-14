package com.codepath.simpletodo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import java.util.ArrayList;

import static android.media.CamcorderProfile.get;
import static com.codepath.simpletodo.R.id.lvItems;

public class MainActivity extends AppCompatActivity {

    ArrayList<Item> mItemArray;
    ItemsAdapter mItemAdapter;
    ListView mItemListView;
    ItemsDatabaseHelper mItemDB;
    ImageView mImageView;
    TextView mTextView1;
    TextView mTextView2;


    private static final int DISPLAY_ITEM_REQUEST_CODE = 0;
    private static final int ADD_ACTIVITY_RESULT_CODE = 1;
    private static final int DEL_OPERATION_RESULT_CODE = 2;
    private static final int UPDATE_OPERATION_RESULT_CODE = 3;
    private static final String DISPLAY_OBJECT = "Display_object";
    public static final String ITEM_OBJECT = "item_object";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar tool = (Toolbar)findViewById(R.id.main_layout_toolbar);
        setSupportActionBar(tool);
        tool.setLogo(R.drawable.ic_action_home2);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        Log.d("SimpleTODO", "OnCreate of main activity");

        //instantiate DbHelper class
        mItemDB = ItemsDatabaseHelper.getInstance(getApplicationContext());


        //construct the itmes from the database
        mItemArray = mItemDB.getAllItems();

        if(mItemArray.size()<=0){
            displayInitialLogo();
        }

        //Create the adapter to convert the array to views
        mItemAdapter = new ItemsAdapter(this, mItemArray);

        //attach the adapter to list view
        mItemListView = (ListView) findViewById(lvItems);
        mItemListView.setAdapter(mItemAdapter);

        setupListViewListener();
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.menu_add:
                showAddItemActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showAddItemActivity(){

        RelativeLayout layout = (RelativeLayout)findViewById(R.id.main_layout);
        layout.removeView(mImageView);
        layout.removeView(mTextView1);
        layout.removeView(mTextView2);

        Intent intent = new Intent(getApplicationContext(), AddItemActivity.class);
        startActivityForResult(intent,ADD_ACTIVITY_RESULT_CODE);

    }


    private void setupListViewListener(){

        mItemListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> adapter, View view,
                                       int pos,long id){

                Intent intent = new Intent(getApplicationContext(), DisplayItemActivity.class);
                //Item item = mItemArray.get(pos);
                Item itemObj = mItemAdapter.getItem(pos);
                intent.putExtra(ITEM_OBJECT, (Parcelable)itemObj);
                startActivityForResult(intent,DISPLAY_ITEM_REQUEST_CODE);

            }
        });
    }


    // This method is called when the second activity finishes
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // check that it is the SecondActivity with an OK result

        if(requestCode == ADD_ACTIVITY_RESULT_CODE){
            if(resultCode == RESULT_OK){

                Item item = intent.getParcelableExtra(ITEM_OBJECT);
                mItemAdapter.add(item);
                mItemAdapter.notifyDataSetChanged();
            }
        }else if(requestCode == DISPLAY_ITEM_REQUEST_CODE ){
            if(resultCode == RESULT_OK){

                Item newItem = intent.getParcelableExtra(ITEM_OBJECT);
                int operationCode = intent.getIntExtra("Operation",-1);

                if(operationCode == DEL_OPERATION_RESULT_CODE) {

                    for(int i=0; i<mItemArray.size(); i++){
                        if(newItem.getItemId() == mItemArray.get(i).getItemId()){
                            mItemArray.remove(i);
                            mItemAdapter.notifyDataSetChanged();
                            if(mItemArray.size()<=0){
                                displayInitialLogo();
                            }
                            break;
                        }
                    }
                }
                else if(operationCode == UPDATE_OPERATION_RESULT_CODE)
                {
                    Item item = intent.getParcelableExtra(ITEM_OBJECT);
                    for(int i=0; i<mItemArray.size(); i++){
                        if(item.getItemId() == mItemArray.get(i).getItemId()){
                            mItemArray.set(i,item);
                            mItemAdapter.notifyDataSetChanged();
                        }
                    }


                }

            }

        }
    }

    public void displayInitialLogo(){

        RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.main_layout);

        mImageView = new ImageView(this);
        mImageView.setBackgroundResource(R.mipmap.ic_empty_box);
        mImageView.setId(R.id.imageLogoId);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(400,400);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        mImageView.setLayoutParams(layoutParams);
        mainLayout.addView(mImageView);

        mTextView1 = new TextView(this);
        mTextView1.setText("No tasks.");
        mTextView1.setId(R.id.textView1Id);
        mTextView1.setTextColor(Color.BLACK);

        RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        layoutParams1.addRule(RelativeLayout.BELOW,mImageView.getId());
        layoutParams1.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        mTextView1.setLayoutParams(layoutParams1);
        mainLayout.addView(mTextView1);

        mTextView2 = new TextView(this);
        mTextView2.setText("Add a task.");
        mTextView2.setTextColor(Color.GRAY);

        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        layoutParams2.addRule(RelativeLayout.BELOW, mTextView1.getId());
        layoutParams2.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        mTextView2.setLayoutParams(layoutParams2);
        mainLayout.addView(mTextView2);

    }

}

