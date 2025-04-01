package iot.hw;

import iot.Sensor;
import iot.SensorEventListener;
import iot.diagnostics.data.Thresholds;
import iot.messagging.Message;
import iot.messagging.readings.Alarm;
import iot.messagging.readings.SensorFailure;
import iot.messagging.readings.TemperatureReading;
import lombok.Data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static iot.diagnostics.data.TemperatureThresholds.TEMPERATURE_THRESHOLDS;

public class DS18B20 implements Sensor {

    public static final String version = "1.1"; // va in fase di generazione del jar..NON SPOSTARE IN BASSO

    private SensorEventListener listener;

    private Timer samplingTimer;
    private long samplingIntervall = 1000;

    private Timer sendTimer;
    private long sendIntervall = 10000;

    private String monitoredEquipment;
    private static final String BASE_PATH = "/sys/bus/w1/devices/";
    private static final String SENSOR_FOLDER_PREFIX = "28-";
    private static final String DATA_FILE = "w1_slave";

    List<TemperatureReading> readingBuffer = new ArrayList<TemperatureReading>();

    @Override
    public void initialize() {
        System.out.println("[DS18B20] Inizializzazione completata.");
        samplingTimer = new Timer();
        sendTimer = new Timer();
    }

    private Message readData() throws IOException {

        String sensorPath = findSensorPath();
        if (sensorPath == null) {
            System.out.println("[DS18B20] CAnt find Sensor path.");
            return new SensorFailure();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(sensorPath))) {
            String line1 = reader.readLine();
            String line2 = reader.readLine();

            if (line1 != null && line1.contains("YES") && line2 != null && line2.contains("t=")) {
                String tempString = line2.split("t=")[1];
                TemperatureReading tempReading = new TemperatureReading();
                tempReading.setTemperature(Float.parseFloat(tempString)/1000.0);
                readingBuffer.add(tempReading);
                return tempReading;
            } else {
                throw new IOException("Invalid sensor data");
            }
        }
    }

    private static String findSensorPath() {
        java.io.File baseDir = new java.io.File(BASE_PATH);
        if (baseDir.exists()) {
            for (String file : baseDir.list()) {
                if (file.startsWith(SENSOR_FOLDER_PREFIX)) {
                    return BASE_PATH + file + "/" + DATA_FILE;
                }
            }
        }
        return null;
    }

    @Override
    public void turnOn() {
        System.out.println("[DS18B20] Sensore acceso.");

        sendTimer.scheduleAtFixedRate(new TimerTask() {
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
        }, 0, sendIntervall);


        samplingTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Message m = null;
                try {
                    m = readData();
                    if (readingBuffer.size() == 50){
                        List<Message> snapshot;
                        synchronized (readingBuffer) {
                            snapshot = new ArrayList<>(readingBuffer);
                            readingBuffer.clear();
                        }
                        evaluateHealthCondition(snapshot);
                    }

                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }, 0, samplingIntervall);
    }

    @Override
    public void turnOff() {
        System.out.println("[DS18B20] Sensore spento.");
        if (samplingTimer != null) {
            samplingTimer.cancel();
        }
    }

    @Override
    public void reset() {
        System.out.println("[DS18B20] Resetting sensore.");
    }

    @Override
    public void evaluateHealthCondition(List<Message> snapshot) {
        CompletableFuture.runAsync(() -> {
            double rms = Math.sqrt(
                    snapshot.stream()
                            .map(msg -> (TemperatureReading) msg)
                            .mapToDouble(r -> Math.pow(r.getTemperature(), 2))
                            .average()
                            .orElse(0.0)
            );

            Thresholds.Range range = TEMPERATURE_THRESHOLDS.get(monitoredEquipment);
            boolean anomaly = rms < range.min() || rms > range.max();
            if (anomaly) {
                if (listener != null) {
                    Alarm alarm = new Alarm();
                    alarm.setSource("DS18B20:" + monitoredEquipment);
                    alarm.setAlarmMessage("Allarme temperatura: " + rms);
                    listener.onSensorAlarm("DS18B20",alarm);
                }
            }
        });
    }

    @Override
    public void setReadingInterval(int readingInterval) {
        this.samplingIntervall = readingInterval * 1000;
    }

    @Override
    public void setSensorEventListener(SensorEventListener listener) {
        this.listener = listener;
    }

    @Override
    public void setAppliedMachine(String monitoredEquipment) {
        this.monitoredEquipment = monitoredEquipment;
    }
}
