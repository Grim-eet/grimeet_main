package com.grimeet.grimeet.domain.upload.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ImageUploadResult {

    private String url;
    private String key;

}
