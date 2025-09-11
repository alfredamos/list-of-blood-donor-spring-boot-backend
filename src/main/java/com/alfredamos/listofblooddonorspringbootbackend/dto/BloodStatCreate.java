package com.alfredamos.listofblooddonorspringbootbackend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BloodStatCreate {
    @NotBlank(message = "GenoType is required.")
    private String genoType;

    @NotBlank(message = "BloodGroup is required.")
    private String bloodGroup;

    @NotBlank(message = "UserId is required.")
    private UUID userId;
}
