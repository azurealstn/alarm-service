package study.alarmservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import study.alarmservice.domain.Role;
import study.alarmservice.domain.User;
import study.alarmservice.dto.request.UserCreateRequestDto;
import study.alarmservice.dto.request.UserSearchDto;
import study.alarmservice.repository.UserRepository;
import study.alarmservice.service.UserService;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class UserApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("회원가입 성공")
    void join_success() throws Exception {
        // given
        String email = "azurealstn33@gmail.com";
        String password = "abcd1234!";

        UserCreateRequestDto requestDto = UserCreateRequestDto.builder()
                .email(email)
                .password(password)
                .build();

        String content = objectMapper.writeValueAsString(requestDto);

        // expected
        mockMvc.perform(post("/api/v1/users")
                        .contentType(APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 중복 체크")
    void join_fail_email_duplicate_check() throws Exception {
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

        String content = objectMapper.writeValueAsString(requestDto);

        // expected
        mockMvc.perform(post("/api/v1/users")
                        .contentType(APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value(messageSource.getMessage("email.duplicate", null, Locale.KOREA)))
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 중복 체크 영문")
    void join_fail_email_duplicate_check_en() throws Exception {
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

        String content = objectMapper.writeValueAsString(requestDto);

        // expected
        mockMvc.perform(post("/api/v1/users?lang=en")
                        .contentType(APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value(messageSource.getMessage("email.duplicate", null, Locale.ENGLISH)))
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 빈값 검증")
    void join_fail_email_empty_valid() throws Exception {
        // given
        String email = "  ";
        String password = "abcd1234!";

        UserCreateRequestDto requestDto = UserCreateRequestDto.builder()
                .email(email)
                .password(password)
                .build();

        String content = objectMapper.writeValueAsString(requestDto);

        // expected
        mockMvc.perform(post("/api/v1/users")
                        .contentType(APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("클라이언트의 잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.size()").value(2))
                .andExpect(jsonPath("$.validation[0].fieldName").value("email"))
                .andExpect(jsonPath("$.validation[1].fieldName").value("email"))
                .andExpect(jsonPath("$.validation[*].errorMessage", hasItem(containsString(messageSource.getMessage("email.notBlank", null, Locale.KOREA)))))
                .andExpect(jsonPath("$.validation[*].errorMessage", hasItem(containsString(messageSource.getMessage("email.format", null, Locale.KOREA)))))
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 빈값 검증 영문")
    void join_fail_email_empty_valid_en() throws Exception {
        // given
        String email = "  ";
        String password = "abcd1234!";

        UserCreateRequestDto requestDto = UserCreateRequestDto.builder()
                .email(email)
                .password(password)
                .build();

        String content = objectMapper.writeValueAsString(requestDto);

        // expected
        mockMvc.perform(post("/api/v1/users?lang=en")
                        .contentType(APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value(messageSource.getMessage("badRequest", null, Locale.ENGLISH)))
                .andExpect(jsonPath("$.validation.size()").value(2))
                .andExpect(jsonPath("$.validation[0].fieldName").value("email"))
                .andExpect(jsonPath("$.validation[1].fieldName").value("email"))
                .andExpect(jsonPath("$.validation[*].errorMessage", hasItem(containsString(messageSource.getMessage("email.notBlank", null, Locale.ENGLISH)))))
                .andExpect(jsonPath("$.validation[*].errorMessage", hasItem(containsString(messageSource.getMessage("email.format", null, Locale.ENGLISH)))))
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 형식 검증")
    void join_fail_email_format_valid() throws Exception {
        // given
        String email = "azurealstn";
        String password = "abcd1234!";

        UserCreateRequestDto requestDto = UserCreateRequestDto.builder()
                .email(email)
                .password(password)
                .build();

        String content = objectMapper.writeValueAsString(requestDto);

        // expected
        mockMvc.perform(post("/api/v1/users")
                        .contentType(APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("클라이언트의 잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation[0].fieldName").value("email"))
                .andExpect(jsonPath("$.validation[0].errorMessage").value(messageSource.getMessage("email.format", null, Locale.KOREA)))
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 형식 검증 영문")
    void join_fail_email_format_valid_en() throws Exception {
        // given
        String email = "azurealstn";
        String password = "abcd1234!";

        UserCreateRequestDto requestDto = UserCreateRequestDto.builder()
                .email(email)
                .password(password)
                .build();

        String content = objectMapper.writeValueAsString(requestDto);

        // expected
        mockMvc.perform(post("/api/v1/users?lang=en")
                        .contentType(APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value(messageSource.getMessage("badRequest", null, Locale.ENGLISH)))
                .andExpect(jsonPath("$.validation[0].fieldName").value("email"))
                .andExpect(jsonPath("$.validation[0].errorMessage").value(messageSource.getMessage("email.format", null, Locale.ENGLISH)))
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 실패 - 비밀번호 빈값 검증")
    void join_fail_password_empty_valid() throws Exception {
        // given
        String email = "azurealstn33@gmail.com";
        String password = "";

        UserCreateRequestDto requestDto = UserCreateRequestDto.builder()
                .email(email)
                .password(password)
                .build();

        String content = objectMapper.writeValueAsString(requestDto);

        // expected
        mockMvc.perform(post("/api/v1/users")
                        .contentType(APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("클라이언트의 잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.size()").value(2))
                .andExpect(jsonPath("$.validation[0].fieldName").value("password"))
                .andExpect(jsonPath("$.validation[1].fieldName").value("password"))
                .andExpect(jsonPath("$.validation[*].errorMessage", hasItem(containsString(messageSource.getMessage("password.notBlank", null, Locale.KOREA)))))
                .andExpect(jsonPath("$.validation[*].errorMessage", hasItem(containsString(messageSource.getMessage("password.pattern", null, Locale.KOREA)))))
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 실패 - 비밀번호 빈값 검증 영문")
    void join_fail_password_empty_valid_en() throws Exception {
        // given
        String email = "azurealstn33@gmail.com";
        String password = "";

        UserCreateRequestDto requestDto = UserCreateRequestDto.builder()
                .email(email)
                .password(password)
                .build();

        String content = objectMapper.writeValueAsString(requestDto);

        // expected
        mockMvc.perform(post("/api/v1/users?lang=en")
                        .contentType(APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value(messageSource.getMessage("badRequest", null, Locale.ENGLISH)))
                .andExpect(jsonPath("$.validation.size()").value(2))
                .andExpect(jsonPath("$.validation[0].fieldName").value("password"))
                .andExpect(jsonPath("$.validation[1].fieldName").value("password"))
                .andExpect(jsonPath("$.validation[*].errorMessage", hasItem(containsString(messageSource.getMessage("password.notBlank", null, Locale.ENGLISH)))))
                .andExpect(jsonPath("$.validation[*].errorMessage", hasItem(containsString(messageSource.getMessage("password.pattern", null, Locale.ENGLISH)))))
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 실패 - 비밀번호 패턴 검증")
    void join_fail_password_pattern_valid() throws Exception {
        // given
        String email = "azurealstn33@gmail.com";
        String password = "abcde";

        UserCreateRequestDto requestDto = UserCreateRequestDto.builder()
                .email(email)
                .password(password)
                .build();

        String content = objectMapper.writeValueAsString(requestDto);

        // expected
        mockMvc.perform(post("/api/v1/users")
                        .contentType(APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("클라이언트의 잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation[0].fieldName").value("password"))
                .andExpect(jsonPath("$.validation[0].errorMessage").value(messageSource.getMessage("password.pattern", null, Locale.KOREA)))
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 실패 - 비밀번호 패턴 검증 영문")
    void join_fail_password_pattern_valid_en() throws Exception {
        // given
        String email = "azurealstn33@gmail.com";
        String password = "abcde";

        UserCreateRequestDto requestDto = UserCreateRequestDto.builder()
                .email(email)
                .password(password)
                .build();

        String content = objectMapper.writeValueAsString(requestDto);

        // expected
        mockMvc.perform(post("/api/v1/users?lang=en")
                        .contentType(APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value(messageSource.getMessage("badRequest", null, Locale.ENGLISH)))
                .andExpect(jsonPath("$.validation[0].fieldName").value("password"))
                .andExpect(jsonPath("$.validation[0].errorMessage").value(messageSource.getMessage("password.pattern", null, Locale.ENGLISH)))
                .andDo(print());
    }

    @Test
    @DisplayName("회원 단건 조회 성공")
    void user_find_one_success() throws Exception {
        // given
        String email = "azurealstn33@gmail.com";
        String password = "abcd1234!";

        UserCreateRequestDto requestDto = UserCreateRequestDto.builder()
                .email(email)
                .password(password)
                .build();

        Long savedId = userService.join(requestDto);

        // expected
        mockMvc.perform(get("/api/v1/users/{userId}", savedId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(savedId))
                .andExpect(jsonPath("$.email").value(email))
                .andDo(print());
    }

    @Test
    @DisplayName("회원 단건 조회 실패")
    void user_find_one_fail() throws Exception {
        // given
        String email = "azurealstn33@gmail.com";
        String password = "abcd1234!";

        UserCreateRequestDto requestDto = UserCreateRequestDto.builder()
                .email(email)
                .password(password)
                .build();

        Long savedId = userService.join(requestDto);

        // expected
        mockMvc.perform(get("/api/v1/users/{userId}", savedId + 1L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value(messageSource.getMessage("user.notFound", null, Locale.KOREA)))
                .andExpect(jsonPath("$.validation.size()", is(0)))
                .andDo(print());
    }

    @Test
    @DisplayName("회원 단건 조회 실패 영문")
    void user_find_one_fail_en() throws Exception {
        // given
        String email = "azurealstn33@gmail.com";
        String password = "abcd1234!";

        UserCreateRequestDto requestDto = UserCreateRequestDto.builder()
                .email(email)
                .password(password)
                .build();

        Long savedId = userService.join(requestDto);

        // expected
        mockMvc.perform(get("/api/v1/users/{userId}?lang=en", savedId + 1L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value(messageSource.getMessage("user.notFound", null, Locale.ENGLISH)))
                .andExpect(jsonPath("$.validation.size()", is(0)))
                .andDo(print());
    }

    @Test
    @DisplayName("회원 리스트 조회 1 페이지 - page와 size 파라미터를 넘기지 않았을 경우")
    void get_users_with_paging_1() throws Exception {
        // given
        List<User> users = IntStream.range(1, 111)
                .mapToObj(i -> User.builder()
                        .email("abcde" + i + "@gmail.com")
                        .password("12345")
                        .role(Role.GUEST)
                        .build())
                .collect(Collectors.toList());

        userRepository.saveAll(users);

        UserSearchDto userSearchDto = new UserSearchDto();
        String content = objectMapper.writeValueAsString(userSearchDto);

        // expected
        mockMvc.perform(get("/api/v1/users")
                        .contentType(APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users.length()", is(10)))
                .andExpect(jsonPath("$.users[0].email").value("abcde110@gmail.com"))
                .andExpect(jsonPath("$.paging.page").value(1))
                .andExpect(jsonPath("$.paging.size").value(10))
                .andExpect(jsonPath("$.paging.pageCount").value(10))
                .andExpect(jsonPath("$.paging.prev").value(false))
                .andExpect(jsonPath("$.paging.next").value(true))
                .andExpect(jsonPath("$.paging.totalPageCount").value(11))
                .andExpect(jsonPath("$.paging.startPage").value(1))
                .andExpect(jsonPath("$.paging.endPage").value(10))
                .andDo(print());
    }

    @Test
    @DisplayName("회원 리스트 조회 2 페이지 - page와 size 파라미터를 넘겼을 경우")
    void get_users_with_paging_2() throws Exception {
        // given
        List<User> users = IntStream.range(1, 111)
                .mapToObj(i -> User.builder()
                        .email("abcde" + i + "@gmail.com")
                        .password("12345")
                        .role(Role.GUEST)
                        .build())
                .collect(Collectors.toList());

        userRepository.saveAll(users);

        UserSearchDto userSearchDto = UserSearchDto.builder()
                .page(2)
                .size(20)
                .build();

        // expected
        mockMvc.perform(get("/api/v1/users")
                        .contentType(APPLICATION_FORM_URLENCODED_VALUE)
                        .param("page", String.valueOf(userSearchDto.getPage()))
                        .param("size", String.valueOf(userSearchDto.getSize())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users.length()", is(20)))
                .andExpect(jsonPath("$.users[0].email").value("abcde90@gmail.com"))
                .andExpect(jsonPath("$.paging.page").value(2))
                .andExpect(jsonPath("$.paging.size").value(20))
                .andExpect(jsonPath("$.paging.pageCount").value(10))
                .andExpect(jsonPath("$.paging.prev").value(false))
                .andExpect(jsonPath("$.paging.next").value(false))
                .andExpect(jsonPath("$.paging.totalPageCount").value(6))
                .andExpect(jsonPath("$.paging.startPage").value(1))
                .andExpect(jsonPath("$.paging.endPage").value(6))
                .andDo(print());
    }

    @Test
    @DisplayName("회원 리스트 조회 마지막 페이지 - page 파라미터만 넘겼을 경우")
    void get_users_with_paging_3() throws Exception {
        // given
        List<User> users = IntStream.range(1, 111)
                .mapToObj(i -> User.builder()
                        .email("abcde" + i + "@gmail.com")
                        .password("12345")
                        .role(Role.GUEST)
                        .build())
                .collect(Collectors.toList());

        userRepository.saveAll(users);

        UserSearchDto userSearchDto = UserSearchDto.builder()
                .page(11)
                .build();

        // expected
        mockMvc.perform(get("/api/v1/users")
                        .contentType(APPLICATION_FORM_URLENCODED_VALUE)
                        .param("page", String.valueOf(userSearchDto.getPage())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users.length()", is(10)))
                .andExpect(jsonPath("$.users[0].email").value("abcde10@gmail.com"))
                .andExpect(jsonPath("$.paging.page").value(11))
                .andExpect(jsonPath("$.paging.size").value(10))
                .andExpect(jsonPath("$.paging.pageCount").value(10))
                .andExpect(jsonPath("$.paging.prev").value(true))
                .andExpect(jsonPath("$.paging.next").value(false))
                .andExpect(jsonPath("$.paging.totalPageCount").value(11))
                .andExpect(jsonPath("$.paging.startPage").value(11))
                .andExpect(jsonPath("$.paging.endPage").value(11))
                .andDo(print());
    }
}