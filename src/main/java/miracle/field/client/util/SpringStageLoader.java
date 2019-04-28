package miracle.field.client.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SpringStageLoader implements ApplicationContextAware {
    private static ApplicationContext staticContext;
    //инъекция заголовка главного окна
    private String appTitle;
    private static String staticTitle;

    private static final String FXML_DIR = "/view/fxml/";
    private static final String MAIN_STAGE = "main";

    private Parent load(String fxmlName) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        // Load templates
        loader.setLocation(SpringStageLoader.class.getResource(FXML_DIR + fxmlName + ".fxml"));
        // for custom controller constructors,
        // by default this tries to create controller empty constructor
        loader.setClassLoader(SpringStageLoader.class.getClassLoader());
        loader.setControllerFactory(staticContext::getBean);
        return loader.load(SpringStageLoader.class.getResourceAsStream(FXML_DIR + fxmlName + ".fxml"));
    }

    public Stage loadMain() throws IOException {
        Stage stage = new Stage();
        stage.setScene(new Scene(load(MAIN_STAGE)));
        stage.setTitle(staticTitle);
        return stage;
    }

    public Scene loadScene(String fxmlName) throws IOException {
        return new Scene(load(fxmlName));
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        SpringStageLoader.staticContext = context;
        SpringStageLoader.staticTitle = appTitle;
    }
}
