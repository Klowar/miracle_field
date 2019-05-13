package miracle.field.server.handler;

import miracle.field.exception.ValidationException;
import miracle.field.server.service.UserService;
import miracle.field.shared.model.User;
import miracle.field.shared.notification.UserError;
import miracle.field.shared.packet.Packet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.logging.Logger;

@Component
public class LoginHandler extends BaseHandler {
    private final Logger LOGGER = Logger.getLogger(LoginHandler.class.getName());

    private final UserService userService;

    @Autowired
    public LoginHandler(UserService userService) {
        this.userService = userService;
        this.type = "login";
    }

    @Override
    public Packet handle(Packet message) {
        if (!message.getType().equals(type))
            return nextHandler.handle(message);

        Packet returnPacket = null;
        try {
            try {
                User user = (User) message.getData(User.class);

                user = userService.login(user);
                String token = userService.getToken(user);

                returnPacket = new Packet<>(type + "Success", token, user);
            } catch (ValidationException e) {
                LOGGER.severe("Wrong user data");
                UserError error = new UserError("Wrong login or password");
                returnPacket = new Packet<>(type + "Error", "", error);
            }
        } catch (IOException e) {
                LOGGER.severe("Can not get user from packet: " + message);
        }

        LOGGER.info("Sent " + returnPacket);

        return returnPacket;
    }
}
