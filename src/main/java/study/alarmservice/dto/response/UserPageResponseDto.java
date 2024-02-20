package study.alarmservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import study.alarmservice.dto.request.PagingRequest;

import java.util.List;

@Getter
public class UserPageResponseDto {

    private List<UserResponseDto> users;
    private PagingRequest paging;

    @Builder
    public UserPageResponseDto(List<UserResponseDto> users, PagingRequest paging) {
        this.users = users;
        this.paging = paging;
    }
}
