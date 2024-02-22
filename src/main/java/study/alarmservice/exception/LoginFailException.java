package study.alarmservice.exception;

/**
 * statusCode: 401 (Unauthorized)
 */
public class LoginFailException extends CustomRuntimeException {

    public LoginFailException(String message) {
        super(message);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}
