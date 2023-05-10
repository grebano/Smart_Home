package com.maiot.smarthome.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.maiot.smarthome.R;

public class ShuttersActivity extends AppCompatActivity {

    // layout delle lampade
    private LinearLayout ll_Shutter_Switch1 = null;
    private LinearLayout ll_Shutter_Switch2 = null;

    // bottoni di scelta
    private Button bttShuttersModeManual = null;
    private Button bttShuttersModeAuto = null;

    // bottoni di comando
    private Button btt_Shutter_Switch1 = null;
    private Button btt_Shutter_Switch2 = null;

    // immagini dello stato delle lampade
    private ImageView imgShut1_open = null;
    private ImageView imgShut1_closed = null;
    private ImageView imgShut2_open = null;
    private ImageView imgShut2_closed = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shutters);
        Intent intent = getIntent();

        // inizializzazione delle views e set dei click Listener
        initViews();
    }

    private void initViews()
    {
        // associazione dei layout
        ll_Shutter_Switch1 = findViewById(R.id.ll_Shutter_Switch1);
        ll_Shutter_Switch2 = findViewById(R.id.ll_Shutter_Switch2);

        // associazione immagini
        imgShut1_closed = findViewById(R.id.imgShut1_closed);
        imgShut1_open = findViewById(R.id.imgShut1_open);
        imgShut2_closed = findViewById(R.id.imgShut2_closed);
        imgShut2_open = findViewById(R.id.imgShut2_open);

        // visibilità immagini stato
        imgShut1_closed.setVisibility(View.VISIBLE);
        imgShut2_closed.setVisibility(View.VISIBLE);
        imgShut1_open.setVisibility(View.INVISIBLE);
        imgShut2_open.setVisibility(View.INVISIBLE);

        // settaggio click listeners e view nelle due modalità
        automaticModeViews();
        manualModeViews();

        // settaggio click listeners e views delle singole lampade
        shutter1StatusViews();
        shutter2StatusViews();



    }
    private void manualModeViews(){
        // è stata scelta la modalità manuale
        bttShuttersModeManual = findViewById(R.id.bttShuttersModeManual);
        bttShuttersModeManual.setOnClickListener(view -> {
            // visibilità layout
            ll_Shutter_Switch1.setVisibility(View.VISIBLE);
            ll_Shutter_Switch2.setVisibility(View.VISIBLE);

            // visibilità switch
            btt_Shutter_Switch1.setVisibility(View.VISIBLE);
            btt_Shutter_Switch1.setClickable(true);
            btt_Shutter_Switch2.setVisibility(View.VISIBLE);
            btt_Shutter_Switch2.setClickable(true);

            // possibilità di click della modalità
            bttShuttersModeManual.setClickable(false);
            bttShuttersModeAuto.setClickable(true);
        });
    }

    private void automaticModeViews(){
        // è stata scelta la modalità automatica
        bttShuttersModeAuto = findViewById(R.id.bttShuttersModeAuto);
        bttShuttersModeAuto.setOnClickListener(view -> {
            // visibilità layout
            ll_Shutter_Switch1.setVisibility(View.VISIBLE);
            ll_Shutter_Switch2.setVisibility(View.VISIBLE);

            // visibilità switch
            btt_Shutter_Switch1.setVisibility(View.INVISIBLE);
            btt_Shutter_Switch1.setClickable(false);
            btt_Shutter_Switch2.setVisibility(View.INVISIBLE);
            btt_Shutter_Switch2.setClickable(false);

            // possibilità di click della modalità
            bttShuttersModeAuto.setClickable(false);
            bttShuttersModeManual.setClickable(true);
        });
    }

    private void shutter1StatusViews()
    {
        // associazione bottone di switch
        btt_Shutter_Switch1 = findViewById(R.id.btt_Shutter_Switch1);
        btt_Shutter_Switch1.setOnClickListener(view -> {

            // inversione dello stato della lampada 1
            if(imgShut1_closed.getVisibility() == View.VISIBLE)
            {
                imgShut1_closed.setVisibility(View.INVISIBLE);
                imgShut1_open.setVisibility(View.VISIBLE);
                btt_Shutter_Switch1.setText("Turn Off");
            }
            else if(imgShut1_open.getVisibility() == View.VISIBLE)
            {
                imgShut1_open.setVisibility(View.INVISIBLE);
                imgShut1_closed.setVisibility(View.VISIBLE);
                btt_Shutter_Switch1.setText("Turn On ");
            }
        });
    }

    private void shutter2StatusViews() {
        // associazione bottone di switch
        btt_Shutter_Switch2 = findViewById(R.id.btt_Shutter_Switch2);
        btt_Shutter_Switch2.setOnClickListener(view -> {

            // inversione dello stato della lampada 2
            if (imgShut2_closed.getVisibility() == View.VISIBLE) {
                imgShut2_closed.setVisibility(View.INVISIBLE);
                imgShut2_open.setVisibility(View.VISIBLE);
                btt_Shutter_Switch2.setText("Turn Off");
            } else if (imgShut2_open.getVisibility() == View.VISIBLE) {
                imgShut2_open.setVisibility(View.INVISIBLE);
                imgShut2_closed.setVisibility(View.VISIBLE);
                btt_Shutter_Switch2.setText("Turn On ");
            }
        });
    }
}

