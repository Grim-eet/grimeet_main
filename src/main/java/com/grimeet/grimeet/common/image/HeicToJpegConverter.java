package com.grimeet.grimeet.common.image;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Slf4j
@Component
public class HeicToJpegConverter {

    private static final String TEMP_DIR = System.getProperty("java.io.tmpdir");

    /**
     * magick으로 파일 확장자 변환
     * 변환 위해 파일 경로가 필요해 임시 저장
     * @param heicFile
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public File convertToJpeg(MultipartFile heicFile) throws IOException, InterruptedException {
        if (heicFile == null || heicFile.isEmpty()) {
            log.warn("[HeicToJpegConverter] heic 파일이 비었습니다.");
            throw new IllegalArgumentException();
        }

        // HEIC 파일을 임시 파일로 저장
        File tempHeicFile = File.createTempFile("temp_", ".heic", new File(TEMP_DIR));
        File tempJpegFile = new File(TEMP_DIR, tempHeicFile.getName().replace(".heic", ".jpeg"));

        try {
            heicFile.transferTo(tempHeicFile);

            // ImageMagick 실행
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "magick",
                    tempHeicFile.getAbsolutePath(),
                    tempJpegFile.getAbsolutePath()
            );

            Process process = processBuilder.start();
            int exitCode = process.waitFor();

            if (exitCode != 0) {
                throw new IOException("변환 실패");
            }

            return tempJpegFile;
        } catch (IOException | InterruptedException e ) {
            log.warn("[HeicToJpegConverter] heic 파일 변환 실패: {}", tempHeicFile.getAbsolutePath());
            throw e;
        }
        finally {
            if (tempHeicFile.exists()) tempHeicFile.delete();
        }
    }

}
