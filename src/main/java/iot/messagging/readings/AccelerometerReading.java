package iot.messagging.readings;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import iot.messagging.Encodable;
import iot.messagging.Message;
import iot.messagging.MessageType;
import lombok.Data;

@Data
public class AccelerometerReading extends Message implements Encodable {

    private final long messageType = MessageType.statics.getValue();
    private final long timestamp = System.currentTimeMillis() / 1000; // secondi
    private long x, y, z;

    @Override
    public byte[] encode() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("type", messageType);
        node.put("t", timestamp);
        node.put("x", x);
        node.put("y", y);
        node.put("z", z);

        try {
            return mapper.writeValueAsBytes(node);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
