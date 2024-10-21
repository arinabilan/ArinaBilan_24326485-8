package bank.app.appbank.packages.controllers;

import bank.app.appbank.packages.entities.ClientDocumentEntity;
import bank.app.appbank.packages.services.ClientDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/documents")
public class ClientDocumentController {
    @Autowired
    private ClientDocumentService clientDocumentService;

    @GetMapping("/{id}")
    public ResponseEntity<List<ClientDocumentEntity>> findByClientId(@PathVariable Long id) {
        List<ClientDocumentEntity> clientDocuments = clientDocumentService.findByClientId(id);
        return ResponseEntity.ok(clientDocuments);
    }

    @GetMapping("/{clientId}/{title}")
    public ResponseEntity<ClientDocumentEntity> findByClientIdAndTitle(@PathVariable Long clientId, @PathVariable String title){
        ClientDocumentEntity clientDocument = clientDocumentService.findByClientIdAndTitle(clientId, title);
        return ResponseEntity.ok(clientDocument);
    }

    @GetMapping("/rut/{rut}")
    public ResponseEntity<List<ClientDocumentEntity>> findByClientRut(@PathVariable String rut){
        List<ClientDocumentEntity> documents = clientDocumentService.findByClientRut(rut);
        return ResponseEntity.ok(documents);
    }

    @PostMapping("/")
    public ResponseEntity<ClientDocumentEntity> saveClientDocument(@RequestBody ClientDocumentEntity document){
        ClientDocumentEntity clientDocument = clientDocumentService.saveClientDocument(document);
        return ResponseEntity.ok(clientDocument);
    }

    @PutMapping("/")
    public ResponseEntity<ClientDocumentEntity> updateClientDocument(@RequestBody ClientDocumentEntity document){
        ClientDocumentEntity clientDocument = clientDocumentService.updateClientDocument(document);
        return ResponseEntity.ok(clientDocument);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteClientDocument(@PathVariable Long id) throws Exception{
        clientDocumentService.deleteClientDocument(id);
        return ResponseEntity.noContent().build();

    }
}
