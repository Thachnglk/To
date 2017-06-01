package com.iuh.thach.todoapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


public class ListAdapter extends ArrayAdapter<ListToDo> {
    public ListAdapter(Context context, int resource, List<ListToDo> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v==null){
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.custom_layout,null);
        }
        ListToDo p = getItem(position);
        if(p!=null){
            TextView task = (TextView)v.findViewById(R.id.textView2);
            task.setText(p.task);
            TextView date = (TextView)v.findViewById(R.id.textView3);
            date.setText(p.date);
            TextView priority = (TextView) v.findViewById(R.id.textView5);
            priority.setText(p.priority);

        }

        return v;
    }
}