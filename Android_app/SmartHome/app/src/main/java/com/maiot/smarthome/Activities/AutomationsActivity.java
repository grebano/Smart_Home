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

public class AutomationsActivity extends AppCompatActivity {
    private final String TAG = "AutomationsActivity";

    // bottoni di comando
    private Button bttDayMode = null;
    private Button bttNightMode = null;
    private Button bttHomeGymMode = null;
    private Button bttVacationMode = null;

    private DeviceList deviceList = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.automations);

        deviceList = new DeviceList(null);

        // inizializzazione delle views e set dei click Listener
        initViews();
    }

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

    }

    private void dayMode() {
        bttDayMode.setOnClickListener(view -> {
            // visualizzazione a schermo
            Toast.makeText(AutomationsActivity.this, R.string.DAY_MODE_ON, Toast.LENGTH_SHORT).show();

            // si controlla che il servizio stia girando, nel caso lo si arresta
            if(ShuttersService.isRunning) {
                stopService(new Intent(this, ShuttersService.class));
            }
            else{
                Log.i(TAG,"ShuttersService is not running");
            }

            // apertura tapparelle e spegnimento luci
            if(deviceList.getShutterList() != null) {
                for (SmartDevice shutter : deviceList.getShutterList()) {
                    shutter.setStatus(true);
                }
            }
            else
            {
                Log.e(TAG, getResources().getString(R.string.NULL_OBJECT));
            }

            if(deviceList.getLightsList() != null) {
                for (SmartDevice light : deviceList.getLightsList()) {
                    light.setStatus(false);
                }
            }
            else
            {
                Log.e(TAG, getResources().getString(R.string.NULL_OBJECT));
            }
        });
    }

    private void nightMode() {
        bttNightMode.setOnClickListener(view -> {
            // visualizzazione a schermo
            Toast.makeText(AutomationsActivity.this, R.string.NIGHT_MODE_ON, Toast.LENGTH_SHORT).show();

            // si controlla che il servizio stia girando, nel caso lo si arresta
            if(ShuttersService.isRunning) {
                stopService(new Intent(this, ShuttersService.class));
            }
            else{
                Log.i(TAG,"ShuttersService is not running");
            }
            
            // chiusura tapparelle e spegnimento luci
            if(deviceList.getShutterList() != null) {
                for (SmartDevice shutter : deviceList.getShutterList()) {
                    shutter.setStatus(false);
                }
            }
            else
            {
                Log.e(TAG, getResources().getString(R.string.NULL_OBJECT));
            }

            if(deviceList.getLightsList() != null) {
                for (SmartDevice light : deviceList.getLightsList()) {
                    light.setStatus(false);
                }
            }
            else
            {
                Log.e(TAG, getResources().getString(R.string.NULL_OBJECT));
            }
        });
    }

    private void homeGymMode() {
        bttHomeGymMode.setOnClickListener(view -> {
            // visualizzazione a schermo
            Toast.makeText(AutomationsActivity.this, R.string.HOME_GYM_MODE_ON, Toast.LENGTH_SHORT).show();

            if(deviceList.getShutterList() != null) {
                // chiusura tapparelle e accensione luce sala pesi
                for (SmartDevice shutter : deviceList.getShutterList()) {
                    shutter.setStatus(false);
                }
            }
            else
            {
                Log.e(TAG, getResources().getString(R.string.NULL_OBJECT));
            }
            if(deviceList.getLightsList() != null) {
                for (int i = 0; i < deviceList.getLightsList().length; i++) {
                    // la lampada 3 sarà quella in sala pesi
                    if (i == 2)
                        deviceList.getLightsList()[i].setStatus(true);
                    else
                        deviceList.getLightsList()[i].setStatus(false);
                }
            }
            else
            {
                Log.e(TAG, getResources().getString(R.string.NULL_OBJECT));
            }
        });
    }

    private void vacationMode() {
        bttVacationMode.setOnClickListener(view -> {
            // visualizzazione a schermo
            Toast.makeText(AutomationsActivity.this, R.string.VACATION_MODE_ON, Toast.LENGTH_SHORT).show();

            // tapparelle che simulano la presenza di persone

            // si controlla che il servizio non stia già girando e lo si lancia
            if(!ShuttersService.isRunning) {
                startForegroundService(new Intent(this, ShuttersService.class));
            }
            else{
                Log.i(TAG,"ShuttersService is already running");
            }
            if(deviceList.getLightsList() != null) {
                for (SmartDevice light : deviceList.getLightsList()) {
                    light.setStatus(false);
                }
            }
            else
            {
                Log.e(TAG, getResources().getString(R.string.NULL_OBJECT));
            }
        });
    }
}

