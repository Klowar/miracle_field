package miracle.field.server.handler;

import miracle.field.server.packet.Packet;

public interface Handler<T> {

    void registerHandler(Handler handler);
    T handle(Packet message);
}
