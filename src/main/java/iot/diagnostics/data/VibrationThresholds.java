package iot.diagnostics.data;

import java.util.Map;

public class VibrationThresholds extends Thresholds {

    public enum VibrationZone {
        UNRESTRICTED,
        RESTRICTED,
        DAMAGE_POSSIBLE
    }

    public static final Map<String, Map<VibrationZone, Range>> VIBRATION_THRESHOLDS = Map.of(
            "Small-Machine-Rigid", Map.of(
                    VibrationZone.UNRESTRICTED, new Range(0.0, 2.8),
                    VibrationZone.RESTRICTED, new Range(2.8, 4.5),
                    VibrationZone.DAMAGE_POSSIBLE, new Range(4.5, Double.MAX_VALUE)
            ),
            "Medium-Machine-Rigid", Map.of(
                    VibrationZone.UNRESTRICTED, new Range(0.0, 2.8),
                    VibrationZone.RESTRICTED, new Range(2.8, 7.1),
                    VibrationZone.DAMAGE_POSSIBLE, new Range(7.1, Double.MAX_VALUE)
            ),
            "Large-Machine-Rigid", Map.of(
                    VibrationZone.UNRESTRICTED, new Range(0.0, 4.5),
                    VibrationZone.RESTRICTED, new Range(4.5, 7.1),
                    VibrationZone.DAMAGE_POSSIBLE, new Range(7.1, Double.MAX_VALUE)
            ),
            "Large-Machine-Flexible", Map.of(
                    VibrationZone.UNRESTRICTED, new Range(0.0, 7.1),
                    VibrationZone.RESTRICTED, new Range(7.1, 11.2),
                    VibrationZone.DAMAGE_POSSIBLE, new Range(11.2, Double.MAX_VALUE)
            )
    );
}
