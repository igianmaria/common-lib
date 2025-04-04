package iot.diagnostics.data;

import iot.hw.characterization.MachineTemperatureType;

import java.util.Map;

public class TemperatureThresholds extends Thresholds {
    public static final Map<MachineTemperatureType, Range> TEMPERATURE_THRESHOLDS = Map.of(
            MachineTemperatureType.TEST, new Range(0.0, 30.0),
            MachineTemperatureType.COMMERCIAL, new Range(0.0, 70.0),
            MachineTemperatureType.EXTENDED_COMMERCIAL, new Range(-20.0, 85.0),
            MachineTemperatureType.HI_TEMP_COMMERCIAL, new Range(-10.0, 100.0),
            MachineTemperatureType.INDUSTRIAL, new Range(-40.0, 85.0),
            MachineTemperatureType.EXTENDED_INDUSTRIAL, new Range(-40.0, 85.0),
            MachineTemperatureType.POWER_SUPPLY, new Range(-40.0, 130.0),
            MachineTemperatureType.MILITARY, new Range(-55.0, 125.0),
            MachineTemperatureType.AUTOMOTIVE, new Range(-25.0, 125.0),
            MachineTemperatureType.AEC_Q100_LEVEL_2, new Range(-40.0, 105.0)
    );
}
