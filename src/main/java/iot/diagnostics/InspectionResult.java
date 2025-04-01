package iot.diagnostics;

public class InspectionResult {
    private final boolean anomalyDetected;
    private final double alarmValueDetected;
    private final String humanReadableMessage;

    public InspectionResult(boolean anomalyDetected, double value, String message) {
        this.anomalyDetected = anomalyDetected;
        this.alarmValueDetected = value;
        this.humanReadableMessage = message;
    }

    public boolean isAnomalyDetected() {
        return anomalyDetected;
    }

    public double getAlarmValueDetected() {
        return alarmValueDetected;
    }

    public String getHumanReadableMessage() {
        return humanReadableMessage;
    }
}
