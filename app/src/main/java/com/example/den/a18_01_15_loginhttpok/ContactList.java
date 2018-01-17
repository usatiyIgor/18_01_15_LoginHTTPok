package com.example.den.a18_01_15_loginhttpok;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ContactList extends AppCompatActivity {
    private Button btn_logout, btn_add;
    private TextView viewEmpty;
    private ListView my_list;
    private MyAdapter adapter;
    private Context cntx;
    private String emailEnter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        btn_add = findViewById(R.id.btn_add);
        btn_logout = findViewById(R.id.btn_logout);
        viewEmpty = findViewById(R.id.view_empty);
        cntx = this;
        my_list = findViewById(R.id.my_list);
        adapter = new MyAdapter();
        my_list.setAdapter(adapter);
    }
}
