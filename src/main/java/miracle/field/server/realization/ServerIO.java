package miracle.field.server.realization;

import miracle.field.server.controller.BaseHandler;
import miracle.field.server.controller.Handler;
import miracle.field.server.packet.Packet;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class ServerIO implements ServerInterface {

    private final Integer DEFAULT_PORT = 55443;
    private ApplicationContext context;
    private ServerSocket server;
    private List<Socket> clients;
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
        try {
            server = new ServerSocket(port);
            for (Handler handler : context.getBeansOfType(Handler.class).values()) {
                handlerChain.registerHandler(handler);
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
        Object result = handlerChain.handle(packet);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
