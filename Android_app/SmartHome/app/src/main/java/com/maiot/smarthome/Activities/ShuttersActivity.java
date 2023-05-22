package com.maiot.smarthome.Activities;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.maiot.smarthome.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import Services.ShuttersService;
import Devices.ShutterDevice;

public class ShuttersActivity extends AppCompatActivity {

    private final String TAG = "ShuttersActivity";

    // layout delle lampade
    private LinearLayout ll_Shutter_Switch1 = null;
    private LinearLayout ll_Shutter_Switch2 = null;
    private LinearLayout ll_Shutter_Switch3 = null;

    // bottoni di scelta
    private Button bttShuttersModeManual = null;
    private Button bttShuttersModeAuto = null;

    // bottoni di comando
    private Button btt_Shutter_Switch1 = null;
    private Button btt_Shutter_Switch2 = null;
    private Button btt_Shutter_Switch3 = null;

    // immagini dello stato delle lampade
    private ImageView imgShut1_open = null;
    private ImageView imgShut1_closed = null;
    private ImageView imgShut2_open = null;
    private ImageView imgShut2_closed = null;
    private ImageView imgShut3_open = null;
    private ImageView imgShut3_closed = null;
    private ShutterDevice shutter1 = new ShutterDevice(false,"192.168.1.8","");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shutters);
        Intent intent = getIntent();


        // inizializzazione delle views e set dei click Listener
        initViews();
    }

    private void initViews()
    {
        // associazione dei layout
        ll_Shutter_Switch1 = findViewById(R.id.ll_Shutter_Switch1);
        ll_Shutter_Switch2 = findViewById(R.id.ll_Shutter_Switch2);
        ll_Shutter_Switch3 = findViewById(R.id.ll_Shutter_Switch3);

        // associazione immagini
        imgShut1_closed = findViewById(R.id.imgShut1_closed);
        imgShut1_open = findViewById(R.id.imgShut1_open);
        imgShut2_closed = findViewById(R.id.imgShut2_closed);
        imgShut2_open = findViewById(R.id.imgShut2_open);
        imgShut3_closed = findViewById(R.id.imgShut3_closed);
        imgShut3_open = findViewById(R.id.imgShut3_open);

        // visibilità immagini stato
        imgShut1_closed.setVisibility(View.VISIBLE);
        imgShut2_closed.setVisibility(View.VISIBLE);
        imgShut3_closed.setVisibility(View.VISIBLE);
        imgShut1_open.setVisibility(View.INVISIBLE);
        imgShut2_open.setVisibility(View.INVISIBLE);
        imgShut3_open.setVisibility(View.INVISIBLE);

        // settaggio click listeners e view nelle due modalità
        automaticModeViews();
        manualModeViews();

        // settaggio click listeners e views delle singole tapparelle
        shutter1StatusViews();
        shutter2StatusViews();
        shutter3StatusViews();



    }
    private void manualModeViews(){
        // è stata scelta la modalità manuale
        bttShuttersModeManual = findViewById(R.id.bttShuttersModeManual);
        bttShuttersModeManual.setOnClickListener(view -> {
            // visibilità layout
            ll_Shutter_Switch1.setVisibility(View.VISIBLE);
            ll_Shutter_Switch2.setVisibility(View.VISIBLE);
            ll_Shutter_Switch3.setVisibility(View.VISIBLE);

            // visibilità switch
            btt_Shutter_Switch1.setVisibility(View.VISIBLE);
            btt_Shutter_Switch1.setClickable(true);
            btt_Shutter_Switch2.setVisibility(View.VISIBLE);
            btt_Shutter_Switch2.setClickable(true);
            btt_Shutter_Switch3.setVisibility(View.VISIBLE);
            btt_Shutter_Switch3.setClickable(true);

            // possibilità di click della modalità
            bttShuttersModeManual.setClickable(false);
            bttShuttersModeAuto.setClickable(true);

            // si controlla che il servizio stia girando, nel caso lo si arresta
            if(ShuttersService.isRunning) {
                stopService(new Intent(this, ShuttersService.class));
            }
            else{
                Log.i(TAG,"ShuttersService is not running");
            }
        });
    }

    private void automaticModeViews(){
        // è stata scelta la modalità automatica
        bttShuttersModeAuto = findViewById(R.id.bttShuttersModeAuto);
        bttShuttersModeAuto.setOnClickListener(view -> {
            // visibilità layout
            ll_Shutter_Switch1.setVisibility(View.VISIBLE);
            ll_Shutter_Switch2.setVisibility(View.VISIBLE);
            ll_Shutter_Switch3.setVisibility(View.VISIBLE);

            // visibilità switch
            btt_Shutter_Switch1.setVisibility(View.INVISIBLE);
            btt_Shutter_Switch1.setClickable(false);
            btt_Shutter_Switch2.setVisibility(View.INVISIBLE);
            btt_Shutter_Switch2.setClickable(false);
            btt_Shutter_Switch3.setVisibility(View.INVISIBLE);
            btt_Shutter_Switch3.setClickable(false);

            // possibilità di click della modalità
            bttShuttersModeAuto.setClickable(false);
            bttShuttersModeManual.setClickable(true);

            // si controlla che il servizio non stia già girando e lo si lancia
            if(!ShuttersService.isRunning) {
                startForegroundService(new Intent(this, ShuttersService.class));
            }
            else{
                Log.i(TAG,"ShuttersService is already running");
            }
        });
    }

    private void shutter1StatusViews()
    {
        // associazione bottone di switch
        btt_Shutter_Switch1 = findViewById(R.id.btt_Shutter_Switch1);
        btt_Shutter_Switch1.setOnClickListener(view -> {

            // inversione dello stato della tapparella 1
            if(imgShut1_closed.getVisibility() == View.VISIBLE)
            {
                shutter1.setShutterStatus(true);
                imgShut1_closed.setVisibility(View.INVISIBLE);
                imgShut1_open.setVisibility(View.VISIBLE);
                btt_Shutter_Switch1.setText("Close");
            }
            else if(imgShut1_open.getVisibility() == View.VISIBLE)
            {
                shutter1.setShutterStatus(false);
                imgShut1_open.setVisibility(View.INVISIBLE);
                imgShut1_closed.setVisibility(View.VISIBLE);
                btt_Shutter_Switch1.setText("Open ");
            }
        });
    }

    private void shutter2StatusViews() {
        // associazione bottone di switch
        btt_Shutter_Switch2 = findViewById(R.id.btt_Shutter_Switch2);
        btt_Shutter_Switch2.setOnClickListener(view -> {

            // inversione dello stato della tapparella 2
            if (imgShut2_closed.getVisibility() == View.VISIBLE) {
                imgShut2_closed.setVisibility(View.INVISIBLE);
                imgShut2_open.setVisibility(View.VISIBLE);
                btt_Shutter_Switch2.setText("Close");
            } else if (imgShut2_open.getVisibility() == View.VISIBLE) {
                imgShut2_open.setVisibility(View.INVISIBLE);
                imgShut2_closed.setVisibility(View.VISIBLE);
                btt_Shutter_Switch2.setText("Open ");
            }
        });
    }

    private void shutter3StatusViews() {
        // associazione bottone di switch
        btt_Shutter_Switch3 = findViewById(R.id.btt_Shutter_Switch3);
        btt_Shutter_Switch3.setOnClickListener(view -> {

            // inversione dello stato della tapparella 3
            if (imgShut3_closed.getVisibility() == View.VISIBLE) {
                imgShut3_closed.setVisibility(View.INVISIBLE);
                imgShut3_open.setVisibility(View.VISIBLE);
                btt_Shutter_Switch3.setText("Close");
            } else if (imgShut3_open.getVisibility() == View.VISIBLE) {
                imgShut3_open.setVisibility(View.INVISIBLE);
                imgShut3_closed.setVisibility(View.VISIBLE);
                btt_Shutter_Switch3.setText("Open ");
            }
        });
    }

    //--------------------------------HTTP----------------------------------------
