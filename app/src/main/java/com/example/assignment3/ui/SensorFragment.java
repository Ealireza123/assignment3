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
import com.example.assignment3.model.DataType;
import com.example.assignment3.model.LocationItem;
import com.example.assignment3.model.Repository;
import com.example.assignment3.model.TimeBaseDataModel;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SensorFragment extends Fragment implements SensorEventListener {

    private static final int WAIT_TIME_MS = 500;
    private final double FILTER_FACTOR = 0.8;
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
    private TimeBaseDataModel lastSavedItem;
    private long lastSavedTimestamp = -1;

    public SensorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterListener();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sensor, container, false);
        initUI(view);
        fetchSensorFromSensorManager();

        startTimerButton.setOnClickListener(v -> {
            lastSavedItem = new TimeBaseDataModel();
            String currentDateAndTime = ZonedDateTime.now().format(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd | HH:mm:ss")
            );
            lastSavedItem.setTestTime(currentDateAndTime);

            startListeningToSensors();

            timerTextView.setVisibility(View.VISIBLE);
            startCountDown();
            startTimerButton.setVisibility(View.GONE);

        });


        return view;
    }

    private void unregisterListener() {
        sensorManager.unregisterListener(this);
    }

    private void startCountDown() {
        new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                timerTextView.setText("0:0" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                timerTextView.setText("");
                timerTextView.setVisibility(View.GONE);
                startTimerButton.setVisibility(View.VISIBLE);
                unregisterListener();
                saveData();
                lastSavedTimestamp = -1;
            }
        }.start();
    }

    private void saveData() {
        Repository.getInstance(requireContext()).saveLastSearched(lastSavedItem);
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

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor = sensorEvent.sensor;

        if (sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            Log.d("TAG4", "onSensorChanged: X: " + sensorEvent.values[0] + "  Y: " + sensorEvent.values[1] + "  Z: " + sensorEvent.values[2]);

            double difference = (sensorEvent.timestamp
                    - lastSavedTimestamp);
            // for record angle every 500 milliSeconds
            if (lastSavedTimestamp == -1 || difference == 0 || difference > WAIT_TIME_MS) {
                float xAxis = sensorEvent.values[0];
                float yAxis = sensorEvent.values[1];

                double rawAngle = Math.atan2(xAxis, yAxis) / (Math.PI / 180);
                double filteredAngle = filterAcceleratorAngel(rawAngle);

                if (filteredAngle != -1) {
                    lastSavedItem.addItemToList(
                            sensorEvent.timestamp,
                            filteredAngle,
                            DataType.FIRST_METHOD_ANGLE

                    );
                    lastSavedTimestamp = sensorEvent.timestamp;

                    firstMethodTextView
                            .setText(String.format("%.01f", filteredAngle) + "°");
                }
            }
            xValue.setText("xValue: \n" + String.format("%.06f", sensorEvent.values[0]));
            yValue.setText("yValue: \n" + String.format("%.06f", sensorEvent.values[1]));
            zValue.setText("zValue: \n" + String.format("%.06f", sensorEvent.values[2]));
        } else if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            double difference = (sensorEvent.timestamp
                    - lastSavedTimestamp);

            // for record angle every 500 milliSeconds
            if (lastSavedTimestamp == -1 || difference == 0 || difference > WAIT_TIME_MS) {
                float xAxis = sensorEvent.values[0];
                float yAxis = sensorEvent.values[1];
                double rawAngle = Math.atan2(xAxis, yAxis) / (Math.PI / 180);

                List<LocationItem> tmpList;
                try {
                    tmpList = lastSavedItem.getLocationItemList();
                } catch (Exception e) {
                    tmpList = null;
                }

                LocationItem lastSaved;
                if (tmpList != null && !tmpList.isEmpty()) {
                    lastSaved = tmpList
                            .get(tmpList
                                    .size() - 1);
                } else {
                    lastSaved = null;
                }

                if (lastSavedTimestamp == sensorEvent.timestamp) {

                    double filteredAngle;
                    if (lastSaved != null) {
                        filteredAngle =
                                filterGeoAndAcceleratorAngel(lastSaved.getFirstMethodAngle(), rawAngle);
                    } else {
                        filteredAngle =
                                filterGeoAndAcceleratorAngel(10, rawAngle);
                    }

                    lastSavedItem.addItemToList(
                            sensorEvent.timestamp,
                            filteredAngle,
                            DataType.SECOND_METHOD_ANGLE
                    );
                    lastSavedTimestamp = sensorEvent.timestamp;
                    secondMethodTextView
                            .setText(String.format("%.01f", filteredAngle) + "°");
                }

            }
            xGyroValue.setText("xGyroValue: \n" + String.format("%.06f", sensorEvent.values[0]));
            yGyroValue.setText("yGyroValue: \n" + String.format("%.06f", sensorEvent.values[1]));
            zGyroValue.setText("zGyroValue: \n" + String.format("%.06f", sensorEvent.values[2]));
        } else if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            xGeomagneticValue.setText("xMValue: \n" + String.format("%.06f", sensorEvent.values[0]));
            yGeomagneticValue.setText("yMValue: \n" + String.format("%.06f", sensorEvent.values[1]));
            zGeomagneticValue.setText("zMValue: \n" + String.format("%.06f", sensorEvent.values[2]));
        }
    }

    //𝑓𝑖𝑙𝑡𝑒𝑟𝑒𝑑𝑣𝑎𝑙𝑢𝑒(𝑛) = 𝐹 ∗ 𝑓𝑖𝑙𝑡𝑒𝑟𝑒𝑑𝑣𝑎𝑙𝑢𝑒(𝑛 − 1) + (1 − 𝐹) ∗ 𝑠𝑒𝑛𝑠𝑜𝑟𝑣𝑎𝑙𝑢𝑒(𝑛)
    private double filterAcceleratorAngel(double rawAngle) {
        if (rawAngle <= 90 && rawAngle >= 10) {
            if (rawAngle == 10) {
                return ((FILTER_FACTOR * 0) + ((1 - FILTER_FACTOR) * 10));
            }
            return ((FILTER_FACTOR * filterAcceleratorAngel(rawAngle - 1)) + ((1 - FILTER_FACTOR) * rawAngle));
        }
        return -1;
    }

    //𝐶𝑜𝑚𝑝𝑙𝑒𝑛𝑡𝑎𝑟𝑦𝑉𝑎𝑙𝑢𝑒(𝑛) = 𝐹 ∗ 𝑎𝑐𝑐𝐴𝑛𝑔𝑙𝑒(𝑛) + (1 − 𝐹) 𝑔𝑦𝑟𝑜𝐴𝑛𝑔𝑙𝑒(𝑛)
    private double filterGeoAndAcceleratorAngel(double rawAcceleratorAngel, double rawGyroscopeAngel) {
        if (rawAcceleratorAngel <= 90 && rawAcceleratorAngel >= 10) {
            return ((FILTER_FACTOR * rawAcceleratorAngel) + ((1 - FILTER_FACTOR) * rawGyroscopeAngel));
        }
        return -1;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d("TAG4", "onAccuracyChanged: accuracyChanged by " + accuracy);
    }
}