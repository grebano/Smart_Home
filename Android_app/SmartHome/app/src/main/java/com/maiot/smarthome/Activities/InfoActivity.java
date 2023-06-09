package com.maiot.smarthome.Activities;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.maiot.smarthome.R;

public class InfoActivity extends AppCompatActivity{

    private Button bttInfoBack = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.informations);

        bttInfoBack = findViewById(R.id.bttInfoBack);
        bttInfoBack.setOnClickListener(v -> finish());
    }

}
