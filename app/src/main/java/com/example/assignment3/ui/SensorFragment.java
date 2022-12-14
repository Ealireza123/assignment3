package com.example.assignment3.ui;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.assignment3.R;
import com.example.assignment3.model.AnglePerMilliSecondModelItem;
import com.example.assignment3.model.Repository;

import java.util.ArrayList;
import java.util.List;

public class SensorFragment extends Fragment implements SensorEventListener {

    private static final int WAIT_TIME_MS = 500;
    private static final double FILTER_FACTOR = 0.8;

    private Button startTimerButton;
    private TextView timerTextView;
    private TextView xValue;
    private TextView yValue;
    private TextView zValue;
    private TextView xGyroValue;
    private TextView yGyroValue;
    private TextView zGyroValue;
    private TextView xGeomagneticValue;
    private TextView yGeomagneticValue;
    private TextView zGeomagneticValue;
    private TextView firstMethodTextView;
    private TextView secondMethodTextView;

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor gyroscope;
    private Sensor geomagnetic;
    private final List<AnglePerMilliSecondModelItem> firstMethodAngleList =
            new ArrayList<>();
    private final List<AnglePerMilliSecondModelItem> secondMethodAngleList =
            new ArrayList<>();
    private double lastFirstMethodSavedTimestamp = -1;
    private double lastSecondMethodSavedTimestamp = -1;

