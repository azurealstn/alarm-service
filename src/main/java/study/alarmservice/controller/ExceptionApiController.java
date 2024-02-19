package study.alarmservice.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import study.alarmservice.dto.response.ErrorResponseDto;
import study.alarmservice.dto.response.ValidationDto;

import java.util.Locale;
import java.util.Set;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionApiController {

    private final MessageSource messageSource;

    /**
     * @Valid 애너테이션으로 데이터를 검증하고,
     * 해당 데이터에 에러가 있을 경우 스프링에서 정의한 에러, BindingResult를 상속한
     * MethodArgumentNotValidException 에러 글래스를 반환시킨다.
     */
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponseDto methodArgumentNotValidException(MethodArgumentNotValidException e) {
        int code = BAD_REQUEST.value();
        String message = messageSource.getMessage("badRequest", null, Locale.getDefault());
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .code(code)
                .message(message)
                .build();

        for (FieldError fieldError : e.getFieldErrors()) {
            String fieldName = fieldError.getField();
            String errorMessage = fieldError.getDefaultMessage();
            ValidationDto validationDto = ValidationDto.builder()
                    .fieldName(fieldName)
                    .errorMessage(errorMessage)
                    .build();

            errorResponseDto.addValidation(validationDto);
        }

        return errorResponseDto;
    }

}
