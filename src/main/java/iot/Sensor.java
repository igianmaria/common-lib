package iot;


public interface Sensor {
    void initialize();
    void turnOn();
    void turnOff();
    void reset();
    void setReadingInterval(int readingInterval); // in secs
    void setSensorEventListener(SensorEventListener listener);
}

