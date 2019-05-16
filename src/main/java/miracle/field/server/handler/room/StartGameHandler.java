package miracle.field.server.handler.room;

import com.fasterxml.jackson.core.JsonProcessingException;
import miracle.field.server.realization.Room;
import miracle.field.server.realization.SimpleServer;
import miracle.field.shared.packet.Packet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class StartGameHandler extends BaseRoomHandler {

    private final SimpleServer server;
    private final Logger LOGGER = Logger.getLogger(StartGameHandler.class.getName());

    @Autowired
    public StartGameHandler(SimpleServer server) {
        this.type = "startGame";
        this.server = server;
    }

    @Override
    public Packet handle(Packet message) {
        if (!message.getType().equals(type)) {
            return nextHandler.handle(message);
        }
        Room room = getRoomById(
                getUserRoomId(message.getToken())
        );
        Packet returnPacket = new Packet(type + "Error","","Wait other users");
        if (!room.isOpen()) {
            try {
                returnPacket = room.startGame();

                LOGGER.info("Start room " + returnPacket);

                for (String token : room.getUsers()) {
                    if(token.equals(message.getToken()))
                        continue;

                    if (server.getUserByToken(token).isOpen())
                        server.getUserByToken(token).send(
                                getMapper().writeValueAsBytes(returnPacket.createPacketWithoutToken())
                        );
                }
            } catch (JsonProcessingException e) {
                LOGGER.severe("Can not start game");
            }
        }

        LOGGER.info("sent " + returnPacket);

        return returnPacket;
    }
}
