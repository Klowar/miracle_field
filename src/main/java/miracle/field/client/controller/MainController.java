package miracle.field.client.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.NoArgsConstructor;
import miracle.field.client.util.Observer;
import miracle.field.client.util.SpringStageLoader;
import miracle.field.shared.model.User;
import miracle.field.shared.packet.Packet;
import org.java_websocket.client.WebSocketClient;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@NoArgsConstructor
public class MainController extends AbstractFxmlController {
    private static final String SIGN_UP_STAGE = "sign_up";

    @FXML private TextField user;
    @FXML private PasswordField password;
    @FXML private Button loginButton;
    @FXML private Button signUpButton;


    @FXML
    public void onClickLoad() {
        User tempUser = new User();
        tempUser.setUsername(user.getText());
        tempUser.setPassword(password.getText());
        try {
            System.out.println(
                    getContext().getBean(ObjectMapper.class).writeValueAsString(
                            new Packet<>("login", "", tempUser)
                    )
            );
            getContext().getBean(WebSocketClient.class).send(
                    getContext().getBean(ObjectMapper.class).writeValueAsBytes(
                            new Packet<>("login", "", tempUser)
                    )
            );
            getContext().getBean(Observer.class).addWaiter("loginSuccess", this);
            getContext().getBean(Observer.class).addWaiter("loginError", this);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Can not write Login packet to server");
        }
        System.out.println("Login packet sent...");
    }

    @FXML
    public void signUpLoad() throws IOException {
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.setScene(
            getContext().getBean(SpringStageLoader.class).loadScene(SIGN_UP_STAGE)
        );
    }

    @Override
    public void getNotify(Packet packet) {
        switch (packet.getType()) {
            case "loginSuccess":
//                TODO go to main window
                getContext().getBean(Observer.class).removeWaiter("loginSuccess", this);
                break;
            case "loginError":
//                TODO show error hint
                getContext().getBean(Observer.class).removeWaiter("loginError", this);
                break;
            default:
                return;
        }
    }
}
