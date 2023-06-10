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
import Services.LightsService;
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
    private Button bttFullAutoMode = null;
    private Button bttWelcomeMode = null;

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
        bttFullAutoMode = findViewById(R.id.bttFullAutoMode);
        fullAutoMode();
        bttWelcomeMode = findViewById(R.id.bttWelcomeMode);
        welcomeMode();

        bttAutomationsBack = findViewById((R.id.bttAutomationsBack));
        goBackButton();


    }

    /**
     * Metodo che gestisce il click sul bottone per la "day mode"
     */
    private void dayMode() {
        bttDayMode.setOnClickListener(view -> {
            if(thereAreAvailableDevices()) {
                // visualizzazione a schermo
                Toast.makeText(AutomationsActivity.this, R.string.DAY_MODE_ON, Toast.LENGTH_SHORT).show();

                // si controlla che il servizio stia girando, nel caso lo si arresta
                stopAllServices();

                // apertura tapparelle e spegnimento luci
                openAllShutters();

                turnOffAllLights();
            }
        });
    }

    /**
     * Metodo che gestisce il click sul bottone per la "night mode"
     */
    private void nightMode() {
        bttNightMode.setOnClickListener(view -> {
            if(thereAreAvailableDevices()) {
                // visualizzazione a schermo
                Toast.makeText(AutomationsActivity.this, R.string.NIGHT_MODE_ON, Toast.LENGTH_SHORT).show();

                // si controlla che il servizio stia girando, nel caso lo si arresta
                stopAllServices();

                // si controlla che il servizio stia girando, in caso contrario lo si avvia
                startShuttersService();

                // spegnimento luci
                turnOffAllLights();
            }
        });
    }

    /**
     * Metodo che gestice il click sul bottone per la "home gym mode"
     */
    private void homeGymMode() {
        bttHomeGymMode.setOnClickListener(view -> {
            if(thereAreAvailableDevices()) {
                // visualizzazione a schermo
                Toast.makeText(AutomationsActivity.this, R.string.HOME_GYM_MODE_ON, Toast.LENGTH_SHORT).show();

                // si controlla che il servizio stia girando, nel caso lo si arresta
                stopAllServices();

                // chiusura tapparelle e accensione luce sala pesi
                closeAllShutters();

                turnOnGymLight();
            }
        });
    }

    /**
     * Metodo che gestisce il click sul bottone per la "vacation mode"
     */
    private void vacationMode() {
        bttVacationMode.setOnClickListener(view -> {
            if(thereAreAvailableDevices()) {
                // visualizzazione a schermo
                Toast.makeText(AutomationsActivity.this, R.string.VACATION_MODE_ON, Toast.LENGTH_SHORT).show();

                // si controlla che il servizio stia girando, nel caso lo si arresta
                stopAllServices();

                // chiusura tapparelle e spegnimento luci
                closeAllShutters();

                turnOffAllLights();
            }
        });
    }

    /**
     * Metodo che gestisce il click sul bottone per la "full auto mode"
     */
    private void fullAutoMode() {
        bttFullAutoMode.setOnClickListener(view -> {
            if (thereAreAvailableDevices()) {
                // visualizzazione a schermo
                Toast.makeText(AutomationsActivity.this, R.string.FULL_AUTO_MODE_ON, Toast.LENGTH_SHORT).show();

                // si controlla che il servizio stia girando, nel caso lo si arresta
                stopAllServices();

                // si controlla che il servizio stia girando, in caso contrario lo si avvia
                startAllServices();
            }
        });
    }

    /**
     * Metodo che gestisce il click sul bottone per la "welcome mode"
     */
    private void welcomeMode() {
        bttWelcomeMode.setOnClickListener(view -> {
            if (thereAreAvailableDevices()) {
                // visualizzazione a schermo
                Toast.makeText(AutomationsActivity.this, R.string.WELCOME_MODE_ON, Toast.LENGTH_SHORT).show();

                // si controlla che il servizio stia girando, nel caso lo si arresta
                stopAllServices();

                // apertura tapparelle e accensione luci
                openAllShutters();

                turnOnAllLights();
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
     * @return true se ci sono dispositivi disponibili, false altrimenti
     */
    private boolean thereAreAvailableDevices()
    {
        boolean shutters = deviceList.checkShutterCount(this, true) > 0;
        boolean lamps = deviceList.checkLightsCount(this, true) > 0;
        return (lamps || shutters);
    }

    /**
     * Metodo che arresta il servizio delle tapparelle se sta girando
     */
    private void stopShuttersService() {
        if (ShuttersService.isRunning) {
            stopService(new Intent(this, ShuttersService.class));
        } else {
            Log.i(TAG, "ShuttersService is not running");
        }
    }

    /**
     * Merodo che arresta il servizio delle lampade se sta girando
     */
    private void stopLightsService() {
        if (LightsService.isRunning) {
            stopService(new Intent(this, LightsService.class));
        } else {
            Log.i(TAG, "LightsService is not running");
        }
    }

    /**
     * Metodo che arresta tutti i servizi se stanno girando
     */
    private void stopAllServices() {
        stopShuttersService();
        stopLightsService();
    }

    /**
     * Metodo che avvia il servizio delle tapparelle se non sta girando
     */
    private void startShuttersService() {
        if (!ShuttersService.isRunning) {
            startForegroundService(new Intent(this, ShuttersService.class));
        } else {
            Log.i(TAG, "ShuttersService is now running");
        }
    }

    /**
     * Metodo che avvia il servizio delle lampade se non sta girando
     */
    private void startLightsService() {
        if (!LightsService.isRunning) {
            startForegroundService(new Intent(this, LightsService.class));
        } else {
            Log.i(TAG, "LightsService is now running");
        }
    }

    /**
     * Metodo che avvia tutti i servizi se non stanno girando
     */
    private void startAllServices() {
        startShuttersService();
        startLightsService();
    }

    /**
     * Metodo che apre tutte le tapparelle
     */
    private void openAllShutters() {
        if (deviceList.getShutterList() != null) {
            for (SmartDevice shutter : deviceList.getShutterList()) {
                shutter.setStatus(true);
            }
        } else {
            Log.e(TAG, getResources().getString(R.string.NULL_OBJECT));
        }
    }

    /**
     * Metodo che chiude tutte le tapparelle
     */
    private void closeAllShutters() {
        if (deviceList.getShutterList() != null) {
            for (SmartDevice shutter : deviceList.getShutterList()) {
                shutter.setStatus(false);
            }
        } else {
            Log.e(TAG, getResources().getString(R.string.NULL_OBJECT));
        }
    }

    /**
     * Metodo che accende tutte le luci
     */
    private void turnOnAllLights() {
        if (deviceList.getLightsList() != null) {
            for (SmartDevice light : deviceList.getLightsList()) {
                light.setStatus(true);
            }
        } else {
            Log.e(TAG, getResources().getString(R.string.NULL_OBJECT));
        }
    }


    /**
     * Metodo che spegne tutte le luci
     */
    private void turnOffAllLights() {
        if (deviceList.getLightsList() != null) {
            for (SmartDevice light : deviceList.getLightsList()) {
                light.setStatus(false);
            }
        } else {
            Log.e(TAG, getResources().getString(R.string.NULL_OBJECT));
        }
    }

    /**
     * Metodo che accende la luce della sala pesi
     */
    //TODO: da modificare la logica dell'id della lampada
    private void turnOnGymLight() {
        if (deviceList.getLightsList() != null) {
            for (int i = 0; i < deviceList.getLightsList().size(); i++) {
                // la lampada 3 sarà quella in sala pesi
                deviceList.getLightsList().get(i).setStatus(i == 2);
            }
        } else {
            Log.e(TAG, getResources().getString(R.string.NULL_OBJECT));
        }
    }


}

