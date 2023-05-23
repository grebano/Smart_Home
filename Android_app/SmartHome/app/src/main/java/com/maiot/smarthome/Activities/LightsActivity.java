package com.maiot.smarthome.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.maiot.smarthome.R;
import Services.LightsService;

import Devices.DeviceList;
import Interfaces.HttpRequestCompleted;
import Services.ShuttersService;

public class LightsActivity extends AppCompatActivity implements HttpRequestCompleted{
    private final String TAG = "LightsActivity";

    // layout delle lampade
    private LinearLayout ll_Lamp_Switch1 = null;
    private LinearLayout ll_Lamp_Switch2 = null;
    private LinearLayout ll_Lamp_Switch3 = null;

    // bottoni di scelta
    private Button bttLightsModeManual = null;
    private Button bttLightsModeAuto = null;

    // bottoni di comando
    private Button btt_Lamp_Switch1 = null;
    private Button btt_Lamp_Switch2 = null;
    private Button btt_Lamp_Switch3 = null;

    // immagini dello stato delle lampade
    private ImageView imgLamp1_On = null;
    private ImageView imgLamp1_Off = null;
    private ImageView imgLamp2_On = null;
    private ImageView imgLamp2_Off = null;
    private ImageView imgLamp3_On = null;
    private ImageView imgLamp3_Off = null;

    // array di immagini da gestire
    private ImageView[] onImages;
    private ImageView[] offImages;

    // array di pulsanti da gestire
    private Button[] buttons;

