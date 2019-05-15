package miracle.field.client.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import miracle.field.client.controller.AbstractFxmlController;
import miracle.field.client.controller.CabinetController;
import miracle.field.shared.model.User;
import org.java_websocket.client.WebSocketClient;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class SpringStageLoader implements ApplicationContextAware {
    private static ApplicationContext staticContext;

    private static final String FXML_DIR = "/view/fxml/";
    private static final String SIGN_IN_SCENE = "sign_in";
    private static final String TITLE = "Чудесное поле";


    public Scene loadScene(String fxmlName, Map<String, Object> data) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(FXML_DIR + fxmlName + ".fxml"));
        loader.setClassLoader(getClass().getClassLoader());
        loader.setControllerFactory(staticContext::getBean);
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add("style/style.css");
        AbstractFxmlController controller = loader.getController();
        controller.initData(data);
        return scene;
    }


    public Stage loadFirstScene() throws IOException {
        Stage stage = new Stage();
        Scene firstScene = loadScene(SIGN_IN_SCENE, null);
        stage.setOnCloseRequest(we -> {
            staticContext.getBean(WebSocketClient.class).close();
            stage.close();
        });
        stage.setScene(firstScene);
        stage.setTitle(TITLE);
        return stage;
    }


    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        SpringStageLoader.staticContext = context;
    }
}
