package bank.app.appbank.packages.controllers;

import bank.app.appbank.packages.entities.DocumentEntity;
import bank.app.appbank.packages.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/documents")
public class DocumentController {
    @Autowired
    private DocumentService documentService;

    @GetMapping("/")
    public ResponseEntity<List<DocumentEntity>> getAllDocuments() {
        List<DocumentEntity> documents = documentService.getAllDocuments();
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentEntity> findDocumentById(@PathVariable Long id){
        DocumentEntity document = documentService.findDocumentById(id);
        return ResponseEntity.ok(document);
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<DocumentEntity> findDocumentByTittle(@PathVariable String title){
        DocumentEntity document = documentService.findDocumentByTitle(title);
        return ResponseEntity.ok(document);
    }
    @PostMapping("/")
    public ResponseEntity<DocumentEntity> createDocument(@RequestBody DocumentEntity document){
        DocumentEntity documento = documentService.save(document);
        return ResponseEntity.ok(documento);
    }
    @PutMapping("/{id}")
    public ResponseEntity<DocumentEntity> updateDocument(@RequestBody DocumentEntity document){
        DocumentEntity documento = documentService.save(document);
        return ResponseEntity.ok(documento);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteDocument(@PathVariable Long id) throws Exception{
        documentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }
}
