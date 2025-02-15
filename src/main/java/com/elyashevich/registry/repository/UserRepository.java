package com.elyashevich.registry.repository;

import com.elyashevich.registry.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByEmail(final String email);

    Optional<User> findByEmail(final String email);
}
