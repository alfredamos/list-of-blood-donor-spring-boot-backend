package com.alfredamos.listofblooddonorspringbootbackend.mapper;

import com.alfredamos.listofblooddonorspringbootbackend.dto.VitalCreate;
import com.alfredamos.listofblooddonorspringbootbackend.dto.VitalDto;
import com.alfredamos.listofblooddonorspringbootbackend.dto.VitalUpdate;
import com.alfredamos.listofblooddonorspringbootbackend.entities.Vital;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VitalMapper {
    Vital toEntity(VitalDto vitalDto);
    Vital toEntity(VitalCreate vitalRequest);
    Vital toEntity(VitalUpdate vitalRequest);

    @Mapping(source = "user.id", target = "userId")
    VitalDto toDTO(Vital vital);

    List<VitalDto> toDTOList(List<Vital> vitals);
}
