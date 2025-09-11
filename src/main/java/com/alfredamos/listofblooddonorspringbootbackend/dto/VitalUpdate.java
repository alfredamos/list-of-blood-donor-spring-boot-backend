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
public class VitalUpdate {
    @NotBlank(message = "Id is required.")
    private UUID id;

    @NotBlank(message = "PressureUp is required.")
    private Double pressureUp;

    @NotBlank(message = "PressureLow is required.")
    private Double pressureLow;

    @NotBlank(message = "Temperature is required.")
    private Double temperature;

    @NotBlank(message = "Height is required.")
    private Double height;

    @NotBlank(message = "Weight is required.")
    private Double weight;

    @NotBlank(message = "UserId is required.")
    private UUID userId;
}
