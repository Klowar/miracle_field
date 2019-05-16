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
import java.util.Arrays;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
public class ServerRoomGameTurnTest {

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
    private String[] tokens = new String[4];

    @Before
    public void setUp() throws JsonProcessingException, InterruptedException {
        login("open","sesame", 0);
        login("aider","123456", 1);
        login("anna","123456", 2);
        login("nastya","123456", 3);
        findRoom();
    }

    private void login(String login, String pswd, Integer id) throws JsonProcessingException, InterruptedException {
        Waiter waiter = packet -> tokens[id] = packet.getToken();
        observer.addWaiter("loginSuccess", waiter);

        User user = new User();
        user.setUsername(login);
        user.setPassword(pswd);

        connector.send(
                mapper.writeValueAsBytes(
                        new Packet<>("login", "", user)
                )
        );
        Thread.sleep(1000);
        observer.removeWaiter("loginSuccess", waiter);
    }

    private void findRoom() throws JsonProcessingException, InterruptedException {
        Waiter waiter = System.out::println;
        observer.addWaiter("findRoomSuccess",waiter);
        observer.addWaiter("findRoomError",waiter);
        for (String token : tokens) {
            connector.send(
                    mapper.writeValueAsBytes(
                            new Packet<>("findRoom", token, "")
                    )
            );
            Thread.sleep(1000);
        }
    }

    @Test
    public void testGameTurn() throws InterruptedException, JsonProcessingException {
        final String[] playerToken = new String[1];
        Waiter waiter = System.out::println;
        Waiter waiter1 = packet -> playerToken[0] = packet.getToken();
        observer.addWaiter("startTurn", waiter);
        observer.addWaiter("startGameSuccess", waiter1);
        observer.addWaiter("startGameSuccess", waiter);

        observer.addWaiter("gameTurnSuccess",waiter);
        observer.addWaiter("gameTurnError",waiter);
        observer.addWaiter("gameOver",waiter);
        connector.send(
                mapper.writeValueAsBytes(
                        new Packet<>("startGame", tokens[0], "")
                )
        );
        Thread.sleep(1000);

        connector.send(
                mapper.writeValueAsBytes(
                        new Packet<>("gameTurn", playerToken[0], "f")
                )
        );connector.send(
                mapper.writeValueAsBytes(
                        new Packet<>("gameTurn", playerToken[0], "o")
                )
        );connector.send(
                mapper.writeValueAsBytes(
                        new Packet<>("gameTurn", playerToken[0], "x")
                )
        );
        Thread.sleep(2000);
    }

    @Test
    public void testGameOverInOneWord() throws JsonProcessingException, InterruptedException {
        final String[] playerToken = new String[1];
        Waiter waiter = System.out::println;
        Waiter waiter1 = packet -> playerToken[0] = packet.getToken();
        observer.addWaiter("startTurn", waiter);
        observer.addWaiter("gameOver", waiter);
        observer.addWaiter("messageError", waiter);
        observer.addWaiter("startGameSuccess", waiter1);
        observer.addWaiter("startGameSuccess", waiter);

        observer.addWaiter("gameTurnSuccess",waiter);
        observer.addWaiter("gameTurnError",waiter);
        observer.addWaiter("gameOver",waiter);
        connector.send(
                mapper.writeValueAsBytes(
                        new Packet<>("startGame", tokens[0], "")
                )
        );
        Thread.sleep(1000);
        connector.send(
                mapper.writeValueAsBytes(
                        new Packet<>("gameTurn", playerToken[0], "plane")
                )
        );
        Thread.sleep(2000);
    }

    @Test
    public void testGameOverInMultiQueries() throws JsonProcessingException, InterruptedException {
        final String[] playerToken = new String[1];
        Waiter waiter = System.out::println;
        Waiter waiter1 = packet -> playerToken[0] = packet.getToken();
        observer.addWaiter("startTurn", waiter);
        observer.addWaiter("gameOver", waiter);
        observer.addWaiter("messageError", waiter);
        observer.addWaiter("startGameSuccess", waiter1);
        observer.addWaiter("startGameSuccess", waiter);

        observer.addWaiter("gameTurnSuccess",waiter);
        observer.addWaiter("gameTurnError",waiter);
        observer.addWaiter("gameOver",waiter);
        connector.send(
                mapper.writeValueAsBytes(
                        new Packet<>("startGame", tokens[0], "")
                )
        );
        Thread.sleep(1000);
        connector.send(
                mapper.writeValueAsBytes(
                        new Packet<>("gameTurn", playerToken[0], "p")
                )
        );connector.send(
                mapper.writeValueAsBytes(
                        new Packet<>("gameTurn", playerToken[0], "l")
                )
        );connector.send(
                mapper.writeValueAsBytes(
                        new Packet<>("gameTurn", playerToken[0], "a")
                )
        );connector.send(
                mapper.writeValueAsBytes(
                        new Packet<>("gameTurn", playerToken[0], "n")
                )
        );connector.send(
                mapper.writeValueAsBytes(
                        new Packet<>("gameTurn", playerToken[0], "e")
                )
        );
        Thread.sleep(2000);
    }
}
