package com.grimeet.grimeet.common.image;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
@RequiredArgsConstructor
public class WebpImageConverter {

    public ByteArrayInputStream convertToWebp(MultipartFile multipartFile) throws IOException {
        return convertToWebp(multipartFile.getInputStream());
    }

    public ByteArrayInputStream convertToWebp(InputStream inputStream) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(inputStream);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        boolean writeSuccess = ImageIO.write(bufferedImage, "webp", outputStream);
        if (!writeSuccess) {
            throw new IOException("[WebpImageConverter] Webp 변환 실패 - 지원하지 않은 포맷입니다.");
        }
        return new ByteArrayInputStream(outputStream.toByteArray());
    }
}
