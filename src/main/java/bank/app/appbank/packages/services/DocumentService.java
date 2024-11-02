package bank.app.appbank.packages.services;

import bank.app.appbank.packages.entities.DocumentEntity;
import bank.app.appbank.packages.repositories.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class DocumentService {
    @Autowired
    private DocumentRepository documentRepository;

    public ArrayList<DocumentEntity> getAllDocuments() {
        return (ArrayList<DocumentEntity>) documentRepository.findAll();
    }

    public DocumentEntity findDocumentById(Long id){
        return documentRepository.findDocumentById(id);
    }

    public DocumentEntity findDocumentByTitle(String title){
        return documentRepository.findDocumentByTitle(title);
    }
    public ArrayList<DocumentEntity> findDocumentByMinimunRequirements(Boolean minimunRequirements){
        return documentRepository.findDocumentsByMinimunRequirements(minimunRequirements);
    }

    public DocumentEntity save(DocumentEntity document){
        return documentRepository.save(document);
    }

    public Boolean deleteDocument(Long id) throws Exception{
        try{
            documentRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
