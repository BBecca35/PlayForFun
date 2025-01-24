package hu.nye.home.service.Classes;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class FileService {
    
    private static final String IMAGES_FOLDER = "C:/apache-tomcat-9.0.39/webapps/ROOT/images/";
    
    public String saveFileToFileSystem(MultipartFile file) {
        String filePath = IMAGES_FOLDER + file.getOriginalFilename();
        
        File directory = new File(IMAGES_FOLDER);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        
        try {
            file.transferTo(new File(filePath));
            return filePath;
        } catch (IOException e) {
            throw new RuntimeException("Error while saving the file", e);
        }
    }
    
    public byte[] readFileFromFileSystem(String filePath) {
        try {
            return Files.readAllBytes(Paths.get(filePath));
        } catch (IOException e) {
            throw new RuntimeException("Error while reading the file", e);
        }
    }
    
    public void deleteFileFromFileSystem(String filePath) {
        try {
            Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) {
            throw new RuntimeException("Error while deleting the file", e);
        }
    }
}
