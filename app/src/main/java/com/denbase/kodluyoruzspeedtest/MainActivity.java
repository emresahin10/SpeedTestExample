package com.denbase.kodluyoruzspeedtest;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.anastr.speedviewlib.SpeedView;
import com.jignesh13.speedometer.SpeedoMeterView;

import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.SpeedTestTask;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;

public class MainActivity extends AppCompatActivity {

    SpeedoMeterView speedView;
    private Button button;
    private TextView textView;


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        speedView = findViewById(R.id.speedometerview);
        button = findViewById(R.id.button);
        textView = findViewById(R.id.textView);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpeedTestTask speedTestTask = new SpeedTestTask();
                speedTestTask.execute();

            }
        });

    }


        class SpeedTestTask extends AsyncTask<Void,Void,Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                SpeedTestSocket speedTestSocket = new SpeedTestSocket();

                speedTestSocket.addSpeedTestListener(new ISpeedTestListener() {
                    @Override
                    public void onCompletion(SpeedTestReport report) {

                        Log.v("speedtest", "[COMPLETED] rate in octet/s : " + report.getTransferRateOctet());
                        Log.v("speedtest", "[COMPLETED] rate in bit/s   : " + report.getTransferRateBit());


                    }

                    @Override
                    public void onProgress(float percent, SpeedTestReport report) {

                        int deger = (int) report.getTransferRateBit().intValue() / 100000;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                speedView.setSpeed(deger,true);
                                textView.setText("Hızınız \n"+(deger/10)+"  Mbit/s ");


                            }
                        });

                        Log.v("speedtest", "[PROGRESS] progress : " + percent + "%");
                        Log.v("speedtest", "[PROGRESS] rate in octet/s : " + report.getTransferRateOctet());
                        Log.v("speedtest", "[PROGRESS] rate in bit/s   : " + report.getTransferRateBit());
                    }

                    @Override
                    public void onError(SpeedTestError speedTestError, String errorMessage) {
                    }
                });

                speedTestSocket.startDownload("http://ipv4.ikoula.testdebit.info/1M.iso");
                return null;
            }
        }
}