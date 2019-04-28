package miracle.field.client.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import miracle.field.client.util.ApplicationContexHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MainController extends AbstractFxmlController {
    @FXML private TextField user;
    @FXML private TextField password;
    @FXML private Button loginButton;
    private ApplicationContexHolder holder;

    @Autowired
    public MainController(ApplicationContexHolder holder){
        this.holder = holder;
    }
    @FXML
    public void onClickLoad() {
        System.out.println(getContext());
        System.out.println(holder);
    }

}
