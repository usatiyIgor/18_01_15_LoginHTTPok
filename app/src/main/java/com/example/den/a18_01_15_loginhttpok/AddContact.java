package com.example.den.a18_01_15_loginhttpok;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class AddContact extends AppCompatActivity {
    private TextView inputAddress, inputDescription, inputEmail, inputFullName, inputPhoneNumber ;
    private MenuItem doneItem, addItem, logoutItem, downloadItem;
    private FrameLayout myProgress;
    private String address, description, email, name, phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        inputAddress = findViewById(R.id.input_address);
        inputEmail = findViewById(R.id.input_email);
        inputDescription = findViewById(R.id.input_description);
        inputFullName = findViewById(R.id.input_fullName);
        inputPhoneNumber = findViewById(R.id.input_phoneNumber);
        myProgress = findViewById(R.id.my_progress);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        doneItem = menu.findItem(R.id.done_item);
        addItem = menu.findItem(R.id.add_item);
        logoutItem = menu.findItem(R.id.logout_item);
        downloadItem = menu.findItem(R.id.download_item);

        doneItem.setVisible(true);
        addItem.setVisible(false);
        logoutItem.setVisible(false);
        downloadItem.setVisible(false);
        return true;
}
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.done_item) {
            address = inputAddress.getText().toString();
            description = inputDescription.getText().toString();
            email = inputEmail.getText().toString();
            name = inputFullName.getText().toString();
            phone = inputPhoneNumber.getText().toString();

            new AddContact1().execute(address, description, email, name, phone);
        }
        return super.onOptionsItemSelected(item);
    }

    class AddContact1 extends AsyncTask<String,Void,String> {
        boolean isSuccess = true;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            myProgress.setVisibility(View.VISIBLE);
            doneItem.setVisible(false);
            inputAddress.setEnabled(false);
            inputFullName.setEnabled(false);
            inputEmail.setEnabled(false);
            inputPhoneNumber.setEnabled(false);
            inputDescription.setEnabled(false);
        }

        @Override
        protected String doInBackground(String... strings) {
            String res;
            try {
                String token = getSharedPreferences(AuthToken.TOKEN_STORAGE, MODE_PRIVATE)
                        .getString(AuthToken.TOKEN, "");
                res = HttpProvider.getInstance().addContact(token, strings[0],
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
            Toast.makeText(AddContact.this, s, Toast.LENGTH_SHORT).show();
            if(isSuccess) {
                finish();
            }
        }
    }
    }