    public SensorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterListener();
    }

    private void unregisterListener() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sensor, container, false);
        initUI(view);
        fetchSensorFromSensorManager();

        startTimerButton.setOnClickListener(v -> {
            startListeningToSensors();

            timerTextView.setVisibility(View.VISIBLE);
            startTimerButton.setVisibility(View.GONE);
            startCountDown();

        });
        return view;
    }

    private void startCountDown() {
        new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                timerTextView.setText(String.format("0:0%d", millisUntilFinished / 1000));
            }

            public void onFinish() {
                timerTextView.setText("");
                timerTextView.setVisibility(View.GONE);
                startTimerButton.setVisibility(View.VISIBLE);
                unregisterListener();
                saveData();
            }
        }.start();
    }

    private void saveData() {
        Repository.getInstance(requireContext()).saveFile(firstMethodAngleList, "firstMethod");
        Repository.getInstance(requireContext()).saveFile(secondMethodAngleList, "secondMethod");
    }

    private void startListeningToSensors() {
        if (accelerometer != null) {
            sensorManager.registerListener(
                    this,
                    accelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL
            );
            Log.d("TAG4", "onCreate: Registered accelerometer listener");
        } else {
            xValue.setText("Accelerometer Not Supported");
            yValue.setText("Accelerometer Not Supported");
            zValue.setText("Accelerometer Not Supported");
        }

        if (gyroscope != null) {
            sensorManager.registerListener(
                    this,
                    gyroscope,
                    SensorManager.SENSOR_DELAY_NORMAL);
            Log.d("TAG4", "onCreate: Registered Gyro listener");
        } else {
            xGyroValue.setText("Gyro Not Supported");
            yGyroValue.setText("Gyro Not Supported");
            zGyroValue.setText("Gyro Not Supported");
        }

        if (geomagnetic != null) {
            sensorManager.registerListener(this, geomagnetic, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d("TAG4", "onCreate: Registered Geomagnetic listener");
        } else {
            xGeomagneticValue.setText("Geomagnetic Not Supported");
            yGeomagneticValue.setText("Geomagnetic Not Supported");
            zGeomagneticValue.setText("Geomagnetic Not Supported");
        }
    }

    private void fetchSensorFromSensorManager() {
        sensorManager = (SensorManager) requireContext()
                .getSystemService(Context.SENSOR_SERVICE);

        accelerometer = sensorManager
                .getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        gyroscope = sensorManager
                .getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        geomagnetic = sensorManager
                .getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    private void initUI(View view) {
        startTimerButton = view
                .findViewById(R.id.start_timer_button);
        timerTextView = view
                .findViewById(R.id.timer_text_view);

        firstMethodTextView = view
                .findViewById(R.id.first_method_text_view);
        secondMethodTextView = view
                .findViewById(R.id.second_method_text_view);

        xValue = view
                .findViewById(R.id.x_value);
        yValue = view
                .findViewById(R.id.y_value);
        zValue = view
                .findViewById(R.id.z_value);

        xGyroValue = view
                .findViewById(R.id.x_gyro_value);
        yGyroValue = view
                .findViewById(R.id.y_gyro_value);
        zGyroValue = view
                .findViewById(R.id.z_gyro_value);

        xGeomagneticValue = view
                .findViewById(R.id.x_geomagnetic_value);
        yGeomagneticValue = view
                .findViewById(R.id.y_geomagnetic_value);
        zGeomagneticValue = view
                .findViewById(R.id.z_geomagnetic_value);

    }

    private double calculateAccelerometerRawAngle(double xAxis, double yAxis, double zAxis) {
        return Math.atan2(yAxis, zAxis) / (Math.PI / 180);
//        return Math.atan(Math.sqrt((Math.pow(xAxis, 2)) + (Math.pow(yAxis, 2)) + (Math.pow(zAxis, 2))) / zAxis);
    }

    //𝑓𝑖𝑙𝑡𝑒𝑟𝑒𝑑𝑣𝑎𝑙𝑢𝑒(𝑛) = 𝐹 ∗ 𝑓𝑖𝑙𝑡𝑒𝑟𝑒𝑑𝑣𝑎𝑙𝑢𝑒(𝑛 − 1) + (1 − 𝐹) ∗ 𝑠𝑒𝑛𝑠𝑜𝑟𝑣𝑎𝑙𝑢𝑒(𝑛)
    private double filterAcceleratorAngel(double rawAngle) {
        if (rawAngle <= -90 && rawAngle >= -180) {
            rawAngle = Math.abs(rawAngle + 90);
            if (rawAngle == 10) {
                return ((FILTER_FACTOR * 0) + ((1 - FILTER_FACTOR) * 10));
            }
            return ((FILTER_FACTOR * filterAcceleratorAngel(rawAngle - 1)) + ((1 - FILTER_FACTOR) * rawAngle));
        }
        return -1;
    }

    //𝐶𝑜𝑚𝑝𝑙𝑒𝑛𝑡𝑎𝑟𝑦𝑉𝑎𝑙𝑢𝑒(𝑛) = 𝐹 ∗ 𝑎𝑐𝑐𝐴𝑛𝑔𝑙𝑒(𝑛) + (1 − 𝐹) 𝑔𝑦𝑟𝑜𝐴𝑛𝑔𝑙𝑒(𝑛)
    private double filterGeoAndAcceleratorAngel(double rawAcceleratorAngel, double rawGyroscopeAngel) {
        if (rawGyroscopeAngel <= -90 && rawGyroscopeAngel >= -180) {
            rawGyroscopeAngel = Math.abs(rawGyroscopeAngel + 90);
            return ((FILTER_FACTOR * rawAcceleratorAngel) + ((1 - FILTER_FACTOR) * rawGyroscopeAngel));
        }
        return -1;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor = sensorEvent.sensor;
        float rawXAxis = sensorEvent.values[0];
        float rawYAxis = sensorEvent.values[1];
        float rawZAxis = sensorEvent.values[2];

        if (sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            acceleratorSensorJob(sensorEvent, rawXAxis, rawYAxis, rawZAxis);
        } else if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            gyroscopeSensorJob(sensorEvent, rawXAxis, rawYAxis, rawZAxis);
        } else if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            xGeomagneticValue.setText("TYPE_MAGNETIC_FIELD: xValue: \n" + String.format("%.06f", rawXAxis));
            yGeomagneticValue.setText("TYPE_MAGNETIC_FIELD: yValue: \n" + String.format("%.06f", rawYAxis));
            zGeomagneticValue.setText("TYPE_MAGNETIC_FIELD: zValue: \n" + String.format("%.06f", rawZAxis));
        }
    }

    private void gyroscopeSensorJob(SensorEvent sensorEvent, float rawXAxis, float rawYAxis, float rawZAxis) {
        double difference = (sensorEvent.timestamp
                - lastSecondMethodSavedTimestamp);

        // for record angle every 500 milliSeconds
        if (lastSecondMethodSavedTimestamp == -1 || difference > WAIT_TIME_MS) {
            double yDegree = Math.toDegrees(rawYAxis);
            double unsignedYDegree = Math.abs(yDegree);

            Log.d("TAG5", "onSensorChanged: timestamp: " + sensorEvent.timestamp + " gyro angle:" + unsignedYDegree);
            double filteredAngle;
            AnglePerMilliSecondModelItem lastSaved = null;
            if (!firstMethodAngleList.isEmpty()) {
                lastSaved =
                        firstMethodAngleList.get(firstMethodAngleList.size() - 1);
            }

            if (lastSaved != null) {
                filteredAngle =
                        filterGeoAndAcceleratorAngel(lastSaved.getAngle(), unsignedYDegree);
            } else {
                filteredAngle =
                        filterGeoAndAcceleratorAngel(10, unsignedYDegree);
            }

            secondMethodAngleList.add(new AnglePerMilliSecondModelItem(sensorEvent.timestamp, filteredAngle));
            lastSecondMethodSavedTimestamp = sensorEvent.timestamp;
            secondMethodTextView
                    .setText(String.format("%.01f", filteredAngle) + "°");

        }
        xGyroValue.setText("xGyroValue: \n" + String.format("%.06f", rawXAxis));
        yGyroValue.setText("yGyroValue: \n" + String.format("%.06f", rawYAxis));
        zGyroValue.setText("zGyroValue: \n" + String.format("%.06f", rawZAxis));
    }

    private void acceleratorSensorJob(SensorEvent sensorEvent, float rawXAxis, float rawYAxis, float rawZAxis) {
        Log.d("TAG4", "onSensorChanged: X: " + sensorEvent.values[0] + "  Y: " + sensorEvent.values[1] + "  Z: " + sensorEvent.values[2]);

        double difference = (sensorEvent.timestamp
                - lastFirstMethodSavedTimestamp);

        // for record angle every 500 milliSeconds
        if (lastFirstMethodSavedTimestamp == -1 || difference > WAIT_TIME_MS) {
            double rawAngle = calculateAccelerometerRawAngle(rawXAxis, rawYAxis, rawZAxis);
            double filteredAngle = filterAcceleratorAngel(rawAngle);

            if (filteredAngle != -1) {
                firstMethodAngleList.add(
                        new AnglePerMilliSecondModelItem(
                                sensorEvent.timestamp,
                                filteredAngle));
                lastFirstMethodSavedTimestamp = sensorEvent.timestamp;

                firstMethodTextView
                        .setText(String.format("%.01f", filteredAngle) + "°");
            } else {
                Log.d("TAG6", "acceleratorSensorJob: filter is outOf bound " + rawAngle);
            }
        }
        xValue.setText("xValue: \n" + String.format("%.06f", rawXAxis));
        yValue.setText("yValue: \n" + String.format("%.06f", rawYAxis));
        zValue.setText("zValue: \n" + String.format("%.06f", rawZAxis));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d("TAG4", "onAccuracyChanged: accuracyChanged by " + accuracy);
    }
}