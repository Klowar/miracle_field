package miracle.field.server.handler;

import miracle.field.packet.Packet;

public class LoginHandler extends BaseHandler {

    @Override
    public Object handle(Packet message) {
        if (!message.getType().equals("login"))
            return nextHandler.handle(message);

        return null;
    }
}
