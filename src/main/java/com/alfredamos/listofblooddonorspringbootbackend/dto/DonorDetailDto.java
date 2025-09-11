package com.alfredamos.listofblooddonorspringbootbackend.dto;

import com.alfredamos.listofblooddonorspringbootbackend.entities.DonoType;
import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DonorDetailDto {
    private UUID id;
    private Double volumePerDonation;
    private Integer numberOfDonations;
    private DonoType Type;
    private UUID userId;
}
