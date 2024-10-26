package bank.app.appbank.packages.controllers;

import bank.app.appbank.packages.entities.SolicitudeEntity;
import bank.app.appbank.packages.services.SolicitudeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/solicitude")
public class SolicitudeController {
    @Autowired
    private SolicitudeService solicitudeService;

    @PostMapping("/")
    public ResponseEntity<SolicitudeEntity> saveSolicitude(@RequestBody SolicitudeEntity solicitude) {
        SolicitudeEntity saved = solicitudeService.saveSolicitude(solicitude);
        return ResponseEntity.ok(saved);
    }
}
