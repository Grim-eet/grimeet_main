package com.grimeet.grimeet.common.util.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class TempPasswordGenerator {
    private static final SecureRandom random = new SecureRandom();

    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGIT = "0123456789";
    private static final String SPECIAL = "!@#$%^&*()-_=+";
    private static final String ALL = UPPER + LOWER + DIGIT + SPECIAL;


    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 20;

    public String generate() {
        int length = random.nextInt(MAX_LENGTH - MIN_LENGTH + 1) + MIN_LENGTH;

        List<Character> passwordChars = new ArrayList<>();

        // 필수 문자 1개씩 넣기
        passwordChars.add(pickRandomChar(UPPER));
        passwordChars.add(pickRandomChar(LOWER));
        passwordChars.add(pickRandomChar(DIGIT));
        passwordChars.add(pickRandomChar(SPECIAL));

        // 나머지 문자 채우기
        for (int i = 4; i < length; i++) {
            passwordChars.add(pickRandomChar(ALL));
        }

        // 순서 섞기 (안 섞으면 앞 4자리가 고정됨)
        Collections.shuffle(passwordChars, random);

        // 리스트를 String으로 변환
        StringBuilder password = new StringBuilder();
        for (char c : passwordChars) {
            password.append(c);
        }

        log.info("[TempPasswordGenerator] 임시 비밀번호 생성 완료");
        return password.toString();
    }

    private char pickRandomChar(String chars) {
        return chars.charAt(random.nextInt(chars.length()));
    }

}
