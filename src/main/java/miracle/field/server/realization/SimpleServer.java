package miracle.field.server.realization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import miracle.field.server.handler.BaseHandler;
import miracle.field.server.handler.Handler;
import miracle.field.shared.model.User;
import miracle.field.shared.model.Word;
import miracle.field.shared.packet.Packet;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.*;

@Component
public class SimpleServer extends WebSocketServer implements ApplicationContextAware {

//    Constant values
    private ApplicationContext context;
    private ObjectMapper mapper;
    private Handler handlerChain;
//    Dynamic values
    private Map<String, WebSocket> activeUsers;
    private List<Room> rooms;

    public SimpleServer() {
        super( new InetSocketAddress("127.0.0.1", 55443));
        mapper = new ObjectMapper();
        rooms = new LinkedList<>();
        handlerChain = new BaseHandler();
        activeUsers = new HashMap<>();
    }

    public SimpleServer(InetSocketAddress address) {
        super(address);
        mapper = new ObjectMapper();
        rooms = new LinkedList<>();
        handlerChain = new BaseHandler();
        activeUsers = new HashMap<>();
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        //conn.send("Welcome to the server!"); //This method sends a message to the new client
        System.out.println("Client connected: " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("Closed " + conn.getRemoteSocketAddress() + " with exit code " + code);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("Got message: " + message);
        try {
            Packet p = mapper.readValue(message, Packet.class);
            System.out.println(p.getType());
            p = handlerChain.handle(
                mapper.readValue(message, Packet.class)
            );
            conn.send(
                    mapper.writeValueAsBytes(p)
            );
        } catch (IOException e) {
            System.out.println("Client send not Packet class");
        }
    }

    @Override
    public void onMessage(WebSocket conn, ByteBuffer message ) {
        System.out.println("Got message: " + new String(message.array()));
        try {
            Packet p = handlerChain.handle(
                    mapper.readValue(message.array(), Packet.class)
            );
            conn.send(
                    mapper.writeValueAsBytes(p)
            );
            if (!activeUsers.containsKey(p.getToken()) && !p.getToken().isEmpty())
                activeUsers.put(p.getToken(), conn);
        } catch (IOException e) {
            System.out.println("Client send not Packet class");
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.err.println("Error at " + conn.getRemoteSocketAddress()  + " : " + ex.getMessage());
        try {
            conn.send(
                    mapper.writeValueAsBytes(
                            new Packet<>("messageError", "", ex.getMessage())
                    )
            );
        } catch (JsonProcessingException e) {
            System.out.println("Can not convert error packet to " + conn.getRemoteSocketAddress());
        }
    }

    @Override
    public void onStart() {
        System.out.println("Server started successfully");
        System.out.println("Server port: " + getPort());

        for (Handler handler : context.getBeansOfType(Handler.class).values()) {
            System.out.println("Register handler for type: " + handler.getType());
            handlerChain.registerHandler(handler);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
        this.start();
    }

    public Room getRoomById(Integer id) {
        return rooms.get(id);
    }

    public Room getLastRoom() {
        return rooms.get(rooms.size() - 1);
    }

    public WebSocket getUserByToken(String token) {
        return activeUsers.get(token);
    }

    public Room createNewRoom() {
        Room newRoom = new Room();
        return newRoom;
    }

    //  I'm not quite agreed with this
    public class Room {

        private Integer id;
        private Queue<WebSocket> playerOrder;
        private Map<String, WebSocket> players;
        private String salt;
        private String nextToken;
        private Word word;
        private boolean open;

        public Room() {
            id = rooms.size();
            playerOrder = new PriorityQueue<>();
            players = new HashMap<>();
            salt = String.valueOf(Math.random() * 1000);
            open = true;
        }

        public Set<String> getPlayers() {
            return players.keySet();
        }

        public void startGame() throws JsonProcessingException {
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
    }
}
