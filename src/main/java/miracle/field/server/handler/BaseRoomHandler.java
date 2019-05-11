package miracle.field.server.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import miracle.field.server.realization.Room;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.logging.Logger;

public class BaseRoomHandler extends BaseHandler {

    private Logger logger = Logger.getLogger(BaseHandler.class.getName());

    private LinkedList<Room> rooms = new LinkedList<>();
    private ObjectMapper mapper;

    private static Map<String, Integer> userToRoom = new HashMap<>();

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
        return rooms.getLast();
    }

    @Autowired
    public void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
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
