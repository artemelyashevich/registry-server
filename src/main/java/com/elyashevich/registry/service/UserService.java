package com.elyashevich.registry.service;

import com.elyashevich.registry.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    User create(final User user);

    List<User> findAll();

    User findById(final UUID id);

    User findByEmail(final String email);

    User update(final UUID id, final User user);

    void delete(final UUID id);

    void resetPassword(final UUID id, final String password);
}
