package com.grimeet.grimeet.domain.user.controller;


import com.grimeet.grimeet.domain.user.dto.*;
import com.grimeet.grimeet.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserCreateRequestDto userCreateRequestDto ) {
        UserResponseDto responseDto = userService.createUser(userCreateRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 유저 상태 수정 : 탈퇴
    @PatchMapping("/update/userStatus/withdrawal")
    public ResponseEntity<UserResponseDto> updateUserStatusWithdrawal(@Valid @RequestBody UserUpdateStatusRequestDto requestDto) {
        userService.updateUserStatusWithdrawal(requestDto.getEmail());
        return ResponseEntity.ok().build();
    }

    // 유저 상태 수정 : 휴면
    @PatchMapping("/update/userStatus/dormant")
    public ResponseEntity<UserResponseDto> updateUserStatusDormant(@Valid @RequestBody UserUpdateStatusRequestDto requestDto) {
        userService.updateUserStatusDormant(requestDto.getEmail());
        return ResponseEntity.ok().build();
    }

    // 유저 상태 수정 : 일반
    @PatchMapping("/update/userStatus/normal")
    public ResponseEntity<UserResponseDto> updateUserStatusNormal(@Valid @RequestBody UserUpdateStatusRequestDto requestDto) {
        userService.updateUserStatusNormal(requestDto.getEmail());
        return ResponseEntity.ok().build();
    }

    // 유저 정보 수정: 비밀번호, 닉네임, 전화번호
    @PatchMapping("/update/password")
    public ResponseEntity<UserResponseDto> updateUserPassword(@Valid @RequestBody UserUpdatePasswordRequestDto requestDto) {
        UserResponseDto responseDto = userService.updateUserPassword(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PatchMapping("/update/nickname")
    public ResponseEntity<Void> updateNickname(@Valid @RequestBody UserUpdateNicknameRequestDto requestDto) {
        userService.updateUserNickname(requestDto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/update/phoneNumber")
    public void updatePhoneNumber(@Valid @RequestBody UserUpdatePhoneNumberRequestDto requestDto) {
        userService.updateUserPhoneNumber(requestDto);
    }
}
