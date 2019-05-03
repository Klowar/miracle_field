package miracle.field.shared.packet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.Serializable;

public class Packet<T> implements Serializable {

    private String type;
    private String token;

    protected ObjectMapper mapper;
    protected String data;

    public Packet(String type, String token, T object) throws JsonProcessingException {
        mapper = new ObjectMapper();
        this.type = type;
        this.token = token;
        data = mapper.writeValueAsString(object);
    }

    public Packet(T object) throws JsonProcessingException {
        mapper = new ObjectMapper();
        data = mapper.writeValueAsString(object);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public T getData(Class<T> tClass) throws IOException {
        return mapper.readValue(data, tClass);
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
