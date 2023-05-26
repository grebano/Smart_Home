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

    // layout delle singole lampade
    private LinearLayout[] layouts;

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
        offImages = new ImageView[] {imgLamp1_Off, imgLamp2_Off, imgLamp3_Off};
        onImages = new ImageView[] {imgLamp1_On, imgLamp2_On, imgLamp3_On};
        buttons = new Button[] {btt_Lamp_Switch1, btt_Lamp_Switch2, btt_Lamp_Switch3};
        layouts = new LinearLayout[] {ll_Lamp_Switch1, ll_Lamp_Switch2, ll_Lamp_Switch3};

        // inizializzazione immagini e pulsanti in base allo stato reale
        setImageStatus(true);
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

            int i = 0;
            // visibilità layout e pulsanti in base all'esistenza dei dispositivi

            for(LinearLayout linearLayout : layouts)
            {
                if(deviceList.getLightsList() != null)
                {
                    if(i < deviceList.getLightsList().length) {
                        linearLayout.setVisibility(View.VISIBLE);
                        buttons[i].setVisibility(View.VISIBLE);
                        buttons[i].setClickable(true);
                    }
                    else {
                        linearLayout.setVisibility(View.INVISIBLE);
                        buttons[i].setVisibility(View.INVISIBLE);
                        buttons[i].setClickable(false);
                    }
                    i++;
                }
                else
                {
                    Log.e(TAG, getResources().getString(R.string.NULL_OBJECT));
                    break;
                }
            }

            checkLightsCount(true);

            // possibilità di click della modalità
            bttLightsModeManual.setClickable(false);
            bttLightsModeAuto.setClickable(true);

            // aggiornamento stato immagini e pulsanti
            setImageStatus(true);

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
            // visibilità layout e switch
            for(LinearLayout linearLayout : layouts)
            {
                linearLayout.setVisibility(View.INVISIBLE);
            }
            for(Button button : buttons)
            {
                button.setClickable(false);
            }

            // possibilità di click della modalità
            bttLightsModeAuto.setClickable(false);
            bttLightsModeManual.setClickable(true);

            if(checkLightsCount(true) > 0) {

                // visualizzazione a schermo
                Toast.makeText(LightsActivity.this, R.string.AUTOMATIC_MODE, Toast.LENGTH_SHORT).show();

                // si controlla che il servizio non stia già girando e lo si lancia
                if (!LightsService.isRunning) {
                    startForegroundService(new Intent(this, LightsService.class));
                } else {
                    Log.i(TAG, "LightsService is already running");
                }
            }
        });
    }

    private void lamp1StatusViews()
    {
        // associazione bottone di switch
        btt_Lamp_Switch1 = findViewById(R.id.btt_Lamp_Switch1);
        btt_Lamp_Switch1.setOnClickListener(view -> {

            // inversione dello stato della lampada 1
            toggleDeviceStatus(1);
        });
    }

    private void lamp2StatusViews()
    {
        // associazione bottone di switch
        btt_Lamp_Switch2 = findViewById(R.id.btt_Lamp_Switch2);
        btt_Lamp_Switch2.setOnClickListener(view -> {

            // inversione dello stato della lampada 2
            toggleDeviceStatus(2);
        });
    }

    private void lamp3StatusViews()
    {
        // associazione bottone di switch
        btt_Lamp_Switch3 = findViewById(R.id.btt_Lamp_Switch3);
        btt_Lamp_Switch3.setOnClickListener(view -> {

            // inversione dello stato della lampada 3
            toggleDeviceStatus(3);
        });
    }

    // il parametro serve a decidere o meno se fare una richiesta http che
    // scatenerà la onHttpRequestCompleted richiamando la setImageStatus
    // avendo però aggiornato lo stato dei vari dispositivi
    private void setImageStatus(boolean withHttpRequest)
    {
        if(!withHttpRequest) {
            if (deviceList.getLightsList() != null) {
                // visibilità immagini stato e settaggio testo pulsanti
                for (int i = 0; i < deviceList.getLightsList().length; i++) {
                    // la lampada è accesa
                    if (deviceList.getLightsList()[i].getLocalStatus()) {
                        onImages[i].setVisibility(View.VISIBLE);
                        offImages[i].setVisibility(View.INVISIBLE);
                        buttons[i].setText("Turn Off");
                    }

                    // la lampada è spenta
                    else {
                        onImages[i].setVisibility(View.INVISIBLE);
                        offImages[i].setVisibility(View.VISIBLE);
                        buttons[i].setText("Turn On");
                    }
                }
            }
            else {
                Log.e(TAG, getResources().getString(R.string.NULL_OBJECT));
            }
        }
        else {
            if (deviceList.getLightsList() != null) {
                for (int i = 0; i < deviceList.getLightsList().length; i++) {
                    // richieste http /ping
                    deviceList.getLightsList()[i].getHttpStatus();
                }
            }
            else{
                Log.e(TAG,"No available lamp");
            }
        }
    }


    // si cambia lo stato della singola lampada
    private void toggleDeviceStatus(int index)
    {
        index -= 1;
        if (deviceList.getLightsList() != null) {
            if (index < deviceList.getLightsList().length) {
                // inversione dello stato della lampada indicizzata
                if (!deviceList.getLightsList()[index].getLocalStatus()) {

                    deviceList.getLightsList()[index].setStatus(true);
                }
                else {
                    deviceList.getLightsList()[index].setStatus(false);
                }
                return;
            }
        }
        Log.e(TAG, getResources().getString(R.string.NULL_OBJECT));
    }

    private int checkLightsCount(boolean withToast)
    {
        if(deviceList.getLightsList() != null) {
            int count = deviceList.getLightsList().length;
            if(withToast) {
                if (count == 0) {
                    // visualizzazione a schermo
                    Toast.makeText(LightsActivity.this, "there are no lamps available", Toast.LENGTH_SHORT).show();
                }
            }
            return count;
        }
        Log.e(TAG, getResources().getString(R.string.NULL_OBJECT));
        return 0;
    }

    @Override
    public void onHttpRequestCompleted(String response)
    {
        // quando la richiesta è completata aggiorno le views
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        setImageStatus(false);
                    }
                });
    }
}
