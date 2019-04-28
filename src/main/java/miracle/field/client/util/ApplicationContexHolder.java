package miracle.field.client.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContexHolder implements ApplicationContextAware {

    private ApplicationContext context;

    public ApplicationContext getContext() { return context; }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println(applicationContext);
        this.context = applicationContext;
    }

}
