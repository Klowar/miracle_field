package miracle.field.client.controller;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lombok.NoArgsConstructor;
import javafx.scene.control.Button;
import miracle.field.client.util.ServerConnector;
import miracle.field.shared.model.User;
import miracle.field.shared.packet.Packet;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
@NoArgsConstructor
public class SignUpController extends AbstractFxmlController {
    @FXML private TextField user;
    @FXML private PasswordField password;
    @FXML private PasswordField confirmPassword;
    @FXML private Button createButton;

    @FXML
    public void signUp() {
        User tempUser = new User();
        tempUser.setUsername(user.getText());
        tempUser.setPassword(password.getText());
        tempUser.setConfirmPassword(confirmPassword.getText());

        try {
            getContext().getBean(ServerConnector.class).writeObject(
                    new Packet<User>("signUp","", tempUser)
            );
        } catch (IOException e) {
            System.out.println("Can not write SignUp packet to server");
        }
        System.out.println("SignUp packet sent...");
    }

}
