package com.maiot.smarthome.Activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.maiot.smarthome.MainActivity;
import com.maiot.smarthome.R;

/**
 * Classe che rappresenta l'activity che gestisce le impostazioni
 */
public class SettingsActivity extends AppCompatActivity {

    private final String TAG = "SettingsActivity";

    private Button bttSettingsBack = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        goBackButton();
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
}
