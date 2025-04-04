package iot.hw;

import iot.Sensor;
import iot.SensorEventListener;
import iot.diagnostics.InspectionResult;
import iot.diagnostics.VibrationDiagnoseStrategy;
import iot.messagging.Message;
import iot.messagging.readings.AccelerometerReading;
import iot.messagging.readings.Alarm;
import lombok.Data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;


@Data
public class ADXL345 extends Sensor {

    List<AccelerometerReading> readingBuffer = new ArrayList<AccelerometerReading>();


    @Override
    public void initialize() {
        try {
            System.out.println("[ADXL345] Inizializzazione completata.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void turnOn() {
        System.out.println("[ADXL345] Sensore acceso.");

        sendTimer = new Timer();
        sendTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!readingBuffer.isEmpty()) {
                    AccelerometerReading last = readingBuffer.getLast();
                    if (listener != null) {
                        listener.onSensorDataReady("ADXL345", last);
                        readingBuffer.add(last);
                    }
                }
            }
        }, 0, sendInterval * 1000);

        samplingTimer = new Timer();
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
        }, 0, 2 * samplingInterval * 1000);

        readTimer = new Timer();
        readTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    Process p = Runtime.getRuntime().exec("python3 /home/gianmaria/node/plugins/accelReader.py");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    String line = null;

                    line = reader.readLine();

                    if (line != null) {
                        String[] parts = line.trim().split(",");
                        double x = Double.parseDouble(parts[0]);
                        double y = Double.parseDouble(parts[1]);
                        double z = Double.parseDouble(parts[2]);

                        AccelerometerReading reading = new AccelerometerReading();
                        reading.setX(x);
                        reading.setY(y);
                        reading.setZ(z);

                        readingBuffer.add(reading);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }, 0, 2 * samplingInterval * 1000);

    }


    @Override
    public void turnOff() {
        if (sendTimer != null) {
            sendTimer.cancel();
        }
    }


    public void evaluateHealthCondition(List<Message> snapshot) {
        CompletableFuture.runAsync(() -> {
            VibrationDiagnoseStrategy strategy = new VibrationDiagnoseStrategy();
            InspectionResult result = strategy.inspect(snapshot);

            currentRMS = result.getRmsValue();
            System.out.println(this.getClass().getName() + ":" + currentRMS );

            if (result.isAnomalyDetected() && listener != null) {
                Alarm alarm = new Alarm();
                alarm.setSource("ADXL345");
                alarm.setAlarmMessage("Allarme Vibrazioni: " + result.getRmsValue() + ":" + result.getHumanReadableMessage());
                listener.onSensorAlarm("ADXL345", alarm);
            } else {
                System.out.println("[ADXL345] NO Detected anomaly");
            }
        });
    }
}
