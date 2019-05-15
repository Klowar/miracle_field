package miracle.field.server.handler.room;

import com.fasterxml.jackson.core.JsonProcessingException;
import miracle.field.server.realization.Room;
import miracle.field.server.realization.SimpleServer;
import miracle.field.shared.packet.Packet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GameTurnHandler extends BaseRoomHandler {

    private final SimpleServer server;

    @Autowired
    public GameTurnHandler(SimpleServer server) {
        this.type = "gameTurn";
        this.server = server;
    }

    @Override
    public Packet handle(Packet message) {
        if (!message.getType().equals(type))
            nextHandler.handle(message);

        Packet returnPacket;
        Room room = getRoomById(
                getUserRoomId(message.getToken())
        );

        returnPacket = room.makeTurn(message);
        if (returnPacket.getType().equals("gameOver")) {
            try {
                for (String s : room.getUsers()) {
                    removeUserFromRoom(s);
                    if (s.equals(message.getToken()))
                        continue;
                    server.getUserByToken(s).send(
                            getMapper().writeValueAsBytes(returnPacket)
                    );
                }
                room.clean();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        else {
            if (returnPacket.getToken().equals(type + "Error"))
                return returnPacket;
            Packet nextPlayer = room.nextTurn();
            if (nextPlayer.getToken().equals(message.getToken()))
                returnPacket = nextPlayer;
            else
                try {
                    server.getUserByToken(nextPlayer.getToken()).send(
                            getMapper().writeValueAsBytes(nextPlayer)
                    );
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
        }

        return returnPacket;
    }
}
