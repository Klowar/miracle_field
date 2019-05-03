package miracle.field.client.controller;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lombok.NoArgsConstructor;
import javafx.scene.control.Button;
import org.springframework.stereotype.Component;


@Component
@NoArgsConstructor
public class SignUpController extends AbstractFxmlController {
    @FXML private TextField user;
    @FXML private PasswordField password;
    @FXML private PasswordField confirmPassword;
    @FXML private Button createButton;

    @FXML
    public void signUp() {
        System.out.println("Попытка регитрации");
    }

}
