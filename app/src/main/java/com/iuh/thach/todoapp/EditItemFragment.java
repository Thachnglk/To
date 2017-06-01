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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class EditItemFragment extends DialogFragment {

    ListenerDialog mCallBack;
    public interface ListenerDialog{
        public void onSaveEditedItem(String task, String date, String priority);
    }
    public EditItemFragment(){

    }
    public static EditItemFragment newInstance(String title){
        EditItemFragment editFrag = new EditItemFragment();
        Bundle args = new Bundle();
        args.putString("Title", title);
        editFrag.setArguments(args);
        return editFrag;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("Title");
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View v = inflater.inflate(R.layout.fragment_edit_item, null);
        final ArrayList<String> arrayEdit = new ArrayList<>();
        arrayEdit.add("HIGH");
        arrayEdit.add("MEDIUM");
        arrayEdit.add("LOW");

        final ListView listViewEdit = (ListView) v.findViewById(R.id.listView3);
        ArrayAdapter<String> adapterEdit = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,arrayEdit);
        listViewEdit.setAdapter(adapterEdit);
        final EditText edt_Edit = (EditText) v.findViewById(R.id.edt_edit);

        MainActivity activity = (MainActivity) getActivity();

        String ed = activity.getMyData();
        edt_Edit.setText(ed);
        alertBuilder.setTitle(title);
        alertBuilder.setView(v);
        listViewEdit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String s = edt_Edit.getText().toString();
                //set date
                 Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd-MM");
                String date = df.format(c.getTime());
                SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                String time = dateFormat.format(c.getTime());
                final String formatDate = "Edited: " + date + " at " + time;
                ListenerDialog listenerDialog = (ListenerDialog) getActivity();
                listenerDialog.onSaveEditedItem(s,formatDate,arrayEdit.get(position).toString());
                edt_Edit.setText("");
                dismiss();
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
            mCallBack = (ListenerDialog) activity;
        }catch(ClassCastException e){
            throw new ClassCastException(activity.toString() + "must implement ListenerDialog");
        }
    }
}