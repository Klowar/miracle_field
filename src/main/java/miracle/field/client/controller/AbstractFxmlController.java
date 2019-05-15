package miracle.field.client.controller;
import lombok.Data;
import miracle.field.client.util.Waiter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;

@Data
public abstract class AbstractFxmlController implements ApplicationContextAware, Waiter {
    protected Map<String, Object> personalMap = new HashMap<>();
    private ApplicationContext context;

    public ApplicationContext getContext() {
        return context;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    public void initData(Map<String, Object> data){
        personalMap.putAll(data);
    }


}
