package miracle.field.server.service;

import miracle.field.server.gameData.GameInfo;

import java.util.Collection;

public interface GameService<T extends GameInfo> {
    void startGame(Collection<String> players, T gameInfo);
    void finishedGame(T gameInfo);
    void gameMove(T gameInfo, String player, String data);
}
