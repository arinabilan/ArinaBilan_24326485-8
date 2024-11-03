package bank.app.appbank.services;
import bank.app.appbank.packages.entities.DocumentEntity;
import bank.app.appbank.packages.repositories.DocumentRepository;
import bank.app.appbank.packages.services.DocumentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DocumentServiceTest {
    @InjectMocks
    private DocumentService documentService;

    @Mock
    private DocumentRepository documentRepository;

    private DocumentEntity mockDocument;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockDocument = new DocumentEntity();
        mockDocument.setId(1L);
        mockDocument.setTitle("Test Document Title");
        mockDocument.setMinimunRequirements(false);
    }

    @Test
    void testGetAllDocuments() {
        List<DocumentEntity> mockDocuments = new ArrayList<>();
        mockDocuments.add(mockDocument);

        when(documentRepository.findAll()).thenReturn(mockDocuments);

        ArrayList<DocumentEntity> result = documentService.getAllDocuments();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(mockDocument.getTitle(), result.get(0).getTitle());
        verify(documentRepository, times(1)).findAll();
    }

    @Test
    void testFindDocumentById() {
        when(documentRepository.findDocumentById(mockDocument.getId())).thenReturn(mockDocument);

        DocumentEntity result = documentService.findDocumentById(mockDocument.getId());

        assertNotNull(result);
        assertEquals(mockDocument.getId(), result.getId());
        verify(documentRepository, times(1)).findDocumentById(mockDocument.getId());
    }

    @Test
    void testFindDocumentByTitle() {
        when(documentRepository.findDocumentByTitle(mockDocument.getTitle())).thenReturn(mockDocument);

        DocumentEntity result = documentService.findDocumentByTitle(mockDocument.getTitle());

        assertNotNull(result);
        assertEquals(mockDocument.getTitle(), result.getTitle());
        verify(documentRepository, times(1)).findDocumentByTitle(mockDocument.getTitle());
    }

    @Test
    void testSave() {
        when(documentRepository.save(any(DocumentEntity.class))).thenReturn(mockDocument);

        DocumentEntity result = documentService.save(mockDocument);

        assertNotNull(result);
        assertEquals(mockDocument.getId(), result.getId());
        assertEquals(mockDocument.getTitle(), result.getTitle());
        verify(documentRepository, times(1)).save(mockDocument);
    }

    @Test
    void testDeleteDocument() throws Exception {
        doNothing().when(documentRepository).deleteById(mockDocument.getId());

        Boolean result = documentService.deleteDocument(mockDocument.getId());

        assertTrue(result);
        verify(documentRepository, times(1)).deleteById(mockDocument.getId());
    }

    @Test
    void testDeleteDocumentThrowsException() {
        doThrow(new RuntimeException("Error deleting document")).when(documentRepository).deleteById(mockDocument.getId());

        Exception exception = assertThrows(Exception.class, () -> {
            documentService.deleteDocument(mockDocument.getId());
        });

        assertEquals("Error deleting document", exception.getMessage());
        verify(documentRepository, times(1)).deleteById(mockDocument.getId());
    }

    @Test
    void testFindDocumentByMinimunRequirements() {
        // Crea un documento simulado
        DocumentEntity mockDocument1 = new DocumentEntity();
        mockDocument1.setId(1L);
        mockDocument1.setTitle("Test Document 1");
        mockDocument1.setMinimunRequirements(true);

        DocumentEntity mockDocument2 = new DocumentEntity();
        mockDocument2.setId(2L);
        mockDocument2.setTitle("Test Document 2");
        mockDocument2.setMinimunRequirements(true);

        // Crea una lista de documentos simulados
        ArrayList<DocumentEntity> mockDocuments = new ArrayList<>();
        mockDocuments.add(mockDocument1);
        mockDocuments.add(mockDocument2);

        // Configura el comportamiento del repositorio simulado
        when(documentRepository.findDocumentsByMinimunRequirements(true)).thenReturn(mockDocuments);

        // Llama al método que estás probando
        ArrayList<DocumentEntity> result = documentService.findDocumentByMinimunRequirements(true);

        // Verifica que el resultado no sea nulo y contenga los documentos esperados
        assertNotNull(result);
        assertEquals(2, result.size()); // Cambiado a 2
        assertEquals(mockDocument1.getTitle(), result.get(0).getTitle());
        assertEquals(mockDocument1.getMinimunRequirements(), result.get(0).getMinimunRequirements());
        assertEquals(mockDocument2.getTitle(), result.get(1).getTitle()); // Verifica el segundo documento
        assertEquals(mockDocument2.getMinimunRequirements(), result.get(1).getMinimunRequirements()); // Verifica el segundo documento

        // Verifica que el método del repositorio fue llamado correctamente
        verify(documentRepository, times(1)).findDocumentsByMinimunRequirements(true); // Cambiado a 1
    }

}
