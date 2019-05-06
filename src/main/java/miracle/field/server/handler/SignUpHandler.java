package miracle.field.server.handler;

import miracle.field.exception.ValidationException;
import miracle.field.server.repository.UserRepository;
import miracle.field.server.util.TokenGenerator;
import miracle.field.shared.model.User;
import miracle.field.shared.notification.UserError;
import miracle.field.shared.packet.Packet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SignUpHandler extends BaseHandler {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private final TokenGenerator tokenGenerator;

    @Autowired
    public SignUpHandler(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenGenerator tokenGenerator) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.type = "signUp";
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    public Packet handle(Packet message) {
        if (!message.getType().equals(type))
            return nextHandler.handle(message);

        Packet packet = null;
        try {
            try {
                User user = (User) message.getData(User.class);
                if (!user.getPassword().equals(user.getConfirmPassword())) {
                    throw new ValidationException();
                }

                user.setPassword(passwordEncoder.encode(user.getPassword()));

                userRepository.save(user);
                packet = new Packet<>(type + "Success", tokenGenerator.generateToken(), user);

//                ToDo: сделать более информативный вывод ошибок
            } catch (DataIntegrityViolationException | ValidationException constraintException) {
                UserError error = new UserError("Wrong fields");
                packet = new Packet<>(type + "Error", "", error);
            }
        } catch (IOException e) {
            System.out.println("Can not get user from packet: " + message);
        }

        return packet;
    }
}
