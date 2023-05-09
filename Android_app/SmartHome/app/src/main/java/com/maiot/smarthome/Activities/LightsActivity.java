package com.maiot.smarthome.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.maiot.smarthome.R;

public class LightsActivity extends AppCompatActivity {

    private Button bttLightsModeManual = null;
    private Button bttLightsModeAuto = null;
    private LinearLayout llSwitch1 = null;
    private LinearLayout llSwitch2 = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lights);
        Intent intent = getIntent();

        // inizializzazione delle views e set dei click Listener
        initViews();
    }

    private void initViews()
    {
        llSwitch1 = findViewById(R.id.llSwitch1);
        llSwitch2 = findViewById(R.id.llSwitch2);

        // è stata scelta la modalità manuale
        bttLightsModeManual = findViewById(R.id.bttLightsModeManual);
        bttLightsModeManual.setOnClickListener(view -> {
            llSwitch1.setVisibility(View.VISIBLE);
            llSwitch2.setVisibility(View.VISIBLE);
            bttLightsModeManual.setClickable(false);
            bttLightsModeAuto.setClickable(true);
        });

        // è stata scelta la modalità automatica
        bttLightsModeAuto = findViewById(R.id.bttLightsModeAuto);
        bttLightsModeAuto.setOnClickListener(view -> {
            llSwitch1.setVisibility(View.INVISIBLE);
            llSwitch2.setVisibility(View.INVISIBLE);
            bttLightsModeAuto.setClickable(false);
            bttLightsModeManual.setClickable(true);
        });
    }
}
