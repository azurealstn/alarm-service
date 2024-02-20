package study.alarmservice.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

/**
 * statusCode: 404 (리소스 못찾음)
 */
public class UserNotFoundException extends CustomRuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
