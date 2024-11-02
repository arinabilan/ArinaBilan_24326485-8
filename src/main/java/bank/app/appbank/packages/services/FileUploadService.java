package bank.app.appbank.packages.services;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.util.UUID;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileUploadService {

    private final Path fileStorageLocation = Paths.get(Paths.get("").toAbsolutePath().toString() + "/uploads/");

    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        String uploadDir = "/uploads/";

        // Crear la carpeta uploads si no existe
        Path uploadsPath = Paths.get(uploadDir);
        if (!Files.exists(uploadsPath)) {
            Files.createDirectories(uploadsPath);
        }

        Path fullPath = uploadsPath.resolve(fileName);
        Files.copy(file.getInputStream(), fullPath);

        return fileName;
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            }
            else {
                throw new RuntimeException("File not found " + fileName);
            }
        }
        catch (MalformedURLException ex) {
            throw new RuntimeException("File not found " + fileName, ex);
        }
    }

    public String getContentType(String fileName) {
        try {
            Path filePath = fileStorageLocation.resolve(fileName).normalize();
            return Files.probeContentType(filePath);
        }
        catch (IOException e) {
            e.printStackTrace();
            return "application/octet-stream";
        }
    }

}
