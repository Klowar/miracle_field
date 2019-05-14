package miracle.field.client.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import miracle.field.shared.model.User;
import miracle.field.shared.packet.Packet;
import org.java_websocket.client.WebSocketClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.net.URI;
import java.net.URISyntaxException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
public class ServerRoomChatTest {

    @Configuration
    static class ContextConfiguration {
        @Bean
        public WebSocketClient connector() throws URISyntaxException {
            WebSocketClient client = new WebSocketServerConnector(
                    new URI("ws://localhost:55443")
            );
            client.connect();
            return client;
        }
        @Bean
        public ObjectMapper mapper() {
            return new ObjectMapper();
        }

        @Bean
        public Observer observer() {
            return new Observer();
        }
    }

    @Autowired
    private WebSocketClient connector;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private Observer observer;
    private Waiter waiter;
    private String token;
    private String token1;

    @Before
    public void setUp() throws JsonProcessingException, InterruptedException {
        waiter = packet -> token = packet.getToken();
        observer.addWaiter("loginSuccess", waiter);

        User user = new User();
        user.setUsername("open");
        user.setPassword("sesame");

        connector.send(
                mapper.writeValueAsBytes(
                        new Packet<>("login", "", user)
                )
        );
        Thread.sleep(3000);
        observer.removeWaiter("loginSuccess", waiter);
        User user1 = new User();
        user1.setUsername("aider");
        user1.setPassword("123456");

        waiter = packet -> token1 = packet.getToken();
        observer.addWaiter("loginSuccess", waiter);
        connector.send(
                mapper.writeValueAsBytes(
                        new Packet<>("login", "", user1)
                )
        );
        Thread.sleep(3000);
        observer.removeWaiter("loginSuccess", waiter);
    }
    @Test
    public void testRoomFind() throws InterruptedException, JsonProcessingException {
        waiter = packet -> System.out.println(packet.getSerializedData());
        observer.addWaiter("roomFindSuccess", waiter);
        observer.addWaiter("roomChat", waiter);

        connector.send(
                mapper.writeValueAsBytes(
                        new Packet<>("findRoom", token, "")
                )
        );
        connector.send(
                mapper.writeValueAsBytes(
                        new Packet<>("findRoom", token1, "")
                )
        );
        Thread.sleep(3000);
        connector.send(
                mapper.writeValueAsBytes(
                        new Packet<>("roomChat", token, "hello")
                )
        );
        Thread.sleep(4000);
    }
}
