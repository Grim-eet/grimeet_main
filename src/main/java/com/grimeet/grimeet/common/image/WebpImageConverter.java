package com.grimeet.grimeet.common.image;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.io.*;

@Slf4j
@Component
public class WebpImageConverter {

    private static final String TEMP_DIR = System.getProperty("java.io.tmpdir");

    static {
        ImageIO.scanForPlugins();
    }

    public ByteArrayInputStream convertToWebp(InputStream inputStream) throws IOException, InterruptedException {
        File tempInputFile = File.createTempFile("temp_", ".img", new File(TEMP_DIR));
        try (OutputStream out = new FileOutputStream(tempInputFile)) {
            inputStream.transferTo(out);
        }

        try {
            return convertToWebp(tempInputFile);
        } finally {
            if (tempInputFile.exists() && !tempInputFile.delete()) {
                log.warn("[WebpImageConverter] 임시 입력 파일 삭제 실패: {}", tempInputFile.getAbsolutePath());
            }
        }
    }


    public ByteArrayInputStream convertToWebp(File inputFile) throws IOException, InterruptedException {
        if (inputFile == null || !inputFile.exists()) {
            log.warn("[WebpImageConverter] 입력 파일이 없습니다.");
            throw new IllegalArgumentException();
        }

        String outputName = inputFile.getName().replaceFirst("\\.[^.]+$", "") + ".webp";
        File outputWebpFile = new File(TEMP_DIR, outputName);

        ProcessBuilder pb = new ProcessBuilder(
                "cwebp",
                "-q", "80",    // (품질 조정 옵션) 80% 퀄리티
                inputFile.getAbsolutePath(),
                "-o",
                outputWebpFile.getAbsolutePath()
        );

        Process process = pb.start();
        int exitCode = process.waitFor();

        if (exitCode != 0) {
            log.error("[WebpImageConverter] cwebp 변환 실패, code: {}", exitCode);
            throw new IOException("[WebpImageConverter] WebP 변환 실패");
        }

        byte[] webBytes = new FileInputStream(outputWebpFile).readAllBytes();
        if (outputWebpFile.exists()) outputWebpFile.delete();

        return new ByteArrayInputStream(webBytes);
    }


}
