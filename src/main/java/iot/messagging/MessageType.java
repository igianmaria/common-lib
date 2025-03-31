package iot.messagging;

public enum MessageType {
    sensorFailure(0),
    device(1),
    temperature(2),
    statics(3);

    private final int value;

    MessageType(final int newValue) {
        value = newValue;
    }
    public int getValue() { return value; }
}
