package hu.nye.home.controller;

import hu.nye.home.dto.ImageDto;
import hu.nye.home.service.Classes.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping()
public class ImageController {
    private final ImageService imageService;
    
    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }
    
    @PostMapping
    public ResponseEntity<ImageDto> uploadImage(@RequestParam("file") MultipartFile file) {
        String uploadResponse = imageService.uploadImageToFileSystem(file);
        
        ImageDto imageDto = new ImageDto();
        imageDto.setName(file.getOriginalFilename());
        imageDto.setType(file.getContentType());
        imageDto.setPath(uploadResponse);
        
        return ResponseEntity.ok(imageDto);
    }
    
    @GetMapping("/{imageName}")
    public ResponseEntity<byte[]> downloadImage(@PathVariable String imageName) {
        byte[] imageData = imageService.downloadImageFromFileSystem(imageName);
        return ResponseEntity.ok()
                 .contentType(MediaType.IMAGE_JPEG)
                 .body(imageData);
    }
    
    @DeleteMapping("/{imageId}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long imageId) {
        imageService.deleteImage(imageId);
        return ResponseEntity.noContent().build();
    }

}
