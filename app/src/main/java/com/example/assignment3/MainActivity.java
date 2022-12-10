package com.example.assignment3;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initial fragment
        if (savedInstanceState == null) {
            loadFragment(new SensorFragment());
        }

        //initial ui
        initialUI();

    }

    private void initialUI() {
        TextView sensorTextButton = findViewById(R.id.sensor_text_button);
        TextView historyTextButton = findViewById(R.id.history_text_button);

        sensorTextButton.setOnClickListener(v ->
                loadFragment(new SensorFragment()));
        historyTextButton
                .setOnClickListener(v ->
                        loadFragment(new HistoryFragment()));
    }

    private int loadFragment(Fragment fragment) {
        return getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}