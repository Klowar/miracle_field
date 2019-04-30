package miracle.field.server.app;

import miracle.field.server.config.Config;
import miracle.field.server.realization.ServerIO;
import miracle.field.server.realization.ServerInterface;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Launcher {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        ServerInterface serverInterface = new ServerIO();
        serverInterface.start();
    }
}
