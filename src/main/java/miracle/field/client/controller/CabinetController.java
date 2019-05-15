package miracle.field.client.controller;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.Data;
import lombok.NoArgsConstructor;
import miracle.field.shared.model.Statistic;
import miracle.field.shared.model.User;
import miracle.field.shared.packet.Packet;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Data
@NoArgsConstructor
public class CabinetController extends AbstractFxmlController {
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

    }

    @FXML
    public void searchGame(){

    }

}
