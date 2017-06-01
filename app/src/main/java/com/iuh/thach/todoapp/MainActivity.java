package com.iuh.thach.todoapp;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity implements EditItemFragment.ListenerDialog,EnterItemFragment.AddListenerDialog{
    private ListView listView;
    private ArrayList<ListToDo> arrayList;
    private ListAdapter adapter;
    private int pos;
    private SQLite sql;

    String edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(true);
        listView = (ListView) findViewById(R.id.listView);
        arrayList = new ArrayList<>();


        sql = new SQLite(getApplicationContext(),"listToDo.sqlite",null,1);

        sql.queryData("CREATE TABLE IF NOT EXISTS ListToDo(id INTEGER PRIMARY KEY AUTOINCREMENT,task VARCHAR,date VARCHAR,priority VARCHAR)");

        Cursor listtodo = sql.getData("SELECT * FROM ListToDo");
        while (listtodo.moveToNext()) {
            int i = listtodo.getInt(0);
            String t = listtodo.getString(1);
            String d = listtodo.getString(2);
            String p = listtodo.getString(3);
            arrayList.add(new ListToDo(i,t, d,p));
        }
        adapter = new ListAdapter(MainActivity.this, android.R.layout.simple_list_item_1, arrayList);

        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
                edit = arrayList.get(position).task;
                FragmentManager fm = getSupportFragmentManager();
                EditItemFragment dialogFragment = EditItemFragment.newInstance("Edit Item:");
                dialogFragment.show(fm, "edit_alert");
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                sql.queryData("DELETE FROM ListToDo WHERE id = "+arrayList.get(position).id+"");
                arrayList.remove(position);
                adapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    public String getMyData(){
        return edit;
    }


    @Override
    public void onSaveEditedItem(String task, String date,String priority) {
        arrayList.set(pos, new ListToDo(arrayList.get(pos).id, task, date, priority));
        adapter.notifyDataSetChanged();
        sql.queryData("UPDATE ListToDo SET task = '"+task+"',date = '"+date+"',priority = '"+priority+"' WHERE id = "+arrayList.get(pos).id+"");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add_item:
                FragmentManager fm = getSupportFragmentManager();
                EnterItemFragment enterFrag = EnterItemFragment.newInstance("Add item:");
                enterFrag.show(fm,"add_alert");
        }
        return true;
    }


    @Override
    public void onAddItem(String task, String date, String priority) {

        sql.queryData("INSERT INTO ListToDo VALUES(null,'" + task + "','"+ date +"','"+ priority +"')");
        arrayList.clear();
        Cursor listtodo = sql.getData("SELECT * FROM ListToDo");
        while (listtodo.moveToNext()) {
            int i = listtodo.getInt(0);
            String t = listtodo.getString(1);
            String d = listtodo.getString(2);
            String p = listtodo.getString(3);
            arrayList.add(new ListToDo(i,t, d, p));
        }

        adapter = new ListAdapter(MainActivity.this, android.R.layout.simple_list_item_1, arrayList);

        listView.setAdapter(adapter);

    }

    @Override
    public void onSortingItem(String first, String second, String third) {
        Log.d("Test",first+second+third);

    }

    public Comparator<String> comparator = new Comparator<String>() {
        @Override
        public int compare(String lhs, String rhs) {
            lhs = arrayList.get(pos).priority;
            rhs = arrayList.get(pos+1).priority;
            return lhs.compareToIgnoreCase(rhs);
        }
    };

}