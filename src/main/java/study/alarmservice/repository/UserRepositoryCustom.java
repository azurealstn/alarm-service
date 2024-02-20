package study.alarmservice.repository;

import study.alarmservice.domain.User;
import study.alarmservice.dto.request.UserSearchDto;

import java.util.List;

public interface UserRepositoryCustom {

    List<User> findUsers(UserSearchDto userSearchDto);

    int findUsersCount(UserSearchDto userSearchDto);
}
