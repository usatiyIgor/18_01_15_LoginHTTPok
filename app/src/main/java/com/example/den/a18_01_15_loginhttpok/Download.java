package com.example.den.a18_01_15_loginhttpok;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Random;

public class Download extends AppCompatActivity implements View.OnClickListener {
    TextView countView, downloadView, alldoneView;
    Button startBtn;
    ProgressBar progressCircle, progressHorisontal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        countView = findViewById(R.id.count_view);
        downloadView = findViewById(R.id.downloading_view);
        alldoneView = findViewById(R.id.allDone_view);
        startBtn = findViewById(R.id.start_btn);
        progressCircle = findViewById(R.id.progressCircle);
        progressHorisontal = findViewById(R.id.progressHorisontal);
        startBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.start_btn) {
            new Download1().execute();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    class Download1 extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            switch (values[0]) {
                case 1:
                    String count = "Count: " + String.valueOf(values[1]);
                    countView.setText(count);
                    startBtn.setEnabled(false);
                    progressCircle.setVisibility(View.VISIBLE);
                    progressHorisontal.setVisibility(View.VISIBLE);
                    progressHorisontal.setMax(100);
                    downloadView.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    String load = "Downloading: " + String.valueOf(values[1]) + "/" +
                            String.valueOf(values[2]);
                    downloadView.setText(load);
                    break;
                case 3:
                    progressHorisontal.setProgress(values[1]);
                    break;
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Random random = new Random();
                int randomNumber = random.nextInt(15) + 1;
                //15 is the maximum and the 1 is minimum
                publishProgress(1, randomNumber);

                for (int i = 0; i < randomNumber; i++) {
                    publishProgress(2, i + 1, randomNumber);
                    for (int j = 0; j < 100; j++) {
                        publishProgress(3, j + 1);
                        Thread.sleep(30);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressCircle.setVisibility(View.INVISIBLE);
            progressHorisontal.setVisibility(View.INVISIBLE);
            downloadView.setVisibility(View.INVISIBLE);
            startBtn.setEnabled(true);
            countView.setText("Count: ");
            alldoneView.setVisibility(View.VISIBLE);
        }
    }
}
