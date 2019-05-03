package miracle.field.server.handler;

import miracle.field.shared.model.User;
import miracle.field.shared.notification.UserError;
import miracle.field.shared.packet.Packet;
import miracle.field.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class LoginHandler extends BaseHandler {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public LoginHandler(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.type = "login";
    }

    @Override
    public Packet handle(Packet message) {
        if (!message.getType().equals(type))
            return nextHandler.handle(message);

        Packet returnPacket = null;
        try {
            User tempUser = (User) message.getData(User.class);

            Optional<User> userCandidate = userRepository.findByUsername(tempUser.getUsername());
            if(userCandidate.isPresent() &&
                    passwordEncoder.matches(tempUser.getPassword(), userCandidate.get().getPassword())) {

                returnPacket = new Packet<>("successLogin", "", userCandidate.get());
            } else {
                UserError error = new UserError("Wrong login or password");
                returnPacket = new Packet<>("userError", "", error);
            }
        } catch (IOException e) {
            System.out.println("Can not get user from packet: " + message);
        }

        return returnPacket;
    }
}
