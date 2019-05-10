package miracle.field.server.handler;

import java.util.HashMap;
import java.util.Map;

public class BaseRoomHandler extends BaseHandler {

    private static Map<String, Integer> userToRoom = new HashMap<>();

    static Integer getUserRoomId(String token) {
        return userToRoom.get(token);
    }

    static void addUserToRoom(String token, Integer id) {
        userToRoom.put(token, id);
    }
}
