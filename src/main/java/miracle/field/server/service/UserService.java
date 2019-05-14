package miracle.field.server.service;

import miracle.field.shared.model.User;

public interface UserService {
    void signUp(User user);
    User login(User user);
    User getAuthorizedUser(String token);
    String getToken(User user);
}