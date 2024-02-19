package study.alarmservice.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import study.alarmservice.domain.User;
import study.alarmservice.dto.request.UserCreateRequestDto;
import study.alarmservice.repository.UserRepository;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Validated
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long join(@Valid UserCreateRequestDto requestDto) {
        User user = requestDto.toEntity();
        user.hashPassword(passwordEncoder.encode(user.getPassword()));
        user.guestUser();
        return userRepository.save(user).getId();
    }
}
