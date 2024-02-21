package study.alarmservice.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class UserSearchDto extends PagingRequest {

    private String searchEmail;

    @Builder
    public UserSearchDto(int page, int size) {
        super(page, size);
    }

}
