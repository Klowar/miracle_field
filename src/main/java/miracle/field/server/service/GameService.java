package miracle.field.server.service;

import miracle.field.server.gameData.GameInfo;
import miracle.field.shared.model.Word;
import miracle.field.shared.packet.Packet;
import org.java_websocket.WebSocket;

import java.util.Map;

public interface GameService<T extends GameInfo> {
    void startGame(Map<String, WebSocket> players, T gameInfo);
    void finishedGame(T gameInfo);

//  ход игры
    void gameMove(T gameInfo, String player);

}
