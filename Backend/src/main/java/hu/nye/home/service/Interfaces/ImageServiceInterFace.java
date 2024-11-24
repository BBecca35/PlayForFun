package hu.nye.home.service.Interfaces;

import hu.nye.home.entity.ImageModel;
import org.springframework.web.multipart.MultipartFile;

public interface ImageServiceInterFace {
    
    String uploadImageToFileSystem(MultipartFile file);
    byte[] downloadImageFromFileSystem(String imageName);
    void deleteImage(Long id);
    ImageModel replaceImage(Long existingImageId, MultipartFile newImageFile);
    ImageModel uploadImageAndReturnEntity(MultipartFile file);
}
