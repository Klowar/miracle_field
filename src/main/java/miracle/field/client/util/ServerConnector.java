package miracle.field.client.util;

import miracle.field.shared.packet.Packet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

@Component
public class ServerConnector {

    private Socket socket;
    private Observer observer;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    @Autowired
    public ServerConnector(Observer observer) {
        this.observer = observer;
        try {
            this.socket = new Socket("localhost", 55443);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
            new Thread(this::readObject).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void writeObject(Object object) {
        try {
            oos.writeObject(object);
        } catch (IOException e) {
            System.out.println("Can not write package");
        }
    }

    public void readObject() {
        Packet packet = null;
        try {
            packet = (Packet) ois.readObject();
            observer.notifyClients(packet);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return;
//            TODO handle 1) Connection broken !important 2) Server sent not Packet class
        }
        readObject();
    }
}
