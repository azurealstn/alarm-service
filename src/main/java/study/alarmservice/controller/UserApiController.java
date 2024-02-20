package study.alarmservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import study.alarmservice.domain.User;
import study.alarmservice.dto.request.UserCreateRequestDto;
import study.alarmservice.dto.request.UserSearchDto;
import study.alarmservice.dto.response.UserPageResponseDto;
import study.alarmservice.dto.response.UserResponseDto;
import study.alarmservice.service.UserService;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class UserApiController {

    private final UserService userService;

    @PostMapping("/users")
    public Long join(@Valid @RequestBody UserCreateRequestDto requestDto) {
        return userService.join(requestDto);
    }

    @GetMapping("/users/{userId}")
    public UserResponseDto findById(@PathVariable(name = "userId") Long userId) {
        return userService.findById(userId);
    }

    @GetMapping("/users")
    public UserPageResponseDto findAll(@ModelAttribute("userSearchDto") UserSearchDto userSearchDto) {
        return userService.findAll(userSearchDto);
    }
}
