package com.example.acccidentdemo;

import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MapActivity extends AppCompatActivity {

    private TextView tvMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);

        tvMap = findViewById(R.id.tv_text);

    }
}
