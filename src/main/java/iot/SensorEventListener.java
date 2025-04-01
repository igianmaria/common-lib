package iot;

import iot.messagging.Message;

public interface SensorEventListener {
    void onSensorDataReady(String sensorName, Message message);
    void onSensorAlarm(String sensorName, Message message);
}


