package com.example.den.a18_01_15_loginhttpok;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class ContactList extends AppCompatActivity {
    private TextView viewEmpty;
    private FrameLayout myProgress;
    private ListView my_list;
    private MyAdapter adapter;
    private Context cntx;
    private MenuItem addItem, logoutItem, downloadItem, deleteItem;
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        myProgress = findViewById(R.id.my_progress);
        viewEmpty = findViewById(R.id.view_empty);
        cntx = this;
        my_list = findViewById(R.id.my_list);
        adapter = new MyAdapter();
        my_list.setAdapter(adapter);
        token = getSharedPreferences(AuthToken.TOKEN_STORAGE,MODE_PRIVATE)
                .getString(AuthToken.TOKEN,"");

        my_list.setOnItemClickListener((parent,view,position,id)->{
            Person person = (Person) adapter.getItem(position);
            Intent intent = new Intent(cntx,ContactView.class);
            intent.putExtra("Name", person.getFullName());
            intent.putExtra("Email", person.getEmail());
            intent.putExtra("Phone", person.getPhoneNumber());
            intent.putExtra("Description", person.getDescription());
            intent.putExtra("Address", person.getAddress());
            intent.putExtra("Id", person.getContactId());
            intent.putExtra("Position", Integer.toString(position));
            startActivityForResult(intent, 1);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        if(intent != null){
            new GetAllContacts().execute();
        }
    }
    class GetAllContacts extends AsyncTask<Void, Void, String> {
        boolean isSuccess = true;
        Persons persons = new Persons();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            myProgress.setVisibility(View.VISIBLE);
            viewEmpty.setVisibility(View.INVISIBLE);
            my_list.setEnabled(false);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            adapter.clearAllContacts();
            for (Person person : persons.contacts) {
                adapter.addPerson(person);
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            String res = "";
            try {
                persons = HttpProvider.getInstance().getAllContacts(token);
                if (persons.contacts != null) {
                    publishProgress();
                } else {
                    isSuccess = false;
                }
            } catch (IOException e) {
                res = "Connection error! Check your internet!";
                isSuccess = false;
            } catch (Exception e) {
                e.printStackTrace();
                res = e.getMessage();
                isSuccess = false;
            }
            return res;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(ContactList.this, s, Toast.LENGTH_SHORT).show();
            if (isSuccess) {
                myProgress.setVisibility(View.INVISIBLE);
                my_list.setEnabled(true);
            } else {
                myProgress.setVisibility(View.INVISIBLE);
                viewEmpty.setVisibility(View.VISIBLE);
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        addItem = menu.findItem(R.id.add_item);
        logoutItem = menu.findItem(R.id.logout_item);
        downloadItem = menu.findItem(R.id.download_item);
        deleteItem = menu.findItem(R.id.delete_item);
        deleteItem.setVisible(true);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //LOGOUT
        if (item.getItemId() == R.id.logout_item) {
            deleteToken();
            setResult(RESULT_OK);
            finish();

            //ADD CONTACT
        } else if (item.getItemId() == R.id.add_item) {
            Intent intent = new Intent(this, AddContact.class);
            startActivity(intent);

            //DOWNLOAD Activity
        } else if (item.getItemId() == R.id.download_item) {
            Intent intent = new Intent(this, Download.class);
            startActivity(intent);

            //Delete contact list
        } else if (item.getItemId() == R.id.delete_item) {
            new ClearContactList().execute();
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //update existing contact
        if (resultCode == RESULT_OK && requestCode == 1) {//ContactViewAct
            String nameNew = data.getStringExtra("Name");
            String emailNew = data.getStringExtra("Email");
            String phoneNew = data.getStringExtra("Phone");
            String descriptionNew = data.getStringExtra("Description");
            String addressNew = data.getStringExtra("Address");
            String pos = data.getStringExtra("Position");
            int position = Integer.parseInt(pos);
            Person person = (Person) adapter.getItem(position);
            adapter.updatePerson(person, nameNew, emailNew, phoneNew, descriptionNew, addressNew);
        }
        // delete contact
        else if (resultCode == 2 && requestCode == 1) {//ContactViewAct
            String pos = data.getStringExtra("Position");
            int position = Integer.parseInt(pos);
            Person person = (Person) adapter.getItem(position);
            adapter.removePerson(person);
            if (adapter.getCount() == 0) {
                myProgress.setVisibility(View.GONE);
                viewEmpty.setVisibility(View.VISIBLE);
            }
        }
    }


    class ClearContactList extends AsyncTask<Void,Void,String> {
        boolean isSuccess = true;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            myProgress.setVisibility(View.VISIBLE);
            viewEmpty.setVisibility(View.INVISIBLE);
            my_list.setEnabled(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            String res;
            try {
                res = HttpProvider.getInstance().clearContactList(token);
            }catch (IOException e) {
                res = "Connection error! Check your internet!";
                isSuccess = false;
            } catch (Exception e) {
                e.printStackTrace();
                res = e.getMessage();
                isSuccess = false;
            }
            return res;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(ContactList.this, s, Toast.LENGTH_SHORT).show();
            if (isSuccess) {
                adapter.clearAllContacts();
                new GetAllContacts().execute();
            }
        }
    }




    private void deleteToken() {
        SharedPreferences sharedPreferences = getSharedPreferences(AuthToken.TOKEN_STORAGE, MODE_PRIVATE);
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.remove(AuthToken.TOKEN);
        ed.commit();
    }
}
