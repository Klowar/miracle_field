package miracle.field.server.util.validator;

import miracle.field.shared.model.User;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Service
public class PasswordsMatchValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;
        if(!user.getPassword().equals(user.getConfirmPassword())) {
            errors.rejectValue("password", "passwords.error");
        }
    }
}
