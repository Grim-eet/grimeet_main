package com.grimeet.grimeet.domain.upload.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.grimeet.grimeet.common.exception.ExceptionStatus;
import com.grimeet.grimeet.common.exception.GrimeetException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3ImageServiceImpl implements S3ImageService {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;

    @Override
    public String upload(MultipartFile image) {
        if (image.isEmpty() || Objects.isNull(image.getOriginalFilename())) {
            throw new GrimeetException(ExceptionStatus.INVALID_FILE);
        }

        this.validateImageFileExtension(image.getOriginalFilename());
        this.validateContentType(image.getContentType());

        try {
            return this.uploadImageToS3(image);
        } catch (Exception e) {
            log.error("AWS PutObject exception - message: {}, cause: {}", e.getMessage(), e.getCause(), e);
            throw new GrimeetException(ExceptionStatus.S3_UPLOAD_FAIL);
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
        List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png", "gif", "webp");

        if (!allowedExtensions.contains(extension)) {
            log.error("허용되지 않은 확장자: {}", extension);
            throw new GrimeetException(ExceptionStatus.INVALID_FILE);
        }
    }

    // contentType 검증
    private void validateContentType(String contentType) {
        if (contentType == null || !contentType.startsWith("image/")) {
            log.error("허용되지 않은 contentType: {}", contentType);
            throw new GrimeetException(ExceptionStatus.INVALID_FILE);
        }
    }

    private String uploadImageToS3(MultipartFile image) throws IOException {
        String originalFilename = image.getOriginalFilename(); //원본 파일 명
        String contentType = image.getContentType();
        String extension = extractExtension(originalFilename);

        // 고유한 S3 파일 이름 생성
        // 타임 스탬프
        String prefix = "profile/" + extension + "/";
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String randomSuffix = UUID.randomUUID().toString().substring(0, 5);
        String s3FileName = prefix + timestamp + "_" + randomSuffix + "_" + originalFilename;

        log.info("이미지 업로드 시도 - 파일명: {}, 확장자: {}, contentType: {}, s3FileName: {}",
                originalFilename, extension, contentType, s3FileName);

        try (
                InputStream inputStream = image.getInputStream();
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(IOUtils.toByteArray(inputStream))
        ) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);
            metadata.setContentLength(byteArrayInputStream.available());

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, s3FileName, byteArrayInputStream, metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead);
            amazonS3.putObject(putObjectRequest);

            String uploadedUrl = amazonS3.getUrl(bucketName, s3FileName).toString();
            log.info("이미지 업로드 완료 - URL: {}", uploadedUrl);
            return uploadedUrl;

        } catch (IOException e) {
            log.error("이미지 파일 업로드 중 IOException 발생 - 파일명: {}, 예외: {}", originalFilename, e.getMessage(), e);
            throw e; // 상위에서 다시 처리
        }
    }

    // 확장자 추출
    private String extractExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex == -1) {
            return "";
        }
        return fileName.substring(lastDotIndex + 1).toLowerCase();
    }

    @Override
    public void deleteImageFromS3(String imageAddress) {
        String key = extractKeyFromUrl(imageAddress);
        try {
            amazonS3.deleteObject(new DeleteObjectRequest(bucketName, key));
            log.info("이미지 삭제 완료 - key: {}", key);
        } catch (Exception e){
            log.error("이미지 삭제 실패 - key: {}, 예외: {}", key, e.getMessage(), e);
            throw new GrimeetException(ExceptionStatus.S3_DELETE_FAIL);
        }
    }

    @Override
    public void deleteIfNotDefault(String imageUrl) {
        if (imageUrl != null && !isCustomProfileImage(imageUrl)) {
            deleteImageFromS3(imageUrl);
        } else {
            log.info("기본 프로필 이미지여서 삭제 생략: {}", imageUrl);
        }
    }

    private boolean isCustomProfileImage(String imageUrl) {
        return imageUrl != null && !imageUrl.startsWith("https://api.dicebear.com");
    }

    @Override
    public String extractKeyFromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            String decodedPath = URLDecoder.decode(url.getPath(), "UTF-8");
            return decodedPath.substring(1);
        } catch (Exception e) {
            log.error("S3 키 추출 실패 - URL: {}, 예외: {}", imageUrl, e.toString());
            throw new GrimeetException(ExceptionStatus.INVALID_FILE);
        }
    }

}
