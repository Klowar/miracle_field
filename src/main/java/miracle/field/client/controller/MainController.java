package miracle.field.client.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Arc;
import javafx.stage.Stage;
import javafx.util.Duration;
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
