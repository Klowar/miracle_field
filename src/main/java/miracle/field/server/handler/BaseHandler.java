package miracle.field.server.handler;

import miracle.field.shared.packet.Packet;
import org.springframework.stereotype.Component;

@Component
public class BaseHandler implements Handler {

    protected Handler nextHandler;
    protected String type;

    public BaseHandler() {
        this.type = "reply";
    }

    @Override
    public void registerHandler(Handler handler) {
        if (nextHandler == null)
            this.nextHandler = handler;
        else
            this.nextHandler.registerHandler(handler);
    }

    @Override
    public Packet handle(Packet message) {
        return nextHandler.handle(message);
    }

    @Override
    public String getType() { return type; }
}
