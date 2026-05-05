package ru.glashiii.springauthms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.glashiii.springauthms.entities.UserInfo;

import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {
    Optional<UserInfo> findByEmail(String email);

}
