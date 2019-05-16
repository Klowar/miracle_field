package miracle.field.client.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lombok.NoArgsConstructor;
import miracle.field.client.util.Observer;
import miracle.field.client.util.Waiter;
import miracle.field.shared.model.User;
import miracle.field.shared.packet.Packet;
import org.java_websocket.client.WebSocketClient;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
        sign(tempUser);
    }

    private void sign(User tempUser){
        sendPacket("signUp", "", tempUser);
        addWaiter("signUpSuccess");
        addWaiter("signUpError");

    }

    @Override
    public void getNotify(Packet packet) {
        switch (packet.getType()) {
            case "signUpSuccess":
                removeWaiter("signUpSuccess");
                removeWaiter("signUpError");
                break;
            case "signUpError":
//                TODO show error hint

                break;
            default:
                return;
        }
    }

}
