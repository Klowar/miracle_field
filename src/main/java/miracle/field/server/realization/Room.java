package miracle.field.server.realization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import miracle.field.server.service.RoomService;
import miracle.field.shared.model.Word;
import miracle.field.shared.packet.Packet;
import org.java_websocket.WebSocket;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class Room {

    private final ObjectMapper mapper;

    private RoomService roomService;

    private Integer id;
    private Queue<WebSocket> playerOrder;
    private Map<String, WebSocket> players;
    private String salt;
    private String nextToken;
    private Word word;
    private boolean open;

    public Room(Integer id, ObjectMapper mapper) {
        this.id = id;
        playerOrder = new PriorityQueue<>();
        players = new HashMap<>();
        salt = String.valueOf(Math.random() * 1000);
        open = true;
        this.mapper = mapper;
    }

    public Set<String> getPlayers() {
        return players.keySet();
    }

    public void startGame() throws JsonProcessingException {

        this. word = roomService.generateWord();

        writeToRoom(
                new Packet<>("wordLength","", word.getWord().length())
        );
        writeToRoom(
                new Packet<>("wordDescription","", word.getDescription())
        );
        playerOrder.addAll(players.values());
        nextTurn();
    }

    public void nextTurn() throws JsonProcessingException {
        WebSocket s = playerOrder.poll();
        playerOrder.add(s);

        s.send(
                mapper.writeValueAsBytes(
                        new Packet("startTurn","", salt)
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

    public boolean isNext(String token) {
//          TODO more complex check
        return (nextToken + salt).equals(token);
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

    @Autowired
    public void setRoomService(RoomService roomService) {
        this.roomService = roomService;
    }
}
