package miracle.field.server.controller;

import miracle.field.server.packet.Packet;

public class BaseHandler implements Handler {

    private Handler nextHandler;

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
