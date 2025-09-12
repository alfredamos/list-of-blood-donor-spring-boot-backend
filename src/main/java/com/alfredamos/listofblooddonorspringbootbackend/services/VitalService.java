package com.alfredamos.listofblooddonorspringbootbackend.services;

import com.alfredamos.listofblooddonorspringbootbackend.dto.VitalCreate;
import com.alfredamos.listofblooddonorspringbootbackend.dto.VitalDto;
import com.alfredamos.listofblooddonorspringbootbackend.dto.VitalUpdate;
import com.alfredamos.listofblooddonorspringbootbackend.entities.Vital;
import com.alfredamos.listofblooddonorspringbootbackend.entities.User;
import com.alfredamos.listofblooddonorspringbootbackend.exceptions.NotFoundException;
import com.alfredamos.listofblooddonorspringbootbackend.mapper.*;
import com.alfredamos.listofblooddonorspringbootbackend.repositories.UserRepository;
import com.alfredamos.listofblooddonorspringbootbackend.repositories.VitalRepository;
import com.alfredamos.listofblooddonorspringbootbackend.utils.ResponseMessage;
import com.alfredamos.listofblooddonorspringbootbackend.utils.SameUserAndAdmin;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VitalService {
    private final VitalRepository vitalRepository;
    private final AuthService authService;
    private final VitalMapper vitalMapper;
    private final UserRepository userRepository;
    private final SameUserAndAdmin sameUserAndAdmin;

    public VitalDto createVital(VitalCreate vitalRequest) {
        //----> Fetch the user that creates the donor-detail.
        var user = authService.getUserFromContext();

        //----> Check for ownership or admin privilege.
        sameUserAndAdmin.checkForOwnerShipOrAdmin(user.getId());

        //----> map the donor-detail-request to donor-detail.
        var vital = vitalMapper.toEntity(vitalRequest);
        vital.setUser(user); //----> Set the user on the blood-stat.

        //----> save the donor-detail in the database.
        var newVital = vitalRepository.save(vital);

        //----> Send back the result.
        return vitalMapper.toDTO(newVital);
    }

    public ResponseMessage deleteVital(UUID id) {
        //----> Fetch the donor-detail to be deleted.
        var vital = fetchVitalById(id);

        //----> Check for ownership or admin privilege.
        sameUserAndAdmin.checkForOwnerShipOrAdmin(vital.getUser().getId());

        //----> Delete the donor-detail with the given id.
        vitalRepository.delete(vital);

        //----> Send back the response.
        return new ResponseMessage("Vital has been deleted successfully!", "success", HttpStatus.OK);
    }

    public VitalDto editVitalById(UUID id, VitalUpdate vitalRequest) {
        //----> Fetch the donor-detail to be updated.
        var vitalDb = fetchVitalById(id);

        //----> Check for ownership or admin privilege.
        sameUserAndAdmin.checkForOwnerShipOrAdmin(vitalDb.getUser().getId());

        //----> Map the donor-detail-request to donor-detail.
        var vital = vitalMapper.toEntity(vitalRequest);

        //----> Get the user associated with this donor-detail.
        var user = authService.getUserFromContext();
        vital.setUser(user);

        //----> Save the edited donor-detail in the database.
        vitalRepository.save(vital);

        //----> Send back the result.
        return vitalMapper.toDTO(vital);
    }

    public VitalDto findVitalById(UUID id) {
        //----> Fetch the donor-detail.
        var vital = fetchVitalById(id);

        //----> Check for ownership or admin privilege.
        sameUserAndAdmin.checkForOwnerShipOrAdmin(vital.getUser().getId());

        //----> Send back the result.
        return vitalMapper.toDTO(vital);
    }

    public List<VitalDto> findAllVitals() {
        //----> Only admin can perform this action.
        sameUserAndAdmin.checkForAdmin();

        //----> fetch all the donor-details from the database.
        var vitals = vitalRepository.findAll();

        //----> send back the results.
        return vitalMapper.toDTOList(vitals);
    }

    public ResponseMessage deleteVitalByUserId(UUID userId) {
        //----> Fetch the user whose donor-details are to be deleted.
        var user = fetchUserById(userId);

        //----> Check for ownership or admin privilege.
        sameUserAndAdmin.checkForOwnerShipOrAdmin(user.getId());

        //----> delete donor-details associated with this user.
        vitalRepository.deleteVitalByUser(user);

        return new ResponseMessage("Vital has been deleted successfully!", "success", HttpStatus.OK);
    }

    public ResponseMessage deleteAllVitals() {
        //----> Only admin can perform this action.
        sameUserAndAdmin.checkForAdmin();

        //----> Delete all donor-details.
        vitalRepository.deleteAll();

        //----> Send back response.
        return new ResponseMessage("Vital has been deleted successfully!", "success", HttpStatus.OK);
    }

    public List<VitalDto> findVitalByUserId(UUID userId) {
        var user = fetchUserById(userId); //----> fetch the user with this user-id.

        //----> Check for ownership or admin privilege.
        sameUserAndAdmin.checkForOwnerShipOrAdmin(user.getId());

        //----> Fetch all donor-details associate with this user.
        var vitals = vitalRepository.findVitalByUser(user);

        //----> Send back the response.
        return vitalMapper.toDTOList(vitals);
    }

    private Vital fetchVitalById(UUID id) {
        return vitalRepository.findById(id).orElseThrow(() -> new NotFoundException("Donor detail not found"));
    }

    private User fetchUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found!"));
    }

}
