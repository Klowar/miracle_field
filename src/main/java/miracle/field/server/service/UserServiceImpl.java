package miracle.field.server.service;

import miracle.field.exception.ValidationException;
import miracle.field.server.repository.UserRepository;
import miracle.field.server.util.TokenGenerator;
import miracle.field.server.util.validator.EmptyFieldsValidator;
import miracle.field.server.util.validator.PasswordsMatchValidator;
import miracle.field.shared.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.DataBinder;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private Map<User, String> authenticatedUsers;

    private final TokenGenerator tokenGenerator;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private PasswordsMatchValidator passwordsMatchValidator;
    private EmptyFieldsValidator emptyFieldsValidator;
    private MessageSource messageSource;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           TokenGenerator tokenGenerator,
                           PasswordsMatchValidator passwordsMatchValidator,
                           EmptyFieldsValidator emptyFieldsValidator,
                           MessageSource messageSource) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenGenerator = tokenGenerator;
        this.passwordsMatchValidator = passwordsMatchValidator;
        this.emptyFieldsValidator = emptyFieldsValidator;
        this.messageSource = messageSource;
        this.authenticatedUsers = new HashMap<>();
    }


    @Override
    public User login(User user) {
        Optional<User> userCandidate = userRepository.findByUsername(user.getUsername());

        if(userCandidate.isPresent() &&
                passwordEncoder.matches(user.getPassword(), userCandidate.get().getPassword())) {
            return userCandidate.get();
        } else {
            throw new ValidationException();
        }
    }

    @Override
    public User getAuthorizedUser(String token) {
        return authenticatedUsers.entrySet()
                .stream()
                .filter(entry -> token.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .findFirst().get();
    }

    @Override
    public String getToken(User user) {
        String token = authenticatedUsers.get(user);
        if (token == null) {
            token = tokenGenerator.generateToken();
            authenticatedUsers.put(user, token);
        }
        return token;
    }

    @Override
    public void signUp(User user) {

        DataBinder dataBinder = new DataBinder(user);
        dataBinder.addValidators(emptyFieldsValidator);
        dataBinder.addValidators(passwordsMatchValidator);

        dataBinder.validate(user);
        if(dataBinder.getBindingResult().hasErrors()) {
            throw new ValidationException(
                    messageSource.getMessage(dataBinder.getBindingResult().getAllErrors().get(0), Locale.US)
            );
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

    }
}
