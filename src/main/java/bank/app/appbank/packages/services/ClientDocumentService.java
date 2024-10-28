package bank.app.appbank.packages.services;

import bank.app.appbank.packages.entities.ClientDocumentEntity;
import bank.app.appbank.packages.entities.ClientEntity;
import bank.app.appbank.packages.entities.DocumentEntity;
import bank.app.appbank.packages.repositories.ClientDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
//Este archivo servicio es logica de negocio, servicio contiene toda la logica, es capa superior a repositorio,
//por lo tanto llama a repositorio.
@Service
public class ClientDocumentService {
    @Autowired
    private ClientDocumentRepository clientDocumentRepository;//se conecta con la capa inferior que es repositorio
    @Autowired
    private ClientService clientService;//necesitamos conectarse con repositorio del cliente, porque vamos a buscar
    //informacion asociada a cliente

    @Autowired
    private FileUploadService fileUploadService;
    @Autowired
    private DocumentService documentService;

    public ArrayList<ClientDocumentEntity> findByClientId(Long id) {
        return clientDocumentRepository.findByClientId(id);
    }

    public ArrayList<ClientDocumentEntity> findByClientRut(String rut) {
        return clientDocumentRepository.findByClientRut(rut);
    }

    public ClientDocumentEntity findByClientIdAndTitle(Long clientId, String title){
        return clientDocumentRepository.findByClientIdAndTitle(clientId, title);
    }

    public ClientDocumentEntity saveClientDocument(ClientDocumentEntity document){
        return clientDocumentRepository.save(document);
    }

    public ClientDocumentEntity updateClientDocument(ClientDocumentEntity document){
        return clientDocumentRepository.save(document);
    }

    public Boolean deleteClientDocument(Long id) throws Exception{
        try{
            clientDocumentRepository.deleteById(id);
            return true;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public Boolean uploadClientDocument(Long client_id, Long document_type, MultipartFile file) throws Exception{
        String fileName = fileUploadService.uploadFile(file);
        ClientDocumentEntity clientDocumentEntity = new ClientDocumentEntity();
        ClientEntity clientEntity = clientService.getById(client_id);
        DocumentEntity documentEntity = documentService.findDocumentById(document_type);
        clientDocumentEntity.setClient(clientEntity);
        clientDocumentEntity.setDocument(documentEntity);
        clientDocumentEntity.setFechaCarga(LocalDate.now());
        clientDocumentEntity.setRutaDocumento(fileName);
        clientDocumentEntity.setEstado(false);
        clientDocumentRepository.save(clientDocumentEntity);
        return true;
    }
}

