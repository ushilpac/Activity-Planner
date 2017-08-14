package com.codepath.simpletodo;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Shilpa's on 7/24/2017.
 */

public class ItemsAdapter extends ArrayAdapter<Item> {

    private static final String PRIORITY_HIGH="HIGH";
    private static final String PRIORITY_LOW="LOW";
    private static final String PRIORITY_MEDIUM="MEDIUM";

    // View lookup cache
    private static class ViewHolder {
        TextView title;
        TextView priority;
        TextView dueDate;
    }


    public ItemsAdapter(Context context, ArrayList<Item> items){
        super(context,0,items);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        Item item = getItem(position);
        // view lookup cache stored in tag
        ViewHolder viewHolder;

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {

            viewHolder = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.work_item, parent, false);

            viewHolder.title = (TextView) convertView.findViewById(R.id.list_item_layout_title);
            viewHolder.priority = (TextView) convertView.findViewById(R.id.list_item_layout_priority);
            viewHolder.dueDate = (TextView)  convertView.findViewById(R.id.list_item_layout_dueDate);

            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        }else{
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Populate the data into the template view using the data object
        viewHolder.title.setText(item.getTitle());
        viewHolder.dueDate.setText(item.getDueDate());
        String priorityValue = item.getPriority();
        viewHolder.priority.setText(priorityValue);

        if(priorityValue.equals(PRIORITY_HIGH)){
            viewHolder.priority.setTextColor(Color.RED);
        }else if(priorityValue.equals(PRIORITY_MEDIUM)){
            viewHolder.priority.setTextColor(Color.MAGENTA);
        }else if(priorityValue.equals(PRIORITY_LOW)){
            viewHolder.priority.setTextColor(Color.BLUE);
        }
        // Return the completed view to render on screen
        return convertView;
    }


}
