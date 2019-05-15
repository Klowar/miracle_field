package miracle.field.server.handler.room;

import com.fasterxml.jackson.core.JsonProcessingException;
import miracle.field.server.realization.Room;
import miracle.field.server.realization.SimpleServer;
import miracle.field.shared.packet.Packet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StartGameHandler extends BaseRoomHandler {

    private final SimpleServer server;

    @Autowired
    public StartGameHandler(SimpleServer server) {
        this.type = "startGame";
        this.server = server;
    }

    @Override
    public Packet handle(Packet message) {
        if (!message.getType().equals(type)) {
            nextHandler.handle(message);
        }
        Room room = getRoomById(
                getUserRoomId(message.getToken())
        );
        Packet returnPacket = new Packet(type + "Error","","Wait other users");
        if (!room.isOpen()) {
            try {
                returnPacket = room.startGame();
                for (String token : room.getUsers()) {
                    if (token.equals(message.getToken()))
                        continue;
                    server.getUserByToken(token).send(
                            getMapper().writeValueAsBytes(returnPacket)
                    );
                }

                return returnPacket;
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return returnPacket;
    }
}
