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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import study.alarmservice.dto.request.UserCreateRequestDto;

import java.util.Locale;

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
                .andExpect(jsonPath("$.validation[*].errorMessage", Matchers.hasItem(Matchers.containsString(messageSource.getMessage("email.notBlank", null, Locale.getDefault())))))
                .andExpect(jsonPath("$.validation[*].errorMessage", Matchers.hasItem(Matchers.containsString(messageSource.getMessage("email.format", null, Locale.getDefault())))))
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
                .andExpect(jsonPath("$.message").value("클라이언트의 잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.size()").value(2))
                .andExpect(jsonPath("$.validation[0].fieldName").value("email"))
                .andExpect(jsonPath("$.validation[1].fieldName").value("email"))
                .andExpect(jsonPath("$.validation[*].errorMessage", Matchers.hasItem(Matchers.containsString(messageSource.getMessage("email.notBlank", null, Locale.ENGLISH)))))
                .andExpect(jsonPath("$.validation[*].errorMessage", Matchers.hasItem(Matchers.containsString(messageSource.getMessage("email.format", null, Locale.ENGLISH)))))
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
                .andExpect(jsonPath("$.validation[0].errorMessage").value(messageSource.getMessage("email.format", null, Locale.getDefault())))
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
                .andExpect(jsonPath("$.message").value("클라이언트의 잘못된 요청입니다."))
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
                .andExpect(jsonPath("$.validation[*].errorMessage", Matchers.hasItem(Matchers.containsString(messageSource.getMessage("password.notBlank", null, Locale.getDefault())))))
                .andExpect(jsonPath("$.validation[*].errorMessage", Matchers.hasItem(Matchers.containsString(messageSource.getMessage("password.pattern", null, Locale.getDefault())))))
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
                .andExpect(jsonPath("$.message").value("클라이언트의 잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.size()").value(2))
                .andExpect(jsonPath("$.validation[0].fieldName").value("password"))
                .andExpect(jsonPath("$.validation[1].fieldName").value("password"))
                .andExpect(jsonPath("$.validation[*].errorMessage", Matchers.hasItem(Matchers.containsString(messageSource.getMessage("password.notBlank", null, Locale.ENGLISH)))))
                .andExpect(jsonPath("$.validation[*].errorMessage", Matchers.hasItem(Matchers.containsString(messageSource.getMessage("password.pattern", null, Locale.ENGLISH)))))
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
                .andExpect(jsonPath("$.validation[0].errorMessage").value(messageSource.getMessage("password.pattern", null, Locale.getDefault())))
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
                .andExpect(jsonPath("$.message").value("클라이언트의 잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation[0].fieldName").value("password"))
                .andExpect(jsonPath("$.validation[0].errorMessage").value(messageSource.getMessage("password.pattern", null, Locale.ENGLISH)))
                .andDo(print());
    }
}