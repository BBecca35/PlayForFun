package hu.nye.home.service.Classes;

import hu.nye.home.entity.ImageModel;
import hu.nye.home.repository.ImageRepository;
import hu.nye.home.service.Interfaces.ImageServiceInterFace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

@Service
public class ImageService implements ImageServiceInterFace {
    
    private final ImageRepository imageRepository;
    
    private final String IMAGES_FOLDER = "./src/main/resources/images/";
    
    @Autowired
    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }
    
    
    @Override
    public String uploadImageToFileSystem(MultipartFile file) {
        String imagePath = IMAGES_FOLDER + file.getOriginalFilename();
        ImageModel image = imageRepository.save(ImageModel.builder()
                                                  .name(file.getOriginalFilename())
                                                  .type(file.getContentType())
                                                  .path(imagePath).build());
        try{
            file.transferTo(new File(imagePath));
            return "File Uploaded successfully: " + imagePath;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public byte[] downloadImageFromFileSystem(String imageName) {
        Optional<ImageModel> imageData = imageRepository.findByName(imageName);
        String imagePath = imageData.get().getPath();
        try{
            return Files.readAllBytes(new File(imagePath).toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
