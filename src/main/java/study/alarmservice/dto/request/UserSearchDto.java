package study.alarmservice.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class UserSearchDto extends PagingRequest {

    private int page;
    private int size;
    private String searchEmail;

    @Builder
    public UserSearchDto(int page, int size) {
        this.page = page;
        this.size = size;
    }

}
