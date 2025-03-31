package iot;

import iot.messagging.Message;
import iot.messagging.readings.TemperatureReading;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class DS18B20 implements Sensor {

    public static final String version = "1.1"; // va in fase di generazione del jar..NON SPOSTARE IN BASSO

    private SensorEventListener listener;
    private Timer timer;
    private long intervalMillis = 5000; // 5 secondi tra una lettura e l'altra
    private final Random random = new Random();

    @Override
    public void initialize() {
        System.out.println("[DS18B20] Inizializzazione completata.");
        timer = new Timer();
    }

    private Message readData() {
        TemperatureReading reading = new TemperatureReading();
        reading.setTemperature(random.nextDouble());
        return reading;
    }

    @Override
    public void turnOn() {
        System.out.println("[DS18B20] Sensore acceso.");
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Message m = readData();
                if (listener != null) {
                    listener.onSensorDataReady("DS18B20", m );
                }
            }
        }, 0, intervalMillis);
    }

    @Override
    public void turnOff() {
        System.out.println("[DS18B20] Sensore spento.");
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    public void reset() {
        System.out.println("[DS18B20] Resetting sensore.");
    }

    @Override
    public void setReadingInterval(int readingInterval) {
        this.intervalMillis = readingInterval * 1000;
    }

    @Override
    public void setSensorEventListener(SensorEventListener listener) {
        this.listener = listener;
    }
}
