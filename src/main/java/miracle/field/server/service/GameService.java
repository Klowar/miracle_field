package miracle.field.server.service;

import miracle.field.shared.model.Word;
import miracle.field.shared.packet.Packet;
import org.java_websocket.WebSocket;

import java.util.Map;

public interface GameService {
    void startGame(Map<String, WebSocket> players);
    void finishedGame();

//  ход игры
    void gameMove(Packet packet);

}
