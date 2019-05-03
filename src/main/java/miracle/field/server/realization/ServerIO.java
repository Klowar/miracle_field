package miracle.field.server.realization;

import miracle.field.packet.Packet;
import miracle.field.server.handler.BaseHandler;
import miracle.field.server.handler.Handler;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class ServerIO implements ServerInterface {
    // CONSTANTS
    private final Integer DEFAULT_PORT = 55443;
    // Seems like final objects
    private ApplicationContext context;
    private ServerSocket server;
    // Changeable objects
    private Set<Socket> clients;
    private Set<Room> rooms;
    private Handler handlerChain;

    public ServerIO(Integer port) {
        init(port);
    }

    public ServerIO() {
        init(DEFAULT_PORT);
    }

    @Override
    public void init(Integer port) {
        System.out.println("Starting server");
        System.out.println("Port: " + port);

        handlerChain = new BaseHandler();
        clients = new HashSet<>();
        try {
            server = new ServerSocket(port);
            for (Handler handler : context.getBeansOfType(Handler.class).values()) {
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
            System.out.println("Client connected");
        } catch (IOException e) {
            System.out.println("Problem with client connection");
        }
        clients.add(client);
        waitConnections();
    }

    @Override
    public void registerHandler(Handler handler) {
        handlerChain.registerHandler(handler);
    }

    @Override
    public void handlePacket(Packet packet) {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    private class Room {
        private Set<Socket> players;

        public void writeToRoom(Packet packet) {
            ObjectOutputStream stream = null;
            for (Socket s : players) {
                try {
                    stream = new ObjectOutputStream(s.getOutputStream());
                    stream.writeObject(packet);
                } catch (IOException e) {
                    System.out.println("Can not write packet " +
                            packet + "to player " + s );
                }
            }
        }

    }


}
