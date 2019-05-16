package miracle.field.client.controller;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Data;
import lombok.NoArgsConstructor;
import miracle.field.client.gui.panes.AlphabetPane;
import miracle.field.client.gui.panes.RoulettePane;
import miracle.field.client.util.SpringStageLoader;
import miracle.field.server.gameData.MiracleFieldInfo;
import miracle.field.server.handler.LoginHandler;
import miracle.field.shared.packet.Packet;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

@Component
@Data
@NoArgsConstructor
public class MainController extends AbstractFxmlController {
    private final Logger LOGGER = Logger.getLogger(LoginHandler.class.getName());
    private final String CALL_WORD_STAGE_TITLE = "Назовите слово";
    @FXML
    private RoulettePane roulettePain;
    @FXML
    private Button spinRouletteButton;
    @FXML
    private AlphabetPane alphabetPane;
    @FXML
    private TextArea wordDescriptionArea;
    @FXML
    private TextArea roomChat;
    @FXML
    private TextField newMessage;

    private int shift = 0;

    @Override
    public void getNotify(Packet packet) {
        switch (packet.getType()) {
            case "startGameSuccess":
                removeWaiter("startGameSuccess");
                removeWaiter("startGameError");
                Platform.runLater(() -> {
                    waitDescription();
                    waitstartTurn();
                });

                break;
            case "roomWordDescriptionSuccess":
                removeWaiter("roomWordDescriptionSuccess");
                removeWaiter("roomWordDescriptionError");
                Platform.runLater(() -> {
                    wordDescriptionArea.setText(packet.getData());
                });
                break;
            case "startTurn":
                if (personalMap.get("token").equals(packet.getToken())) {
                    spinRouletteButton.setDisable(false);
                    System.out.println(packet.getData());
                    try {
                        MiracleFieldInfo info = (MiracleFieldInfo) packet.getData(MiracleFieldInfo.class);
                        shift = info.getTurnScore();
                        spinRouletteButton.setDisable(false);
                    } catch (IOException e) {
                        LOGGER.severe("Can not get game info from packet: " + packet);
                    }
                }
                break;
            case "roomChat":
                roomChat.setText(
                        roomChat.getText() + "\n" + packet.getData()
                );
                break;
            default:
                return;
        }
    }


    private void waitstartTurn() {
        sendPacket("gameTurn", (String) personalMap.get("token"), null);
        addWaiter("startTurn");
    }

    @FXML
    public void spinRoulette() {
        roulettePain.spinRoulette(shift);
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
            LOGGER.severe("CAN NOT OPEN CALL WORD WINDOW");
            parentStage.close();
        }

    }

    @FXML
    public void sendMessage() {
        sendPacket("roomChat", (String) personalMap.get("token"), newMessage.getText());
        newMessage.clear();
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
        addWaiter("roomChat");
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
