package miracle.field.server.realization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import miracle.field.server.handler.BaseHandler;
import miracle.field.server.handler.Handler;
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

    private ApplicationContext context;
    private ObjectMapper mapper;
    private List<Room> rooms;
    private Handler handlerChain;

    public SimpleServer() {
        super( new InetSocketAddress("127.0.0.1", 55443));
        mapper = new ObjectMapper();
        rooms = new LinkedList<>();
        handlerChain = new BaseHandler();
    }

    public SimpleServer(InetSocketAddress address) {
        super(address);
        mapper = new ObjectMapper();
        rooms = new LinkedList<>();
        handlerChain = new BaseHandler();
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        //conn.send("Welcome to the server!"); //This method sends a message to the new client
        System.out.println("Client connected");
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

    //  I'm not quite agreed with this
    private class Room {
        //      String is a CSRF token
        private Map<String, WebSocket> players;
        private String salt;

        public void writeToOne(Packet packet, String key) {
            try {
                players.get(key).send(
                        mapper.writeValueAsBytes(packet)
                );
            } catch (JsonProcessingException e) {
                System.out.println("Can not convert packet to bytes " + packet);
            }
        }

        public void writeToRoom(Packet packet) {
            for (WebSocket s : players.values()) {
                try {
                    s.send(
                            mapper.writeValueAsBytes(packet)
                    );
                } catch (JsonProcessingException e) {
                    System.out.println("Can not convert packet to bytes " + packet);
                }
            }
        }
    }
}
