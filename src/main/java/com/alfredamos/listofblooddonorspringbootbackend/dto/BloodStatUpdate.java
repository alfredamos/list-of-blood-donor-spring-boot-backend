package com.alfredamos.listofblooddonorspringbootbackend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BloodStatUpdate {
    private UUID id;

    @NotBlank(message = "GenoType is required.")
    private String genoType;

    @NotBlank(message = "BloodGroup is required.")
    private String bloodGroup;

}
