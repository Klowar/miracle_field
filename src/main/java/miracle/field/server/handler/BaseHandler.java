package miracle.field.server.handler;

import miracle.field.packet.Packet;

public class BaseHandler implements Handler {

    protected Handler nextHandler;

    @Override
    public void registerHandler(Handler handler) {
        if (nextHandler == null)
            this.nextHandler = handler;
        else
            this.nextHandler.registerHandler(handler);
    }

    @Override
    public Object handle(Packet message) {
        return nextHandler.handle(message);
    }
}
