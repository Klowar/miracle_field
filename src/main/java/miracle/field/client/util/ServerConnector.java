package miracle.field.client.util;

import miracle.field.shared.packet.Packet;
import org.springframework.stereotype.Component;

import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

@Component
public class ServerConnector {

    private Socket socket;
    private PropertyChangeListener listener;

    public ServerConnector() throws IOException {
        this.socket = new Socket("localhost", 55443);
    }

    public ServerConnector(String host, Integer port) throws IOException {
        this.socket = new Socket(host, port);
    }

    public void writeObject(Object object) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(object);
    }

    public Packet readObject() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        return (Packet) ois.readObject();
    }


}
