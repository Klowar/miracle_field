package miracle.field.client.util;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

@Component
public class SpringStageLoader implements ApplicationContextAware {
    private static ApplicationContext staticContext;

    private static final String FXML_DIR = "/view/fxml/";
    private static final String SIGN_IN_STAGE = "sign_in";
    private static final String SIGN_IN_STAGE_TITLE = "Вход";


    private Parent load(String fxmlName) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(FXML_DIR + fxmlName + ".fxml"));
        // for custom handler constructors,
        // by default this tries to create handler empty constructor
        loader.setClassLoader(getClass().getClassLoader());
        loader.setControllerFactory(staticContext::getBean);
        return loader.load();
    }


    public Stage loadFirstScene() throws IOException {
        Stage stage = new Stage();
        Scene mainScene = new Scene(load(SIGN_IN_STAGE));
        mainScene.getStylesheets().clear();
        mainScene.getStylesheets().add("style/style.css");
        stage.setScene(mainScene);
        stage.setTitle(SIGN_IN_STAGE_TITLE);
        return stage;
    }

    public Scene loadScene(String fxmlName) throws IOException {
        return new Scene(load(fxmlName));
    }



    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        SpringStageLoader.staticContext = context;
    }
}
