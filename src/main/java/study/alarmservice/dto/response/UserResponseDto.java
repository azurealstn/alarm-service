package study.alarmservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import study.alarmservice.domain.User;

@Getter
public class UserResponseDto {

    private Long userId;
    private String email;

    @Builder
    public UserResponseDto(Long userId, String email) {
        this.userId = userId;
        this.email = email;
    }

    public UserResponseDto(User user) {
        userId = user.getId();
        email = user.getEmail();
    }
}
