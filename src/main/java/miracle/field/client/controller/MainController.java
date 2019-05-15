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
import miracle.field.client.util.Observer;
import miracle.field.client.util.SpringStageLoader;
import miracle.field.shared.packet.Packet;
import org.java_websocket.client.WebSocketClient;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@Data
public class MainController extends AbstractFxmlController {
    private final String CALL_WORD_STAGE_TITLE = "Назовите слово";
    @FXML private RoulettePane roulettePain;
    @FXML private Button spinRouletteButton;
    @FXML private AlphabetPane alphabetPane;
    @FXML private TextArea wordDescriptionArea;

    public MainController() {
    }

    @Override
    public void getNotify(Packet packet) {
        switch (packet.getType()) {
            case "roomWordDescription":
                getContext().getBean(Observer.class).removeWaiter("roomWordDescription", this);
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
    public void openCallWordWindow(){
        Stage parentStage = (Stage) roulettePain.getScene().getWindow();
        try {
            Stage modalStage = getContext().getBean(SpringStageLoader.class).loadModalWindow(parentStage, "callWord",CALL_WORD_STAGE_TITLE, new HashMap<>(), Modality.WINDOW_MODAL);
            modalStage.setX(parentStage.getX() + 200);
            modalStage.setY(parentStage.getY() + 100);
            modalStage.show();
        } catch (IOException e) {
            //TODO
        }

    }

    @FXML
    public void initialize() {
        getContext().getBean(Observer.class).addWaiter("roomWordDescriptionSuccess", this);
        getContext().getBean(Observer.class).addWaiter("roomWordDescriptionError", this);
//        getContext().getBean(Observer.class).addWaiter("", this);
        Platform.runLater(() -> {
            ObservableList<Node> alphabetPaneChildren = alphabetPane.getChildren();
            for(Node node : alphabetPaneChildren) {
                if(node.getClass().equals(Button.class)) {
                    Button button = (Button)node;
                    button.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                        chooseLetter(button.getText());
                    });
                }
            }
        });
    }
    private void chooseLetter(String letter){
        String token = (String) personalMap.get("token");
        try {
            getContext().getBean(WebSocketClient.class).send(
                    getContext().getBean(ObjectMapper.class).writeValueAsBytes(
                            new Packet<>("gameTurn", token, letter)
                    )
            );
        } catch (IOException e) {
            System.out.println("Can not write gameTurn packet to server");
        }
        System.out.println("gameTurn packet sent...");
    }


    @Override
    public void initData(Map<String, Object> data){
        super.initData(data);
    }
}
