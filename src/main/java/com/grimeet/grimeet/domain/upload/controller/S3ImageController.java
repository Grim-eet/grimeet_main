package com.grimeet.grimeet.domain.upload.controller;

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
@Tag(name = "S3 Image", description = "이미지(프로필/게시물) 업로드 및 삭제 API")
public class S3ImageController {

    private final S3ImageService s3ImageService;
    private static final String DEFAULT_IMAGE_PREFIX = "https://api.dicebear.com";

    @Operation(summary = "프로필 이미지 업로드", description = "MultipartFile로 이미지를 S3에 업로드하고, public URL을 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이미지 업로드 성공"),
            @ApiResponse(responseCode = "400", description = "파일이 없거나 유효하지 않은 확장자"),
            @ApiResponse(responseCode = "500", description = "S3 업로드 실패")
    })
    @PostMapping("/profile-image")
    public ResponseEntity<String> uploadProfileImage(@RequestPart("image") MultipartFile image) {
        String uploadedUrl = s3ImageService.upload(image);
        return ResponseEntity.ok(uploadedUrl);
    }

    @Operation(summary = "프로필 이미지 삭제",
            description = "기본 이미지(Dicebear)인지 확인 후, 기본 이미지가 아니라면 S3에서 파일 삭제를 수행합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이미지 삭제 성공 또는 기본 이미지라 삭제 생략"),
            @ApiResponse(responseCode = "400", description = "삭제 대상 URL이 비정상"),
            @ApiResponse(responseCode = "500", description = "S3 삭제 실패")
    })
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
     * 필요할 때 활성화
     */
//    @PostMapping("/images")
//    public ResponseEntity<String> uploadPostImage(@RequestPart("image") MultipartFile image) {
//        String url = s3ImageService.upload(image);
//        return ResponseEntity.ok(url);
//    }

}
