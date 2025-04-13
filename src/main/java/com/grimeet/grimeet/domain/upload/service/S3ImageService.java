package com.grimeet.grimeet.domain.upload.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface S3ImageService {

    String upload(MultipartFile image);

    void deleteImageFromS3(String imageAddress);

    void deleteIfNotDefault(String imageUrl);

    String extractKeyFromUrl(String imageUrl);
}
