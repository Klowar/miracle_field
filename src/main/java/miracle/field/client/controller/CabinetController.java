package miracle.field.client.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import lombok.Data;
import lombok.NoArgsConstructor;
import miracle.field.client.util.SpringStageLoader;
import miracle.field.shared.model.Statistic;
import miracle.field.shared.model.User;
import miracle.field.shared.packet.Packet;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

@Component
@Data
@NoArgsConstructor
public class CabinetController extends AbstractFxmlController {
    private final String MAIN_SCENE = "main";
    @FXML
    private Label username;
    @FXML
    private Label wins;
    @FXML
    private Label loses;
    @FXML
    private Label gameTotal;
    @FXML
    private Label score;

    private final Logger LOGGER = Logger.getLogger(CabinetController.class.getName());

    private void seTextUsername() {
        User user = (User) personalMap.get("user");
        username.setText(user.getUsername());
    }

    private void seStatistic() {
        User user = (User) personalMap.get("user");
        Statistic statistic = user.getStatistic();
        wins.setText(String.valueOf(statistic.getWins()));
        loses.setText(String.valueOf(statistic.getLoses()));
        gameTotal.setText(String.valueOf(statistic.getGameTotal()));
        score.setText(String.valueOf(statistic.getScore()));
    }


    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            seTextUsername();
            seStatistic();
        });

    }

    @Override
    public void getNotify(Packet packet) {
        switch (packet.getType()){
            case "findRoomError":
            case "findRoomSuccess":
                Platform.runLater(() -> {
                    mainSceneLoad();
                });
                removeWaiter("findRoomSuccess");
                removeWaiter("findRoomError");
                break;
            default:
                break;
        }
    }

    private void mainSceneLoad()  {
        Stage stage = (Stage) username.getScene().getWindow();
        Scene mainScene = null;
        try {
            mainScene = getContext().getBean(SpringStageLoader.class).loadScene(MAIN_SCENE, new HashMap<>());
        } catch (IOException e) {
            LOGGER.severe("CAN NOT LOAD SCENE");
        }
        stage.setScene(mainScene);
        stage.show();
    }

    @FXML
    public void searchGame() {
        sendFindRoomPacket();
    }

    private void sendFindRoomPacket() {
        sendPacket("findRoom", (String) personalMap.get("token"), null);
        addWaiter("findRoomSuccess");
        addWaiter("findRoomError");
    }

}
