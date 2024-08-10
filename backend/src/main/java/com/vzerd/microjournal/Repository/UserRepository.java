package com.vzerd.microjournal.Repository;

import com.vzerd.microjournal.Model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserModel, Integer> {
    @Query("SELECT email FROM UserModel")
    List<String> getAllEmails();

    @Query("SELECT token FROM UserModel")
    List<UUID> getAllTokens();

    @Query("SELECT name FROM UserModel WHERE token = ?1")
    String getNameByToken(UUID token);

    @Modifying
    @Query("UPDATE UserModel SET token = null WHERE token = ?1")
    int setTokenToNull(UUID token);

    @Query("SELECT password FROM UserModel WHERE email = ?1")
    String getPasswordByEmail(String email);

    @Modifying
    @Query("UPDATE UserModel SET token = ?1 WHERE email = ?2")
    int updateTokenByEmail(UUID token, String email);

    @Modifying
    @Query("DELETE FROM UserModel WHERE token = ?1")
    int deleteUserByToken(UUID token);

    @Query("SELECT id FROM UserModel WHERE token = ?1")
    Integer getIdByToken(UUID token);
}
