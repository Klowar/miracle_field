package miracle.field.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import miracle.field.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MainController extends AbstractFxmlController {
    @FXML private TextField user;
    @FXML private TextField password;
    @FXML private Button loginButton;

//    private final UserRepository userRepository;
//
//    @Autowired
//    public MainController(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }

    @FXML
    public void onClickLoad() {
        System.out.println(user.getText());
        System.out.println(password.getText());
//        TODO login logic
    }
}
