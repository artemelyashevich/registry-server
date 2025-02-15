package com.elyashevich.registry.service.impl;

import com.elyashevich.registry.entity.Role;
import com.elyashevich.registry.entity.User;
import com.elyashevich.registry.exception.ResourceAlreadyExistException;
import com.elyashevich.registry.exception.ResourceNotFoundException;
import com.elyashevich.registry.repository.UserRepository;
import com.elyashevich.registry.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    public static final String USER_WITH_EMAIL_ALREADY_EXISTS_TEMPLATE = "User with email '%s' already exists";
    public static final String USER_WITH_ID_WAS_NOR_FOUND_TEMPLATE = "User with id '%s' was nor found";
    public static final String USER_WITH_EMAIL_WAS_NOT_FOUND_TEMPLATE = "User with email '%s' was not found";
    private final UserRepository userRepository;

    @Transactional
    @Override
    public User create(final User user) {
        log.debug("Attempting to create a new user");

        if (this.userRepository.existsByEmail(user.getEmail())) {
            throw new ResourceAlreadyExistException(USER_WITH_EMAIL_ALREADY_EXISTS_TEMPLATE.formatted(user.getEmail()));
        }

        var newUser = this.userRepository.save(user);

        log.info("Created new user: {}", newUser.getEmail());
        return newUser;
    }

    @Override
    public List<User> findAll() {
        log.debug("Attempting to find all users");

        var users = this.userRepository.findAll();

        log.info("Found {} users", users.size());
        return users;
    }

    @Override
    public User findById(final UUID id) {
        log.debug("Attempting to find user with id {}", id);

        var user = this.userRepository.findById(id).orElseThrow(
                () -> {
                    var message = USER_WITH_ID_WAS_NOR_FOUND_TEMPLATE.formatted(id);
                    log.warn(message);

                    return new ResourceNotFoundException(message);
                }
        );

        log.info("Found user with id {}", user.getId());
        return user;
    }

    @Override
    public User findByEmail(final String email) {
        log.debug("Attempting to find user with email {}", email);

        var user = this.userRepository.findByEmail(email).orElseThrow(
                () -> {
                    var message = USER_WITH_EMAIL_WAS_NOT_FOUND_TEMPLATE.formatted(email);
                    log.warn(message);

                    return new ResourceNotFoundException(message);
                }
        );

        log.info("Found user with email {}", user.getEmail());
        return user;
    }

    @Transactional
    @Override
    public User update(final UUID id, final User user) {
        log.debug("Attempting to update user with id {}", id);

        var candidate = this.findById(id);
        candidate.setEmail(user.getEmail());
        candidate.setFirstName(user.getFirstName());
        candidate.setLastName(user.getLastName());

        var updatedUser = this.userRepository.save(candidate);

        log.info("Updated user with id {}", updatedUser.getId());
        return updatedUser;
    }

    @Transactional
    @Override
    public void delete(final UUID id) {
        log.debug("Attempting to delete user with id {}", id);

        var user = this.findById(id);
        this.userRepository.delete(user);

        log.info("Deleted user with id {}", user.getId());
    }

    @Transactional
    @Override
    public void resetPassword(final UUID id, final String password) {
        var user = this.findById(id);
        user.setPassword(password);
        this.userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = this.userRepository.findByEmail(username).orElseThrow(
                ()->new ResourceNotFoundException("User with email '%s' was nor found".formatted(username))
        );
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(Role::name)
                        .map(SimpleGrantedAuthority::new)
                        .toList()
        );
    }
}
