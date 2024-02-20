package study.alarmservice.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import study.alarmservice.domain.User;
import study.alarmservice.dto.request.PagingRequest;
import study.alarmservice.dto.request.UserCreateRequestDto;
import study.alarmservice.dto.request.UserSearchDto;
import study.alarmservice.dto.response.UserPageResponseDto;
import study.alarmservice.dto.response.UserResponseDto;
import study.alarmservice.exception.EmailDuplicateException;
import study.alarmservice.exception.UserNotFoundException;
import study.alarmservice.repository.UserRepository;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Validated
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MessageSource messageSource;

    @Transactional
    public Long join(@Valid UserCreateRequestDto requestDto) {
        User user = requestDto.toEntity();
        Optional<User> hasID = userRepository.findByEmail(user.getEmail());
        if (hasID.isPresent()) {
            throw new EmailDuplicateException(messageSource.getMessage("email.duplicate", null, LocaleContextHolder.getLocale()));
        }

        user.hashPassword(passwordEncoder.encode(user.getPassword()));
        user.guestUser();
        return userRepository.save(user).getId();
    }

    public UserResponseDto findById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(messageSource.getMessage("user.notFound", null, LocaleContextHolder.getLocale())));

        UserResponseDto responseDto = UserResponseDto.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .build();

        return responseDto;
    }

    public UserPageResponseDto findAll(UserSearchDto userSearchDto) {
        List<UserResponseDto> users = userRepository.findUsers(userSearchDto).stream()
                .map(UserResponseDto::new)
                .collect(Collectors.toList());

        int totalRowCount = userRepository.findUsersCount(userSearchDto);
        PagingRequest paging = PagingRequest.of(userSearchDto.getPage(), userSearchDto.getSize(), totalRowCount, 10);

        UserPageResponseDto userPageResponseDto = UserPageResponseDto.builder()
                .users(users)
                .paging(paging)
                .build();

        return userPageResponseDto;
    }
}
