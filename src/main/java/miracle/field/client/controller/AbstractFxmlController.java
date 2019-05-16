package miracle.field.client.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import lombok.Data;
import miracle.field.client.util.Observer;
import miracle.field.client.util.Waiter;
import miracle.field.shared.packet.Packet;
import org.java_websocket.client.WebSocketClient;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Data
public abstract class AbstractFxmlController implements ApplicationContextAware, Waiter {
    protected Map<String, Object> personalMap;
    private ApplicationContext context;

    public ApplicationContext getContext() {
        return context;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    public void initData(Map<String, Object> data) {
        personalMap = new HashMap<>();
        personalMap.putAll(data);
    }
    protected void sendPacket(String type, String token, Object data) {
        try {
            getContext().getBean(WebSocketClient.class).send(
                    getContext().getBean(ObjectMapper.class).writeValueAsBytes(
                            new Packet<>(type, token, data)
                    )
            );
        } catch (IOException e) {
            System.out.println("Can not write " + type + " packet to server");
        }
        System.out.println(type + " packet sent...");
    }
    protected void addWaiter(String name) {
        getContext().getBean(Observer.class).addWaiter(name, this);
    }

    protected void removeWaiter(String name) {
        getContext().getBean(Observer.class).removeWaiter(name, this);
    }

}