/*
    private void setShutter(String baseUrl, boolean state, boolean actualState){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //doInBackground
                URL url = null;
                if(state != actualState) {
                    if (state) {
                        try {
                            url = new URL(baseUrl + "/on");
                        } catch (MalformedURLException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        try {
                            url = new URL(baseUrl + "/off");
                        } catch (MalformedURLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    HttpURLConnection urlConnection = null;
                    try {
                        urlConnection = (HttpURLConnection) url.openConnection();
                        try {
                            try {
                                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                                in.close();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        } finally {
                            urlConnection.disconnect();
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    urlConnection.disconnect();
                }
            }
        }).start();

    }

    private void setShutterStatus(String baseUrl, boolean state)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //doInBackground
                URL url = null;
                try {
                    url = new URL(baseUrl + "/ping");
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
                HttpURLConnection urlConnection = null;
                try {
                    urlConnection = (HttpURLConnection) url.openConnection();
                    try {
                        try {
                            BufferedInputStream in = new BufferedInputStream(urlConnection.getInputStream());

                            byte[] contents = new byte[1024];
                            int bytesRead = 0;
                            String strContents = "";
                            while((bytesRead = in.read(contents)) != -1) {
                                strContents += new String(contents, 0, bytesRead);
                            }
                            in.close();
                            if (strContents.equals("on"))
                            {
                                setShutter(baseUrl,state,true);
                            }
                            else if (strContents.equals("off"))
                            {
                                setShutter(baseUrl,state,false);
                            }
                            if(state)
                                Log.i(TAG,"is: " + strContents + " --> going on");
                            else
                                Log.i(TAG,"is: " + strContents + " --> going off");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    finally {
                        urlConnection.disconnect();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                urlConnection.disconnect();
            }
        }).start();
    }*/
}

