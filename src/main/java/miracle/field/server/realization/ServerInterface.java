package miracle.field.server.realization;

import miracle.field.server.handler.Handler;
import miracle.field.server.packet.Packet;
import org.springframework.context.ApplicationContextAware;

public interface ServerInterface extends ApplicationContextAware {

    void init(Integer port);
    void start();
    void registerHandler(Handler handler);
    void handlePacket(Packet packet);

}
