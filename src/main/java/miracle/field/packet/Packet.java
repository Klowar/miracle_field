package miracle.field.packet;

import miracle.field.server.model.User;

import java.io.Serializable;

public abstract class Packet implements Serializable {

    private String type;
    protected byte[] body;
    private User author;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

}
