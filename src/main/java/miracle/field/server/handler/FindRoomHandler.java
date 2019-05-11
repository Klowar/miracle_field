package miracle.field.server.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import miracle.field.server.realization.Room;
import miracle.field.server.realization.SimpleServer;
import miracle.field.shared.packet.Packet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FindRoomHandler extends BaseRoomHandler {

    private final SimpleServer server;

    @Autowired
    public FindRoomHandler(SimpleServer server) {
        this.type = "findRoom";
        this.server = server;
    }

    @Override
    public Packet handle(Packet message) {
        if (!message.getType().equals(type))
            return nextHandler.handle(message);

        Packet returnPacket = null;
        String user = message.getToken();
        Room room = server.getLastRoom();

        try {
            if (room.isOpen()) {
                room.addPlayer(
                        server.getUserByToken(user),
                        user
                );
            }
            else{
                room = server.createNewRoom();
                room.addPlayer(
                        server.getUserByToken(user),
                        user
                );
            }
            addUserToRoom(
                    user,
                    room.getId()
            );
            returnPacket = new Packet<>(type + "Success", "", room.getId());
        } catch (JsonProcessingException constraintException) {
//          TODO more info to user ???
            returnPacket = new Packet<>(type + "Error", "", "");
        }

        return returnPacket;
    }


}
