package miracle.field.client.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.java_websocket.client.WebSocketClient;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SpringStageLoader implements ApplicationContextAware {
    private static ApplicationContext staticContext;

    private static final String FXML_DIR = "/view/fxml/";
    private static final String SIGN_IN_SCENE = "sign_in";
    private static final String SIGN_IN_SCENE_TITLE = "Вход";


    private Parent load(String fxmlName) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(FXML_DIR + fxmlName + ".fxml"));
        // for custom handler constructors,
        // by default this tries to create handler with empty constructor
        loader.setClassLoader(getClass().getClassLoader());
        loader.setControllerFactory(staticContext::getBean);
        return loader.load();
    }


    public Stage loadFirstScene() throws IOException {
        Stage stage = new Stage();
        Scene mainScene = new Scene(load(SIGN_IN_SCENE));
        mainScene.getStylesheets().clear();
        mainScene.getStylesheets().add("style/style.css");
        stage.setOnCloseRequest(we -> {
            staticContext.getBean(WebSocketClient.class).close();
            stage.close();
        });
        stage.setScene(mainScene);
        stage.setTitle(SIGN_IN_SCENE_TITLE);
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
