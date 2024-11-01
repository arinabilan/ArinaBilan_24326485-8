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

    @GetMapping("/{id}")
    public ResponseEntity<SavingCapacityEntity> getCapacityByClientId(@PathVariable Long id) {
        SavingCapacityEntity savingCapacity = savingCapacityService.getByClientId(id);
        return ResponseEntity.ok(savingCapacity);
    }

    @PostMapping("/")
    public ResponseEntity<SavingCapacityEntity> createSavingCapacity(@RequestBody SavingCapacityEntity saving) {
        SavingCapacityEntity savingCapacity = savingCapacityService.saveCapacity(saving);
        return ResponseEntity.ok(savingCapacity);
    }

    @PutMapping("/")
    public ResponseEntity<SavingCapacityEntity> updateSavingCapacity(@RequestBody SavingCapacityEntity saving) {
        SavingCapacityEntity savingCapacity = savingCapacityService.saveCapacity(saving);
        return ResponseEntity.ok(savingCapacity);
    }
}
