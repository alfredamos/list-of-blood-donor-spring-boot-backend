package com.alfredamos.listofblooddonorspringbootbackend.dto;

import com.alfredamos.listofblooddonorspringbootbackend.entities.Address;
import com.alfredamos.listofblooddonorspringbootbackend.entities.Gender;
import com.alfredamos.listofblooddonorspringbootbackend.validations.ValueOfEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    @NotBlank(message = "Name is required.")
    private String name;

    private Address address;

    @NotBlank(message = "Email is required.")
    @Email(message = "Email must be valid.")
    private String email;

    @NotBlank(message = "Phone is required.")
    private String phone;

    @NotBlank(message = "Image is required.")
    private String image;

    @ValueOfEnum(enumClass = Gender.class, message = "It must be either Male of Female!")
    @Enumerated(EnumType.STRING)
    private Gender gender;
}
