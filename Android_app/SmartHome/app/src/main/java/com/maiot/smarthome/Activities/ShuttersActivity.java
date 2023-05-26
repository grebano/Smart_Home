package com.maiot.smarthome.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.maiot.smarthome.R;

import Devices.DeviceList;
import Devices.SmartDevice;
import Interfaces.HttpRequestCompleted;
import Services.ShuttersService;

public class ShuttersActivity extends AppCompatActivity implements HttpRequestCompleted{

    private final String TAG = "ShuttersActivity";

    // layout delle tapparelle
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

    // immagini dello stato delle tapparelle
    private ImageView imgShut1_open = null;
    private ImageView imgShut1_closed = null;
    private ImageView imgShut2_open = null;
    private ImageView imgShut2_closed = null;
    private ImageView imgShut3_open = null;
    private ImageView imgShut3_closed = null;

    // array di immagini da gestire
    private ImageView[] closedImages;
    private ImageView[] openImages;

    // array di pulsanti da gestire
    private Button[] buttons;

    // layout delle singole tapparelle
    private LinearLayout[] layouts;

    private DeviceList deviceList = null;
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shutters);

        // iscrizione all'evento scansione completata
        HttpRequestCompleted httpRequestCompleted = this;
        deviceList = new DeviceList(httpRequestCompleted);

        // inizializzazione delle views e set dei click Listener
        initViews();
        closedImages = new ImageView[] {imgShut1_closed, imgShut2_closed, imgShut3_closed};
        openImages = new ImageView[] {imgShut1_open, imgShut2_open, imgShut3_open};
        buttons = new Button[] {btt_Shutter_Switch1, btt_Shutter_Switch2, btt_Shutter_Switch3};
        layouts = new LinearLayout[] {ll_Shutter_Switch1, ll_Shutter_Switch2, ll_Shutter_Switch3};

        // inizializzazione immagini e pulsanti in base allo stato reale
        setImageStatus(true);
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

            int i = 0;
            // visibilità layout e pulsanti in base all'esistenza dei dispositivi

            for(LinearLayout linearLayout : layouts)
            {
                if(deviceList.getShutterList() != null) {
                    if (i < deviceList.getShutterList().length) {
                        linearLayout.setVisibility(View.VISIBLE);
                        buttons[i].setVisibility(View.VISIBLE);
                        buttons[i].setClickable(true);
                    } else {
                        linearLayout.setVisibility(View.INVISIBLE);
                        buttons[i].setVisibility(View.INVISIBLE);
                        buttons[i].setClickable(false);
                    }
                    i++;
                }
                else
                {
                    Log.e(TAG, String.valueOf(R.string.NULL_OBJECT));
                    break;
                }
            }

            checkShutterCount(true);

            // possibilità di click della modalità
            bttShuttersModeManual.setClickable(false);
            bttShuttersModeAuto.setClickable(true);

            // aggiornamento stato immagini e pulsanti
            setImageStatus(true);

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
            bttShuttersModeAuto.setClickable(false);
            bttShuttersModeManual.setClickable(true);

            if(checkShutterCount(true) > 0) {

                // visualizzazione a schermo
                Toast.makeText(ShuttersActivity.this, R.string.AUTOMATIC_MODE, Toast.LENGTH_SHORT).show();

                // si controlla che il servizio non stia già girando e lo si lancia
                if (!ShuttersService.isRunning) {
                    startForegroundService(new Intent(this, ShuttersService.class));
                } else {
                    Log.i(TAG, "ShuttersService is already running");
                }
            }
        });
    }

    private void shutter1StatusViews()
    {
        // associazione bottone di switch
        btt_Shutter_Switch1 = findViewById(R.id.btt_Shutter_Switch1);
        btt_Shutter_Switch1.setOnClickListener(view -> {

            // inversione dello stato della tapparella 1
            toggleDeviceStatus(1);
        });
    }

    private void shutter2StatusViews() {
        // associazione bottone di switch
        btt_Shutter_Switch2 = findViewById(R.id.btt_Shutter_Switch2);
        btt_Shutter_Switch2.setOnClickListener(view -> {

            // inversione dello stato della tapparella 2
            toggleDeviceStatus(2);
        });
    }

    private void shutter3StatusViews() {
        // associazione bottone di switch
        btt_Shutter_Switch3 = findViewById(R.id.btt_Shutter_Switch2);
        btt_Shutter_Switch3.setOnClickListener(view -> {

            // inversione dello stato della tapparella 3
            toggleDeviceStatus(3);
        });
    }

    // il parametro serve a decidere o meno se fare una richiesta http che
    // scatenerà la onHttpRequestCompleted richiamando la setImageStatus
    // avendo però aggiornato lo stato dei vari dispositivi
    private void setImageStatus(boolean withHttpRequest)
    {
        if(!withHttpRequest) {
            if (deviceList.getShutterList() != null) {
                // visibilità immagini stato e settaggio testo pulsanti
                for (int i = 0; i < deviceList.getShutterList().length; i++) {
                    // la tapparella è aperta
                    if (deviceList.getShutterList()[i].getLocalStatus()) {
                        openImages[i].setVisibility(View.VISIBLE);
                        closedImages[i].setVisibility(View.INVISIBLE);
                        buttons[i].setText("Close");
                    }

                    // la tapparella è chiusa
                    else {
                        openImages[i].setVisibility(View.INVISIBLE);
                        closedImages[i].setVisibility(View.VISIBLE);
                        buttons[i].setText("Open");
                    }
                }
            }
            else {
                Log.e(TAG, String.valueOf(R.string.NULL_OBJECT));
            }
        }
        else
        {
            if (deviceList.getShutterList() != null) {
                for (int i = 0; i < deviceList.getShutterList().length; i++) {
                    // richieste http /ping
                    deviceList.getShutterList()[i].getHttpStatus();
                }
            }
            else{
                Log.e(TAG,"No available shutter");
            }
        }
    }

    // si cambia lo stato della singola tapparella
    private void toggleDeviceStatus(int index)
    {
        index -= 1;
        if (deviceList.getShutterList() != null) {
            if (index < deviceList.getShutterList().length) {
                // inversione dello stato della tapparella indicizzata
                if (!deviceList.getShutterList()[index].getLocalStatus()) {

                    deviceList.getShutterList()[index].setStatus(true);
                }
                else {
                    deviceList.getShutterList()[index].setStatus(false);
                }
            }
            else {
                Log.e(TAG, "Trying to set a non existing device");
            }
        }
        else {
            Log.e(TAG, String.valueOf(R.string.NULL_OBJECT));
        }
    }

    private int checkShutterCount(boolean withToast)
    {
        if(deviceList.getShutterList() != null) {
            int count = deviceList.getShutterList().length;
            if(withToast) {
                if (count == 0) {
                    // visualizzazione a schermo
                    Toast.makeText(ShuttersActivity.this, "there are no shutters available", Toast.LENGTH_SHORT).show();
                }
            }
            return count;
        }
        else
        {
            Log.e(TAG, String.valueOf(R.string.NULL_OBJECT));
            return 0;
        }
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

