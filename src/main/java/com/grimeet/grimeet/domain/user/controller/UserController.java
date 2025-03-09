package com.grimeet.grimeet.domain.user.controller;


import com.grimeet.grimeet.domain.user.dto.UserCreateRequestDto;
import com.grimeet.grimeet.domain.user.dto.UserUpdatePasswordRequestDto;
import com.grimeet.grimeet.domain.user.dto.UserResponseDto;
import com.grimeet.grimeet.domain.user.dto.UserUpdateStatusRequestDto;
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
}
