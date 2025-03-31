package iot.messagging;

@FunctionalInterface
public interface Encodable {
    byte[] encode(Message message);
}
