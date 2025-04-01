package iot.diagnostics;
import iot.messagging.readings.AccelerometerReading;
import java.util.List;

public class VibrationDiagnoseStrategy implements InspectionStrategy<AccelerometerReading> {

    @Override
    public InspectionResult inspect(List<AccelerometerReading> batch) {
        double rms = Math.sqrt(
                batch.stream()
                        .mapToDouble(r -> {
                            double mag = Math.sqrt(r.getX() * r.getX() + r.getY() * r.getY() + r.getZ() * r.getZ());
                            return mag * mag;
                        })
                        .average()
                        .orElse(0)
        );

        boolean isAnomalous = rms > 1.0; // soglia fittizia
        return new InspectionResult(isAnomalous, rms, isAnomalous ? "Vibrazione anomala" : "Normale");
    }
}
