package miracle.field.server.handler;

import miracle.field.shared.model.User;
import miracle.field.shared.packet.Packet;
import miracle.field.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoginHandler extends BaseHandler {

    private final UserRepository userRepository;

    @Autowired
    public LoginHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.type = "login";
    }

    @Override
    public Packet handle(Packet message) {
        if (!message.getType().equals(type))
            return nextHandler.handle(message);

        try {
            User tempUser = (User) message.getData(User.class);
            System.out.println(tempUser);
//            TODO find user in DB and check password equality
        } catch (IOException e) {
            System.out.println("Can not get user from packet: " + message);
        }
//            TODO generating success or failed package
        return null;
    }
}
