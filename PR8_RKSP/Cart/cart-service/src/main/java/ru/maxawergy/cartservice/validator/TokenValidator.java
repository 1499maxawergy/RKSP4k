package ru.maxawergy.cartservice.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.maxawergy.cartservice.model.User;
import ru.maxawergy.cartservice.repository.UserRepository;

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
