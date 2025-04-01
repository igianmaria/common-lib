package iot.diagnostics.data;

import java.util.Map;

public class VibrationTresholds extends Thresholds {
    public static final Map<String, Range> VIBRATION_THRESHOLDS = Map.of(
            "compressore", new Range(0.0, 4.5),
            "motore_elettrico", new Range(0.0, 4.5),
            "pompa_idraulica", new Range(0.0, 4.5),
            "turbina_vapore", new Range(0.0, 4.5),
            "turbina_gas", new Range(0.0, 4.5),
            "ventilatore_industriale", new Range(0.0, 4.5)
    );
}
