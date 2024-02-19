package study.alarmservice.service;

import jakarta.validation.ConstraintViolationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import study.alarmservice.domain.Role;
import study.alarmservice.domain.User;
import study.alarmservice.dto.request.UserCreateRequestDto;
import study.alarmservice.repository.UserRepository;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원가입 성공")
    void join_success() {
        // given
        String email = "azurealstn33@gmail.com";
        String password = "abcd1234!";

        UserCreateRequestDto requestDto = UserCreateRequestDto.builder()
                .email(email)
                .password(password)
                .build();

        // when
        Long savedId = userService.join(requestDto);
        User user = userRepository.findById(savedId).get();
        String hashPassword = user.getPassword();

        // then
        assertThat(userRepository.count()).isEqualTo(1);
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(passwordEncoder.matches(password, hashPassword)).isTrue();
        assertThat(user.getRole()).isEqualTo(Role.GUEST);
    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 빈값 검증")
    void join_fail_email_empty_valid() {
        // given
        String email = "  ";
        String password = "abcd1234!";

        UserCreateRequestDto requestDto = UserCreateRequestDto.builder()
                .email(email)
                .password(password)
                .build();

        // expected
        assertThatThrownBy(() -> userService.join(requestDto))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 형식 검증")
    void join_fail_email_format_valid() {
        // given
        String email = "azurealstn";
        String password = "abcd1234!";

        UserCreateRequestDto requestDto = UserCreateRequestDto.builder()
                .email(email)
                .password(password)
                .build();

        // expected
        assertThatThrownBy(() -> userService.join(requestDto))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("회원가입 실패 - 비밀번호 빈값 검증")
    void join_fail_password_format_valid() {
        // given
        String email = "azurealstn33@gmail.com";
        String password = "";

        UserCreateRequestDto requestDto = UserCreateRequestDto.builder()
                .email(email)
                .password(password)
                .build();

        // expected
        assertThatThrownBy(() -> userService.join(requestDto))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("회원가입 실패 - 비밀번호 패턴 검증")
    void join_fail_password_pattern_valid() {
        // given
        String email = "azurealstn33@gmail.com";
        String password = "abcde";

        UserCreateRequestDto requestDto = UserCreateRequestDto.builder()
                .email(email)
                .password(password)
                .build();

        // expected
        assertThatThrownBy(() -> userService.join(requestDto))
                .isInstanceOf(ConstraintViolationException.class);
    }
}