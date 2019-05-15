package miracle.field.client.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import lombok.Data;
import lombok.NoArgsConstructor;
import miracle.field.client.util.Observer;
import miracle.field.client.util.SpringStageLoader;
import miracle.field.shared.model.Statistic;
import miracle.field.shared.model.User;
import miracle.field.shared.packet.Packet;
import org.java_websocket.client.WebSocketClient;
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

    private void seTextUsername(){
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
    public void initData(Map<String, Object> data){
        super.initData(data);
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
        if(packet.getType().equals("findRoomSuccess")){
            getContext().getBean(Observer.class).removeWaiter("findRoomSuccess", this);
            Platform.runLater(() -> {
                try {
                    mainSceneLoad();
                } catch (IOException e) {
                    //TODO
                }
            });
        }
        else if(packet.getType().equals("findRoomError")) {
            System.out.println(packet.getData());
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
        try {
            getContext().getBean(WebSocketClient.class).send(
                    getContext().getBean(ObjectMapper.class).writeValueAsBytes(
                            new Packet<>("findRoom", (String)personalMap.get("token"), null)
                    )
            );
            getContext().getBean(Observer.class).addWaiter("findRoomSuccess", this);
            getContext().getBean(Observer.class).addWaiter("findRoomError", this);

        } catch (IOException e) {
            System.out.println("Can not write Find Room packet to server");
        }
        System.out.println("Find Room packet sent...");
        }

}
