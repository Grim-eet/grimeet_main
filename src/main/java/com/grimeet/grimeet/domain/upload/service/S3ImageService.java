package com.grimeet.grimeet.domain.upload.service;

import org.springframework.web.multipart.MultipartFile;

public interface S3ImageService {

    String upload(MultipartFile image);

    String uploadImage(MultipartFile image);

    void validateImageFileExtention(String fileName);

    String uploadImageToS3(MultipartFile image);

    void deleteImageFromS3(String imageAddress);

}
