package miracle.field.server.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import miracle.field.server.realization.Room;
import miracle.field.server.realization.SimpleServer;
import miracle.field.shared.packet.Packet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChatRoomHandler extends BaseRoomHandler {

    private final SimpleServer server;

    @Autowired
    public ChatRoomHandler(SimpleServer server) {
        this.type = "roomChat";
        this.server = server;
    }

    @Override
    public Packet handle(Packet message) {
        if (!message.getType().equals(type))
            return nextHandler.handle(message);

        Packet returnPacket = null;
        String user = message.getToken();
        Room room = server.getRoomById(
                getUserRoomId(user)
        );

        try {
            room.writeToRoom(
                    new Packet(type,"", message.getSerializedData())
            );
        } catch (JsonProcessingException e) {
            System.out.println("Can not write packet to room: " +  message.getType());
        }

        return returnPacket;
    }
}
