package miracle.field.client.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import lombok.Data;
import lombok.NoArgsConstructor;
import miracle.field.shared.packet.Packet;
import org.springframework.stereotype.Component;


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
    public void initialize() {
        super.initialize();
    }

    @FXML
    private void callWord() {
        String word = wordF.getText();
        String token = (String) personalMap.get("token");
        helper.sendPacket("gameTurn", token, word);
    }
}
