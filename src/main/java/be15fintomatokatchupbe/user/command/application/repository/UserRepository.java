package be15fintomatokatchupbe.user.command.application.repository;

import be15fintomatokatchupbe.common.domain.StatusType;
import be15fintomatokatchupbe.user.command.domain.aggregate.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByEmail(String email);

    boolean existsByLoginId(@NotBlank String loginId);

    Optional<Object> findByLoginId(@NotBlank String loginId);

    Object findByEmail(String email);

    User findByUserId(Long userId);

    Optional<User> findByUserIdAndIsDeleted(Long id, StatusType statusType);

    List<User> findByUserIdInAndIsDeleted(List<Long> userId, StatusType statusType);

    String findFcmTokenByUserId(Long userId);
}

