package miracle.field.client.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import miracle.field.shared.packet.Packet;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;

public class WebSocketServerConnector extends WebSocketClient {
        @Autowired
        private Observer observer;
        private ObjectMapper mapper;

        public WebSocketServerConnector(URI serverUri, Draft draft) {
            super(serverUri, draft);
            mapper = new ObjectMapper();
        }

        public WebSocketServerConnector(URI serverURI) {
            super(serverURI);
            mapper = new ObjectMapper();
        }

        @Override
        public void onOpen(ServerHandshake handshake) {
//          TODO
        }

        @Override
        public void onClose(int code, String reason, boolean remote) {
//          TODO
        }

        @Override
        public void onMessage(String message) {
            try {
                observer.notifyClients(
                    mapper.readValue(message, Packet.class)
                );
            } catch (IOException e) {
                System.out.println("Server sent not packet class");
            }
        }

        @Override
        public void onMessage(ByteBuffer message) {
            try {
                observer.notifyClients(
                        mapper.readValue(message.array(), Packet.class)
                );
            } catch (IOException e) {
                System.out.println("Server sent not packet class");
            }
        }

        @Override
        public void onError(Exception ex) {
            System.err.println("An error occurred:" + ex.getMessage());
        }

}
