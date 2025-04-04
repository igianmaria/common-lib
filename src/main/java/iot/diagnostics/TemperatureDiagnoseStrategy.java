package iot.diagnostics;

import iot.Node;
import iot.diagnostics.data.TemperatureThresholds;
import iot.diagnostics.data.Thresholds;
import iot.messagging.Message;
import iot.messagging.readings.TemperatureReading;

import java.util.List;

public class TemperatureDiagnoseStrategy implements InspectionStrategy<Message> {

    @Override
    public InspectionResult inspect(List<Message> batch) {
        double rms = Math.sqrt(
                batch.stream()
                        .map(r -> (TemperatureReading) r)
                        .mapToDouble(r -> Math.pow(r.getTemperature(), 2)) // ← da millesimi di gradi a a gradi
                        .average()
                        .orElse(0.0)
        );
        System.out.println("[DS18B20] RMS: " + rms);
        Thresholds.Range range = TemperatureThresholds.TEMPERATURE_THRESHOLDS.get(Node.TemperatureType);
        boolean anomalyDetected = rms < range.min() || rms > range.max();
        return new InspectionResult(anomalyDetected, rms, "Temperatura Critica");
    }
}