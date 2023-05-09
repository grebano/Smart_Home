package com.maiot.smarthome.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.maiot.smarthome.R;

public class LightsActivity extends AppCompatActivity {

    // layout delle lampade
    private LinearLayout llSwitch1 = null;
    private LinearLayout llSwitch2 = null;

    // bottoni di scelta
    private Button bttLightsModeManual = null;
    private Button bttLightsModeAuto = null;

    // bottoni di comando
    private Button bttSwitch1 = null;
    private Button bttSwitch2 = null;

    // immagini dello stato delle lampade
    private ImageView imgLamp1_On = null;
    private ImageView imgLamp1_Off = null;
    private ImageView imgLamp2_On = null;
    private ImageView imgLamp2_Off = null;
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
        // associazione dei layout
        llSwitch1 = findViewById(R.id.llSwitch1);
        llSwitch2 = findViewById(R.id.llSwitch2);

        // associazione immagini
        imgLamp1_Off = findViewById(R.id.imgLamp1_Off);
        imgLamp1_On = findViewById(R.id.imgLamp1_On);
        imgLamp2_Off = findViewById(R.id.imgLamp2_Off);
        imgLamp2_On = findViewById(R.id.imgLamp2_On);

        // settaggio click listeners e view nelle due modalità
        automaticModeViews();
        manualModeViews();

        // settaggio click listeners e views delle singole lampade
        lamp1StatusViews();
        lamp2StatusViews();



    }
    private void manualModeViews(){
        // è stata scelta la modalità manuale
        bttLightsModeManual = findViewById(R.id.bttLightsModeManual);

        // visibilità immagini stato
        imgLamp1_Off.setVisibility(View.VISIBLE);
        imgLamp2_Off.setVisibility(View.VISIBLE);
        imgLamp1_On.setVisibility(View.INVISIBLE);
        imgLamp2_On.setVisibility(View.INVISIBLE);

        bttLightsModeManual.setOnClickListener(view -> {
            // visibilità layout
            llSwitch1.setVisibility(View.VISIBLE);
            llSwitch2.setVisibility(View.VISIBLE);

            // visibilità switch
            bttSwitch1.setVisibility(View.VISIBLE);
            bttSwitch1.setClickable(true);
            bttSwitch2.setVisibility(View.VISIBLE);
            bttSwitch2.setClickable(true);

            // possibilità di click della modalità
            bttLightsModeManual.setClickable(false);
            bttLightsModeAuto.setClickable(true);
        });
    }

    private void automaticModeViews(){
        // è stata scelta la modalità automatica
        bttLightsModeAuto = findViewById(R.id.bttLightsModeAuto);

        // visibilità immagini stato
        imgLamp1_Off.setVisibility(View.VISIBLE);
        imgLamp2_Off.setVisibility(View.VISIBLE);
        imgLamp1_On.setVisibility(View.INVISIBLE);
        imgLamp2_On.setVisibility(View.INVISIBLE);

        bttLightsModeAuto.setOnClickListener(view -> {
            // visibilità layout
            llSwitch1.setVisibility(View.VISIBLE);
            llSwitch2.setVisibility(View.VISIBLE);

            // visibilità switch
            bttSwitch1.setVisibility(View.INVISIBLE);
            bttSwitch1.setClickable(false);
            bttSwitch2.setVisibility(View.INVISIBLE);
            bttSwitch2.setClickable(false);

            // possibilità di click della modalità
            bttLightsModeAuto.setClickable(false);
            bttLightsModeManual.setClickable(true);
        });
    }

    private void lamp1StatusViews()
    {
        // associazione bottone di switch
        bttSwitch1 = findViewById(R.id.bttSwitch1);
        bttSwitch1.setOnClickListener(view -> {

            // inversione dello stato della lampada 1
            if(imgLamp1_Off.getVisibility() == View.VISIBLE)
            {
                imgLamp1_Off.setVisibility(View.INVISIBLE);
                imgLamp1_On.setVisibility(View.VISIBLE);
                bttSwitch1.setText("Turn Off");
            }
            else if(imgLamp1_On.getVisibility() == View.VISIBLE)
            {
                imgLamp1_On.setVisibility(View.INVISIBLE);
                imgLamp1_Off.setVisibility(View.VISIBLE);
                bttSwitch1.setText("Turn On ");
            }
        });
    }

    private void lamp2StatusViews()
    {
        // associazione bottone di switch
        bttSwitch2 = findViewById(R.id.bttSwitch2);
        bttSwitch2.setOnClickListener(view -> {

            // inversione dello stato della lampada 2
            if(imgLamp2_Off.getVisibility() == View.VISIBLE)
            {
                imgLamp2_Off.setVisibility(View.INVISIBLE);
                imgLamp2_On.setVisibility(View.VISIBLE);
                bttSwitch2.setText("Turn Off");
            }
            else if(imgLamp2_On.getVisibility() == View.VISIBLE)
            {
                imgLamp2_On.setVisibility(View.INVISIBLE);
                imgLamp2_Off.setVisibility(View.VISIBLE);
                bttSwitch2.setText("Turn On ");
            }
        });
    }
}
