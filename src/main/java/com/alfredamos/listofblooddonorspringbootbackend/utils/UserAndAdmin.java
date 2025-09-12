package com.alfredamos.listofblooddonorspringbootbackend.utils;

import com.alfredamos.listofblooddonorspringbootbackend.dto.UserDto;
import com.alfredamos.listofblooddonorspringbootbackend.entities.Role;
import com.alfredamos.listofblooddonorspringbootbackend.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserAndAdmin {
    private Role role;
    private User user;

}
