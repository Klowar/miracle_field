package miracle.field.client.controller;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import lombok.NoArgsConstructor;
import miracle.field.client.gui.panes.AlphabetPane;
import miracle.field.client.gui.panes.RoulettePane;
import miracle.field.shared.packet.Packet;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@NoArgsConstructor
public class MainController extends AbstractFxmlController {
    @FXML private RoulettePane roulettePain;
    @FXML private Button spinRouletteButton;
    @FXML private AlphabetPane alphabetPane;

    @FXML
    public void spinRoulette() {
        roulettePain.spinRoulette();
        spinRouletteButton.setDisable(true);
    }
    @FXML
    public void chooseLetter(String text){


    }

    @Override
    public void getNotify(Packet packet) {

    }

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            ObservableList<Node> alphabetPaneChildren = alphabetPane.getChildren();
            for(Node node : alphabetPaneChildren) {
                Button button = (Button)node;
               button.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                   chooseLetter(button.getText());
               });
            }
        });
    }
}
