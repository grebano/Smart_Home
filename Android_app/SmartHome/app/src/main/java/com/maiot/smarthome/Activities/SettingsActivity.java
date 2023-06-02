package com.maiot.smarthome.Activities;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
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

        // inizializzazione dei filtri per gli input
        ipEditTextFilter();
        macEditTextFilter();

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
            highLevelThresholdEditText();
            lowLevelThresholdEditText();
                });
    }



    /**
     * funzione che gestisce la edittext per l'orario di apertura delle tapparelle
     */
    private void openingTimeEditText() {
        // controllo che il campo non sia vuoto
        if (opening_time.getText().toString().equals("")) {
            Log.i(TAG, "openingTimeEditText: campo vuoto");
            return;
        }
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
        // controllo che il campo non sia vuoto
        if (closing_time.getText().toString().equals("")) {
            Log.i(TAG, "closingTimeEditText: campo vuoto");
            return;
        }
        // controllo che l'orario inserito sia valido
        if (Integer.parseInt(closing_time.getText().toString()) < 0 || Integer.parseInt(closing_time.getText().toString()) > 24) {
            Log.i(TAG, "closingTimeEditText: orario inserito non valido");
            return;
        }
        Constants.NIGHT_BEGINNING_TIME = Integer.parseInt(closing_time.getText().toString());
        Log.i(TAG, "closingTimeEditText: " + Constants.NIGHT_BEGINNING_TIME);
    }

    /**
     * funzione che gestisce la edittext per la soglia alta del wifi
     */
    private void highLevelThresholdEditText() {
        // controllo che la soglia inserita non sia vuota
        if (high_Level_threshold.getText().toString().equals("")) {
            Log.i(TAG, "highLevelThresholdEditText: campo vuoto");
            return;
        }
        // controllo che la soglia inserita sia valida
        if (Integer.parseInt(high_Level_threshold.getText().toString()) < 0 || Integer.parseInt(high_Level_threshold.getText().toString()) > 100) {
            Log.i(TAG, "highLevelThresholdEditText: soglia inserita non valida");
            return;
        }
        Constants.WIFI_NEAR_THRESHOLD = Integer.parseInt(high_Level_threshold.getText().toString());
        Log.i(TAG, "highLevelThresholdEditText: " + Constants.WIFI_NEAR_THRESHOLD);
    }

    /**
     * funzione che gestisce la edittext per la soglia bassa del wifi
     */
    private void lowLevelThresholdEditText() {
        // controllo che la soglia inserita non sia vuota
        if (low_Level_threshold.getText().toString().equals("")) {
            Log.i(TAG, "lowLevelThresholdEditText: campo vuoto");
            return;
        }
        // controllo che la soglia inserita sia valida
        if (Integer.parseInt(low_Level_threshold.getText().toString()) < 0 || Integer.parseInt(low_Level_threshold.getText().toString()) > 100) {
            Log.i(TAG, "lowLevelThresholdEditText: soglia inserita non valida");
            return;
        }
        Constants.WIFI_FAR_THRESHOLD = Integer.parseInt(low_Level_threshold.getText().toString());
        Log.i(TAG, "lowLevelThresholdEditText: " + Constants.WIFI_FAR_THRESHOLD);
    }

    /**
     * filtro per la edittext dell'indirizzo ip
     */
    private void ipEditTextFilter() {
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       android.text.Spanned dest, int dstart, int dend) {
                if (end > start) {
                    String destTxt = dest.toString();
                    String resultingTxt = destTxt.substring(0, dstart)
                            + source.subSequence(start, end)
                            + destTxt.substring(dend);
                    if (!resultingTxt
                            .matches("^\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3})?)?)?)?)?)?")) {
                        return "";
                    } else {
                        String[] splits = resultingTxt.split("\\.");
                        for (int i = 0; i < splits.length; i++) {
                            if (Integer.valueOf(splits[i]) > 255) {
                                return "";
                            }
                        }
                    }
                }
                return null;
            }

        };
        ip_shutter_1.setFilters(filters);
        ip_shutter_2.setFilters(filters);
        ip_shutter_3.setFilters(filters);
        ip_lamp_1.setFilters(filters);
        ip_lamp_2.setFilters(filters);
        ip_lamp_3.setFilters(filters);
    }

    /**
     * filtro per la edittext dell'indirizzo mac
     */
    private void macEditTextFilter() {
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       android.text.Spanned dest, int dstart, int dend) {
                if (end > start) {
                    String destTxt = dest.toString();
                    String resultingTxt = destTxt.substring(0, dstart)
                            + source.subSequence(start, end)
                            + destTxt.substring(dend);
                    if (!resultingTxt
                            .matches("^[\\dA-F]{2}(?:[\\-][\\dA-F]{2}){5}$")) {
                        return "";
                    }
                    else {
                        String[] splits = resultingTxt.split(":");
                        for (int i = 0; i < splits.length; i++) {
                            if (splits[i].length() != 2) {
                                return "";
                            }
                        }
                    }
                }
                return null;
            }

        };
        mac_shutter_1.setFilters(filters);
        mac_shutter_2.setFilters(filters);
        mac_shutter_3.setFilters(filters);
        mac_lamp_1.setFilters(filters);
        mac_lamp_2.setFilters(filters);
        mac_lamp_3.setFilters(filters);
    }
}
