package miracle.field.server.handler.room;

import com.fasterxml.jackson.databind.ObjectMapper;
import miracle.field.server.handler.BaseHandler;
import miracle.field.server.realization.Room;
import miracle.field.server.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.logging.Logger;

@Component
public class BaseRoomHandler extends BaseHandler {

    private final Logger logger = Logger.getLogger(BaseHandler.class.getName());
    @Autowired
    protected GameService gameService;
    private static ObjectMapper mapper;
    private static Map<String, Integer> userToRoom;

    private static LinkedList<Room> rooms;

    public BaseRoomHandler() {
        this.type = "roomReply";
        rooms = new LinkedList<>();
        userToRoom = new HashMap<>();
        mapper = new ObjectMapper();
    }

    static Integer getUserRoomId(String token) {
        return userToRoom.get(token);
    }

    static void addUserToRoom(String token, Integer id) {
        userToRoom.put(token, id);
    }

    static List<Room> getRooms() {
        return rooms;
    }

    public Room createRoom() {
        Room newRoom = new Room(rooms.size(), mapper, gameService);
        rooms.add(newRoom);
        return newRoom;
    }

    public Room getRoomById(Integer id) {
        return rooms.get(id);
    }

    public Room getLastRoom() {
        try {
            return rooms.getLast();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public ObjectMapper getMapper() {
        return mapper;
    }

    public Logger getLogger() {
        return this.logger;
    }

    public void removeUserFromRoom(String token) {
        userToRoom.remove(token);
    }
}
