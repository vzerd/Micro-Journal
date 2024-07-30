package com.vizz.microjournal.repository;

import com.vizz.microjournal.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.UUID;

public interface UsersRepository extends JpaRepository<Users, Integer> {
    @Query("SELECT email FROM Users")
    List<String> getAllEmails();

    @Query("SELECT token FROM Users")
    List<UUID> getAllTokens();

    @Query("SELECT name FROM Users WHERE token = ?1")
    String getNameByToken(UUID token);

    @Modifying
    @Query("UPDATE Users SET token = null WHERE token = ?1")
    int setTokenToNull(UUID token);

    @Query("SELECT password FROM Users WHERE email = ?1")
    String getPasswordByEmail(String email);

    @Modifying
    @Query("UPDATE Users SET token = ?1 WHERE email = ?2")
    int updateTokenByEmail(UUID token, String email);

    @Modifying
    @Query("DELETE FROM Users WHERE token = ?1")
    int deleteUserByToken(UUID token);

    @Query("SELECT id FROM Users WHERE token = ?1")
    Integer getIdByToken(UUID token);
}
