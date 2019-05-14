package miracle.field.server.realization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import miracle.field.server.gameData.MiracleFieldInfo;
import miracle.field.server.service.GameService;
import miracle.field.shared.packet.Packet;
import org.java_websocket.WebSocket;

import java.util.*;

public class Room {

    private Integer id;
    private boolean open;

    private final ObjectMapper mapper;

    private GameService gameService;
    private MiracleFieldInfo gameInfo;

    private Queue<WebSocket> playerOrder;
    private Map<String, WebSocket> players;
    private String nextToken;

    public Room(Integer id,
                ObjectMapper mapper) {
        this.id = id;
        playerOrder = new PriorityQueue<>();
        players = new HashMap<>();
        open = true;
        this.mapper = mapper;
        this.gameService = gameService;
    }

    public Set<String> getPlayers() {
        return players.keySet();
    }

    public void startGame() throws JsonProcessingException {
//      ToDo: what is start???
        gameInfo = new MiracleFieldInfo(players.keySet());

        gameService.startGame(players, gameInfo);

        writeToRoom(
                new Packet<>("gameInfo","", gameInfo)
        );

        playerOrder.addAll(players.values());
        nextTurn();
    }

    public void nextTurn() throws JsonProcessingException {
        WebSocket s = playerOrder.poll();
        playerOrder.add(s);

        s.send(
                mapper.writeValueAsBytes(
                        new Packet("startTurn","", "")
                )
        );
        setNextToken(
                players.entrySet()
                        .stream()
                        .filter(entry -> s.equals(entry.getValue()))
                        .map(Map.Entry::getKey)
                        .findFirst().get()
        );
    }

    public void writeToOne(Packet packet, String token) throws JsonProcessingException {
        players.get(token).send(
                mapper.writeValueAsBytes(packet)
        );
    }

    public void addPlayer(WebSocket webSocket, String token) {
        players.put(token, webSocket);
        if (players.size() > 3) {
            open = false;
            try {
                startGame();
            } catch (JsonProcessingException e) {

            }
        }
    }

    public void removePlayer(String token) {
        players.remove(token);
    }

    public void removeAllPlayers() {
        players.clear();
    }

    public WebSocket getNext() {
        return playerOrder.peek();
    }

    public void setNext(String token) {
        playerOrder.add(
                players.get(token)
        );
    }

    public String getNextToken() {
        return nextToken;
    }


    public void setNextToken(String nextToken) {
        this.nextToken = nextToken;
    }

    public void writeToRoom(Packet packet) throws JsonProcessingException {
        for (WebSocket s : players.values()) {
            s.send(
                    mapper.writeValueAsBytes(packet)
            );
        }
    }

    public Integer getId() {
        return id;
    }

    public boolean isOpen() {
        return open;
    }

}
