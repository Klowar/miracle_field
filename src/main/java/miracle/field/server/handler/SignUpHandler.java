package miracle.field.server.handler;

import miracle.field.exception.ValidationException;
import miracle.field.server.repository.UserRepository;
import miracle.field.server.util.TokenGenerator;
import miracle.field.server.util.validator.EmptyFieldsValidator;
import miracle.field.server.util.validator.PasswordsMatchValidator;
import miracle.field.shared.model.User;
import miracle.field.shared.notification.UserError;
import miracle.field.shared.packet.Packet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.DataBinder;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Component
public class SignUpHandler extends BaseHandler {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private final TokenGenerator tokenGenerator;

    private Validator validator;
    private PasswordsMatchValidator passwordsMatchValidator;
    private EmptyFieldsValidator emptyFieldsValidator;
    private MessageSource messageSource;


    @Autowired
    public SignUpHandler(UserRepository userRepository,
                         PasswordEncoder passwordEncoder,
                         TokenGenerator tokenGenerator,
                         Validator validator,
                         PasswordsMatchValidator passwordsMatchValidator,
                         EmptyFieldsValidator emptyFieldsValidator,
                         MessageSource messageSource) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.validator = validator;
        this.passwordsMatchValidator = passwordsMatchValidator;
        this.emptyFieldsValidator = emptyFieldsValidator;
        this.messageSource = messageSource;
        this.type = "signUp";
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    public Packet handle(Packet message) {
        if (!message.getType().equals(type))
            return nextHandler.handle(message);

        Packet returnPacket = null;
        try {
            try {
                User user = (User) message.getData(User.class);

                DataBinder dataBinder = new DataBinder(user);
                dataBinder.addValidators(emptyFieldsValidator);
                dataBinder.addValidators(passwordsMatchValidator);

                dataBinder.validate(user);
                List<ConstraintViolation<User>> errors = List.copyOf(validator.validate(user));

                if(dataBinder.getBindingResult().hasErrors()) {
                   throw new ValidationException(
                           messageSource.getMessage(dataBinder.getBindingResult().getAllErrors().get(0), Locale.US)
                   );
                }

                if(!errors.isEmpty()) {
                    throw new ValidationException(
                            messageSource.getMessage(errors.get(0).getMessage(), null, Locale.US)
                    );
                }

                user.setPassword(passwordEncoder.encode(user.getPassword()));
                userRepository.save(user);

                returnPacket = new Packet<>(type + "Success", tokenGenerator.generateToken(), user);

            } catch (ValidationException constraintException) {
                UserError error = new UserError(constraintException.getMessage());
                returnPacket = new Packet<>(type + "Error", "", error);
            } catch (DataIntegrityViolationException constraintException) {
                UserError error = new UserError("Such username already exists");
                returnPacket = new Packet<>(type + "Error", "", error);
            }
        } catch (IOException e) {
            System.out.println("Can not get user from packet: " + message);
        }
        System.out.println(returnPacket);
        return returnPacket;
    }
}
