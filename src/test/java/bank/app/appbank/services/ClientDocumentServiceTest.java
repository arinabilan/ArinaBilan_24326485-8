package bank.app.appbank.services;
import bank.app.appbank.packages.entities.ClientDocumentEntity;
import bank.app.appbank.packages.entities.ClientEntity;
import bank.app.appbank.packages.entities.DocumentEntity;
import bank.app.appbank.packages.repositories.ClientDocumentRepository;
import bank.app.appbank.packages.services.ClientDocumentService;
import bank.app.appbank.packages.services.ClientService;
import bank.app.appbank.packages.services.DocumentService;
import bank.app.appbank.packages.services.FileUploadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
public class ClientDocumentServiceTest {
    @InjectMocks
    private ClientDocumentService clientDocumentService;

    @Mock
    private ClientDocumentRepository clientDocumentRepository;

    @Mock
    private ClientService clientService;

    @Mock
    private DocumentService documentService;

    @Mock
    private FileUploadService fileUploadService;

    @Mock
    private MultipartFile mockFile;

    private ClientEntity mockClient;
    private DocumentEntity mockDocument;
    private ClientDocumentEntity mockClientDocument;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockClient = new ClientEntity();
        mockClient.setId(1L);

        mockDocument = new DocumentEntity();
        mockDocument.setId(1L);
        mockClient.setRut("12345678-9");

        mockClientDocument = new ClientDocumentEntity();
        mockClientDocument.setId(1L);
        mockDocument.setTitle("Test Document Title"); // Agrega un título al documento
        mockClientDocument.setClient(mockClient);
        mockClientDocument.setDocument(mockDocument);
        mockClientDocument.setFechaCarga(LocalDate.now());
        mockClientDocument.setEstado(false);
    }

    @Test
    void testFindByClientId() {
        ArrayList<ClientDocumentEntity> documents = new ArrayList<>();
        documents.add(mockClientDocument);
        when(clientDocumentRepository.findByClientId(1L)).thenReturn(documents);

        ArrayList<ClientDocumentEntity> result = clientDocumentService.findByClientId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(mockClientDocument.getId(), result.get(0).getId());
        verify(clientDocumentRepository, times(1)).findByClientId(1L);
    }

    @Test
    void testSaveClientDocument() {
        when(clientDocumentRepository.save(any(ClientDocumentEntity.class))).thenReturn(mockClientDocument);

        ClientDocumentEntity savedDocument = clientDocumentService.saveClientDocument(mockClientDocument);

        assertNotNull(savedDocument);
        assertEquals(mockClientDocument.getId(), savedDocument.getId());
        verify(clientDocumentRepository, times(1)).save(mockClientDocument);
    }

    @Test
    void testUpdateClientDocument() {
        when(clientDocumentRepository.save(any(ClientDocumentEntity.class))).thenReturn(mockClientDocument);

        ClientDocumentEntity updatedDocument = clientDocumentService.updateClientDocument(mockClientDocument);

        assertNotNull(updatedDocument);
        assertEquals(mockClientDocument.getId(), updatedDocument.getId());
        verify(clientDocumentRepository, times(1)).save(mockClientDocument);
    }

    @Test
    void testDeleteClientDocument() throws Exception {
        doNothing().when(clientDocumentRepository).deleteById(1L);

        Boolean result = clientDocumentService.deleteClientDocument(1L);

        assertTrue(result);
        verify(clientDocumentRepository, times(1)).deleteById(1L);
    }

    @Test
    void testUploadClientDocument() throws Exception {
        when(fileUploadService.uploadFile(mockFile)).thenReturn("path/to/file");
        when(clientService.getById(1L)).thenReturn(mockClient);
        when(documentService.findDocumentById(1L)).thenReturn(mockDocument);
        when(clientDocumentRepository.save(any(ClientDocumentEntity.class))).thenReturn(mockClientDocument);

        Boolean result = clientDocumentService.uploadClientDocument(1L, 1L, mockFile);

        assertTrue(result);
        verify(fileUploadService, times(1)).uploadFile(mockFile);
        verify(clientService, times(1)).getById(1L);
        verify(documentService, times(1)).findDocumentById(1L);
        verify(clientDocumentRepository, times(1)).save(any(ClientDocumentEntity.class));
    }

    @Test
    void testFindByClientRut() {
        // Prepara los datos de prueba
        ArrayList<ClientDocumentEntity> documents = new ArrayList<>();
        documents.add(mockClientDocument);

        // Simula el comportamiento del repositorio
        when(clientDocumentRepository.findByClientRut("12345678-9")).thenReturn(documents);

        // Ejecuta el método que estamos probando
        ArrayList<ClientDocumentEntity> result = clientDocumentService.findByClientRut("12345678-9");

        // Verifica el resultado
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(mockClientDocument.getId(), result.get(0).getId());
        assertEquals("12345678-9", result.get(0).getClient().getRut());

        // Verifica que el repositorio fue llamado correctamente
        verify(clientDocumentRepository, times(1)).findByClientRut("12345678-9");
    }

    @Test
    void testFindByClientIdAndTitle() {
        // Prepara el escenario de prueba
        when(clientDocumentRepository.findByClientIdAndTitle(1L, "Test Document Title")).thenReturn(mockClientDocument);


        ClientDocumentEntity result = clientDocumentService.findByClientIdAndTitle(1L, "Test Document Title");

        // Verifica el resultado
        assertNotNull(result);
        assertEquals(mockClientDocument.getId(), result.getId());
        assertEquals("Test Document Title", result.getDocument().getTitle()); // Verifica el título del documento

        // Verifica que el repositorio fue llamado correctamente
        verify(clientDocumentRepository, times(1)).findByClientIdAndTitle(1L, "Test Document Title");
    }

    @Test
    void testDeleteClientDocumentThrowsException() {
        // Configurar el escenario para lanzar una excepción al intentar eliminar el documento
        doThrow(new RuntimeException("Error deleting document")).when(clientDocumentRepository).deleteById(1L);

        // Ejecutar la acción y verificar que se lanza la excepción
        Exception exception = assertThrows(Exception.class, () -> {
            clientDocumentService.deleteClientDocument(1L);
        });

        // Verificar que el mensaje de la excepción es el esperado
        assertEquals("Error deleting document", exception.getMessage());

        // Verificar que el repositorio fue llamado
        verify(clientDocumentRepository, times(1)).deleteById(1L);
    }
}
