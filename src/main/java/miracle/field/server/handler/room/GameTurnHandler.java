package miracle.field.server.handler.room;

import com.fasterxml.jackson.core.JsonProcessingException;
import miracle.field.server.realization.Room;
import miracle.field.server.realization.SimpleServer;
import miracle.field.shared.packet.Packet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

@Component
public class GameTurnHandler extends BaseRoomHandler {

    private final SimpleServer server;
    private final Logger LOGGER = Logger.getLogger(GameTurnHandler.class.getName());

    @Autowired
    public GameTurnHandler(SimpleServer server) {
        this.type = "gameTurn";
        this.server = server;
    }

    @Override
    public Packet handle(Packet message) {
        if (!message.getType().equals(type))
            return nextHandler.handle(message);

        Room room = getRoomById(
                getUserRoomId(message.getToken())
        );

        if (room == null)
            return new Packet(type + "Error","","");

        Collection<String> users = room.getUsers();

        Packet returnPacket = null;
        try {
            returnPacket = room.makeTurn(message);

            LOGGER.info("Turn status: " + returnPacket.getType());

            if (returnPacket.getType().equals("gameOver")) {
                users = new ArrayList<>(room.getUsers());
                removeUsersFromRoom(users);
                room.clean();
            } else {
                if (returnPacket.getToken().equals(type + "Error"))
                    return returnPacket;
                Packet nextPlayer = room.nextTurn();

                LOGGER.info("Next Player : " + nextPlayer.getToken() + " Score: " + nextPlayer.getData());

                if (nextPlayer.getToken().equals(message.getToken()))
                    returnPacket = nextPlayer;
                else {
                    server.getUserByToken(nextPlayer.getToken()).send(
                            getMapper().writeValueAsBytes(nextPlayer)
                    );
                }
            }

            for (String s : users) {
                if (s.equals(message.getToken()))
                    continue;
                if (server.getUserByToken(s).isOpen())
                    server.getUserByToken(s).send(
                            getMapper().writeValueAsBytes(returnPacket.createPacketWithoutToken())
                    );
            }

        } catch (JsonProcessingException e) {
            LOGGER.severe("CAN NOT TURN");
        } catch (IOException e) {
            LOGGER.severe("CAN NOT TURN");
        }

        return returnPacket;
    }

    private void removeUsersFromRoom(Collection<String> users) {
        for (String token : users)
            removeUserFromRoom(token);
    }
}
