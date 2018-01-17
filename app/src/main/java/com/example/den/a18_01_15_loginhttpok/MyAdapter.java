package com.example.den.a18_01_15_loginhttpok;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Den on 1/16/2018.
 */

public class MyAdapter extends BaseAdapter {
    private ArrayList<Person> persons;

    public MyAdapter() {
        persons = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return persons.size();
    }

    @Override
    public Object getItem(int position) {
        return persons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.my_row, parent, false);
        }
        Person p = persons.get(position);
        TextView nameTxt = convertView.findViewById(R.id.name_txt);
        TextView emailTxt = convertView.findViewById(R.id.email_txt);
        nameTxt.setText(p.getFullName());
        emailTxt.setText(p.getEmail());

        return convertView;
    }

    public void addPerson(Person p) {
        persons.add(0, p);
        notifyDataSetChanged();
    }
}
