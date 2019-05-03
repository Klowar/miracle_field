package miracle.field.server.handler;

import miracle.field.shared.packet.Packet;

public interface Handler {

    void registerHandler(Handler handler);
    Packet handle(Packet message);
    String getType();
}
