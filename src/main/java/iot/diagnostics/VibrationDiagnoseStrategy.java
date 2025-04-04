package iot.diagnostics;

import iot.diagnostics.data.VibrationThresholds;
import iot.messagging.Message;
import iot.messagging.readings.AccelerometerReading;

import java.util.List;

public class VibrationDiagnoseStrategy implements InspectionStrategy<Message> {

    String machineType;

    @Override
    public InspectionResult inspect(List<Message> snapshot) {
        double rms = Math.sqrt(
                snapshot.stream()
                        .map(r -> ((AccelerometerReading) r))
                        .mapToDouble(r -> {
                            double mag = Math.sqrt(r.getX() * r.getX() + r.getY() * r.getY() + r.getZ() * r.getZ());
                            return mag * mag;
                        })
                        .average()
                        .orElse(0)
        );

        VibrationThresholds.VibrationZone zone = VibrationThresholds.getVibrationZone(rms);

        boolean isAnomalous = (zone != VibrationThresholds.VibrationZone.UNRESTRICTED);
        String description = switch (zone) {
            case UNRESTRICTED -> "Normale";
            case RESTRICTED -> "Vibrazioni in zona di attenzione";
            case DAMAGE_POSSIBLE -> "Vibrazioni anomale o guarsto in atto o incombente!";
        };

        return new InspectionResult(isAnomalous, rms, description);
    }
}
