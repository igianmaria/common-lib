package iot.hw;
import iot.Sensor;
import iot.SensorEventListener;
import iot.messagging.Message;
import iot.messagging.readings.AccelerometerReading;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ADXL345 implements Sensor {

    SensorEventListener listener;

    Timer sendTimer;
    long sendInterval = 50000;

    private Timer samplingTimer;
    private long samplingIntervall = 1000;

    String monitoredEquipment;

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

                        if (listener != null) {
                            AccelerometerReading reading = new AccelerometerReading();
                            reading.setX(x);
                            reading.setY(y);
                            reading.setZ(z);

                            listener.onSensorDataReady("ADXL345", reading);
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }, 0, sendInterval);

        samplingTimer = new Timer();
        samplingTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

            }
        }, 0, samplingIntervall);
    }

    @Override
    public void turnOff() {
        if (sendTimer != null) {
            sendTimer.cancel();
        }
    }

    @Override
    public void reset() {
    }

    @Override
    public void evaluateHealthCondition(List<Message> snapshot) {

    }


    @Override
    public void setReadingInterval(int readingInterval) {
        sendInterval = readingInterval * 1000;
    }

    @Override
    public void setSensorEventListener(SensorEventListener listener) {
        this.listener = listener;
    }

    @Override
    public void setAppliedMachine(String monitoredEquipment) {

    }
}
