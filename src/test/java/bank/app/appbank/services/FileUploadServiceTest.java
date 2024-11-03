package bank.app.appbank.services;
import bank.app.appbank.packages.services.FileUploadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FileUploadServiceTest {
    private FileUploadService fileUploadService;

    @BeforeEach
    void setUp() {
        fileUploadService = new FileUploadService();
    }

    @Test
    void testLoadFileAsResource_FileNotFound() {
        // Intenta cargar un archivo que no existe
        Exception exception = assertThrows(RuntimeException.class, () -> {
            fileUploadService.loadFileAsResource("nonexistentfile.txt");
        });

        // Verifica el mensaje de error
        assertEquals("File not found nonexistentfile.txt", exception.getMessage());
    }

    @Test
    void testGetContentType_ExistingFile() throws IOException {
        // Prepara el entorno creando un archivo temporal
        String testFileName = "testfile.txt";
        Path testFilePath = Paths.get("uploads").resolve(testFileName);
        Files.createDirectories(testFilePath.getParent());
        Files.write(testFilePath, "Contenido de prueba".getBytes());

        // Obtiene el tipo de contenido
        String contentType = fileUploadService.getContentType(testFileName);

        // Verifica que el tipo de contenido no sea null y corresponde a un archivo de texto
        assertNotNull(contentType);
        assertEquals("text/plain", contentType); // Dependiendo de la implementación, puede variar

        // Limpia el archivo creado
        Files.deleteIfExists(testFilePath);
    }

    
}
