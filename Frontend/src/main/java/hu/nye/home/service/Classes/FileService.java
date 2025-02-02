package hu.nye.home.service.Classes;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileService {
    
    private static final String IMAGES_FOLDER = "C:/apache-tomcat-9.0.39/webapps/ROOT/images/";
    
    public String saveFileToFileSystem(MultipartFile file) {
        // Ellenőrizzük az eredeti fájlnevet
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        
        // Ha az eredeti fájlnév tartalmaz kiterjesztést
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        } else {
            // Ha nincs kiterjesztés, MIME típus alapján próbáljuk meghatározni
            String mimeType = file.getContentType();
            if (mimeType != null) {
                switch (mimeType) {
                    case "image/jpeg":
                        extension = ".jpg";
                        break;
                    case "image/png":
                        extension = ".png";
                        break;
                    case "image/gif":
                        extension = ".gif";
                        break;
                    default:
                        throw new RuntimeException("Nem támogatott fájltípus: " + mimeType);
                }
            }
        }
        
        // Az új fájlnév az eredeti fájlnév és a kiterjesztés kombinációja
        String newFilename = UUID.randomUUID().toString() + extension; // Használj UUID-t, ha egyedi neveket szeretnél
        String filePath = IMAGES_FOLDER + newFilename;
        
        File directory = new File(IMAGES_FOLDER);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        
        try {
            file.transferTo(new File(filePath));
            return newFilename;
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
