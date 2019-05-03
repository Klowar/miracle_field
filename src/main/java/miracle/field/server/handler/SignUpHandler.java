package miracle.field.server.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import miracle.field.exception.ValidationException;
import miracle.field.server.repository.UserRepository;
import miracle.field.shared.model.User;
import miracle.field.shared.packet.Packet;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SignUpHandler extends BaseHandler {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public SignUpHandler(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.type = "signUp";
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
                packet = new Packet<>("successSignUp", "", user);

//                ToDo: сделать более информативный вывод ошибок
            } catch (DataIntegrityViolationException | ValidationException constraintException) {
                packet = new Packet<>("userError", "", "Wrong field");
            }
        } catch (IOException e) {
            System.out.println("Can not get user from packet: " + message);
        }

        return packet;
    }
}
