package com.alfredamos.listofblooddonorspringbootbackend.services;

import com.alfredamos.listofblooddonorspringbootbackend.dto.UserDto;
import com.alfredamos.listofblooddonorspringbootbackend.entities.User;
import com.alfredamos.listofblooddonorspringbootbackend.exceptions.NotFoundException;
import com.alfredamos.listofblooddonorspringbootbackend.mapper.UserMapper;
import com.alfredamos.listofblooddonorspringbootbackend.repositories.UserRepository;
import com.alfredamos.listofblooddonorspringbootbackend.utils.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public ResponseMessage deleteUserById(UUID id) {
        //----> Check for existence of user.
        var exists = userRepository.existsById(id);

        //----> Throw error if user does nt exist.
        if (!exists) {
            throw new NotFoundException("User not found");
        }

        //----> Delete the user with the given id from database.
        userRepository.deleteById(id);

        //----> Send back the response.
        return new ResponseMessage("User has been deleted successfully!", "success", HttpStatus.OK);
    }

    public UserDto getUserById(UUID id) {
        //----> Check for existence of user.
        var user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found in the database!"));

        //----> Send back the response.
        return userMapper.toDTO(user);
    }

    public List<UserDto> getAllUsers() {
        //----> Get all users from the database.
        var users = userRepository.findAll();
        return userMapper.toDTOList(users);
    }
}
