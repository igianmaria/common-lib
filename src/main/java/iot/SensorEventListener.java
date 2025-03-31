package iot;

import iot.messagging.Message;

@FunctionalInterface
public interface SensorEventListener {
    void onSensorDataReady(String sensorName, Message message);
}


