package miracle.field.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;

@Component
public class MainController extends AbstractFxmlController {
    @FXML private TextField user;
    @FXML private TextField password;
    @FXML private Button loginButton;

    public MainController() {
    }

    @FXML
    public void onClickLoad() {
        System.out.println(user.getText());
        System.out.println(password.getText());
//        TODO login logic
    }
}
