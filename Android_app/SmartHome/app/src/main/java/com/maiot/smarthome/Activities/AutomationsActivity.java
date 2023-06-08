package com.maiot.smarthome.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.maiot.smarthome.R;

import Devices.DeviceList;
import Devices.SmartDevice;
import Services.ShuttersService;


/**
 * Classe che rappresenta l'activity che gestisce le automazioni
 */
public class AutomationsActivity extends AppCompatActivity {
    private final String TAG = "AutomationsActivity";

    // bottoni di comando
    private Button bttDayMode = null;
    private Button bttNightMode = null;
    private Button bttHomeGymMode = null;
    private Button bttVacationMode = null;

    // bottone per tornare alla main activity
    private Button bttAutomationsBack = null;

    private DeviceList deviceList = null;


    /**
     * Metodo che viene chiamato quando l'activity viene creata
     * @param savedInstanceState stato dell'istanza
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.automations);

        deviceList = new DeviceList(null);

        // inizializzazione delle views e set dei click Listener
        initViews();
    }

    /**
     * inizializzazione delle views e set dei click Listener
     */
    private void initViews() {

        // collegamento degli id e click listeners
        bttDayMode = findViewById(R.id.bttDayMode);
        dayMode();
        bttNightMode = findViewById(R.id.bttNightMode);
        nightMode();
        bttHomeGymMode = findViewById(R.id.bttHomeGymMode);
        homeGymMode();
        bttVacationMode = findViewById(R.id.bttVacationMode);
        vacationMode();
        bttAutomationsBack = findViewById((R.id.bttAutomationsBack));
        goBackButton();

    }

    /**
     * Metodo che gestisce il click sul bottone per la "day mode"
     */
    private void dayMode() {
        bttDayMode.setOnClickListener(view -> {
            if(thereAreAvailableDevices(true)) {
                // visualizzazione a schermo
                Toast.makeText(AutomationsActivity.this, R.string.DAY_MODE_ON, Toast.LENGTH_SHORT).show();

                // si controlla che il servizio stia girando, nel caso lo si arresta
                if (ShuttersService.isRunning) {
                    stopService(new Intent(this, ShuttersService.class));
                } else {
                    Log.i(TAG, "ShuttersService is not running");
                }

                // apertura tapparelle e spegnimento luci
                if (deviceList.getShutterList() != null) {
                    for (SmartDevice shutter : deviceList.getShutterList()) {
                        shutter.setStatus(true);
                    }
                } else {
                    Log.e(TAG, getResources().getString(R.string.NULL_OBJECT));
                }

                if (deviceList.getLightsList() != null) {
                    for (SmartDevice light : deviceList.getLightsList()) {
                        light.setStatus(false);
                    }
                } else {
                    Log.e(TAG, getResources().getString(R.string.NULL_OBJECT));
                }
            }
        });
    }

    /**
     * Metodo che gestisce il click sul bottone per la "night mode"
     */
    private void nightMode() {
        bttNightMode.setOnClickListener(view -> {
            if(thereAreAvailableDevices(true)) {
                // visualizzazione a schermo
                Toast.makeText(AutomationsActivity.this, R.string.NIGHT_MODE_ON, Toast.LENGTH_SHORT).show();

                // si controlla che il servizio stia girando, in caso contrario lo si avvia
                if (ShuttersService.isRunning) {
                    Log.i(TAG, "ShuttersService is already running");
                } else {
                    startForegroundService(new Intent(this, ShuttersService.class));
                    Log.i(TAG, "ShuttersService is not running");
                }

                // spegnimento luci
                if (deviceList.getLightsList() != null) {
                    for (SmartDevice light : deviceList.getLightsList()) {
                        light.setStatus(false);
                    }
                } else {
                    Log.e(TAG, getResources().getString(R.string.NULL_OBJECT));
                }
            }
        });
    }

    /**
     * Metodo che gestice il click sul bottone per la "home gym mode"
     */
    private void homeGymMode() {
        bttHomeGymMode.setOnClickListener(view -> {
            if(thereAreAvailableDevices(true)) {
                // visualizzazione a schermo
                Toast.makeText(AutomationsActivity.this, R.string.HOME_GYM_MODE_ON, Toast.LENGTH_SHORT).show();

                if (deviceList.getShutterList() != null) {
                    // chiusura tapparelle e accensione luce sala pesi
                    for (SmartDevice shutter : deviceList.getShutterList()) {
                        shutter.setStatus(false);
                    }
                } else {
                    Log.e(TAG, getResources().getString(R.string.NULL_OBJECT));
                }
                if (deviceList.getLightsList() != null) {
                    for (int i = 0; i < deviceList.getLightsList().size(); i++) {
                        // la lampada 3 sarà quella in sala pesi
                        deviceList.getLightsList().get(i).setStatus(i == 2);
                    }
                } else {
                    Log.e(TAG, getResources().getString(R.string.NULL_OBJECT));
                }
            }
        });
    }

    /**
     * Metodo che gestisce il click sul bottone per la "vacation mode"
     */
    private void vacationMode() {
        bttVacationMode.setOnClickListener(view -> {
            if(thereAreAvailableDevices(true)) {
                // visualizzazione a schermo
                Toast.makeText(AutomationsActivity.this, R.string.VACATION_MODE_ON, Toast.LENGTH_SHORT).show();

                // chiusura tapparelle e spegnimento luci
                if (deviceList.getShutterList() != null) {
                    for (SmartDevice light : deviceList.getShutterList()) {
                        light.setStatus(false);
                    }
                } else {
                    Log.e(TAG, getResources().getString(R.string.NULL_OBJECT));
                }
                if (deviceList.getLightsList() != null) {
                    for (SmartDevice light : deviceList.getLightsList()) {
                        light.setStatus(false);
                    }
                } else {
                    Log.e(TAG, getResources().getString(R.string.NULL_OBJECT));
                }
            }
        });
    }

    /**
     * Metodo che gestisce il click sul bottone per tornare alla schermata precedente
     */
    private void goBackButton(){
        bttAutomationsBack.setOnClickListener(view -> finish());
    }


    /**
     * Metodo che controlla se ci sono dispositivi disponibili per le automazioni
     * @param withToast true se si vuole visualizzare un toast in caso non ci siano dispositivi disponibili
     * @return true se ci sono dispositivi disponibili, false altrimenti
     */
    private boolean thereAreAvailableDevices(boolean withToast)
    {

        boolean shutters = deviceList.checkShutterCount(this, true) > 0;
        boolean lamps = deviceList.checkLightsCount(this, true) > 0;

        if(withToast) {
            if((!lamps) && (!shutters)) {
               Toast.makeText(AutomationsActivity.this, "there are no available devices", Toast.LENGTH_SHORT).show();
            }
        }
        return (lamps || shutters);
    }
}

