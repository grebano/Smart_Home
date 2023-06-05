package com.maiot.smarthome.Activities;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.maiot.smarthome.MainActivity;
import com.maiot.smarthome.R;

import Miscellaneous.Constants;
import Miscellaneous.IpAddr_MacAddr;

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
        setHintEditText();

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

        ip_lamp_1 = findViewById(R.id.ip_lamp_1);

        ip_lamp_2 = findViewById(R.id.ip_lamp_2);

        ip_lamp_3 = findViewById(R.id.ip_lamp_3);

        mac_lamp_1 = findViewById(R.id.mac_lamp_1);

        mac_lamp_2 = findViewById(R.id.mac_lamp_2);

        mac_lamp_3 = findViewById(R.id.mac_lamp_3);
    }

    /**
     * funzione che setta gli hint degli edit text controllando che i valori iniziali non siano nulli
     */
    private void setHintEditText() {
        // TODO: 10/05/2021 settare anche gli hint per tapparelle e wifi (magari usare dei loop)

        if (IpAddr_MacAddr.SHUTTER1_IP != null && !IpAddr_MacAddr.SHUTTER1_IP.equals("")) {
            ip_shutter_1.setHint(IpAddr_MacAddr.SHUTTER1_IP);
        }
        if (IpAddr_MacAddr.SHUTTER2_IP != null && !IpAddr_MacAddr.SHUTTER2_IP.equals("")) {
            ip_shutter_2.setHint(IpAddr_MacAddr.SHUTTER2_IP);
        }
        if (IpAddr_MacAddr.SHUTTER3_IP != null && !IpAddr_MacAddr.SHUTTER3_IP.equals("")) {
            ip_shutter_3.setHint(IpAddr_MacAddr.SHUTTER3_IP);
        }
        if (IpAddr_MacAddr.LAMP1_IP != null && !IpAddr_MacAddr.LAMP1_IP.equals("")) {
            ip_lamp_1.setHint(IpAddr_MacAddr.LAMP1_IP);
        }
        if (IpAddr_MacAddr.LAMP2_IP != null && !IpAddr_MacAddr.LAMP2_IP.equals("")) {
            ip_lamp_2.setHint(IpAddr_MacAddr.LAMP2_IP);
        }
        if (IpAddr_MacAddr.LAMP3_IP != null && !IpAddr_MacAddr.LAMP3_IP.equals("")) {
            ip_lamp_3.setHint(IpAddr_MacAddr.LAMP3_IP);
        }
        if (IpAddr_MacAddr.LAMP1_MAC != null && !IpAddr_MacAddr.LAMP1_MAC.equals("")) {
            mac_lamp_1.setHint(IpAddr_MacAddr.LAMP1_MAC);
        }
        if (IpAddr_MacAddr.LAMP2_MAC != null && !IpAddr_MacAddr.LAMP2_MAC.equals("")) {
            mac_lamp_2.setHint(IpAddr_MacAddr.LAMP2_MAC);
        }
        if (IpAddr_MacAddr.LAMP3_MAC != null && !IpAddr_MacAddr.LAMP3_MAC.equals("")) {
            mac_lamp_3.setHint(IpAddr_MacAddr.LAMP3_MAC);
        }
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

            ipShutter1EditText();
            ipShutter2EditText();
            ipShutter3EditText();

            ipLamp1EditText();
            ipLamp2EditText();
            ipLamp3EditText();

            macLamp1EditText();
            macLamp2EditText();
            macLamp3EditText();

            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
            finish();
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
     * funzione che gestisce la edittext per l'indirizzo ip della tapparella 1
     */
    private void ipShutter1EditText() {
        // controllo che il campo non sia vuoto
        if (ip_shutter_1.getText().toString().equals("")) {
            Log.i(TAG, "ipShutter1EditText: campo vuoto");
            return;
        }
        IpAddr_MacAddr.SHUTTER1_IP = ip_shutter_1.getText().toString();
        Log.i(TAG, "ipShutter1EditText: " + IpAddr_MacAddr.SHUTTER1_IP);
    }

    /**
     * funzione che gestisce la edittext per l'indirizzo ip della tapparella 2
     */
    private void ipShutter2EditText() {
        // controllo che il campo non sia vuoto
        if (ip_shutter_2.getText().toString().equals("")) {
            Log.i(TAG, "ipShutter2EditText: campo vuoto");
            return;
        }
        IpAddr_MacAddr.SHUTTER2_IP = ip_shutter_2.getText().toString();
        Log.i(TAG, "ipShutter2EditText: " + IpAddr_MacAddr.SHUTTER2_IP);
    }

    /**
     * funzione che gestisce la edittext per l'indirizzo ip della tapparella 3
     */
    private void ipShutter3EditText() {
        // controllo che il campo non sia vuoto
        if (ip_shutter_3.getText().toString().equals("")) {
            Log.i(TAG, "ipShutter3EditText: campo vuoto");
            return;
        }
        IpAddr_MacAddr.SHUTTER3_IP = ip_shutter_3.getText().toString();
        Log.i(TAG, "ipShutter3EditText: " + IpAddr_MacAddr.SHUTTER3_IP);
    }

    /**
     * funzione che gestisce la edittext per l'indirizzo ip della lampadina 1
     */
    private void ipLamp1EditText() {
        // controllo che il campo non sia vuoto
        if (ip_lamp_1.getText().toString().equals("")) {
            Log.i(TAG, "ipLamp1EditText: campo vuoto");
            return;
        }
        IpAddr_MacAddr.LAMP1_IP = ip_lamp_1.getText().toString();
        Log.i(TAG, "ipLamp1EditText: " + IpAddr_MacAddr.LAMP1_IP);
    }

    /**
     * funzione che gestisce la edittext per l'indirizzo ip della lampadina 2
     */
    private void ipLamp2EditText() {
        // controllo che il campo non sia vuoto
        if (ip_lamp_2.getText().toString().equals("")) {
            Log.i(TAG, "ipLamp2EditText: campo vuoto");
            return;
        }
        IpAddr_MacAddr.LAMP2_IP = ip_lamp_2.getText().toString();
        Log.i(TAG, "ipLamp2EditText: " + IpAddr_MacAddr.LAMP2_IP);
    }

    /**
     * funzione che gestisce la edittext per l'indirizzo ip della lampadina 3
     */
    private void ipLamp3EditText() {
        // controllo che il campo non sia vuoto
        if (ip_lamp_3.getText().toString().equals("")) {
            Log.i(TAG, "ipLamp3EditText: campo vuoto");
            return;
        }
        IpAddr_MacAddr.LAMP3_IP = ip_lamp_3.getText().toString();
        Log.i(TAG, "ipLamp3EditText: " + IpAddr_MacAddr.LAMP3_IP);
    }

    /**
     * funzione che gestisce la edittext per l'indirizzo mac della lampadina 1
     */
    private void macLamp1EditText() {
        // controllo che il campo non sia vuoto
        if (mac_lamp_1.getText().toString().equals("")) {
            Log.i(TAG, "macLamp1EditText: campo vuoto");
            return;
        }
        IpAddr_MacAddr.LAMP1_MAC = mac_lamp_1.getText().toString();
        Log.i(TAG, "macLamp1EditText: " + IpAddr_MacAddr.LAMP1_MAC);
    }

    /**
     * funzione che gestisce la edittext per l'indirizzo mac della lampadina 2
     */
    private void macLamp2EditText() {
        // controllo che il campo non sia vuoto
        if (mac_lamp_2.getText().toString().equals("")) {
            Log.i(TAG, "macLamp2EditText: campo vuoto");
            return;
        }
        IpAddr_MacAddr.LAMP2_MAC = mac_lamp_2.getText().toString();
        Log.i(TAG, "macLamp2EditText: " + IpAddr_MacAddr.LAMP2_MAC);
    }

    /**
     * funzione che gestisce la edittext per l'indirizzo mac della lampadina 3
     */
    private void macLamp3EditText() {
        // controllo che il campo non sia vuoto
        if (mac_lamp_3.getText().toString().equals("")) {
            Log.i(TAG, "macLamp3EditText: campo vuoto");
            return;
        }
        IpAddr_MacAddr.LAMP3_MAC = mac_lamp_3.getText().toString();
        Log.i(TAG, "macLamp3EditText: " + IpAddr_MacAddr.LAMP3_MAC);
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
    // TODO: 10/05/2017 da controllare
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
        mac_lamp_1.setFilters(filters);
        mac_lamp_2.setFilters(filters);
        mac_lamp_3.setFilters(filters);
    }

}
