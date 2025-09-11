package com.alfredamos.listofblooddonorspringbootbackend.services;

import com.alfredamos.listofblooddonorspringbootbackend.entities.User;
import com.alfredamos.listofblooddonorspringbootbackend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }
}
