package com.alfredamos.listofblooddonorspringbootbackend.mapper;

import com.alfredamos.listofblooddonorspringbootbackend.dto.DonorDetailCreate;
import com.alfredamos.listofblooddonorspringbootbackend.dto.DonorDetailDto;
import com.alfredamos.listofblooddonorspringbootbackend.dto.DonorDetailUpdate;
import com.alfredamos.listofblooddonorspringbootbackend.entities.DonorDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DonorDetailMapper {
    DonorDetail toEntity(DonorDetailDto donorDetailDto);
    DonorDetail toEntity(DonorDetailCreate donorDetailRequest);
    DonorDetail toEntity(DonorDetailUpdate donorDetailRequest);

    @Mapping(source = "user.id", target = "userId")
    DonorDetailDto toDTO(DonorDetail donorDetail);

    List<DonorDetailDto> toDTOList(List<DonorDetail> donorDetails);
}
