package com.s23010372.agrizone;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class SensorActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor tempSensor, humiditySensor, pressureSensor, lightSensor;
    private TextView tvTemperature, tvHumidity, tvSensorStatus;
    private Button btnRefreshSensors;

    // Store latest sensor values
    private Float latestTemperature = null;
    private Float latestHumidity = null;
    private Float latestPressure = null;
    private Float latestLight = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        initializeViews();
        setupSensors();
        setupClickListeners();
        loadInitialSensorData();
    }

    private void initializeViews() {
        tvTemperature = findViewById(R.id.tvTemperature);
        tvHumidity = findViewById(R.id.tvHumidity);
        tvSensorStatus = findViewById(R.id.tvSensorStatus);
        btnRefreshSensors = findViewById(R.id.btnRefreshSensors);
    }

    private void setupSensors() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // Get available sensors
        tempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        humiditySensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    }

    private void setupClickListeners() {
        if (btnRefreshSensors != null) {
            btnRefreshSensors.setOnClickListener(v -> {
                Toast.makeText(this, "Refreshing sensor data...", Toast.LENGTH_SHORT).show();
                refreshSensorData();
            });
        }
    }

    private void loadInitialSensorData() {
        updateSensorStatus();
        if (tempSensor == null && humiditySensor == null) {
            // If no real sensors available, show simulated data
            showSimulatedData();
        }
    }

    private void updateSensorStatus() {
        StringBuilder status = new StringBuilder();

        if (tempSensor != null) {
            status.append("🌡️ Temperature: Available\n");
        } else {
            status.append("🌡️ Temperature: Not Available\n");
        }

        if (humiditySensor != null) {
            status.append("💧 Humidity: Available\n");
        } else {
            status.append("💧 Humidity: Not Available\n");
        }

        if (pressureSensor != null) {
            status.append("📊 Pressure: Available\n");
        } else {
            status.append("📊 Pressure: Not Available\n");
        }

        if (lightSensor != null) {
            status.append("💡 Light: Available");
        } else {
            status.append("💡 Light: Not Available");
        }

        if (tvSensorStatus != null) {
            tvSensorStatus.setText(status.toString());
        }
    }

    private void refreshSensorData() {
        // Re-register sensor listeners to get fresh data
        onPause();
        onResume();

        // If no real sensors available after 2 seconds, show simulated data
        new android.os.Handler().postDelayed(() -> {
            if (latestTemperature == null && latestHumidity == null) {
                showSimulatedData();
                Toast.makeText(this, "No environmental sensors detected. Showing simulated data.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Real sensor data updated successfully", Toast.LENGTH_SHORT).show();
            }
        }, 2000);
    }

    private void showSimulatedData() {
        // Generate realistic sensor data for demonstration
        double temperature = 25.0 + (Math.random() * 10.0); // 25-35°C
        int humidity = 50 + (int)(Math.random() * 30); // 50-80%

        tvTemperature.setText(String.format("%.1f°C (Simulated)", temperature));
        tvHumidity.setText(humidity + "% (Simulated)");
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Register listeners for available sensors
        if (tempSensor != null) {
            sensorManager.registerListener(this, tempSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (humiditySensor != null) {
            sensorManager.registerListener(this, humiditySensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (pressureSensor != null) {
            sensorManager.registerListener(this, pressureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (lightSensor != null) {
            sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

        // Set timeout for sensor data
        new android.os.Handler().postDelayed(() -> {
            if (latestTemperature == null && latestHumidity == null) {
                showSimulatedData();
            }
        }, 3000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                latestTemperature = event.values[0];
                tvTemperature.setText(String.format("%.1f°C", latestTemperature));
                break;

            case Sensor.TYPE_RELATIVE_HUMIDITY:
                latestHumidity = event.values[0];
                tvHumidity.setText(String.format("%.1f%%", latestHumidity));
                break;

            case Sensor.TYPE_PRESSURE:
                latestPressure = event.values[0];
                // Pressure can be displayed in additional card if layout supports it
                break;

            case Sensor.TYPE_LIGHT:
                latestLight = event.values[0];
                // Light can be displayed in additional card if layout supports it
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        String accuracyText;
        switch (accuracy) {
            case SensorManager.SENSOR_STATUS_ACCURACY_HIGH:
                accuracyText = "High Accuracy";
                break;
            case SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM:
                accuracyText = "Medium Accuracy";
                break;
            case SensorManager.SENSOR_STATUS_ACCURACY_LOW:
                accuracyText = "Low Accuracy";
                break;
            default:
                accuracyText = "Unreliable";
                break;
        }

        Toast.makeText(this, sensor.getName() + " - " + accuracyText, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }
}
