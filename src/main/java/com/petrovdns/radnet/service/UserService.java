package com.petrovdns.radnet.service;

import com.petrovdns.radnet.entity.User;
import com.petrovdns.radnet.entity.enums.ERole;
import com.petrovdns.radnet.exceptions.RegistrationFailedException;
import com.petrovdns.radnet.payload.request.SignupRequest;
import com.petrovdns.radnet.exceptions.UserExistException;
import com.petrovdns.radnet.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createUser(SignupRequest userIn) {
        if(userRepository.existsUserByUserName(userIn.getUsername())) {
            throw new UserExistException("the user " + userIn.getUsername() + " already exists. Please check credentials");
        }

        User user = new User();
        user.setEmail(userIn.getEmail());
        user.setName(userIn.getFirstname());
        user.setLastName(userIn.getLastname());
        user.setUserName(userIn.getUsername());
        user.setPassword(passwordEncoder.encode(userIn.getPassword()));
        user.getRoles().add(ERole.ROLE_USER);

        try {
            LOG.info("Saving user {}", userIn.getEmail());
            userRepository.save(user);
        } catch (Exception e) {
            LOG.error("Error during registration. {}", e.getMessage());
            throw new RegistrationFailedException("An error occurred during user registration. Please try again.");
        }
    }
}
