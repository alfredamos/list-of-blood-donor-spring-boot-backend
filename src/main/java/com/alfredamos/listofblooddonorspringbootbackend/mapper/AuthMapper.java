package com.alfredamos.listofblooddonorspringbootbackend.mapper;


import com.alfredamos.listofblooddonorspringbootbackend.dto.EditProfile;
import com.alfredamos.listofblooddonorspringbootbackend.dto.Signup;
import com.alfredamos.listofblooddonorspringbootbackend.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    User toEntity(Signup signup);
    User toEntity(EditProfile editProfile);
}
