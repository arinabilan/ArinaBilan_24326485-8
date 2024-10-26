package bank.app.appbank.packages.controllers;

import bank.app.appbank.packages.entities.SavingCapacityEntity;
import bank.app.appbank.packages.services.SavingCapacityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/savingCapacity")
public class SavingCapacityController {
    @Autowired
    SavingCapacityService savingCapacityService;

    @PostMapping("/")
    public ResponseEntity<SavingCapacityEntity> createSavingCapacity(@RequestBody SavingCapacityEntity saving) {
        SavingCapacityEntity savingCapacity = savingCapacityService.saveCapacity(saving);
        return ResponseEntity.ok(savingCapacity);
    }
}
