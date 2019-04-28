package miracle.field.client.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;

@Configuration
@ComponentScan({"miracle.field.client.controller",
                "miracle.field.client.form",
                "miracle.field.client.util"})
@PropertySource("classpath:app.properties")
public class Config {

    @Resource
    private Environment env;

}
