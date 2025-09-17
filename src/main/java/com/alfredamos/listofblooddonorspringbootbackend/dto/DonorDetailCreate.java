package com.alfredamos.listofblooddonorspringbootbackend.dto;

import com.alfredamos.listofblooddonorspringbootbackend.entities.Category;
import com.alfredamos.listofblooddonorspringbootbackend.validations.ValueOfEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DonorDetailCreate {
    @NotNull(message = "VolumePerDonation is required.")
    private Double volumePerDonation;

    @NotNull(message = "NumberOfDonations is required.")
    private Integer numberOfDonations;

    @ValueOfEnum(enumClass = Category.class, message = "It must be First or OneOf or Premium")
    private Category category;
}
