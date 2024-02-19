package study.alarmservice.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ValidationDto {

    private String fieldName;
    private String errorMessage;

    @Builder
    public ValidationDto(String fieldName, String errorMessage) {
        this.fieldName = fieldName;
        this.errorMessage = errorMessage;
    }
}
