package miracle.field.client.controller;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Data;
import lombok.NoArgsConstructor;
import miracle.field.client.gui.panes.AlphabetPane;
import miracle.field.client.gui.panes.RoulettePane;
import miracle.field.client.util.SpringStageLoader;
import miracle.field.shared.packet.Packet;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;

@Component
@Data
@NoArgsConstructor
public class MainController extends AbstractFxmlController {
    private final String CALL_WORD_STAGE_TITLE = "Назовите слово";
    @FXML
    private RoulettePane roulettePain;
    @FXML
    private Button spinRouletteButton;
    @FXML
    private AlphabetPane alphabetPane;
    @FXML
    private TextArea wordDescriptionArea;

    @Override
    public void getNotify(Packet packet) {
        switch (packet.getType()) {
            case "startGameSuccess":
                removeWaiter("startGameSuccess");
                removeWaiter("startGameError");
                Platform.runLater(() -> {
                    waitDescription();
                });
                break;
            case "roomWordDescriptionSuccess":
                removeWaiter("roomWordDescriptionSuccess");
                removeWaiter("roomWordDescriptionError");

                Platform.runLater(() -> {
                    wordDescriptionArea.setText(packet.getData());
                });
                break;

            default:
                return;
        }
    }

    @FXML
    public void spinRoulette() {
        roulettePain.spinRoulette();
        spinRouletteButton.setDisable(true);
    }

    @FXML
    public void openCallWordWindow() {
        Stage parentStage = (Stage) roulettePain.getScene().getWindow();
        try {
            Stage modalStage = getContext().getBean(SpringStageLoader.class).loadModalWindow(parentStage, "callWord", CALL_WORD_STAGE_TITLE, new HashMap<>(), Modality.WINDOW_MODAL);
            modalStage.setX(parentStage.getX() + 200);
            modalStage.setY(parentStage.getY() + 100);
            modalStage.show();
        } catch (IOException e) {
            //TODO
        }

    }

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            startGame();
            addActions();
        });
    }

    private void chooseLetter(String letter) {
        String token = (String) personalMap.get("token");
        sendPacket("gameTurn", token, letter);
    }

    private void startGame() {
        sendPacket("startGame", (String) personalMap.get("token"), null);
        addWaiter("startGameSuccess");
        addWaiter("startGameError");
    }

    private void waitDescription() {
        sendPacket("roomWordDescription", (String) personalMap.get("token"), null);
        addWaiter("roomWordDescriptionSuccess");
        addWaiter("roomWordDescriptionError");
    }

    private void addActions() {
        ObservableList<Node> alphabetPaneChildren = alphabetPane.getChildren();
        for (Node node : alphabetPaneChildren) {
            if (node.getClass().equals(Button.class)) {
                Button button = (Button) node;
                button.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                    chooseLetter(button.getText());
                });
            }
        }
    }
}
