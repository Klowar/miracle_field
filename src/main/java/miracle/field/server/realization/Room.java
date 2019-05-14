package miracle.field.server.realization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import miracle.field.server.gameData.MiracleFieldInfo;
import miracle.field.server.service.GameService;
import miracle.field.shared.packet.Packet;
import org.java_websocket.WebSocket;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Room {

    private Integer id;
    private boolean open;

    private final ObjectMapper mapper;

    private GameService gameService;
    private MiracleFieldInfo gameInfo;

    private Queue<String> playerOrder;

    public Room(Integer id,
                ObjectMapper mapper,
                GameService gameService) {
        this.id = id;
        this.mapper = mapper;
        this.gameService = gameService;

        playerOrder = new PriorityQueue<>();
        open = true;
    }


    public Packet startGame() throws JsonProcessingException {
        gameInfo = new MiracleFieldInfo(new HashSet<>(playerOrder));

        gameService.startGame(playerOrder, gameInfo);

        return new Packet<>("gameStart","", this.getId());
    }

    public Packet nextTurn() {
        if (gameInfo.isChangeTurn()) {
            playerOrder.add(
                    playerOrder.poll()
            );
            gameInfo.setChangeTurn(true);
        }

        gameInfo.setCurrentPlayer(
                playerOrder.peek()
        );

        return new Packet("startTurn","", "");
    }

    public Packet getWord() {
        return new Packet("wordLength","", String.valueOf(gameInfo.getWord().length()));
    }

    public Packet getWordDescription() {
        return new Packet("wordLength","", gameInfo.getWordDescription());
    }

    public Collection<String> getUsers() {
        return playerOrder;
    }

    public Packet makeTurn(Packet packet) {
        gameService.gameMove(
                gameInfo,
                packet.getToken(),
                packet.getData()
        );
        if (gameInfo.getWinner().equals(gameInfo.getCurrentPlayer())) {
            gameService.finishedGame(gameInfo);
            return new Packet("gameOver", "", "");
        }
        else
            return nextTurn();
    }

    public void addPlayer(String token) {
        if (playerOrder.size() > 3) {
            open = false;
        }
        else {
            playerOrder.add(token);
        }
    }

    public void removePlayer(String token) {
        playerOrder.remove(token);
    }

    public void removeAllPlayers() {
        playerOrder.clear();
    }

    public Integer getId() {
        return id;
    }

    public boolean isOpen() {
        return open;
    }

    public void clean() {
        gameInfo = null;
        playerOrder.clear();
        open = true;
    }
}
