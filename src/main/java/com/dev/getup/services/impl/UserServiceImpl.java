package com.dev.getup.services.impl;

import com.dev.getup.domain.entities.User;
import com.dev.getup.repositories.UserRepo;
import com.dev.getup.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    @Override
    public User getUserById(UUID id) {
        return userRepo
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));
    }
}
