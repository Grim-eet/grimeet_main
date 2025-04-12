package com.grimeet.grimeet.domain.upload.service;

import com.amazonaws.services.s3.AmazonS3;
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
        return "";
    }

    @Override
    public String uploadImage(MultipartFile image) {
        return "";
    }

    @Override
    public void validateImageFileExtention(String fileName) {

    }

    @Override
    public String uploadImageToS3(MultipartFile image) {
        return "";
    }

    @Override
    public void deleteImageFromS3(String imageAddress) {

    }
}
