package com.alfredamos.listofblooddonorspringbootbackend.controllers;

import com.alfredamos.listofblooddonorspringbootbackend.dto.VitalCreate;
import com.alfredamos.listofblooddonorspringbootbackend.dto.VitalDto;
import com.alfredamos.listofblooddonorspringbootbackend.dto.VitalUpdate;
import com.alfredamos.listofblooddonorspringbootbackend.services.VitalService;
import com.alfredamos.listofblooddonorspringbootbackend.utils.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vitals")
public class VitalController {
    private final VitalService vitalService;

    @PostMapping
    public ResponseEntity<VitalDto> createVital(VitalCreate vitalRequest) {
        var vitalDto = vitalService.createVital(vitalRequest);

        return ResponseEntity.ok(vitalDto);
    }

    @GetMapping
    public ResponseEntity<List<VitalDto>> getAllVitals() {
        var allVitals = vitalService.findAllVitals();

        return ResponseEntity.ok(allVitals);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VitalDto> getVitalById(@PathVariable(value = "id") UUID id) {
        var vitalDto = vitalService.findVitalById(id);

        return ResponseEntity.ok(vitalDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessage> deleteVitalById(@PathVariable(value = "id") UUID id) {
        var responseMessage = vitalService.deleteVital(id);

        return ResponseEntity.ok(responseMessage);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<VitalDto> editVitalById(@PathVariable(value = "id") UUID id, VitalUpdate vitalRequest) {
        var vital = vitalService.editVitalById(id, vitalRequest);

        return ResponseEntity.ok(vital);
    }

    @DeleteMapping("/delete-by-user-id/{userId}")
    public ResponseEntity<ResponseMessage> deleteVitalByUserId(@PathVariable(value = "userId") UUID id) {
        var response = vitalService.deleteVitalByUserId(id);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/all/delete-all")
    public ResponseEntity<ResponseMessage> deleteAllVitals() {
        var response = vitalService.deleteAllVitals();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-by-user-id/{userId}")
    public ResponseEntity<List<VitalDto>> getVitalByUserId(@PathVariable(value = "userId") UUID userId) {
        var vitalsDto = vitalService.findVitalByUserId(userId);

        return ResponseEntity.ok(vitalsDto);
    }
}
