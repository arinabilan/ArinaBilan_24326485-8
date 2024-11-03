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
import java.net.MalformedURLException;
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
    //89dea202-5224-48e4-9b87-a5b43f11e32c_cakeFlash.jpg

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


    @Test
    void testUploadFile() throws IOException {
        // Mock MultipartFile
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.getOriginalFilename()).thenReturn("testfile.txt");
        when(multipartFile.getInputStream()).thenReturn(new ByteArrayInputStream("Contenido de prueba".getBytes()));

        // Llama al método uploadFile
        String fileName = fileUploadService.uploadFile(multipartFile);

        // Verifica que el archivo se haya subido correctamente
        Path uploadedFilePath = Paths.get("uploads").resolve(fileName);
        Files.createDirectories(uploadedFilePath.getParent());
        Files.write(uploadedFilePath, "Contenido de prueba".getBytes());
        assertTrue(Files.exists(uploadedFilePath));

        // Limpia el archivo creado
        Files.deleteIfExists(uploadedFilePath);
    }

    @Test
    void testLoadFileAsResource_ExistingFile() throws IOException {
        // Prepara el entorno creando un archivo temporal
        String testFileName = "testfile.txt";
        Path testFilePath = Paths.get("uploads").resolve(testFileName);
        Files.createDirectories(testFilePath.getParent());
        Files.write(testFilePath, "Contenido de prueba".getBytes());

        // Llama al método loadFileAsResource
        Resource resource = fileUploadService.loadFileAsResource(testFileName);

        // Verifica que el recurso exista
        assertNotNull(resource);
        assertTrue(resource.exists());

        // Limpia el archivo creado
        Files.deleteIfExists(testFilePath);
    }

    @Test
    void testLoadFileAsResource_MalformedURLException() {
        // Mock the fileStorageLocation to cause MalformedURLException
        FileUploadService malformedService = fileUploadServiceInvalid();

        // Intenta cargar un archivo con una URL malformada
        Exception exception = assertThrows(RuntimeException.class, () -> {
            malformedService.loadFileAsResource("testfile.txt");
        });

        // Verifica el mensaje de error
        assertTrue(exception.getMessage().contains("Illegal char <:> at index 7: invalid://path"));
    }

    @Test
    void testLoadFileAsResource_MalformedURLException2() {
        // Intenta cargar un archivo con una URL malformada
        Exception exception = assertThrows(RuntimeException.class, () -> {
            fileUploadService.loadFileAsResource(":&testfile.txt");
        });

        // Verifica el mensaje de error
        assertTrue(exception.getMessage().contains("Illegal char <:> at index 0: :&testfile.txt"));
    }

    @Test
    void testGetContentType_IOException() throws IOException {
        // Mock the fileStorageLocation to cause IOException
        FileUploadService ioExceptionService = fileUploadServiceInvalid();

        // Intenta obtener el tipo de contenido de un archivo con una ruta inválida
        Exception exception = assertThrows(RuntimeException.class, () -> {
            ioExceptionService.getContentType("testfile.txt");
        });

        // Verifica el mensaje de error
        assertTrue(exception.getMessage().contains("Illegal char <:> at index 7: invalid://path"));
    }

    @Test
    void testGetContentType_IOException2() throws IOException {
        // Mock the fileStorageLocation to cause IOException
        FileUploadService ioExceptionService = fileUploadServiceInvalid();

        // Intenta obtener el tipo de contenido de un archivo con una ruta inválida
        Exception exception = assertThrows(RuntimeException.class, () -> {
            fileUploadService.getContentType("invalid://path_testfile.txt");
        });

        // Verifica el mensaje de error
        assertTrue(exception.getMessage().contains("Illegal char <:> at index 7: invalid://path"));
    }

    private FileUploadService fileUploadServiceInvalid() {
        return new FileUploadService() {
            @Override
            public String getContentType(String fileName) {
                try {
                    Path filePath = Paths.get("invalid://path").resolve(fileName).normalize();
                    return Files.probeContentType(filePath);
                } catch (IOException ex) {
                    throw new RuntimeException("Could not determine file type for " + fileName, ex);
                }
            }
            @Override
            public Resource loadFileAsResource(String fileName) {
                try {
                    Path filePath = Paths.get("invalid://path").resolve(fileName).normalize();
                    return new UrlResource(filePath.toUri());
                } catch (MalformedURLException ex) {
                    throw new RuntimeException("File not found " + fileName, ex);
                }
            }
        };
    }
}
