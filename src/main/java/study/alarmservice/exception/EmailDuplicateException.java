package study.alarmservice.exception;

/**
 * 이메일 중복 예외
 */
public class EmailDuplicateException extends CustomRuntimeException {

    public EmailDuplicateException(String message) {
        super(message);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
