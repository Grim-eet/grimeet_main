package com.grimeet.grimeet.domain.user.controller;


import com.grimeet.grimeet.domain.user.dto.*;
import com.grimeet.grimeet.domain.user.entity.User;
import com.grimeet.grimeet.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name="User", description = "사용자 생성, 사용자 정보 관련 API")
public class UserController {

    private final UserService userService;

    @Operation(summary = "탈퇴 회원으로 전환", description = "사용자 상태를 'WITHDRAWAL'로 변경합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상태 변경 완료", content = @Content),
            @ApiResponse(responseCode = "404", description = "일치하는 유저정보를 찾을 수 없습니다.", content = @Content)
    })
    @PatchMapping("/update/userStatus/withdrawal")
    public ResponseEntity<UserResponseDto> updateUserStatusWithdrawal(@Valid @RequestBody UserUpdateStatusRequestDto requestDto) {
        userService.updateUserStatusWithdrawal(requestDto.getEmail());
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "휴면 회원으로 전환", description = "사용자 상태를 'DORMANT'로 변경합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상태 변경 완료", content = @Content),
            @ApiResponse(responseCode = "404", description = "일치하는 유저정보를 찾을 수 없습니다.", content = @Content)
    })
    @PatchMapping("/update/userStatus/dormant")
    public ResponseEntity<UserResponseDto> updateUserStatusDormant(@Valid @RequestBody UserUpdateStatusRequestDto requestDto) {
        userService.updateUserStatusDormant(requestDto.getEmail());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "일반 회원으로 전환", description = "사용자 상태를 'NORMAL'로 변경합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상태 변경 완료", content = @Content),
            @ApiResponse(responseCode = "404", description = "일치하는 유저정보를 찾을 수 없습니다.", content = @Content)
    })
    @PatchMapping("/update/userStatus/normal")
    public ResponseEntity<UserResponseDto> updateUserStatusNormal(@Valid @RequestBody UserUpdateStatusRequestDto requestDto) {
        userService.updateUserStatusNormal(requestDto.getEmail());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "사용자 정보 변경", description = "닉네임 혹은 전화번호를 변경합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자 정보 변경 완료", content = @Content),
            @ApiResponse(responseCode = "400", description = "이미 존재하는 사용자 정보입니다.", content = @Content),
            @ApiResponse(responseCode = "404", description = "일치하는 유저정보를 찾을 수 없습니다.", content = @Content)
    })
    @PatchMapping("/update")
    public ResponseEntity<UserResponseDto> updateUserInfo(@Valid @RequestBody UserUpdateRequestDto requestDto) {
        UserResponseDto responseDto = userService.updateUserInfo(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "비밀번호 변경", description = "비밀번호를 새로운 값으로 변경합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "비밀번호 변경 완료", content = @Content),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 비밀번호입니다.", content = @Content),
            @ApiResponse(responseCode = "404", description = "일치하는 유저정보를 찾을 수 없습니다.", content = @Content)

    })
    @PatchMapping("/update/password")
    public ResponseEntity<UserResponseDto> updateUserPassword(@Valid @RequestBody UserUpdatePasswordRequestDto requestDto) {
        UserResponseDto responseDto = userService.updateUserPassword(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "프로필 이미지 수정", description = "기존 이미지를 삭제하고 새 이미지를 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "프로필 이미지 변경 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 형식 또는 이미지 파일"),
            @ApiResponse(responseCode = "404", description = "해당 유저를 찾을 수 없음")
    })
    @PatchMapping("/update/profile-image")
    public ResponseEntity<UserResponseDto> updateUserProfileImage(@Valid @ModelAttribute UserUpdateProfileImageRequestDto requestDto) {
        UserResponseDto responseDto = userService.updateUserProfileImage(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "프로필 이미지 수정", description = "기존 이미지를 삭제하고 새 이미지를 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "프로필 이미지 변경 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 형식 또는 이미지 파일"),
            @ApiResponse(responseCode = "404", description = "해당 유저를 찾을 수 없음")
    })
    @DeleteMapping("/update/profile-image")
    public ResponseEntity<UserResponseDto> deleteUserProfileImage(@Valid @RequestBody UserDeleteProfileImageRequestDto requestDto) {
        UserResponseDto responseDto = userService.deleteUserProfileImage(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "사용자 계정(이메일) 찾기", description = "기존 사용자의 계정(이메일)을 이름, 전화번호로 찾습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자 계정 찾기 성공"),
            @ApiResponse(responseCode = "404", description = "해당 유저를 찾을 수 없음")
    })
    @GetMapping("/find/email")
    public ResponseEntity<String> findUserEmail(@Valid @RequestBody UserFindEmailRequestDto requestDto) {
        String email = userService.findUserEmailByNameAndPhoneNumber(requestDto);
        return ResponseEntity.ok(email);
    }

    @Operation(summary = "사용자 비밀번호 찾기", description = "기존 사용자의 비밀번호를 임시 값으로 설정하고, 이메일로 발급한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자 이메일로 임시 비밀번호 발급 완료 "),
            @ApiResponse(responseCode = "404", description = "해당 유저를 찾을 수 없음")
    })
    @PostMapping("/find/password")
    public ResponseEntity<Void> findUserPasswordByEmail(@Valid @RequestBody UserFindPasswordRequestDto requestDto) {
        userService.findUserPasswordByEmail(requestDto);
        return ResponseEntity.ok().build();
    }

}
