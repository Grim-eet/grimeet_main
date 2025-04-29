package com.grimeet.grimeet.domain.upload.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.grimeet.grimeet.common.exception.ExceptionStatus;
import com.grimeet.grimeet.common.exception.GrimeetException;
import com.grimeet.grimeet.common.image.HeicToJpegConverter;
import com.grimeet.grimeet.common.image.WebpImageConverter;
import com.grimeet.grimeet.domain.upload.dto.ImageUploadResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3ImageServiceImpl implements S3ImageService {

    private final AmazonS3 amazonS3;
    private final WebpImageConverter webpImageConverter;
    private final HeicToJpegConverter heicToJpegConverter;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;

    private static final List<String> ALLOWED_EXTENSIONS = List.of("jpg", "jpeg", "png", "webp", "heic");
    private static final List<String> ALLOWED_CONTENT_TYPES = List.of(
            "image/jpeg", "image/png", "image/heic", "image/webp"
    );

    /**
     * AWS S3에 이미지 업로드 실행 로직
     * @param image
     * @return ImageUploadResult(uploadedUrl, s3FileName)
     */
    @Override
    public ImageUploadResult upload(MultipartFile image) {
        if (Objects.isNull(image) || image.isEmpty() || Objects.isNull(image.getOriginalFilename())) {
            throw new GrimeetException(ExceptionStatus.INVALID_FILE);
        }

        this.validateImageFileExtension(image.getOriginalFilename());
        this.validateContentType(image.getContentType());

        try {
            return this.uploadImageToS3(image);
        } catch (Exception e) {
            log.error("[S3ImageService] 프로필 이미지 업로드 실패 - message: {}, cause: {}", e.getMessage(), e.getCause(), e);
            throw new GrimeetException(ExceptionStatus.S3_UPLOAD_FAIL);
        }
    }

    /**
     * S3 bucket에 업로드된 이미지를 조회 후 삭제
     * imageKey가 없다면 현재 기본 프로필이므로 삭제 생략
     * @param imageKey
     */
    @Override
    public void deleteImageFromS3(String imageKey) {
        if (imageKey.isBlank()) {
            log.info("기본 프로필 이미지여서 삭제 생략 - URL: {}", imageKey);
            return;
        }

        try {
            amazonS3.deleteObject(new DeleteObjectRequest(bucketName, imageKey));
            log.info("이미지 삭제 완료 - key: {}", imageKey);
        } catch (Exception e){
            log.error("이미지 삭제 실패 - key: {}, 예외: {}", imageKey, e.getMessage(), e);
            throw new GrimeetException(ExceptionStatus.S3_DELETE_FAIL);
        }
    }

    /**
     * 타임스탬프 + 랜덤값 + 기존 파일명으로 파일명 변경 후 이미지 업로드 시도
     * 이미지 확장자를 webp로 전환
     * heic 확장자는 jpeg로 임시 저장, webp로 변환
     * jpeg, jpg, png 확장자는 webp로 변환
     * @param image
     * @return
     * @throws IOException
     */
    private ImageUploadResult uploadImageToS3(MultipartFile image) throws IOException, InterruptedException {
        String originalFileName = image.getOriginalFilename();
        String extension = getExtension(originalFileName);

        try (ByteArrayInputStream webpInputStream =
                     extension.equals("heic")
                     ? convertHeicToWebp(image)
                     : webpImageConverter.convertToWebp(image)) {

            // 고유한 S3 파일 이름 생성 : 타임 스탬프 + 확장자 webp
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String randomSuffix = UUID.randomUUID().toString().substring(0, 5);
            String s3FileName = "profile/" + timestamp + "_" + randomSuffix + ".webp";

            // 확장자 변환
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image/webp");
            metadata.setContentLength(webpInputStream.available());

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, s3FileName, webpInputStream, metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead);

            amazonS3.putObject(putObjectRequest);

            String uploadedUrl = amazonS3.getUrl(bucketName, s3FileName).toString();

            log.info("[S3ImageService] 이미지 업로드 완료 - 파일명: {}, URL: {}",s3FileName, uploadedUrl);

            return new ImageUploadResult(uploadedUrl, s3FileName);
        }
    }

    // 확장자 검증
    private void validateImageFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex == -1) {
            log.error("파일명에 확장자가 없음: {}", fileName);
            throw new GrimeetException(ExceptionStatus.INVALID_FILE);
        }

        String extension = fileName.substring(lastDotIndex + 1).toLowerCase();

        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            log.error("허용되지 않은 확장자: {}", extension);
            throw new GrimeetException(ExceptionStatus.INVALID_FILE);
        }
    }

    // contentType 검증
    private void validateContentType(String contentType) {
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            log.error("허용되지 않은 contentType: {}", contentType);
            throw new GrimeetException(ExceptionStatus.INVALID_FILE);
        }
    }

    private String getExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex == -1) {
            log.error("[S3ImageService] 파일명에 확장자가 없음: {}", fileName);
            throw new GrimeetException(ExceptionStatus.INVALID_FILE);
        }
        return fileName.substring(lastDotIndex + 1).toLowerCase();
    }

    private ByteArrayInputStream convertHeicToWebp(MultipartFile heicImage) throws IOException, InterruptedException {
        File jpegFile = null;
        try {
            jpegFile = heicToJpegConverter.convertToJpeg(heicImage);
            return webpImageConverter.convertToWebp(new FileInputStream(jpegFile));
        } finally {
            if (jpegFile != null && jpegFile.exists()) {
                boolean isDeleted = jpegFile.delete();
                if (!isDeleted) {
                    log.warn("[S3ImageService] JPEG 임시 파일 삭제 실패: {}", jpegFile.getAbsolutePath());
                }

            }
        }
    }

}
