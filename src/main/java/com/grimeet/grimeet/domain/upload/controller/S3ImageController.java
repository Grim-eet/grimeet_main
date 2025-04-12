package com.grimeet.grimeet.domain.upload.controller;

import com.grimeet.grimeet.domain.upload.service.S3ImageService;
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

    /**
     * [POST] 사용자 프로필 이미지 업로드
     * - MultipartFile로 받은 이미지를 S3에 업로드
     * - 업로드된 이미지의 public URL을 반환
     */
    @PostMapping("/profile-image")
    public ResponseEntity<String> uploadProfileImage(@RequestPart("image") MultipartFile image) {
        String uploadedUrl = s3ImageService.upload(image);
        return ResponseEntity.ok(uploadedUrl);
    }

    /**
     * [DELETE] 사용자 프로필 이미지 삭제
     * - 기본 이미지(Dicebear)는 삭제하지 않음
     * - S3ImageService를 통해 삭제 요청 처리
     */
    @DeleteMapping("/profile-image")
    public ResponseEntity<Void> deleteProfileImage(@RequestParam("url") String imageUrl) {
        if (!isDefaultProfileImage(imageUrl)) {
            s3ImageService.deleteImageFromS3(imageUrl);
            log.info("사용자 이미지 삭제 완료: {}", imageUrl);
        } else {
            log.info("기본 이미지 삭제 생략: {}", imageUrl);
        }
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
     */
//    @PostMapping("/images")
//    public ResponseEntity<String> uploadPostImage(@RequestPart("image") MultipartFile image) {
//        String url = s3ImageService.upload(image);
//        return ResponseEntity.ok(url);
//    }

}
