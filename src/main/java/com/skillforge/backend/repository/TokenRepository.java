package com.skillforge.backend.repository;

import com.skillforge.backend.entity.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenRepository extends JpaRepository<UserToken, UUID> {

    @Query(value = """
      select t from UserToken t inner join User u on t.user.userId = u.userId where u.userId = :id and (t.expired = false or t.revoked = false)
      """)
    List<UserToken> findAllValidTokens(String id);

    Optional<UserToken> findByToken(String token);

}
