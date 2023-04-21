package com.maiot.smarthome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button bttLights = null;
    private final int ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bttLights= findViewById(R.id.bttLights);
        button_start_lights_activity();
    }

    private void button_start_lights_activity() {
        bttLights.setOnClickListener(view -> {
            Intent intent = new Intent(getString(R.string.LAUNCH_LIGHTS_ACTIVITY));
            intent.putExtra(getString(R.string.LABEL_MESSAGE_LIGHTS),"ciao");
            //la funzione sotto vuole un intent e un intero che è quello che viene restituito nella backpropagation
            //per capire quale activity è stata lanciata (è un id in pratica)
            startActivity(intent);
        });
    }
}