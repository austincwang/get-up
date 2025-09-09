package com.dev.getup.services;

import com.dev.getup.domain.entities.User;

import java.util.UUID;

public interface UserService {
    User getUserById(UUID id);
}
