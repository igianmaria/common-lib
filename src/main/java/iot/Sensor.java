package iot;

import com.fasterxml.jackson.annotation.JsonIgnore;
import iot.messagging.Message;
import lombok.Data;

import java.util.List;
import java.util.Timer;

@Data
public abstract class Sensor {

    public String version = "0.0"; // va in fase di generazione del jar..NON SPOSTARE IN BASSO
    public SensorEventListener listener;

    public Timer samplingTimer;
    @JsonIgnore
    public long samplingInterval = 10;

    public Timer sendTimer;
    public long sendInterval = 10;

    public Timer readTimer;
    @JsonIgnore
    public double readInterval = 1;

    @JsonIgnore
    public double currentRMS = 0.0;

    public String updateURL = "";

    public abstract void initialize();
    public abstract void turnOn();
    public abstract void turnOff();
    public abstract void evaluateHealthCondition(List<Message> snapshot);
}