    private DeviceList deviceList = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lights);

        // iscrizione all'evento scansione completata
        HttpRequestCompleted httpRequestCompleted = this;
        deviceList = new DeviceList(httpRequestCompleted);

        // inizializzazione delle views e set dei click Listener
        initViews();
        onImages = new ImageView[] {imgLamp1_Off, imgLamp2_Off, imgLamp3_Off};
        offImages = new ImageView[] {imgLamp1_On, imgLamp2_On, imgLamp3_On};
        buttons = new Button[] {btt_Lamp_Switch1, btt_Lamp_Switch2, btt_Lamp_Switch3};

        // inizializzazione immagini e pulsanti in base allo stato reale
        setImageStatus();



        // inizializzazione delle views e set dei click Listener
        initViews();
    }

    private void initViews()
    {
        // associazione dei layout
        ll_Lamp_Switch1 = findViewById(R.id.ll_Lamp_Switch1);
        ll_Lamp_Switch2 = findViewById(R.id.ll_Lamp_Switch2);
        ll_Lamp_Switch3 = findViewById(R.id.ll_Lamp_Switch3);

        // associazione immagini
        imgLamp1_Off = findViewById(R.id.imgLamp1_Off);
        imgLamp1_On = findViewById(R.id.imgLamp1_On);
        imgLamp2_Off = findViewById(R.id.imgLamp2_Off);
        imgLamp2_On = findViewById(R.id.imgLamp2_On);
        imgLamp3_Off = findViewById(R.id.imgLamp3_Off);
        imgLamp3_On = findViewById(R.id.imgLamp3_On);

        // visibilità immagini stato
        imgLamp1_Off.setVisibility(View.VISIBLE);
        imgLamp2_Off.setVisibility(View.VISIBLE);
        imgLamp3_Off.setVisibility(View.VISIBLE);
        imgLamp1_On.setVisibility(View.INVISIBLE);
        imgLamp2_On.setVisibility(View.INVISIBLE);
        imgLamp3_On.setVisibility(View.INVISIBLE);

        // settaggio click listeners e view nelle due modalità
        automaticModeViews();
        manualModeViews();

        // settaggio click listeners e views delle singole lampade
        lamp1StatusViews();
        lamp2StatusViews();
        lamp3StatusViews();



    }
    private void manualModeViews(){
        // è stata scelta la modalità manuale
        bttLightsModeManual = findViewById(R.id.bttLightsModeManual);
        bttLightsModeManual.setOnClickListener(view -> {
            // visibilità layout
            ll_Lamp_Switch1.setVisibility(View.VISIBLE);
            ll_Lamp_Switch2.setVisibility(View.VISIBLE);
            ll_Lamp_Switch3.setVisibility(View.VISIBLE);

            // visibilità switch
            btt_Lamp_Switch1.setVisibility(View.VISIBLE);
            btt_Lamp_Switch1.setClickable(true);
            btt_Lamp_Switch2.setVisibility(View.VISIBLE);
            btt_Lamp_Switch2.setClickable(true);
            btt_Lamp_Switch3.setVisibility(View.VISIBLE);
            btt_Lamp_Switch3.setClickable(true);

            // possibilità di click della modalità
            bttLightsModeManual.setClickable(false);
            bttLightsModeAuto.setClickable(true);

            // si controlla che il servizio stia girando, nel caso lo si arresta
            if(LightsService.isRunning) {
                stopService(new Intent(this, LightsService.class));
            }
            else{
                Log.i(TAG,"LightsService is not running");
            }
        });
    }

    private void automaticModeViews(){
        // è stata scelta la modalità automatica
        bttLightsModeAuto = findViewById(R.id.bttLightsModeAuto);
        bttLightsModeAuto.setOnClickListener(view -> {
            // visibilità layout
            ll_Lamp_Switch1.setVisibility(View.VISIBLE);
            ll_Lamp_Switch2.setVisibility(View.VISIBLE);
            ll_Lamp_Switch3.setVisibility(View.VISIBLE);

            // visibilità switch
            btt_Lamp_Switch1.setVisibility(View.INVISIBLE);
            btt_Lamp_Switch1.setClickable(false);
            btt_Lamp_Switch2.setVisibility(View.INVISIBLE);
            btt_Lamp_Switch2.setClickable(false);
            btt_Lamp_Switch3.setVisibility(View.INVISIBLE);
            btt_Lamp_Switch3.setClickable(false);

            // possibilità di click della modalità
            bttLightsModeAuto.setClickable(false);
            bttLightsModeManual.setClickable(true);

            // visualizzazione a schermo
            Toast.makeText(LightsActivity.this, R.string.AUTOMATIC_MODE, Toast.LENGTH_SHORT).show();

            // si controlla che il servizio non stia già girando e lo si lancia
            if(!LightsService.isRunning) {
                startForegroundService(new Intent(this, LightsService.class));
            }
            else{
                Log.i(TAG,"ShuttersService is already running");
            }
        });
    }

    private void lamp1StatusViews()
    {
        // associazione bottone di switch
        btt_Lamp_Switch1 = findViewById(R.id.btt_Lamp_Switch1);
        btt_Lamp_Switch1.setOnClickListener(view -> {

            // inversione dello stato della lampada 1
            if(imgLamp1_Off.getVisibility() == View.VISIBLE)
            {
                imgLamp1_Off.setVisibility(View.INVISIBLE);
                imgLamp1_On.setVisibility(View.VISIBLE);
                btt_Lamp_Switch1.setText("Turn Off");
            }
            else if(imgLamp1_On.getVisibility() == View.VISIBLE)
            {
                imgLamp1_On.setVisibility(View.INVISIBLE);
                imgLamp1_Off.setVisibility(View.VISIBLE);
                btt_Lamp_Switch1.setText("Turn On ");
            }
        });
    }

    private void lamp2StatusViews()
    {
        // associazione bottone di switch
        btt_Lamp_Switch2 = findViewById(R.id.btt_Lamp_Switch2);
        btt_Lamp_Switch2.setOnClickListener(view -> {

            // inversione dello stato della lampada 2
            if(imgLamp2_Off.getVisibility() == View.VISIBLE)
            {
                imgLamp2_Off.setVisibility(View.INVISIBLE);
                imgLamp2_On.setVisibility(View.VISIBLE);
                btt_Lamp_Switch2.setText("Turn Off");
            }
            else if(imgLamp2_On.getVisibility() == View.VISIBLE)
            {
                imgLamp2_On.setVisibility(View.INVISIBLE);
                imgLamp2_Off.setVisibility(View.VISIBLE);
                btt_Lamp_Switch2.setText("Turn On ");
            }
        });
    }

    private void lamp3StatusViews()
    {
        // associazione bottone di switch
        btt_Lamp_Switch3 = findViewById(R.id.btt_Lamp_Switch3);
        btt_Lamp_Switch3.setOnClickListener(view -> {

            // inversione dello stato della lampada 3
            if(imgLamp3_Off.getVisibility() == View.VISIBLE)
            {
                imgLamp3_Off.setVisibility(View.INVISIBLE);
                imgLamp3_On.setVisibility(View.VISIBLE);
                btt_Lamp_Switch3.setText("Turn Off");
            }
            else if(imgLamp3_On.getVisibility() == View.VISIBLE)
            {
                imgLamp3_On.setVisibility(View.INVISIBLE);
                imgLamp3_Off.setVisibility(View.VISIBLE);
                btt_Lamp_Switch3.setText("Turn On ");
            }
        });
    }

    private void setImageStatus()
    {
        // visibilità immagini stato e settaggio testo pulsanti
        for(int i = 0; i < deviceList.getShutterList().length; i++)
        {
            // la tapparella è aperta
            if(deviceList.getShutterList()[i].getStatus())
            {
                onImages[i].setVisibility(View.VISIBLE);
                offImages[i].setVisibility(View.INVISIBLE);
                buttons[i].setText("Close");
            }

            // la tapparella è chiusa
            else
            {
                onImages[i].setVisibility(View.INVISIBLE);
                offImages[i].setVisibility(View.VISIBLE);
                buttons[i].setText("Open");
            }
        }
    }

    @Override
    public void onHttpRequestCompleted(String response)
    {
        // quando la richiesta è completata aggiorno le views
        setImageStatus();
    }
}
