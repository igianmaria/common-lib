package iot.diagnostics;

import lombok.Data;

@Data
public class InspectionResult {
    private final boolean anomalyDetected;
    private final double rmsValue;
    private final String humanReadableMessage;

    public InspectionResult(boolean anomalyDetected, double value, String message) {
        this.anomalyDetected = anomalyDetected;
        this.rmsValue = value;
        this.humanReadableMessage = message;
    }
}
