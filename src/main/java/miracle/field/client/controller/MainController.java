package miracle.field.client.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.NoArgsConstructor;
import miracle.field.client.util.ServerConnector;
import miracle.field.client.util.SpringStageLoader;
import miracle.field.shared.model.User;
import miracle.field.shared.packet.Packet;
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
            getContext().getBean(ServerConnector.class).writeObject(
                    new Packet<User>("login","", tempUser)
            );
        } catch (IOException e) {
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

}
