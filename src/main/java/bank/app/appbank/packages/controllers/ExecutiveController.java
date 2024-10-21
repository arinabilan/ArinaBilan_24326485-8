package bank.app.appbank.packages.controllers;

import bank.app.appbank.packages.entities.ExecutiveEntity;
import bank.app.appbank.packages.services.ExecutiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/executives")
public class ExecutiveController {
    @Autowired
    private ExecutiveService executiveService;

    @GetMapping("/{id}")
    public ResponseEntity<ExecutiveEntity> getExecutive(@PathVariable Long id) {
        ExecutiveEntity executive = executiveService.getExecutive(id);
        return ResponseEntity.ok(executive);
    }

    @PostMapping("/")
    public ResponseEntity<ExecutiveEntity> createExecutive(@RequestBody ExecutiveEntity executive) {
        ExecutiveEntity newExecutive = executiveService.addExecutive(executive);
        return ResponseEntity.ok(newExecutive);
    }

    @PutMapping("/")
    public ResponseEntity<ExecutiveEntity> updateExecutive(@RequestBody ExecutiveEntity executive) {
        ExecutiveEntity updatedExecutive = executiveService.updateExecutive(executive);
        return ResponseEntity.ok(updatedExecutive);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteExecutive(@PathVariable Long id) throws Exception{
        executiveService.deleteExecutive(id);
        return ResponseEntity.noContent().build();
    }
}
