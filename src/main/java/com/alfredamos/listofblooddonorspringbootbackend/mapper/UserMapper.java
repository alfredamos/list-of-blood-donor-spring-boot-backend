package com.alfredamos.listofblooddonorspringbootbackend.mapper;

import com.alfredamos.listofblooddonorspringbootbackend.dto.UserDto;
import com.alfredamos.listofblooddonorspringbootbackend.entities.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(User userDto);

    UserDto toDTO(User user);

    List<UserDto> toDTOList(List<User> users);
}
