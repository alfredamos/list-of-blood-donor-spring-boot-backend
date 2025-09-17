package com.alfredamos.listofblooddonorspringbootbackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VitalCreate {
    @NotNull(message = "PressureUp is required.")
    private Double pressureUp;

    @NotNull(message = "PressureLow is required.")
    private Double pressureLow;

    @NotNull(message = "Temperature is required.")
    private Double temperature;

    @NotNull(message = "Height is required.")
    private Double height;

    @NotNull(message = "Weight is required.")
    private Double weight;

}
