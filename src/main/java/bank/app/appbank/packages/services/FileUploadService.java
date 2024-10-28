package bank.app.appbank.packages.services;

import config.FileStorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Service
public class FileUploadService {

    @Autowired
    private FileStorageProperties fileStorageProperties;

    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        String uploadDir = fileStorageProperties.getUploadDir();

        // Crear la carpeta uploads si no existe
        Path uploadsPath = Paths.get(uploadDir);
        if (!Files.exists(uploadsPath)) {
            Files.createDirectories(uploadsPath);
        }

        Path fullPath = uploadsPath.resolve(fileName);
        Files.copy(file.getInputStream(), fullPath);

        return fileName;
    }
}
