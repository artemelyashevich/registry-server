package com.elyashevich.registry.service;

import com.elyashevich.registry.entity.User;

public interface RefreshTokenService {

    String create(final String token, final User user);
}
