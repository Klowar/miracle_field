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
public class ServerRoomFindTest {

    @Configuration
    static class ContextConfiguration {

        // this bean will be injected into the OrderServiceTest class
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

    @Before
    public void setUp() throws JsonProcessingException {
        waiter = packet -> token = packet.getToken();
        observer.addWaiter("loginSuccess", waiter);

        User user = new User();
        user.setUsername("greenmapc");
        user.setPassword("123456");

        connector.send(
                mapper.writeValueAsBytes(
                        new Packet<>("login", "", user)
                )
        );
    }
    @Test
    public void testRoomFind() throws InterruptedException, JsonProcessingException {
        waiter = packet -> Assert.assertTrue(true);
        observer.addWaiter("roomFindSuccess", waiter);
        connector.send(
                mapper.writeValueAsBytes(
                        new Packet<>("findRoom", token, "")
                )
        );
        Thread.sleep(3000);
    }
}
