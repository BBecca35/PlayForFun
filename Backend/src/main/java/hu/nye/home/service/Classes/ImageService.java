package hu.nye.home.service.Classes;

import hu.nye.home.entity.ImageModel;
import hu.nye.home.repository.ImageRepository;
import hu.nye.home.service.Interfaces.ImageServiceInterFace;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class ImageService implements ImageServiceInterFace {
    
    private final ImageRepository imageRepository;
    private static final String IMAGES_FOLDER = "./src/main/resources/images/";
    
    @Autowired
    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }
    
    
    @Override
    public String uploadImageToFileSystem(MultipartFile file) {
        String imagePath = IMAGES_FOLDER + file.getOriginalFilename();
        try {
            file.transferTo(new File(imagePath));
            ImageModel image = imageRepository.save(ImageModel.builder()
                                                      .name(file.getOriginalFilename())
                                                      .type(file.getContentType())
                                                      .path(imagePath)
                                                      .build());
            
            return "File Uploaded successfully: " + image.getPath();
        } catch (IOException e) {
            throw new RuntimeException("Error while saving the file", e);
        }
    }
    
    @Override
    public byte[] downloadImageFromFileSystem(String imageName) {
        Optional<ImageModel> imageData = imageRepository.findByName(imageName);
        if (imageData.isEmpty()) {
            throw new RuntimeException("Image not found with name: " + imageName);
        }
        String imagePath = imageData.get().getPath();
        try{
            return Files.readAllBytes(new File(imagePath).toPath());
        } catch (IOException e) {
            throw new RuntimeException("Error while reading the file", e);
        }
    }
    
    @Override
    public void deleteImage(Long id) {
        ImageModel image = imageRepository.findById(id)
                             .orElseThrow(() -> new EntityNotFoundException("Image not found"));
        
        try {
            Files.deleteIfExists(Paths.get(image.getPath()));
        } catch (IOException e) {
            throw new RuntimeException("Error while deleting the file", e);
        }
        
        imageRepository.delete(image);
    }
    
    @Override
    public ImageModel replaceImage(Long existingImageId, MultipartFile newImageFile) {
        deleteImage(existingImageId);
        return uploadImageAndReturnEntity(newImageFile);
    }
    
    @Override
    public ImageModel uploadImageAndReturnEntity(MultipartFile file) {
        String imagePath = IMAGES_FOLDER + file.getOriginalFilename();
        
        try {
            file.transferTo(new File(imagePath));
            return imageRepository.save(ImageModel.builder()
                                          .name(file.getOriginalFilename())
                                          .type(file.getContentType())
                                          .path(imagePath)
                                          .build());
        } catch (IOException e) {
            throw new RuntimeException("Error while saving the file", e);
        }
    }
}
