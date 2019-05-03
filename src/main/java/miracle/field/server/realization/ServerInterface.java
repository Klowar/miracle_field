package miracle.field.server.realization;

import miracle.field.shared.packet.Packet;
import miracle.field.server.handler.Handler;

public interface ServerInterface {

    void init(Integer port);
    void start();
    void registerHandler(Handler handler);
    void handlePacket(Packet packet);

}
