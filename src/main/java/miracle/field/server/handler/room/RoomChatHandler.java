package miracle.field.server.handler.room;

import com.fasterxml.jackson.core.JsonProcessingException;
import miracle.field.server.realization.Room;
import miracle.field.server.realization.SimpleServer;
import miracle.field.server.service.UserService;
import miracle.field.shared.packet.Packet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoomChatHandler extends BaseRoomHandler {

    private final SimpleServer server;
    private final UserService service;

    @Autowired
    public RoomChatHandler(SimpleServer server, UserService userService) {
        this.type = "roomChat";
        this.server = server;
        this.service = userService;
    }

    @Override
    public Packet handle(Packet message) {
        if (!message.getType().equals(type))
            return nextHandler.handle(message);

        Packet returnPacket = new Packet(type + "Error", "","You do not have room");
        Room room = this.getRoomById(
                getUserRoomId(message.getToken())
        );
        if (room == null)
            return returnPacket;

        returnPacket = new Packet(
                message.getType(),
                "",
                service.getAuthorizedUser(
                        message.getToken()).getUsername()
                        + ": "
                        + message.getSerializedData()
                );

        for (String token : room.getUsers()) {
            if (token.equals(message.getToken()))
                continue;
            try {
                if (server.getUserByToken(token).isOpen())
                    server.getUserByToken(token).send(
                        getMapper().writeValueAsBytes(returnPacket)
                    );
                else
                    continue;
            } catch (JsonProcessingException e) {
                continue;
            }
        }

        return returnPacket;
    }
}
