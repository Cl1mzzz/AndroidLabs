package com.example.lab5;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private BubbleLevelView bubbleLevelView;
    private TextView textX, textY;
    private float[] gravity = new float[3];
    private float[] geomagnetic = new float[3];
    private float[] rotationMatrix = new float[9];
    private float[] orientation = new float[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bubbleLevelView = findViewById(R.id.bubbleView);
        textX = findViewById(R.id.textX);
        textY = findViewById(R.id.textY);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            double pitch = Math.toDegrees(Math.atan2(-x, Math.sqrt(y * y + z * z)));
            double roll = Math.toDegrees(Math.atan2(y, z));

            if (z < 0) {
                if (y > 0) {
                    roll = 180 - roll;
                } else {
                    roll = -180 - roll;
                }
            }

            bubbleLevelView.updateBubble((float)pitch, (float)roll);

            textX.setText(String.format("X: %.1f°", pitch));
            textY.setText(String.format("Y: %.1f°", roll));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        }
    }
}
