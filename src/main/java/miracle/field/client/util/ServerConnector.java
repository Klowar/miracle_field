package miracle.field.client.util;

import org.springframework.stereotype.Component;

import java.beans.PropertyChangeListener;
import java.io.IOException;
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

    public void write(Object object) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(object);
    }

    public void getObject() {
//        TODO some logic
    }


}
