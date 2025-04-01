package iot.messagging.readings;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import iot.messagging.Message;
import iot.messagging.MessageType;
import lombok.Data;

@Data
public class TemperatureReading extends Message  {

    private double temperature;
    long messageType = MessageType.temperature.getValue();
    private final long timestamp = System.currentTimeMillis() / 1000; // secondi

    @Override
    public byte[] encode() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("type", messageType);
        node.put("t", timestamp);
        node.put("temp", temperature);

        try {
            return mapper.writeValueAsBytes(node);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
