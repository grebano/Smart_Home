package com.maiot.smarthome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button bttLights = null;
    private Button bttShutters = null;
    private Button bttAutomations = null;

    private WifiManager wifiManager = null;

    private LocationManager locationManager = null;
    private int LOCATION_PERMISSION_REQUEST_CODE = 1000;

    private final int ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bttLights = findViewById(R.id.bttLights);
        button_start_lights_activity();

        bttShutters = findViewById(R.id.bttShutters);
        button_start_shutters_activity();

        bttAutomations = findViewById(R.id.bttAutomations);
        button_start_automations_activity();

        // gestione dei permessi riguardanti la posizione
        getLocationPermission();

        // se il wifi è disattivato lo attivo e lo notifico all'utente
        if(!wifiManager.isWifiEnabled()){
            Toast.makeText(getApplicationContext(), "WiFi is Not enabled.  Enabling...", Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);
        }
        // se la posizione è disattivata mostro il dialog per attivarla
        if(!locationManager.isLocationEnabled()){
            buildAlertMessageNoGps();
        }
    }

    private void button_start_lights_activity() {
        bttLights.setOnClickListener(view -> {
            Intent intent = new Intent(getString(R.string.LAUNCH_LIGHTS_ACTIVITY));
            intent.putExtra(getString(R.string.LABEL_MESSAGE_LIGHTS),"lamps");
            //la funzione sotto vuole un intent e un intero che è quello che viene restituito nella backpropagation
            //per capire quale activity è stata lanciata (è un id in pratica)
            startActivity(intent);
        });
    }

    private void button_start_shutters_activity() {
        bttShutters.setOnClickListener(view -> {
            Intent intent = new Intent(getString(R.string.LAUNCH_SHUTTERS_ACTIVITY));
            intent.putExtra(getString(R.string.LABEL_MESSAGE_SHUTTERS),"shutters");
            //la funzione sotto vuole un intent e un intero che è quello che viene restituito nella backpropagation
            //per capire quale activity è stata lanciata (è un id in pratica)
            startActivity(intent);
        });
    }

    private void button_start_automations_activity() {
        bttAutomations.setOnClickListener(view -> {
            Intent intent = new Intent(getString(R.string.LAUNCH_AUTOMATIONS_ACTIVITY));
            intent.putExtra(getString(R.string.LABEL_MESSAGE_AUTOMATIONS),"automations");
            //la funzione sotto vuole un intent e un intero che è quello che viene restituito nella backpropagation
            //per capire quale activity è stata lanciata (è un id in pratica)
            startActivity(intent);
        });
    }

    //-------------------------------PERMESSI-POSIZIONE------------------------------------------------------------

    private void getLocationPermission()
    {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        checkPermissions(""+ Manifest.permission.ACCESS_FINE_LOCATION, LOCATION_PERMISSION_REQUEST_CODE+0);
    }

    private void checkPermissions(String permission, int requestCode) {
        if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission))
                showExplanationAndRequestPermission("Permission Needed", "Rationale", permission, requestCode);
            else
                requestPermission(permission, requestCode);
        }
    }

    private void showExplanationAndRequestPermission(String title,
                                                     String message,
                                                     final String permission,
                                                     final int permissionRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestPermission(permission, permissionRequestCode);
                    }
                });
        builder.create().show();
    }

    private void requestPermission(String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{permissionName}, permissionRequestCode);
    }

    private boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}