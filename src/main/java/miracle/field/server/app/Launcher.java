package miracle.field.server.app;

import miracle.field.server.config.Config;
import miracle.field.server.realization.ServerIO;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Launcher {

    public static void main(String[] args) {
        new AnnotationConfigApplicationContext(Config.class);
        new ServerIO();
    }
}
