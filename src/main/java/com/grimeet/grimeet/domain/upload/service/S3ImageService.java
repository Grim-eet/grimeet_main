package com.grimeet.grimeet.domain.upload.service;

import com.grimeet.grimeet.domain.upload.dto.ImageUploadResult;
import org.springframework.web.multipart.MultipartFile;

public interface S3ImageService {

    ImageUploadResult upload(MultipartFile image);

    void deleteImageFromS3(String imageAddress);

}
