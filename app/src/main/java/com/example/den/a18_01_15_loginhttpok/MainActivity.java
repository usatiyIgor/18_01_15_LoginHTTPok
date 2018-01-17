package com.example.den.a18_01_15_loginhttpok;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private EditText inputEmail, inputPassword;
    private Button loginBtn, regBtn;
    private ProgressBar myProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputEmail = findViewById(R.id.input_email);
        inputPassword = findViewById(R.id.input_password);
        loginBtn = findViewById(R.id.login_btn);
        regBtn = findViewById(R.id.reg_btn);
        myProgress = findViewById(R.id.progressBar);
        regBtn.setOnClickListener(v -> new RegTask().execute());
        loginBtn.setOnClickListener(v -> new LoginTask().execute());
    }

    class RegTask extends AsyncTask<Void, Void, String> {
        String email, password;
        boolean isSucccess = true;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            email = inputEmail.getText().toString();
            password = inputPassword.getText().toString();
            myProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            String res = "Registration OK!";
            try {
                String token = HttpProvider.getInstance().registration(email, password);
                getSharedPreferences(AuthToken.TOKEN_STORAGE,MODE_PRIVATE)
                        .edit()
                        .putString(AuthToken.TOKEN,token)
                        .commit();
                Log.d("MY_TAG", "doInBack" + token);
            } catch (IOException e) {
                res = "Connection error";
                isSucccess = false;
            } catch (Exception e) {
                e.printStackTrace();
                res = e.getMessage();
                isSucccess = false;
            }

            return res;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            myProgress.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
            if (isSucccess) {
                Intent intent = new Intent(MainActivity.this,ContactList.class);
                startActivityForResult(intent,1);
            }
        }
    }

    class LoginTask extends AsyncTask<Void, Void, String> {
        String email, password;
        boolean isSucccess = true;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            email = inputEmail.getText().toString();
            password = inputPassword.getText().toString();
            myProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            String res = "Login OK!";
            try {
                String token = HttpProvider.getInstance().login(email, password);
                getSharedPreferences(AuthToken.TOKEN_STORAGE,MODE_PRIVATE)
                        .edit()
                        .putString(AuthToken.TOKEN,token)
                        .commit();
                Log.d("MY_TAG", "doInBack" + token);
            } catch (IOException e) {
                res = "Connection error";
                isSucccess = false;
            } catch (Exception e) {
                e.printStackTrace();
                res = e.getMessage();
                isSucccess = false;
            }

            return res;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            myProgress.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
            if (isSucccess) {
                Intent intent = new Intent(MainActivity.this,ContactList.class);
                startActivityForResult(intent,1);
            }
        }
    }
}
