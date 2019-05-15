package miracle.field.client.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import miracle.field.client.gui.panes.AlphabetPane;
import miracle.field.client.gui.panes.RoulettePane;
import miracle.field.client.util.SpringStageLoader;
import miracle.field.client.util.Waiter;
import miracle.field.shared.packet.Packet;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@Data
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

    public MainController() {
    }

    @Override
    public void getNotify(Packet packet) {
        switch (packet.getType()) {
            case "roomWordDescription":
                HashMap<String, Waiter> removeWaitersMap = new HashMap<>();
                removeWaitersMap.put("roomWordDescription", this);
                helper.removeWaiters(removeWaitersMap);
                Platform.runLater(() -> {
                    wordDescriptionArea.setText(packet.getData());
                });
                break;
            case "":
//
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
        super.initialize();
        sendStartGamePacket();
        sendRoomWordDescriptionPacket();
        Platform.runLater(() -> {
            ObservableList<Node> alphabetPaneChildren = alphabetPane.getChildren();
            for (Node node : alphabetPaneChildren) {
                if (node.getClass().equals(Button.class)) {
                    Button button = (Button) node;
                    button.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                        chooseLetter(button.getText());
                    });
                }
            }
        });
    }

    private void chooseLetter(String letter) {
        String token = (String) personalMap.get("token");
        helper.sendPacket("gameTurn", token, letter);
    }

    private void sendStartGamePacket() {
        helper.sendPacket("startGame", (String) personalMap.get("token"), null);
        Map<String, Waiter> addWaitersMap = new HashMap<>();
        addWaitersMap.put("startGameSuccess", this);
        addWaitersMap.put("startGameError", this);
        helper.addWaiters(addWaitersMap);
    }

    private void sendRoomWordDescriptionPacket() {
        helper.sendPacket("roomWordDescription", (String) personalMap.get("token"), null);
        Map<String, Waiter> addWaitersMap = new HashMap<>();
        addWaitersMap.put("roomWordDescriptionSuccess", this);
        addWaitersMap.put("roomWordDescriptionError", this);
        helper.addWaiters(addWaitersMap);
    }


    @Override
    public void initData(Map<String, Object> data) {
        super.initData(data);
    }
}
