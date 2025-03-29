package it.gi.iot.messagging;

import it.gi.iot.messagging.Message;

@FunctionalInterface
public interface Encodable {
    byte[] encode(Message message);
}
