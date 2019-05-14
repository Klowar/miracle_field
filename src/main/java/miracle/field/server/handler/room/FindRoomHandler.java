package miracle.field.server.handler.room;

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

        Packet returnPacket;
        String user = message.getToken();
        Room room = this.getLastRoom();

        try {
            if (getUserRoomId(message.getToken()) != null)
                return new Packet<>(type + "Error","","You already in room");

            if(room == null || !room.isOpen()) {
                room = this.createRoom();
                room.addPlayer(
                        user
                );
                getLogger().info("Created new room");
            } else {
                room.addPlayer(
                        user
                );
            }
            addUserToRoom(
                    user,
                    room.getId()
            );
            getLogger().info("User " + user + " added to " + room.getId() + " room");
            returnPacket = new Packet<>(type + "Success", "", room.getId());
        } catch (JsonProcessingException constraintException) {
            returnPacket = new Packet<>(type + "Error", "", "");
            getLogger().severe("Can not create room or add user");
        }

        return returnPacket;
    }


}
