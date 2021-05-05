package net.javaguides.springboot.springsecurity.service;

import net.javaguides.springboot.springsecurity.model.User;
import net.javaguides.springboot.springsecurity.web.dto.UserRegistrationDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User findByEmail(String email);

    User loadCurrentUser();

    User save(UserRegistrationDto registration);

    User update(User user);

    void delete(User user);

}
