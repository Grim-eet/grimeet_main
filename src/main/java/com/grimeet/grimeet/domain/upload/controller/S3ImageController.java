package com.grimeet.grimeet.domain.upload.controller;

import com.grimeet.grimeet.domain.upload.service.S3ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
public class S3ImageController {

    private final S3ImageService s3ImageService;
    private static final String DEFAULT_IMAGE_PREFIX = "https://api.dicebear.com";

    @PostMapping("/profile-image")
    public ResponseEntity<String> uploadProfileImage(@RequestPart("image") MultipartFile image) {
        String uploadedUrl = s3ImageService.upload(image);
        return ResponseEntity.ok(uploadedUrl);
    }
}
