package miracle.field.server.realization;

import miracle.field.shared.packet.Packet;
import miracle.field.server.handler.BaseHandler;
import miracle.field.server.handler.Handler;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class ServerIO implements ServerInterface, ApplicationContextAware {
    // CONSTANTS
    private final Integer DEFAULT_PORT = 55443;
    // Seems like final objects
    private ApplicationContext context;
    private ServerSocket server;
    // Changeable objects
    private Set<Socket> clients;
    private List<Room> rooms;
    private Handler handlerChain;

    public ServerIO() {}

    @Override
    public void init(Integer port) {
        System.out.println("Starting server");
        System.out.println("Port: " + port);

        handlerChain = new BaseHandler();
        clients = new HashSet<>();
        try {
            server = new ServerSocket(port);
            for (Handler handler : context.getBeansOfType(Handler.class).values()) {
                System.out.println("Register handler for type: " + handler.getType());
                registerHandler(handler);
            }
        } catch (IOException e) {
            System.out.println("Can not start server");
        } catch (NullPointerException e) {
            System.out.println("No handlers");
        }
    }

    @Override
    public void start() {
        System.out.println("Start listening for clients");
        new Thread(this::waitConnections).start();
    }

    private void waitConnections() {
        Socket client = null;
        try {
            client = server.accept();
            clients.add(client);
            new Thread(new Client(client)).start();
            System.out.println("Client connected");
        } catch (IOException e) {
            System.out.println("Problem with client connection");
            clients.remove(client);
        }
        waitConnections();
    }

    @Override
    public void registerHandler(Handler handler) {
        handlerChain.registerHandler(handler);
    }

    @Override
    public Packet handlePacket(Packet packet) {
//      TODO some filter of wrong packets here
        return handlerChain.handle(packet);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
        init(DEFAULT_PORT);
        start();
    }

    private class Client implements Runnable {

        private Socket socket;
        private ObjectInputStream ois;
        private ObjectOutputStream oos;

        public Client(Socket socket) throws IOException {
            this.socket = socket;
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
        }

        @Override
        public void run() {
            System.out.println("Start listening client");
            listen();
        }

        private void listen() {
            Packet packet = null;
            try {
                packet = (Packet) ois.readObject();
                System.out.println("Get packet: " + packet.getType());
                packet = handlePacket(packet);
                System.out.println("Created packet: " + packet.getType());
                oos.writeObject(packet);
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Problem with client connection");
                clients.remove(socket);
                return;
            }
            if (packet.getType().equals("enterRoom")) {
                return;
            }
            listen();
        }

    }

//  I'm not quite agreed with this
    private class Room {
//      String is a CSRF token
        private Map<String, Socket> players;

        public void writeToOne(Packet packet, String key) {
            try {
                ObjectOutputStream stream = new ObjectOutputStream(
                        players.get(key).getOutputStream()
                );
                stream.writeObject(packet);
            } catch (IOException e) {
                clients.remove(players.get(key));
                System.out.println("Can not write packet " + packet);
            }
        }

        public void writeToRoom(Packet packet) {
            ObjectOutputStream stream = null;
            for (Socket s : players.values()) {
                try {
                    stream = new ObjectOutputStream(s.getOutputStream());
                    stream.writeObject(packet);
                } catch (IOException e) {
                    clients.remove(s);
                    System.out.println("Can not write packet " +
                            packet + " to player " + s );
                }
            }
        }
    }
}
