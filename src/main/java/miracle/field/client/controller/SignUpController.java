package miracle.field.client.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lombok.NoArgsConstructor;
import javafx.scene.control.Button;
import miracle.field.client.util.Observer;
import miracle.field.shared.model.User;
import miracle.field.shared.packet.Packet;
import org.java_websocket.client.WebSocketClient;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
@NoArgsConstructor
public class SignUpController extends AbstractFxmlController {
    @FXML private TextField user;
    @FXML private PasswordField password;
    @FXML private PasswordField confirmPassword;

    @FXML
    public void signUp() {
        User tempUser = new User();
        tempUser.setUsername(user.getText());
        tempUser.setPassword(password.getText());
        tempUser.setConfirmPassword(confirmPassword.getText());

        try {
            getContext().getBean(WebSocketClient.class).send(
                    getContext().getBean(ObjectMapper.class).writeValueAsBytes(
                            new Packet<>("signUp", "", tempUser)
                    )
            );
            getContext().getBean(Observer.class).addWaiter("signUpSuccess", this);
            getContext().getBean(Observer.class).addWaiter("signUpError", this);
        } catch (IOException e) {
            System.out.println("Can not write SignUp packet to server");
        }
        System.out.println("SignUp packet sent...");
    }

    @Override
    public void getNotify(Packet packet) {
        switch (packet.getType()) {
            case "signUpSuccess":
//                TODO go to main window
                getContext().getBean(Observer.class).removeWaiter("signUpSuccess", this);
                break;
            case "signUpError":
//                TODO show error hint
                getContext().getBean(Observer.class).removeWaiter("signUpError", this);
                break;
            default:
                return;
        }
    }
}
