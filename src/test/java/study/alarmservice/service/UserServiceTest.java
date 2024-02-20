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
import study.alarmservice.dto.request.UserSearchDto;
import study.alarmservice.dto.response.UserPageResponseDto;
import study.alarmservice.dto.response.UserResponseDto;
import study.alarmservice.exception.EmailDuplicateException;
import study.alarmservice.exception.UserNotFoundException;
import study.alarmservice.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    @DisplayName("회원가입 실패 - 이메일 중복 체크")
    void join_fail_email_duplicate_check() {
        // given
        User user = User.builder()
                .email("azurealstn33@gmail.com")
                .password("12345")
                .role(Role.GUEST)
                .build();

        userRepository.save(user);

        String email = "azurealstn33@gmail.com";
        String password = "abcd1234!";

        UserCreateRequestDto requestDto = UserCreateRequestDto.builder()
                .email(email)
                .password(password)
                .build();

        // expected
        assertThatThrownBy(() -> userService.join(requestDto))
                .isInstanceOf(EmailDuplicateException.class);
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

    @Test
    @DisplayName("회원 단건 조회 성공")
    void user_find_one_success() {
        // given
        String email = "azurealstn33@gmail.com";
        String password = "abcd1234!";

        UserCreateRequestDto requestDto = UserCreateRequestDto.builder()
                .email(email)
                .password(password)
                .build();

        // when
        Long savedId = userService.join(requestDto);
        UserResponseDto responseDto = userService.findById(savedId);

        // then
        assertThat(responseDto.getUserId()).isEqualTo(savedId);
        assertThat(responseDto.getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("회원 단건 조회 실패")
    void user_find_one_fail() {
        // given
        String email = "azurealstn33@gmail.com";
        String password = "abcd1234!";

        UserCreateRequestDto requestDto = UserCreateRequestDto.builder()
                .email(email)
                .password(password)
                .build();

        // expected
        Long savedId = userService.join(requestDto);
        assertThatThrownBy(() -> userService.findById(savedId + 1L))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("회원 리스트 조회 1 페이지 - page와 size 파라미터를 넘기지 않았을 경우")
    void get_users_with_paging_1() {
        // given
        List<User> users = IntStream.range(1, 111)
                .mapToObj(i -> User.builder()
                        .email("abcde" + i + "@gmail.com")
                        .password("12345")
                        .role(Role.GUEST)
                        .build())
                .collect(Collectors.toList());

        userRepository.saveAll(users);

        // when
        UserSearchDto userSearchDto = new UserSearchDto();

        UserPageResponseDto responseDto = userService.findAll(userSearchDto);

        // then
        assertThat(users.size()).isEqualTo(110);
        assertThat(responseDto.getUsers().size()).isEqualTo(10);
        assertThat(responseDto.getUsers().get(0).getEmail()).isEqualTo("abcde110@gmail.com");
        assertThat(responseDto.getUsers().get(9).getEmail()).isEqualTo("abcde101@gmail.com");

        assertThat(responseDto.getPaging().getPage()).isEqualTo(1);
        assertThat(responseDto.getPaging().getSize()).isEqualTo(10);
        assertThat(responseDto.getPaging().getPageCount()).isEqualTo(10);
        assertThat(responseDto.getPaging().isPrev()).isFalse();
        assertThat(responseDto.getPaging().isNext()).isTrue();
        assertThat(responseDto.getPaging().getTotalPageCount()).isEqualTo(11);
        assertThat(responseDto.getPaging().getStartPage()).isEqualTo(1);
        assertThat(responseDto.getPaging().getEndPage()).isEqualTo(10);
    }

    @Test
    @DisplayName("회원 리스트 조회 1 페이지 - page와 size 파라미터를 넘겼을 경우")
    void get_users_with_paging_2() {
        // given
        List<User> users = IntStream.range(1, 111)
                .mapToObj(i -> User.builder()
                        .email("abcde" + i + "@gmail.com")
                        .password("12345")
                        .role(Role.GUEST)
                        .build())
                .collect(Collectors.toList());

        userRepository.saveAll(users);

        // when
        UserSearchDto userSearchDto = UserSearchDto.builder()
                .page(1)
                .size(20)
                .build();

        UserPageResponseDto responseDto = userService.findAll(userSearchDto);

        // then
        assertThat(users.size()).isEqualTo(110);
        assertThat(responseDto.getUsers().size()).isEqualTo(20);
        assertThat(responseDto.getUsers().get(0).getEmail()).isEqualTo("abcde110@gmail.com");
        assertThat(responseDto.getUsers().get(19).getEmail()).isEqualTo("abcde91@gmail.com");

        assertThat(responseDto.getPaging().getPage()).isEqualTo(1);
        assertThat(responseDto.getPaging().getSize()).isEqualTo(20);
        assertThat(responseDto.getPaging().getPageCount()).isEqualTo(10);
        assertThat(responseDto.getPaging().isPrev()).isFalse();
        assertThat(responseDto.getPaging().isNext()).isFalse();
        assertThat(responseDto.getPaging().getTotalPageCount()).isEqualTo(6);
        assertThat(responseDto.getPaging().getStartPage()).isEqualTo(1);
        assertThat(responseDto.getPaging().getEndPage()).isEqualTo(6);
    }

    @Test
    @DisplayName("회원 리스트 조회 0명인 경우")
    void get_users_with_paging_3() {
        // given
        UserSearchDto userSearchDto = new UserSearchDto();

        UserPageResponseDto responseDto = userService.findAll(userSearchDto);

        // then
        assertThat(responseDto.getUsers().size()).isEqualTo(0);
        assertThat(responseDto.getPaging().getStartPage()).isEqualTo(1);
        assertThat(responseDto.getPaging().getEndPage()).isEqualTo(1);
        assertThat(responseDto.getPaging().getTotalPageCount()).isEqualTo(1);
        assertThat(responseDto.getPaging().getTotalRowCount()).isEqualTo(0);
    }

}