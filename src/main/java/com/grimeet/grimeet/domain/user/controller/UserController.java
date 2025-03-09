package com.grimeet.grimeet.domain.user.controller;


import com.grimeet.grimeet.domain.user.dto.*;
import com.grimeet.grimeet.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    public UserResponseDto createUser(@Valid @RequestBody UserCreateRequestDto userCreateRequestDto ) {
        return userService.createUser(userCreateRequestDto);
    }

    @PatchMapping("/update/password")
    public UserResponseDto updateUserPassword(@Valid @RequestBody UserUpdatePasswordRequestDto requestDto) {
        return userService.updateUserPassword(requestDto);
    }

    @PatchMapping("/update/userStatus")
    public void updateUserStatus(@Valid @RequestBody UserUpdateStatusRequestDto requestDto) {
        userService.updateWithdrawUser(requestDto.getEmail());
    }

    @PatchMapping("/update/nickname")
    public void updateNickname(@Valid @RequestBody UserUpdateNicknameRequestDto requestDto) {
        userService.updateUserNickname(requestDto);
    }
}
