package study.alarmservice.domain;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN("ROLE_ADMIN", "어드민 사용자"),
    GUEST("ROLE_GUEST", "일반 사용자");

    private String key;
    private String value;

    Role(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
