package com.alfredamos.listofblooddonorspringbootbackend.services;

import com.alfredamos.listofblooddonorspringbootbackend.dto.BloodStatCreate;
import com.alfredamos.listofblooddonorspringbootbackend.dto.BloodStatDto;
import com.alfredamos.listofblooddonorspringbootbackend.dto.BloodStatUpdate;
import com.alfredamos.listofblooddonorspringbootbackend.entities.BloodStat;
import com.alfredamos.listofblooddonorspringbootbackend.entities.User;
import com.alfredamos.listofblooddonorspringbootbackend.exceptions.NotFoundException;
import com.alfredamos.listofblooddonorspringbootbackend.mapper.BloodStatMapper;
import com.alfredamos.listofblooddonorspringbootbackend.repositories.BloodStatRepository;
import com.alfredamos.listofblooddonorspringbootbackend.repositories.UserRepository;
import com.alfredamos.listofblooddonorspringbootbackend.utils.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BloodStatService {
    private final BloodStatRepository bloodStatRepository;
    private final AuthService authService;
    private final BloodStatMapper bloodStatMapper;
    private final UserRepository userRepository;

    public BloodStatDto createBloodStat(BloodStatCreate bloodStatRequest) {
        //----> Get the user associated with the creation of this blood-stat.
        var user = authService.getUserFromContext();

        //----> Map blood-create to blood-stat.
        var bloodStat = bloodStatMapper.toEntity(bloodStatRequest);

        //----> set the associated user on the blood-stat.
        bloodStat.setUser(user);

        //----> Save the blood stat in the database.
        bloodStatRepository.save(bloodStat);

        //----> send back the result.
        return bloodStatMapper.toDTO(bloodStat);

    }

    public ResponseMessage deleteBloodStatById(UUID id) {
        //----> Check for existence of user.
        var bloodStat = fetchBloodStatById(id);

        //----> Delete the blood-stat with the giving id.
        bloodStatRepository.delete(bloodStat);

        //----> send back the response.
        return new ResponseMessage("BloodStat is deleted successfully!", "success", HttpStatus.OK);
    }

    public BloodStatDto editBloodStatById(UUID id, BloodStatUpdate bloodStatRequest) {
        //----> Check for existence of blood-stat with the giving id.
        fetchBloodStatById(id);

        //----> Map bloodStatRequest to bloodStat.
        var bloodStat = bloodStatMapper.toEntity(bloodStatRequest);

        //----> fetch the user associated with the update of this blood-stat.
        var user = authService.getUserFromContext();
        bloodStat.setUser(user);

        //----> save the edited blood-stat in the database.
        bloodStatRepository.save(bloodStat);

        //----> send back the results.
        return bloodStatMapper.toDTO(bloodStat);
    }

    public BloodStatDto getBloodStatById(UUID id) {
        //----> Check for existence of blood-stat with the giving id.
        var bloodStat = fetchBloodStatById(id);

        //----> Send back the result.
        return bloodStatMapper.toDTO(bloodStat);
    }

    public List<BloodStatDto> getAllBloodStats() {
       //----> Get all the blood-stats from the database.
        var bloodStats = bloodStatRepository.findAll();

        //----> Send back the results.
        return bloodStatMapper.toDTOList(bloodStats);
    }


    public ResponseMessage deleteAllBloodStats(){
        //----> delete all blood-stats in the database.
        bloodStatRepository.deleteAll();

        //----> send back the response.
        return new ResponseMessage("BloodStats deleted successfully!", "success", HttpStatus.OK);
    }


    public ResponseMessage deleteAllBloodStatsByUserId(UUID userId) {
        //----> Fetch user with the given user-id.
        var user = fetchUserById(userId);

        //----> Delete all the blood-stats associated with this user.
        bloodStatRepository.deleteBloodStatsByUser(user);

        //----> send back the response.
        return new ResponseMessage("BloodStats are deleted successfully!", "success", HttpStatus.OK);
    }


    public List<BloodStatDto> getBloodStatsByUserId(UUID userId) {
        //----> Fetch user with the given user-id.
        var user = fetchUserById(userId);

       //----> Get all the blood-stats associated with this user.
       var bloodStats = bloodStatRepository.findBloodStatByUser(user);

       //----> Send back the result.
       return bloodStatMapper.toDTOList(bloodStats);

    }

    private User fetchUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found!"));
    }

    private BloodStat fetchBloodStatById(UUID id) {
        return bloodStatRepository.findById(id).orElseThrow(() -> new NotFoundException("Blood Stat Not Found in the database!"));
    }
}
