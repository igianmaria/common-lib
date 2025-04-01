package iot.messagging.readings;

import iot.messagging.Message;
import iot.messagging.MessageType;
import lombok.Data;

@Data
public class Alarm extends Message {

    long messageType = MessageType.temperature.getValue();
    private final long timestamp = System.currentTimeMillis() / 1000; // secondi
    String source;
    String alarmMessage;

    @Override
    public byte[] encode() {
        return new byte[0];
    }
}
