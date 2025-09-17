package com.alfredamos.listofblooddonorspringbootbackend.dto;

import com.alfredamos.listofblooddonorspringbootbackend.entities.Category;
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
    private Category category;
    private UUID userId;
}
