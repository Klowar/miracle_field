package miracle.field.server.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import miracle.field.server.realization.Room;

import java.util.*;
import java.util.logging.Logger;

public class BaseRoomHandler extends BaseHandler {

    private final Logger logger = Logger.getLogger(BaseHandler.class.getName());

    private static final ObjectMapper mapper = new ObjectMapper();
    private static Map<String, Integer> userToRoom = new HashMap<>();

    private LinkedList<Room> rooms = new LinkedList<>();

    static Integer getUserRoomId(String token) {
        return userToRoom.get(token);
    }

    static void addUserToRoom(String token, Integer id) {
        userToRoom.put(token, id);
    }

    public Room createRoom() {
        Room newRoom = new Room(rooms.size(), mapper);
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

    public List<Room> getRooms() {
        return rooms;
    }

    public ObjectMapper getMapper() {
        return mapper;
    }

    public Logger getLogger() {
        return this.logger;
    }
}
