package com.example.users.controller;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;

/***
 * 静态资源可以直接访问，这里似乎没有必要
 */
@RestController
public class ImageController {

    @GetMapping("/image1/{imageName}")
    public ResponseEntity<byte[]> getImage(@PathVariable String imageName) throws IOException {
        Resource resource = new ClassPathResource("/image/" + imageName);
        byte[] imageBytes = Files.readAllBytes(resource.getFile().toPath());

        String fileExtension = FilenameUtils.getExtension(imageName); // Using FilenameUtils from Apache Commons IO

        MediaType mediaType;
        switch (fileExtension.toLowerCase()) {
            case "jpg":
            case "jpeg":
                mediaType = MediaType.IMAGE_JPEG;
                break;
            case "png":
                mediaType = MediaType.IMAGE_PNG;
                break;
            case "gif":
                mediaType = MediaType.IMAGE_GIF;
                break;
            // Add more cases for other image types if needed
            default:
                mediaType = MediaType.APPLICATION_OCTET_STREAM;
        }

        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(imageBytes);
    }
}

