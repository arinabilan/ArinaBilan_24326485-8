package bank.app.appbank.packages.controllers;

import bank.app.appbank.packages.entities.ClientDatesEntity;
import bank.app.appbank.packages.repositories.ClientDatesRepository;
import bank.app.appbank.packages.services.ClientDatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/clientdates")
public class ClientDatesController {
    @Autowired
    private ClientDatesService clientDatesService;

    @PostMapping("/")
    public ResponseEntity<ClientDatesEntity> createDates(@RequestBody ClientDatesEntity clientDates) {
        ClientDatesEntity dates = clientDatesService.saveDates(clientDates);
        return ResponseEntity.ok(dates);
    }
}
