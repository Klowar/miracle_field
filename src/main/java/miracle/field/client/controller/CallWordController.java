package miracle.field.client.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import lombok.Data;
import lombok.NoArgsConstructor;
import miracle.field.client.util.Observer;
import miracle.field.shared.packet.Packet;
import org.java_websocket.client.WebSocketClient;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
@NoArgsConstructor
@Data
public class CallWordController extends AbstractFxmlController {
    @FXML
    private TextField wordF;

    @Override
    public void getNotify(Packet packet) {

    }

    @FXML
    private void callWord() {
        String word = wordF.getText();
        String token = (String) personalMap.get("token");
        try {
            getContext().getBean(WebSocketClient.class).send(
                    getContext().getBean(ObjectMapper.class).writeValueAsBytes(
                            new Packet<>("gameTurn", token, word)
                    )
            );
            //TODO
//            getContext().getBean(Observer.class).addWaiter("????", this);
//            getContext().getBean(Observer.class).addWaiter("????", this);
        } catch (IOException e) {
            System.out.println("Can not write gameTurn packet to server");
        }
        System.out.println("gameTurn packet sent...");
    }
}
