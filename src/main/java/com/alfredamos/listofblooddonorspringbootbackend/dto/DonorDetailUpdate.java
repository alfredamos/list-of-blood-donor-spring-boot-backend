package com.alfredamos.listofblooddonorspringbootbackend.dto;

import com.alfredamos.listofblooddonorspringbootbackend.entities.Category;
import com.alfredamos.listofblooddonorspringbootbackend.validations.ValueOfEnum;
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
public class DonorDetailUpdate {
    private UUID id;

    @NotNull(message = "VolumePerDonation is required.")
    private Double volumePerDonation;

    @NotNull(message = "NumberOfDonations is required.")
    private Integer numberOfDonations;

    @ValueOfEnum(enumClass = Category.class, message = "It must be First or OneOf or Premium")
    private Category category;

}
