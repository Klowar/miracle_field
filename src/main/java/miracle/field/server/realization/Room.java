package miracle.field.server.realization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import miracle.field.server.gameData.MiracleFieldInfo;
import miracle.field.server.service.GameService;
import miracle.field.shared.packet.Packet;

import java.util.*;
import java.util.logging.Logger;

public class Room {

    private Integer id;
    private boolean open;

    private GameService gameService;
    private MiracleFieldInfo gameInfo;

    private Queue<String> playerOrder;

    private final Logger LOGGER = Logger.getLogger(Room.class.getName());

    public Room(Integer id,
                ObjectMapper mapper,
                GameService gameService) {
        this.id = id;
        this.gameService = gameService;

        playerOrder = new LinkedList<>();
        open = true;
    }


    public Packet startGame() throws JsonProcessingException {
        gameInfo = new MiracleFieldInfo(new HashSet<>(playerOrder));

        gameService.startGame(playerOrder, gameInfo);

        LOGGER.info("Game started. Word is " + gameInfo.getWord());

        return new Packet<>("startGameSuccess", gameInfo.getCurrentPlayer(), gameInfo);
    }

    public Packet nextTurn() throws JsonProcessingException {
        if (gameInfo.isChangeTurn()) {
            playerOrder.add(
                    playerOrder.poll()
            );
        }

        gameInfo.setCurrentPlayer(
                playerOrder.peek()
        );
        gameInfo.setChangeTurn(true);

        return new Packet("startTurn", gameInfo.getCurrentPlayer(), gameInfo);
    }

    public Packet getWordDescription() {
        return new Packet("roomWordDescriptionSuccess","", gameInfo.getWordDescription());
    }

    public Collection<String> getUsers() {
        return playerOrder;
    }

    public Packet makeTurn(Packet packet) throws JsonProcessingException {

        LOGGER.info("Got " + packet);

        if (!packet.getToken().equals(gameInfo.getCurrentPlayer()))
            return new Packet("gameTurnError", "", gameInfo);

        gameService.gameMove(
                gameInfo,
                packet.getToken(),
                packet.getData()
        );

        if (gameInfo.getCurrentPlayer().equals(gameInfo.getWinner())) {
            return new Packet("gameOver", "", gameInfo);
        }
        else {
            return new Packet("gameTurnSuccess","", gameInfo);
        }
    }

    public void addPlayer(String token) {
        if (open) {
            playerOrder.add(token);
            if (playerOrder.size() > 3)
                open = false;
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
