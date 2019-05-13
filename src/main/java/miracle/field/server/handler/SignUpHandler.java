package miracle.field.server.handler;

import miracle.field.exception.ValidationException;
import miracle.field.server.service.UserService;
import miracle.field.shared.model.User;
import miracle.field.shared.notification.UserError;
import miracle.field.shared.packet.Packet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.logging.Logger;

@Component
public class SignUpHandler extends BaseHandler {
    private final Logger LOGGER = Logger.getLogger(SignUpHandler.class.getName());

    private final UserService userService;

    @Autowired
    public SignUpHandler(UserService userService) {
        this.userService = userService;
        this.type = "signUp";
    }

    @Override
    public Packet handle(Packet message) {
        if (!message.getType().equals(type))
            return nextHandler.handle(message);

        Packet returnPacket = null;
        try {
            try {
                User user = (User) message.getData(User.class);
                userService.signUp(user);

                returnPacket = new Packet<>(type + "Success", "", user);

            } catch (ValidationException constraintException) {
                UserError error = new UserError(constraintException.getMessage());

                returnPacket = new Packet<>(type + "Error", "", error);

            } catch (DataIntegrityViolationException constraintException) {
                UserError error = new UserError("Such username already exists");

                returnPacket = new Packet<>(type + "Error", "", error);

            }
        } catch (IOException e) {
            LOGGER.severe("Can not get user from packet: " + message);
        }

        LOGGER.info("Sent " + returnPacket);

        return returnPacket;
    }
}
