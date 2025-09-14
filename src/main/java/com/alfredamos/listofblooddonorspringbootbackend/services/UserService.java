package com.alfredamos.listofblooddonorspringbootbackend.services;

import com.alfredamos.listofblooddonorspringbootbackend.dto.UserDto;
import com.alfredamos.listofblooddonorspringbootbackend.entities.User;
import com.alfredamos.listofblooddonorspringbootbackend.exceptions.NotFoundException;
import com.alfredamos.listofblooddonorspringbootbackend.mapper.UserMapper;
import com.alfredamos.listofblooddonorspringbootbackend.repositories.UserRepository;
import com.alfredamos.listofblooddonorspringbootbackend.utils.ResponseMessage;
import com.alfredamos.listofblooddonorspringbootbackend.utils.SameUserAndAdmin;
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
    private final SameUserAndAdmin sameUserAndAdmin;

    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public ResponseMessage deleteUserById(UUID id) {
        //----> Check for existence of user.
        var user = fetchUser(id);

        //----> Check for same user or admin privilege.
        sameUserAndAdmin.checkForOwnerShipOrAdmin(user.getId());

        //----> Delete the user with the given id from database.
        userRepository.deleteById(id);

        //----> Send back the response.
        return new ResponseMessage("User has been deleted successfully!", "success", HttpStatus.OK);
    }

    public UserDto getUserById(UUID id) {
        //----> Check for existence of user.
        var user = fetchUser(id);

        //----> Check for same user or admin privilege.
        sameUserAndAdmin.checkForOwnerShipOrAdmin(user.getId());

        //----> Send back the response.
        return userMapper.toDTO(user);
    }

    public List<UserDto> getAllUsers() {
        //----> Only admin can perform this action
        sameUserAndAdmin.checkForAdmin();

        //----> Get all users from the database.
        var users = userRepository.findAll();
        return users.stream().map(userMapper::toDTO).toList();


    }

    private User fetchUser(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found in the database!"));
    }
}
