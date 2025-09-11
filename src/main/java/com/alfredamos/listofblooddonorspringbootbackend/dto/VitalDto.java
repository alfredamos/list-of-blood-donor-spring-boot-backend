package com.alfredamos.listofblooddonorspringbootbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VitalDto {
    private UUID id;
    private Double pressureUp;
    private Double pressureLow;
    private Double temperature;
    private Double height;
    private Double weight;
    private UUID userId;
}
