package com.iuh.thach.todoapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class EnterItemFragment extends DialogFragment {
    AddListenerDialog CallBack;
    public interface AddListenerDialog{
        public void onAddItem(String task, String date, String priority);
        public void onSortingItem(String first, String second, String third);
    }
    public EnterItemFragment(){

    }

    public static EnterItemFragment newInstance(String title){
        EnterItemFragment frag = new EnterItemFragment();
        Bundle bundle = new Bundle();
        bundle.putString("Title", title);
        frag.setArguments(bundle);
        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("Title");

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.fragment_enter_item, null);
        alertBuilder.setTitle(title);
        alertBuilder.setView(v);
        final EditText edt_Add = (EditText) v.findViewById(R.id.edt_add);
        final ArrayList<String> arrayAdd = new ArrayList<>();
        arrayAdd.add("HIGH");
        arrayAdd.add("MEDIUM");
        arrayAdd.add("LOW");

        ListView listAdd = (ListView) v.findViewById(R.id.listView2);
        ArrayAdapter<String> adapterListAdd = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,arrayAdd);
        listAdd.setAdapter(adapterListAdd);
        listAdd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AddListenerDialog onSorting = (AddListenerDialog) getActivity();
                onSorting.onSortingItem(arrayAdd.get(0).toString(),arrayAdd.get(1).toString(),arrayAdd.get(2).toString());
                String add = edt_Add.getText().toString();
                if(add == ""){
                    while(add==null){
                        Toast.makeText(getActivity(),"Enter the task...",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    //set date
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("dd-MM");
                    String date = df.format(c.getTime());
                    SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                    String time = dateFormat.format(c.getTime());
                    String formatDate = "Add: " + date + " at " + time;
                    String pri = arrayAdd.get(position).toString();
                    AddListenerDialog addListenerDialog = (AddListenerDialog) getActivity();
                    addListenerDialog.onAddItem(add, formatDate, pri);
                    edt_Add.setText("");
                    dismiss();
                }
            }
        });


        alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        return alertBuilder.create();
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try{
            CallBack = (AddListenerDialog) activity;
        }catch(ClassCastException e){
            throw new ClassCastException(activity.toString() + "must implement ListenerDialog");
        }
    }

}