package iot;


import iot.hw.characterization.MachineTemperatureType;
import iot.hw.characterization.MachineVibrationType;

import java.util.ArrayList;
import java.util.List;

public class Node {

    public static MachineTemperatureType TemperatureType = MachineTemperatureType.UNDEFINED;
    public static MachineVibrationType VibrationType = MachineVibrationType.UNDEFINED;

    public static List<Sensor> sensors = new ArrayList<Sensor>();

}
