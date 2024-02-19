package study.alarmservice.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ErrorResponseDto {

    private int code;
    private String message;
    private List<ValidationDto> validation = new ArrayList<>();

    @Builder
    public ErrorResponseDto(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public void addValidation(ValidationDto validationDto) {
        validation.add(validationDto);
    }
}
