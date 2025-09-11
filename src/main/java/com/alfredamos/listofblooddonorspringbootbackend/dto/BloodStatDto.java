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
public class BloodStatDto {
    private UUID id;
    private String genoType;
    private String bloodGroup;
    private UUID userId;
}
