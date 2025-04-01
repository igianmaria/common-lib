package iot.diagnostics.data;

import java.util.Map;

public class TemperatureThresholds extends Thresholds {
    public static final Map<String, Range> TEMPERATURE_THRESHOLDS = Map.of(
            "compressore", new Range(30.0, 90.0),
            "motore_elettrico", new Range(40.0, 100.0),
            "pompa_idraulica", new Range(35.0, 95.0),
            "turbina_vapore", new Range(50.0, 150.0),
            "turbina_gas", new Range(50.0, 120.0),
            "ventilatore_industriale", new Range(20.0, 80.0)
    );
}
