package hu.nye.home.service.Interfaces;

import org.springframework.web.multipart.MultipartFile;

public interface ImageServiceInterFace {
    
    String uploadImageToFileSystem(MultipartFile file);
    byte[] downloadImageFromFileSystem(String imageName);
}
