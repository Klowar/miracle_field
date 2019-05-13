package miracle.field.client.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import lombok.NoArgsConstructor;
import miracle.field.client.gui.panes.RoulettePane;
import miracle.field.shared.packet.Packet;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class MainController extends AbstractFxmlController {
    @FXML private RoulettePane roulettePain;
    @FXML private Button spinRouletteButton;

    @FXML
    public void spinRoulette() {
        roulettePain.spinRoulette();
        spinRouletteButton.setDisable(true);
    }

    @Override
    public void getNotify(Packet packet) {

    }
}
