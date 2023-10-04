package ru.maxawergy.catalogservice.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.maxawergy.catalogservice.model.User;
import ru.maxawergy.catalogservice.repository.UserRepository;

@Component
public class TokenValidator {

    private final UserRepository userRepository;

    @Autowired
    public TokenValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User validateUser(String token){
        return userRepository.findUserByToken(token);
    }
}
