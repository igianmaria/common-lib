package iot.messagging.readings;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import iot.messagging.Encodable;
import iot.messagging.Message;
import iot.messagging.MessageType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DeviceStateReading extends Message implements Encodable {

    long messageType = MessageType.device.getValue();
    private final long timestamp = System.currentTimeMillis() / 1000;

    @Override
    public byte[] encode() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("type", messageType);
        node.put("t", timestamp);
        node.put("up", getUptimeSeconds());
        try {
            return mapper.writeValueAsBytes(node);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    public long getUptimeSeconds() {
        try (BufferedReader reader = new BufferedReader(new FileReader("/proc/uptime"))) {
            String line = reader.readLine();
            if (line != null) {
                String[] parts = line.split(" ");
                return (long) Double.parseDouble(parts[0]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
