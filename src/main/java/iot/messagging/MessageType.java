package iot.messagging;

public enum MessageType {
    device(0),
    temperature(1),
    statics(2);

    private final int value;

    MessageType(final int newValue) {
        value = newValue;
    }
    public int getValue() { return value; }
}
