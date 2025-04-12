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
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
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

        this.validateImageFileExtention(image.getOriginalFilename());

        try {
            return this.uploadImageToS3(image);
        } catch (IOException e) {
            log.error("사용자 프로필 이미지 업로드 중 IOException 발생", e);
            throw new GrimeetException(ExceptionStatus.S3_UPLOAD_FAIL);
        }

    }

    private void validateImageFileExtention(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex == -1) {
            throw new GrimeetException(ExceptionStatus.INVALID_FILE);
        }

        String extention = fileName.substring(lastDotIndex + 1).toLowerCase();
        List<String> allowedExtentionList = Arrays.asList("jpg", "jpeg", "png", "gif");

        if (!allowedExtentionList.contains(extention)) {
            throw new GrimeetException(ExceptionStatus.INVALID_FILE);
        }
    }

    private String uploadImageToS3(MultipartFile image) throws IOException{
        String originalFilename = image.getOriginalFilename(); //원본 파일 명

        // 고유한 S3 파일 이름 생성
        String s3FileName = UUID.randomUUID().toString().substring(0, 10) + "_" + originalFilename;

        try (
            InputStream inputStream = image.getInputStream();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(IOUtils.toByteArray(inputStream))
        ) {
            ObjectMetadata metadata = new ObjectMetadata();

            // Content-Type 설정
            String contentType = image.getContentType();
            if (contentType != null) {
                metadata.setContentType(contentType);
            }

            metadata.setContentLength(byteArrayInputStream.available());

            // S3에 업로드
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, s3FileName, byteArrayInputStream, metadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead);
            amazonS3.putObject(putObjectRequest);

            // S3 URL 변환
            return amazonS3.getUrl(bucketName, s3FileName).toString();

        } catch (IOException e){
            log.error("사용자 프로필 이미지 업로드 중 IOException 발생", e);
            throw new GrimeetException(ExceptionStatus.S3_UPLOAD_FAIL);
        }

    }

    @Override
    public void deleteImageFromS3(String imageAddress) {
        String key = getKeyFromImageAddress(imageAddress);
        try {
            amazonS3.deleteObject(new DeleteObjectRequest(bucketName, key));
        } catch (Exception e){
            throw new GrimeetException(ExceptionStatus.S3_DELETE_FAIL);
        }
    }

    private String getKeyFromImageAddress(String imageAddress){
        try {
            URL url = new URL(imageAddress);
            String decodingKey = URLDecoder.decode(url.getPath(), "UTF-8");
            return decodingKey.substring(1); // 맨 앞의 '/' 제거

        } catch (MalformedURLException | UnsupportedEncodingException e){
            throw new GrimeetException(ExceptionStatus.INVALID_FILE);
        }
    }

}
