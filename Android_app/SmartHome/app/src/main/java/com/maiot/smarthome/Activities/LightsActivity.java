package com.maiot.smarthome.Activities;

import android.content.Intent;
import android.graphics.Color;
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


/**
 * Classe che rappresenta l'activity che gestisce le luci
 */
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

    // bottone per tornare alla main activity
    private Button bttLightsBack = null;

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


    /**
     * metodo che viene chiamato quando l'activity viene creata
     * @param savedInstanceState
     */
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

    /**
     * inizializzazione delle views e set dei click Listener
     */
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

        // settaggio click listeners pulsante "indietro"
        goBackButton();
    }

    /**
     * Metodo che setta il click listener del pulsante "manual"
     */
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
                    if(i < deviceList.getLightsList().size()) {
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
            // si controlla che ci siano lampade
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


    /**
     * Metodo che setta il click listener del pulsante "automatic"
     */
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

    /**
     * Metodo che setta il click listener del pulsante della lampada 1
     */
    private void lamp1StatusViews()
    {
        // associazione bottone di switch
        btt_Lamp_Switch1 = findViewById(R.id.btt_Lamp_Switch1);
        btt_Lamp_Switch1.setOnClickListener(view -> {

            // inversione dello stato della lampada 1
            toggleDeviceStatus(1);
        });
    }

    /**
     * Metodo che setta il click listener del pulsante della lampada 2
     */
    private void lamp2StatusViews()
    {
        // associazione bottone di switch
        btt_Lamp_Switch2 = findViewById(R.id.btt_Lamp_Switch2);
        btt_Lamp_Switch2.setOnClickListener(view -> {

            // inversione dello stato della lampada 2
            toggleDeviceStatus(2);
        });
    }

    /**
     * Metodo che setta il click listener del pulsante della lampada 3
     */
    private void lamp3StatusViews()
    {
        // associazione bottone di switch
        btt_Lamp_Switch3 = findViewById(R.id.btt_Lamp_Switch3);
        btt_Lamp_Switch3.setOnClickListener(view -> {

            // inversione dello stato della lampada 3
            toggleDeviceStatus(3);
        });
    }


    /**
     * Metodo che gessce la rapperesentazione grafica dello stato dei dispositivi
     * @param withHttpRequest
     */
    // il parametro serve a decidere o meno se fare una richiesta http che
    // scatenerà la onHttpRequestCompleted richiamando la setImageStatus
    // avendo però aggiornato lo stato dei vari dispositivi
    private void setImageStatus(boolean withHttpRequest)
    {
        if(!withHttpRequest) {
            if (deviceList.getLightsList() != null && deviceList.getLightsList().size() > 0) {
                // visibilità immagini stato e settaggio testo pulsanti
                for (int i = 0; i < deviceList.getLightsList().size(); i++) {
                    // la lampada è accesa
                    if (deviceList.getLightsList().get(i).getLocalStatus()) {
                        onImages[i].setVisibility(View.VISIBLE);
                        offImages[i].setVisibility(View.INVISIBLE);
                        buttons[i].setText("Turn Off");
                        buttons[i].setTextColor(Color.rgb(255,0,0));
                    }

                    // la lampada è spenta
                    else {
                        onImages[i].setVisibility(View.INVISIBLE);
                        offImages[i].setVisibility(View.VISIBLE);
                        buttons[i].setText("Turn On");
                        buttons[i].setTextColor(Color.rgb(0,255,0));
                    }
                }
            }
            else {
                Log.e(TAG, getResources().getString(R.string.NULL_OBJECT));
            }
        }
        else {
            if (deviceList.getLightsList() != null && deviceList.getLightsList().size() > 0) {
                for (int i = 0; i < deviceList.getLightsList().size(); i++) {
                    // richieste http /ping
                    deviceList.getLightsList().get(i).getHttpStatus();
                }
            }
            else{
                Log.e(TAG,"No available lamp");
            }
        }
    }


    /**
     * metodo che inverte lo stato della lampada indicizzata
     * @param index
     */
    private void toggleDeviceStatus(int index)
    {
        index -= 1;
        if (deviceList.getLightsList() != null && deviceList.getLightsList().size() > 0) {
            if (index < deviceList.getLightsList().size()) {
                // inversione dello stato della lampada indicizzata
                if (!deviceList.getLightsList().get(index).getLocalStatus()) {

                    deviceList.getLightsList().get(index).setStatus(true);
                }
                else {
                    deviceList.getLightsList().get(index).setStatus(false);
                }
                return;
            }
        }
        Log.e(TAG, getResources().getString(R.string.NULL_OBJECT));
    }

    /**
     * Metodo che controlla il numero di lampade disponibili
     * @param withToast
     * @return count
     */
    private int checkLightsCount(boolean withToast)
    {
        if(deviceList.getLightsList() != null) {
            int count = deviceList.getLightsList().size();
            if(withToast) {
                if (count == 0) {
                    // visualizzazione a schermo
                    Toast.makeText(LightsActivity.this, "there are no available lamps", Toast.LENGTH_SHORT).show();
                }
            }
            return count;
        }
        Log.e(TAG, getResources().getString(R.string.NULL_OBJECT));
        return 0;
    }

    /**
     * Metodo che setta il click listener del pulsante "back"
     */
    private void goBackButton(){
        bttLightsBack = findViewById(R.id.bttLightsBack);
        bttLightsBack.setOnClickListener(view -> {
            finish();
        });
    }

    /**
     * metodo da implementare per la gestione della risposta http
     * @param response
     */
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
