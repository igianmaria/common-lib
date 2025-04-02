package iot.diagnostics.data;

import java.util.Map;

public class TemperatureThresholds extends Thresholds {
    public static final Map<String, Range> TEMPERATURE_THRESHOLDS = Map.of(
            "Commercial", new Range(0.0, 70.0),
            "Extended-Commercial", new Range(-20.0, 85.0),
            "Hi-Temp-Commercial", new Range(-10.0, 100.0),
            "Industrial", new Range(-40.0, 85.0),
            "Extended-Industrial", new Range(-40.0, 85.0),
            "Power-Supply", new Range(-40.0, 130.0),
            "Military", new Range(-55.0, 125.0),
            "Automotive", new Range(-25.0, 125.0),
            "AEC-Q100-Level-2", new Range(-40.0, 105.0)
    );
}
