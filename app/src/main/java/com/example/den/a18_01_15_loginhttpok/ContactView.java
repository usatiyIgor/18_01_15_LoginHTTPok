package com.example.den.a18_01_15_loginhttpok;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class ContactView extends AppCompatActivity {
    private TextView viewName, viewEmail, viewPhone, viewDescription, viewAddress;
    private EditText editName, editEmail, editPhone, editDescription, editAddress;
    private FrameLayout myProgress;
    private MenuItem editItem, deleteItem, doneItem, addItem, logoutItem, downloadItem;
    private String token, position, name, email, phone, description, address;
    private Long contactId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_view);
        viewName = findViewById(R.id.view_name);
        viewEmail = findViewById(R.id.view_email);
        viewPhone = findViewById(R.id.view_phone);
        viewDescription = findViewById(R.id.view_description);
        viewAddress = findViewById(R.id.view_address);
        editName = findViewById(R.id.edit_name);
        editEmail = findViewById(R.id.edit_email);
        editPhone = findViewById(R.id.edit_phone);
        editDescription = findViewById(R.id.edit_description);
        editAddress= findViewById(R.id.edit_address);
        myProgress = findViewById(R.id.my_progress);
        token = getSharedPreferences(AuthToken.TOKEN_STORAGE, MODE_PRIVATE)
                .getString(AuthToken.TOKEN, "");

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            //put values from intent into TextViews
            position = intent.getStringExtra("Position");
            contactId = intent.getLongExtra("Id",0);
            viewName.setText(intent.getStringExtra("Name"));
            viewEmail.setText(intent.getStringExtra("Email"));
            viewPhone.setText(intent.getStringExtra("Phone"));
            viewDescription.setText(intent.getStringExtra("Description"));
            viewAddress.setText(intent.getStringExtra("Address"));
    }
}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        //close not needed items
        addItem = menu.findItem(R.id.add_item);
        logoutItem = menu.findItem(R.id.logout_item);
        downloadItem = menu.findItem(R.id.download_item);
        addItem.setVisible(false);
        logoutItem.setVisible(false);
        downloadItem.setVisible(false);

        //open items: edit and delete
        editItem = menu.findItem(R.id.edit_item);
        editItem.setVisible(true);
        deleteItem = menu.findItem(R.id.delete_item);
        deleteItem.setVisible(true);
        doneItem = menu.findItem(R.id.done_item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // EDDING ITEM
        if (item.getItemId() == R.id.edit_item) {
            //close all TextViews and edit item
            viewName.setVisibility(View.INVISIBLE);
            viewEmail.setVisibility(View.INVISIBLE);
            viewPhone.setVisibility(View.INVISIBLE);
            viewDescription.setVisibility(View.INVISIBLE);
            viewAddress.setVisibility(View.INVISIBLE);
            editItem.setVisible(false);
            deleteItem.setVisible(false);

            //open all EditTexts and done item
            editName.setVisibility(View.VISIBLE);
            editEmail.setVisibility(View.VISIBLE);
            editPhone.setVisibility(View.VISIBLE);
            editDescription.setVisibility(View.VISIBLE);
            editAddress.setVisibility(View.VISIBLE);
            doneItem.setVisible(true);

            //put texts from View to Edit
            editName.setText(viewName.getText());
            editEmail.setText(viewEmail.getText());
            editPhone.setText(viewPhone.getText());
            editDescription.setText(viewDescription.getText());
            editAddress.setText(viewAddress.getText());

            //  DONE ITEM
        } else if (item.getItemId() == R.id.done_item) {
            name = editName.getText().toString();
            email = editEmail.getText().toString();
            phone = editPhone.getText().toString();
            description = editDescription.getText().toString();
            address = editAddress.getText().toString();
            new ContactView1().execute(address, description, email, name, phone);
            // DELETE ITEM
        } else if (item.getItemId() == R.id.delete_item) {
            new DeleteContact().execute();
        }
        return super.onOptionsItemSelected(item);
    }
    class ContactView1 extends AsyncTask<String, Void, String> {
        String addressView;
        String descriptionView;
        String emailView;
        String nameView;
        String phoneView;
        boolean isSuccess = true;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            myProgress.setVisibility(View.VISIBLE);
            doneItem.setVisible(false);
            deleteItem.setVisible(false);
            editItem.setVisible(false);
            editName.setEnabled(false);
            editEmail.setEnabled(false);
            editPhone.setEnabled(false);
            editDescription.setEnabled(false);
        }

        @Override
        protected String doInBackground(String... strings) {
            addressView = strings[0];
            descriptionView = strings[1];
            emailView = strings[2];
            nameView = strings[3];
            phoneView = strings[4];
            String res;
            try {
                res = HttpProvider.getInstance().updateContact(contactId, token, strings[0],
                        strings[1],strings[2],strings[3],strings[4]);
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
            Toast.makeText(ContactView.this, s, Toast.LENGTH_SHORT).show();
            if(isSuccess) {
                Intent intent = new Intent();
                intent.putExtra("Name", nameView);
                intent.putExtra("Email", emailView);
                intent.putExtra("Phone", phoneView);
                intent.putExtra("Description", descriptionView);
                intent.putExtra("Address", addressView);
                intent.putExtra("Position", position);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }


    class DeleteContact extends AsyncTask<Void,Void,String>{
        boolean isSuccess = true;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            myProgress.setVisibility(View.VISIBLE);
            doneItem.setVisible(false);
            deleteItem.setVisible(false);
            editItem.setVisible(false);
            editName.setEnabled(false);
            editEmail.setEnabled(false);
            editPhone.setEnabled(false);
            editDescription.setEnabled(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            String res;
            try {
                res = HttpProvider.getInstance().deleteContact(contactId, token);
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
            Toast.makeText(ContactView.this, s, Toast.LENGTH_SHORT).show();
            if(isSuccess) {
                Intent intent = new Intent();
                intent.putExtra("Position", position);
                setResult(2, intent);
                finish();
            }
        }
    }
}