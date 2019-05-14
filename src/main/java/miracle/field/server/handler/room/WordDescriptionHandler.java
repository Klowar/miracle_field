package miracle.field.server.handler.room;

import miracle.field.server.realization.Room;
import miracle.field.shared.packet.Packet;
import org.springframework.stereotype.Component;

@Component
public class WordDescriptionHandler extends BaseRoomHandler {

    public WordDescriptionHandler() {
        this.type = "roomWordDescription";
    }

    @Override
    public Packet handle(Packet message) {
        if (!message.getType().equals(type))
            nextHandler.handle(message);

        Packet returnPacket = null;

        if (getUserRoomId(message.getToken()) == null)
            return new Packet(type+"Error","","Game did not start");

        Room room = getRoomById(
                getUserRoomId(
                        message.getToken()
                )
        );

        return room.getWordDescription();
    }
}
