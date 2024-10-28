package bank.app.appbank.packages.controllers;

import bank.app.appbank.packages.entities.ExecutiveEntity;
import bank.app.appbank.packages.entities.SolicitudeEntity;
import bank.app.appbank.packages.services.SolicitudeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/solicitude")
public class SolicitudeController {
    @Autowired
    private SolicitudeService solicitudeService;

    @GetMapping("/")
    public ResponseEntity<List<SolicitudeEntity>> findAll() {
        List<SolicitudeEntity> solicitudes = solicitudeService.getAllSolicitudes();
        return ResponseEntity.ok(solicitudes);
    }

    @PostMapping("/")
    public ResponseEntity<SolicitudeEntity> saveSolicitude(@RequestBody SolicitudeEntity solicitude) {
        SolicitudeEntity saved = solicitudeService.saveSolicitude(solicitude);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}/")
    public ResponseEntity<SolicitudeEntity> updateSolicitude(@PathVariable("id") Long id, @RequestBody ExecutiveEntity executive) {
        SolicitudeEntity updated = solicitudeService.updateSolicitude(id, executive);
        return ResponseEntity.ok(updated);
    }

}
