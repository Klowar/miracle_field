package miracle.field.client.form;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class LoginForm {

    private String name;
    private String password;

    @JsonIgnore
    public String getData(ObjectMapper mapper) throws JsonProcessingException {
        return mapper.writeValueAsString(this);
    }

}
