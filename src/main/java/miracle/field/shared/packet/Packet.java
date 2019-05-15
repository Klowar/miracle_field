package miracle.field.shared.packet;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.IOException;
import java.io.Serializable;

@Data
public class Packet<T> implements Serializable {

    private String type;
    private String token;

    @JsonIgnore
    protected static ObjectMapper mapper = new ObjectMapper();
    protected String data;

    @JsonCreator
    public Packet(
            @JsonProperty("type") String type,
            @JsonProperty("token") String token,
            @JsonProperty("data") String data) {
        this.type = type;
        this.token = token;
        this.data = data;
    }

    public Packet(String type, String token, T object) throws JsonProcessingException {
        this.type = type;
        this.token = token;
        data = mapper.writeValueAsString(object);
    }

    public Packet(T object) throws JsonProcessingException {
        data = mapper.writeValueAsString(object);
    }

    @JsonIgnore
    public T getData(Class<T> tClass) throws IOException {
        return mapper.readValue(data, tClass);
    }

    @JsonIgnore
    public String getSerializedData() {
        return data;
    }
}
