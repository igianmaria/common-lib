package iot;

import iot.messagging.Message;
import iot.messagging.readings.TemperatureReading;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class DS18B20 implements Sensor {

    public static final String version = "1.1"; // va in fase di generazione del jar..NON SPOSTARE IN BASSO

    private SensorEventListener listener;
    private Timer timer;
    private long intervalMillis = 5000; // 5 secondi tra una lettura e l'altra
    private static final String BASE_PATH = "/sys/bus/w1/devices/";
    private static final String SENSOR_FOLDER_PREFIX = "28-";
    private static final String DATA_FILE = "w1_slave";

    @Override
    public void initialize() {
        System.out.println("[DS18B20] Inizializzazione completata.");
        timer = new Timer();
    }

    private Message readData() throws IOException {

        String sensorPath = findSensorPath();
        if (sensorPath == null) {
            try {
                throw new IOException("Sensor not found");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(sensorPath))) {
            String line1 = reader.readLine(); // first line
            String line2 = reader.readLine(); // second line

            if (line1 != null && line1.contains("YES") && line2 != null && line2.contains("t=")) {
                String tempString = line2.split("t=")[1];
                TemperatureReading tempReading = new TemperatureReading();
                tempReading.setTemperature(Float.parseFloat(tempString));
                return tempReading;
            } else {
                throw new IOException("Invalid sensor data");
            }
        }
    }

    private static String findSensorPath() {
        java.io.File baseDir = new java.io.File(BASE_PATH);
        for (String file : baseDir.list()) {
            if (file.startsWith(SENSOR_FOLDER_PREFIX)) {
                return BASE_PATH + file + "/" + DATA_FILE;
            }
        }
        return null;
    }

    @Override
    public void turnOn() {
        System.out.println("[DS18B20] Sensore acceso.");
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Message m = null;
                try {
                    m = readData();
                    if (listener != null) {
                        listener.onSensorDataReady("DS18B20", m );
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
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
