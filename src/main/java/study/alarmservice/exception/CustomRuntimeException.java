package study.alarmservice.exception;

/**
 * abstract로 클래스를 생성한 이유
 * 1. public abstract int statusCode();
 * -> status 코드에 대해 강제 구현
 * 2. 다른 곳에서 new로 객체 생성을 막는다.
 */
public abstract class CustomRuntimeException extends RuntimeException {

    public CustomRuntimeException() {
        super();
    }

    public CustomRuntimeException(String message) {
        super(message);
    }

    public CustomRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();

}
