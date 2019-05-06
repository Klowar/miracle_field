package miracle.field.client.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import miracle.field.client.util.WebSocketServerConnector;
import org.java_websocket.client.WebSocketClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
@ComponentScan({
        "miracle.field.client.controller",
        "miracle.field.client.util"
})
@PropertySource("classpath:app.properties")
public class Config {

    @Resource
    private Environment env;

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


}
