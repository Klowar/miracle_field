package miracle.field.client.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import lombok.Data;
import lombok.NoArgsConstructor;
import miracle.field.client.util.SpringStageLoader;
import miracle.field.client.util.Waiter;
import miracle.field.shared.model.Statistic;
import miracle.field.shared.model.User;
import miracle.field.shared.packet.Packet;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

    @Override
    public void initData(Map<String, Object> data) {
        super.initData(data);
    }

    @FXML
    public void initialize() {
        super.initialize();
        Platform.runLater(() -> {
            seTextUsername();
            seStatistic();
        });

    }

    @Override
    public void getNotify(Packet packet) {
        if (packet.getType().equals("findRoomSuccess")) {
            Map<String, Waiter> addWaitersMap = new HashMap<>();
            addWaitersMap.put("findRoomSuccess", this);
            addWaitersMap.put("findRoomError", this);
            helper.addWaiters(addWaitersMap);
            Platform.runLater(() -> {
                try {
                    mainSceneLoad();
                } catch (IOException e) {
                    //TODO
                }
            });
        } else if (packet.getType().equals("findRoomError")) {
            System.out.println("И как ты это сделал???? " + packet.getData());
        }
    }

    private void mainSceneLoad() throws IOException {
        Stage stage = (Stage) username.getScene().getWindow();
        Scene mainScene = getContext().getBean(SpringStageLoader.class).loadScene(MAIN_SCENE, new HashMap<>());
        stage.setScene(mainScene);
        stage.show();
    }

    @FXML
    public void searchGame() {
        sendFindRoomPacket();
    }

    private void sendFindRoomPacket() {
        helper.sendPacket("findRoom", (String) personalMap.get("token"), null);
        Map<String, Waiter> addWaitersMap = new HashMap<>();
        addWaitersMap.put("findRoomSuccess", this);
        addWaitersMap.put("findRoomError", this);
        helper.addWaiters(addWaitersMap);
    }

}
