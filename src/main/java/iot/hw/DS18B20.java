package iot.hw;

import iot.Sensor;
import iot.diagnostics.InspectionResult;
import iot.diagnostics.TemperatureDiagnoseStrategy;
import iot.messagging.Message;
import iot.messagging.readings.Alarm;
import iot.messagging.readings.SensorFailure;
import iot.messagging.readings.TemperatureReading;
import lombok.Data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;

@Data
public class DS18B20 extends Sensor {

    private static final String BASE_PATH = "/sys/bus/w1/devices/";
    private static final String SENSOR_FOLDER_PREFIX = "28-";
    private static final String DATA_FILE = "w1_slave";

    List<TemperatureReading> readingBuffer = new ArrayList<TemperatureReading>();

    @Override
    public void initialize() {
        System.out.println("[DS18B20] Inizializzazione completata.");
        readTimer = new Timer();
        sendTimer = new Timer();
        samplingTimer = new Timer();
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


        readTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Message m = null;
                try {
                    m = readData();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }, 0, (long) (readInterval * 1000));


        sendTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (listener != null) {
                    TemperatureReading tempReading = new TemperatureReading();
                    tempReading.setTemperature(currentRMS);
                    listener.onSensorDataReady("ADXL345", tempReading);
                }
            }
        },0, sendInterval * 1000);


        samplingTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                List<Message> snapshot;
                synchronized (readingBuffer) {
                    snapshot = new ArrayList<>(readingBuffer);
                    readingBuffer.clear();
                }
                evaluateHealthCondition(snapshot);
            }
        }, 0, samplingInterval * 1000);

    }

    @Override
    public void turnOff() {
        System.out.println("[DS18B20] Sensore spento.");
        if (samplingTimer != null) {
            samplingTimer.cancel();
        }
    }

    public void reset() {
        System.out.println("[DS18B20] Resetting sensore.");
    }

    public void evaluateHealthCondition(List<Message> snapshot) {
        CompletableFuture.runAsync(() -> {

            TemperatureDiagnoseStrategy strategy = new TemperatureDiagnoseStrategy();
            InspectionResult result = strategy.inspect(snapshot);

            currentRMS = result.getRmsValue();
            System.out.println(this.getClass().getName() + ":" + currentRMS);

            if (result.isAnomalyDetected() && listener != null) {
                Alarm alarm = new Alarm();
                alarm.setSource("DS18B20");
                alarm.setAlarmMessage("Allarme temperatura: " + result.getRmsValue() + ":" + result.getHumanReadableMessage());
                listener.onSensorAlarm("DS18B20", alarm);
            }
        });
    }
}
