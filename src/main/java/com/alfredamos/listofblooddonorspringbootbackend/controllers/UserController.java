package com.alfredamos.listofblooddonorspringbootbackend.controllers;

import com.alfredamos.listofblooddonorspringbootbackend.dto.UserDto;
import com.alfredamos.listofblooddonorspringbootbackend.services.UserService;
import com.alfredamos.listofblooddonorspringbootbackend.utils.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessage> deleteUserById(@PathVariable(value = "id") UUID id) {
        var response = userService.deleteUserById(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        var usersDto = userService.getAllUsers();

        return ResponseEntity.ok(usersDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable(value = "id") UUID id){
        var userDto = userService.getUserById(id);

        return ResponseEntity.ok(userDto);
    }

}
