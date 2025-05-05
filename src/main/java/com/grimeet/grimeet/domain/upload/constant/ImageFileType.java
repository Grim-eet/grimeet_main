package com.grimeet.grimeet.domain.upload.constant;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ImageFileType {
    JPG("jpg", "image/jpg"),
    JPEG("jpeg", "image/jpeg"),
    PNG("png", "image/png"),
    WEBP("webp", "image/webp"),
    HEIC("heic", "image/heic"),
    HEIF("heif", "image/heif");

    private final String extension;
    private final String contentType;

    ImageFileType(String extension, String contentType) {
        this.extension = extension;
        this.contentType = contentType;
    }

    public static boolean isAllowedExtension(String extension) {
        return Arrays.stream(values())
                .anyMatch(type -> type.extension.equalsIgnoreCase(extension));
    }

    public static boolean isAllowedContentType(String inputType) {
        return Arrays.stream(values())
                .anyMatch(type -> type.contentType.equalsIgnoreCase(inputType));
    }
}
