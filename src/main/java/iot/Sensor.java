package iot;

import iot.messagging.Message;

import java.util.List;
import java.util.Timer;

public interface Sensor {
    void initialize();
    void turnOn();
    void turnOff();
    void reset();
    void evaluateHealthCondition(List<Message> snapshot);
    void setReadingInterval(int readingInterval); // in secs
    void setSensorEventListener(SensorEventListener listener);
    void setAppliedMachine(String monitoredEquipment);
}

