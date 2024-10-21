package bank.app.appbank.packages.controllers;

import bank.app.appbank.packages.entities.ClientEntity;
import bank.app.appbank.packages.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {
    @Autowired
    ClientService clientService;

    @GetMapping("/")
    public ResponseEntity<List<ClientEntity>> listClients(){
        List<ClientEntity> clients = clientService.getClients();
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientEntity> getById(@PathVariable Long id){
        ClientEntity client = clientService.getById(id);
        return ResponseEntity.ok(client);
    }

    @GetMapping("/rut/{rut}")
    public ResponseEntity<ClientEntity> getClientByRut(@PathVariable String rut){
        ClientEntity client = clientService.getClientByRut(rut);
        return ResponseEntity.ok(client);
    }

    @GetMapping("/{name}/{surname}")
    public ResponseEntity<List<ClientEntity>> getClientByNameAndSurname(@PathVariable String name, @PathVariable String surname){
        List<ClientEntity> clients = clientService.getClientByNameAndSurname(name,surname);
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<ClientEntity>> getClientByName(@PathVariable String name){
        List<ClientEntity> clients = clientService.getClientByName(name);
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/surname/{surname}")
    public ResponseEntity<List<ClientEntity>> getBySurname(@PathVariable String surname){
        List<ClientEntity> clients = clientService.getBySurname(surname);
        return ResponseEntity.ok(clients);
    }

    //@GetMapping("/email/{email}")
    //public ResponseEntity<Optional<ClientEntity>> getByEmail(@PathVariable String email){
        //Optional<ClientEntity> clients = clientService.findByEmail(email);
        //return ResponseEntity.ok(clients);
    //}

    @PostMapping("/login/email")
    public ResponseEntity<Object> findByEmailAndPassword(@RequestBody Map<String, String> credentials){
        String email = credentials.get("email");
        String password = credentials.get("password");
        try{
            ClientEntity client = clientService.findByEmailAndPassword(email,password);
            return ResponseEntity.ok(client);
        } catch (IllegalArgumentException e) {
            // Si hay algún problema con la autenticación, retorna un error
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }


    }

    @GetMapping("/start")
    public ResponseEntity<List<ClientEntity>> getByBirthdayBetween(@RequestParam("startDate") LocalDate startDate, @RequestParam("endDate") LocalDate endDate){ //despues en front se llama con {params:(startDate, endDate)}
        List<ClientEntity> clients = clientService.getByBirthdayBetween(startDate, endDate);
        return ResponseEntity.ok(clients);
    }

    @PostMapping("/")
    public ResponseEntity<ClientEntity> saveClient(@RequestBody ClientEntity client){
        ClientEntity clientNew = clientService.saveClient(client);
        return ResponseEntity.ok(clientNew);
    }

    @PutMapping("/")
    public ResponseEntity<ClientEntity> updateClient(@RequestBody ClientEntity client){
        ClientEntity clientUpdated = clientService.updateClient(client);
        return ResponseEntity.ok(clientUpdated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteClient(@PathVariable Long id) throws Exception{
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
}
