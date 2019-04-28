package miracle.field.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import miracle.field.config.Config;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

public class Launcher extends Application {
    private static AnnotationConfigApplicationContext context;
    //   1) Дина Рубина Синдром Питрушки
//   2) На солнечной стороне улицы
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() {
        context = new AnnotationConfigApplicationContext(Config.class);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass()
                .getResource("/view/fxml/main.fxml"));
        stage.setTitle("JavaFX Maven Spring");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Override
    public void stop() throws IOException {
        context.close();
    }
}
