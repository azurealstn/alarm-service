package study.alarmservice.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import study.alarmservice.domain.QUser;
import study.alarmservice.domain.User;
import study.alarmservice.dto.request.UserSearchDto;

import java.util.List;
import java.util.Optional;

import static study.alarmservice.domain.QUser.*;

@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public List<User> findUsers(UserSearchDto userSearchDto) {
        return query
                .selectFrom(user)
                .limit(userSearchDto.getLimit(userSearchDto.getSize()))
                .offset(userSearchDto.getOffset(userSearchDto.getPage(), userSearchDto.getSize()))
                .orderBy(user.id.desc())
                .fetch();
    }

    @Override
    public int findUsersCount(UserSearchDto userSearchDto) {
        Long count = query
                .select(user.count())
                .from(user)
                .fetchOne();

        return Optional.ofNullable(count).orElse(0L).intValue();
    }

}
