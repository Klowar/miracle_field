package miracle.field.client.app;

import javafx.application.Application;
import javafx.stage.Stage;
import miracle.field.client.config.Config;
import miracle.field.client.util.SpringStageLoader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Launcher extends Application {
    private static ApplicationContext context;
    private SpringStageLoader springStageLoader;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() {
        context = new AnnotationConfigApplicationContext(Config.class);
        springStageLoader = context.getBean(SpringStageLoader.class);
    }

    @Override
    public void start(Stage stage) throws Exception {
        springStageLoader.loadFirstScene().show();
    }

    @Override
    public void stop() {
        context = null;
    }
}
