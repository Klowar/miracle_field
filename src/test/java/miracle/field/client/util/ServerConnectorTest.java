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
public class ServerConnectorTest {

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

    @Before
    public void setUp() {
        waiter = packet -> System.out.println(packet.getType());
    }

    @Test
    public void testRoomChat() throws JsonProcessingException, InterruptedException {
        waiter = packet -> Assert.assertEquals("loginSuccess", packet.getType());
        observer.addWaiter("loginError", waiter);
        observer.addWaiter("loginSuccess", waiter);

        User user = new User();
        user.setUsername("greenmapc");
        user.setPassword("123456");
        user.setConfirmPassword("123456");

        connector.send(
                mapper.writeValueAsBytes(
                        new Packet<>("login","", user)
                )
        );

//        ToDo: find how send user token
        connector.send(
                mapper.writeValueAsBytes(
                        new Packet<>("findRoom", "", "")
                )
        );
        Thread.sleep(100000);
    }

}