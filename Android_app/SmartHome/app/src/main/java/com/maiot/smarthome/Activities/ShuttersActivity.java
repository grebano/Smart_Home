package com.maiot.smarthome.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.maiot.smarthome.R;

import Devices.DeviceList;
import Interfaces.HttpRequestCompleted;
import Services.ShuttersService;

public class ShuttersActivity extends AppCompatActivity implements HttpRequestCompleted{

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

    private ImageView closedImages[];
    private ImageView openImages[];
    private Button buttons[];

    private  HttpRequestCompleted  httpRequestCompleted = null;
    private DeviceList deviceList = null;
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shutters);
        Intent intent = getIntent();

        httpRequestCompleted = this;
        deviceList = new DeviceList(httpRequestCompleted);

        // inizializzazione delle views e set dei click Listener
        initViews();
        closedImages = new ImageView[] {imgShut1_closed, imgShut2_closed, imgShut3_closed};
        openImages = new ImageView[] {imgShut1_open, imgShut2_open, imgShut3_open};
        buttons = new Button[] {btt_Shutter_Switch1, btt_Shutter_Switch2, btt_Shutter_Switch3};
        setImageStatus();
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

            setImageStatus();
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
            ll_Shutter_Switch1.setVisibility(View.INVISIBLE);
            ll_Shutter_Switch2.setVisibility(View.INVISIBLE);
            ll_Shutter_Switch3.setVisibility(View.INVISIBLE);

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
            if(deviceList.shutter1.getStatus() == false)
            {
                deviceList.shutter1.setStatus(true);
            }
            else if(deviceList.shutter1.getStatus() == true)
            {
                deviceList.shutter1.setStatus(false);
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

    private void setImageStatus()
    {
        // visibilità immagini stato
        for(int i = 0; i < deviceList.myList.length; i++)
        {
            if(deviceList.myList[i].getStatus() == true)
            {
                openImages[i].setVisibility(View.VISIBLE);
                closedImages[i].setVisibility(View.INVISIBLE);
                buttons[i].setText("Close");
            }
            else
            {
                openImages[i].setVisibility(View.INVISIBLE);
                closedImages[i].setVisibility(View.VISIBLE);
                buttons[i].setText("Open");
            }
        }
    }

    @Override
    public void onHttpRequestCompleted(String response) {
        setImageStatus();
    }
}

