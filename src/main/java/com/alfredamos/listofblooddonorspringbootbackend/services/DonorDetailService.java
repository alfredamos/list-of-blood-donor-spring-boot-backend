package com.alfredamos.listofblooddonorspringbootbackend.services;

import com.alfredamos.listofblooddonorspringbootbackend.dto.DonorDetailCreate;
import com.alfredamos.listofblooddonorspringbootbackend.dto.DonorDetailDto;
import com.alfredamos.listofblooddonorspringbootbackend.dto.DonorDetailUpdate;
import com.alfredamos.listofblooddonorspringbootbackend.entities.DonorDetail;
import com.alfredamos.listofblooddonorspringbootbackend.entities.User;
import com.alfredamos.listofblooddonorspringbootbackend.exceptions.NotFoundException;
import com.alfredamos.listofblooddonorspringbootbackend.mapper.DonorDetailMapper;
import com.alfredamos.listofblooddonorspringbootbackend.repositories.DonorDetailRepository;
import com.alfredamos.listofblooddonorspringbootbackend.repositories.UserRepository;
import com.alfredamos.listofblooddonorspringbootbackend.utils.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class DonorDetailService {
    private final DonorDetailRepository donorDetailRepository;
    private final AuthService authService;
    private final DonorDetailMapper donorDetailMapper;
    private final UserRepository userRepository;

    public DonorDetailDto createDonorDetail(DonorDetailCreate donorDetailRequest) {
        //----> Fetch the user that creates the donor-detail.
        var user = authService.getUserFromContext();

        //----> map the donor-detail-request to donor-detail.
        var donorDetail = donorDetailMapper.toEntity(donorDetailRequest);
        donorDetail.setUser(user); //----> Set the user on the blood-stat.

        //----> save the donor-detail in the database.
        var newDonorDetail = donorDetailRepository.save(donorDetail);

        //----> Send back the result.
        return donorDetailMapper.toDTO(newDonorDetail);
    }

    public ResponseMessage deleteDonorDetail(UUID id) {
        //----> Fetch the donor-detail to be deleted.
        var donorDetail = fetchDonorDetailById(id);

        //----> Delete the donor-detail with the given id.
        donorDetailRepository.delete(donorDetail);

        //----> Send back the response.
        return new ResponseMessage("DonorDetail has been deleted successfully!", "success", HttpStatus.OK);
    }

    public DonorDetailDto editDonorDetailById(UUID id, DonorDetailUpdate donorDetailRequest) {
        //----> Fetch the donor-detail to be updated.
        fetchDonorDetailById(id);

        //----> Map the donor-detail-request to donor-detail.
        var donorDetail = donorDetailMapper.toEntity(donorDetailRequest);

        //----> Get the user associated with this donor-detail.
        var user = authService.getUserFromContext();
        donorDetail.setUser(user);

        //----> Save the edited donor-detail in the database.
        donorDetailRepository.save(donorDetail);

        //----> Send back the result.
        return donorDetailMapper.toDTO(donorDetail);
    }

    public DonorDetailDto findDonorDetailById(UUID id) {
        //----> Fetch the donor-detail.
        var donorDetail = fetchDonorDetailById(id);

        //----> Send back the result.
        return donorDetailMapper.toDTO(donorDetail);
    }

    public List<DonorDetailDto> findAllDonorDetails() {
        //----> fetch all the donor-details from the database.
        var donorDetails = donorDetailRepository.findAll();

        //----> send back the results.
        return donorDetailMapper.toDTOList(donorDetails);
    }

    public ResponseMessage deleteDonorDetailByUserId(UUID userId) {
        //----> Fetch the user whose donor-details are to be deleted.
        var user = fetchUserById(userId);

        //----> delete donor-details associated with this user.
        donorDetailRepository.deleteDonorDetailByUser(user);

        return new ResponseMessage("DonorDetail has been deleted successfully!", "success", HttpStatus.OK);
    }

    public ResponseMessage deleteAllDonorDetails() {
        //----> Delete all donor-details.
        donorDetailRepository.deleteAll();

        //----> Send back response.
        return new ResponseMessage("DonorDetail has been deleted successfully!", "success", HttpStatus.OK);
    }

    public List<DonorDetailDto> findDonorDetailByUserId(UUID userId) {
        var user = fetchUserById(userId); //----> fetch the user with this user-id.

        //----> Fetch all donor-details associate with this user.
        var donorDetails = donorDetailRepository.findDonorDetailByUser(user);

        //----> Send back the response.
        return donorDetailMapper.toDTOList(donorDetails);
    }

    private DonorDetail fetchDonorDetailById(UUID id) {
        return donorDetailRepository.findById(id).orElseThrow(() -> new NotFoundException("Donor detail not found"));
    }

    private User fetchUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found!"));
    }

}
