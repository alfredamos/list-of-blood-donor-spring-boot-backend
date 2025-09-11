package com.alfredamos.listofblooddonorspringbootbackend.mapper;

import com.alfredamos.listofblooddonorspringbootbackend.dto.BloodStatCreate;
import com.alfredamos.listofblooddonorspringbootbackend.dto.BloodStatDto;
import com.alfredamos.listofblooddonorspringbootbackend.dto.BloodStatUpdate;
import com.alfredamos.listofblooddonorspringbootbackend.entities.BloodStat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BloodStatMapper {
    BloodStat toEntity(BloodStatDto bloodStatDto);
    BloodStat toEntity(BloodStatCreate bloodStaRequest);
    BloodStat toEntity(BloodStatUpdate bloodStaRequest);


    @Mapping(source = "user.id", target = "userId")
    BloodStatDto toDTO(BloodStat bloodStat);

    List<BloodStatDto> toDTOList(List<BloodStat> bloodStats);
}
