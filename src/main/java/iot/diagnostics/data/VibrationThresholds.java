package iot.diagnostics.data;

import iot.Node;
import iot.hw.characterization.MachineVibrationType;

import java.util.Map;

public class VibrationThresholds extends Thresholds {

    public enum VibrationZone {
        UNRESTRICTED,
        RESTRICTED,
        DAMAGE_POSSIBLE
    }

    public static final Map<MachineVibrationType, Map<VibrationZone, Range>> VIBRATION_THRESHOLDS = Map.of(
            MachineVibrationType.SMALL_MACHINE_RIGID, Map.of(
                    VibrationZone.UNRESTRICTED, new Range(0.0, 2.8),
                    VibrationZone.RESTRICTED, new Range(2.8, 4.5),
                    VibrationZone.DAMAGE_POSSIBLE, new Range(4.5, Double.MAX_VALUE)
            ),
            MachineVibrationType.MEDIUM_MACHINE_RIGID, Map.of(
                    VibrationZone.UNRESTRICTED, new Range(0.0, 2.8),
                    VibrationZone.RESTRICTED, new Range(2.8, 7.1),
                    VibrationZone.DAMAGE_POSSIBLE, new Range(7.1, Double.MAX_VALUE)
            ),
            MachineVibrationType.LARGE_MACHINE_RIGID, Map.of(
                    VibrationZone.UNRESTRICTED, new Range(0.0, 4.5),
                    VibrationZone.RESTRICTED, new Range(4.5, 7.1),
                    VibrationZone.DAMAGE_POSSIBLE, new Range(7.1, Double.MAX_VALUE)
            ),
            MachineVibrationType.LARGE_MACHINE_FLEXIBLE, Map.of(
                    VibrationZone.UNRESTRICTED, new Range(0.0, 7.1),
                    VibrationZone.RESTRICTED, new Range(7.1, 11.2),
                    VibrationZone.DAMAGE_POSSIBLE, new Range(11.2, Double.MAX_VALUE)
            )
    );

    public static VibrationThresholds.VibrationZone getVibrationZone(double rms) {
        Map<VibrationThresholds.VibrationZone, Range> thresholds =
                VibrationThresholds.VIBRATION_THRESHOLDS.get(Node.VibrationType);

        if (thresholds == null) {
            throw new IllegalArgumentException("Macchinario non supportato: ");
            //TODO: emit an error??
        }

        for (Map.Entry<VibrationThresholds.VibrationZone, Range> entry : thresholds.entrySet()) {
            if (entry.getValue().contains(rms)) {
                return entry.getKey();
            }
        }
        return VibrationThresholds.VibrationZone.DAMAGE_POSSIBLE;  // TODO: in questo caso segnalo un danno o tutto ok?
     }
}
