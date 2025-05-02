package com.grimeet.grimeet.domain.upload.controller;

import com.grimeet.grimeet.domain.upload.dto.ImageUploadResult;
import com.grimeet.grimeet.domain.upload.service.S3ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
public class S3ImageController {

    private final S3ImageService s3ImageService;
    private static final String DEFAULT_IMAGE_PREFIX = "https://api.dicebear.com";

    @PostMapping("/profile-image")
    public ResponseEntity<String> uploadProfileImage(@RequestPart("image") MultipartFile image) {
        s3ImageService.upload(image);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/profile-image")
    public ResponseEntity<Void> deleteProfileImage(@RequestParam("url") String imageUrl) {
        s3ImageService.deleteImageFromS3(imageUrl);
        return ResponseEntity.ok().build();
    }

    /**
     * 기본 이미지 여부 확인
     */
    private boolean isDefaultProfileImage(String imageUrl) {
        return imageUrl != null && imageUrl.startsWith(DEFAULT_IMAGE_PREFIX);
    }

    /**
     * 일반 이미지 업로드용
     * 필요할 때 활성화
     */
//    @PostMapping("/images")
//    public ResponseEntity<String> uploadPostImage(@RequestPart("image") MultipartFile image) {
//        String url = s3ImageService.upload(image);
//        return ResponseEntity.ok(url);
//    }

}
