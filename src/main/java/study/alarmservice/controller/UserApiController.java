package study.alarmservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.alarmservice.domain.User;
import study.alarmservice.dto.request.UserCreateRequestDto;
import study.alarmservice.service.UserService;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class UserApiController {

    private final UserService userService;

    @PostMapping("/users")
    public Long join(@Valid @RequestBody UserCreateRequestDto requestDto) {
        return userService.join(requestDto);
    }
}
