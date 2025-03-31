package iot;

public interface SensorEventListener {
    void onSensorDataReady(String sensorName, String data);
}


