package com.alfredamos.listofblooddonorspringbootbackend.dto;

import com.alfredamos.listofblooddonorspringbootbackend.entities.DonoType;
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
public class DonorDetailCreate {
    @NotBlank(message = "VolumePerDonation is required.")
    private Double volumePerDonation;

    @NotBlank(message = "NumberOfDonations is required.")
    private Integer numberOfDonations;

    @NotBlank(message = "Type is required.")
    private DonoType Type;

    @NotBlank(message = "UserId is required.")
    private UUID userId;
}
