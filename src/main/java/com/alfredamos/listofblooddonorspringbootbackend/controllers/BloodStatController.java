package com.alfredamos.listofblooddonorspringbootbackend.controllers;

import com.alfredamos.listofblooddonorspringbootbackend.dto.BloodStatCreate;
import com.alfredamos.listofblooddonorspringbootbackend.dto.BloodStatDto;
import com.alfredamos.listofblooddonorspringbootbackend.dto.BloodStatUpdate;
import com.alfredamos.listofblooddonorspringbootbackend.services.BloodStatService;
import com.alfredamos.listofblooddonorspringbootbackend.utils.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/blood-stats")
public class BloodStatController {
    private final BloodStatService bloodStatService;

    @PostMapping
    public ResponseEntity<BloodStatDto> createBloodStat(BloodStatCreate bloodStatRequest) {
        var bloodStat = bloodStatService.createBloodStat(bloodStatRequest);

        return new ResponseEntity<>(bloodStat, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BloodStatDto>> getAllBloodStats() {
       var bloodStats = bloodStatService.getAllBloodStats();

       return ResponseEntity.ok(bloodStats);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BloodStatDto> getBloodStat(@PathVariable(value = "id") UUID id) {
       var bloodStat = bloodStatService.getBloodStatById(id);

       return ResponseEntity.ok(bloodStat);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessage> deleteBloodStatById(@PathVariable(value = "id") UUID id) {
       var response = bloodStatService.deleteBloodStatById(id);

       return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BloodStatDto> editBloodStatById(@PathVariable(value = "id") UUID id, BloodStatUpdate bloodStatRequest) {
       var response = bloodStatService.editBloodStatById(id, bloodStatRequest);

       return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete-by-user-id/{userId}")
    public ResponseEntity<ResponseMessage> deleteBloodStatByUserId(@PathVariable(value = "userId") UUID userId) {
       var response = bloodStatService.deleteBloodStatsByUserId(userId);

       return ResponseEntity.ok(response);
    }

    @DeleteMapping("/all/delete-all")
    public ResponseEntity<ResponseMessage> deleteAllBloodStats() {
       var response = bloodStatService.deleteAllBloodStats();

       return ResponseEntity.ok(response);
    }

    @GetMapping("/get-by-user-id/{userId}")
    public ResponseEntity<List<BloodStatDto>> getBloodStatByUserId(@PathVariable(value = "userId") UUID userId) {
       var bloodStatsDto =  bloodStatService.getBloodStatsByUserId(userId);

       return ResponseEntity.ok(bloodStatsDto);
    }
}
