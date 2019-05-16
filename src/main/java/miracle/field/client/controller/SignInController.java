package miracle.field.client.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.NoArgsConstructor;
import miracle.field.client.util.Observer;
import miracle.field.client.util.SpringStageLoader;
import miracle.field.shared.model.User;
import miracle.field.shared.packet.Packet;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@NoArgsConstructor
public class SignInController extends AbstractFxmlController {
    private static final String SIGN_UP_SCENE = "sign_up";
    private static final String CABINET_SCENE = "cabinet";

    @FXML
    private TextField user;
    @FXML
    private PasswordField password;
    @FXML
    private Button loginButton;

    @Override
    public void getNotify(Packet packet) {
        switch (packet.getType()) {
            case "loginSuccess":
                removeWaiter("loginSuccess");
                removeWaiter("loginError");
                Platform.runLater(() -> {
                    try {
                        User user = (User) packet.getData(User.class);
                        String token = packet.getToken();
                        cabinetSceneLoad(user, token);
                    }
                    catch (IOException ex) {
                        //TODO
                    }
                });
                break;
            case "loginError":
//                TODO show error hint
                break;
            default:
                return;
        }
    }

    @FXML
    public void onClickLoad() {
        User tempUser = new User();
        tempUser.setUsername(user.getText());
        tempUser.setPassword(password.getText());
        sendPacket("login", "", tempUser);
        addWaiter("loginSuccess");
        addWaiter("loginError");
    }

    @FXML
    public void signUpLoad() throws IOException {
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.setScene(
                getContext().getBean(SpringStageLoader.class).loadScene(SIGN_UP_SCENE, new HashMap<>())
        );
    }

    @FXML
    public void cabinetSceneLoad(User user, String token) throws IOException {
        Stage stage = (Stage) loginButton.getScene().getWindow();
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        getContext().getBean(SpringStageLoader.class).addStaticField("token", token);
        Scene cabinetScene = getContext().getBean(SpringStageLoader.class).loadScene(CABINET_SCENE, map);
        stage.setScene(cabinetScene);
        stage.show();
    }


}
