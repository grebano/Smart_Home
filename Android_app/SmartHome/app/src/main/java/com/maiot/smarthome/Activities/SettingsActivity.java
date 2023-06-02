package com.maiot.smarthome.Activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.maiot.smarthome.MainActivity;
import com.maiot.smarthome.R;

import Miscellaneous.Constants;

/**
 * Classe che rappresenta l'activity che gestisce le impostazioni
 */
public class SettingsActivity extends AppCompatActivity {

    private final String TAG = "SettingsActivity";

    // bottoni
    private Button bttSettingsBack = null;
    private Button bttSaveSettings = null;

    // orari di apertura e chiusura tapparelle
    private EditText opening_time = null;
    private EditText closing_time = null;

    // soglie wifi
    private EditText high_Level_threshold = null;
    private EditText low_Level_threshold = null;

    // indirizzi ip e mac delle tapparelle
    private EditText ip_shutter_1 = null;
    private EditText ip_shutter_2 = null;
    private EditText ip_shutter_3 = null;
    private EditText mac_shutter_1 = null;
    private EditText mac_shutter_2 = null;
    private EditText mac_shutter_3 = null;

    // indirizzi ip e mac delle luci
    private EditText ip_lamp_1 = null;
    private EditText ip_lamp_2 = null;
    private EditText ip_lamp_3 = null;
    private EditText mac_lamp_1 = null;
    private EditText mac_lamp_2 = null;
    private EditText mac_lamp_3 = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        // inizializzazione delle views
        initViews();

        // gestione del click sul bottone per tornare alla main activity
        goBackButton();

        // gestione del click sul bottone per salvare le impostazioni
        saveSettingsButton();
    }

    /**
     * funzione che gestisce l'init delle views
     */
    private void initViews() {
        opening_time = findViewById(R.id.opening_time);
        closing_time = findViewById(R.id.closing_time);
        high_Level_threshold = findViewById(R.id.high_Level_threshold);
        low_Level_threshold = findViewById(R.id.low_Level_threshold);
        ip_shutter_1 = findViewById(R.id.ip_shutter_1);
        ip_shutter_2 = findViewById(R.id.ip_shutter_2);
        ip_shutter_3 = findViewById(R.id.ip_shutter_3);
        mac_shutter_1 = findViewById(R.id.mac_shutter_1);
        mac_shutter_2 = findViewById(R.id.mac_shutter_2);
        mac_shutter_3 = findViewById(R.id.mac_shutter_3);
        ip_lamp_1 = findViewById(R.id.ip_lamp_1);
        ip_lamp_2 = findViewById(R.id.ip_lamp_2);
        ip_lamp_3 = findViewById(R.id.ip_lamp_3);
        mac_lamp_1 = findViewById(R.id.mac_lamp_1);
        mac_lamp_2 = findViewById(R.id.mac_lamp_2);
        mac_lamp_3 = findViewById(R.id.mac_lamp_3);
    }

    /**
     * Metodo che gestisce il click sul bottone per tornare alla main activity
     */
    private void goBackButton(){
        bttSettingsBack = findViewById(R.id.bttSettingsBack);
        bttSettingsBack.setOnClickListener(view -> {
            finish();
        });
    }

    /**
     * funzione che gestisce il click sul bottone per salvare le impostazioni
     */
    private void saveSettingsButton(){
        bttSaveSettings = findViewById(R.id.bttSaveSettings);
        bttSaveSettings.setOnClickListener(view -> {
            openingTimeEditText();
            closingTimeEditText();
                });
    }



    /**
     * funzione che gestisce la edittext per l'orario di apertura delle tapparelle
     */
    private void openingTimeEditText() {
        // controllo che l'orario inserito sia valido
        if (Integer.parseInt(opening_time.getText().toString()) < 0 || Integer.parseInt(opening_time.getText().toString()) > 24) {
            Log.i(TAG, "openingTimeEditText: orario inserito non valido");
            return;
        }
        Constants.MORNING_BEGINNING_TIME = Integer.parseInt(opening_time.getText().toString());
        Log.i(TAG, "openingTimeEditText: " + Constants.MORNING_BEGINNING_TIME);
    }

    /**
     * funzione che gestisce la edittext per l'orario di chiusura delle tapparelle
     */
    private void closingTimeEditText() {
        // controllo che l'orario inserito sia valido
        if (Integer.parseInt(closing_time.getText().toString()) < 0 || Integer.parseInt(closing_time.getText().toString()) > 24) {
            Log.i(TAG, "closingTimeEditText: orario inserito non valido");
            return;
        }
        Constants.NIGHT_BEGINNING_TIME = Integer.parseInt(closing_time.getText().toString());
        Log.i(TAG, "closingTimeEditText: " + Constants.NIGHT_BEGINNING_TIME);
    }

}
