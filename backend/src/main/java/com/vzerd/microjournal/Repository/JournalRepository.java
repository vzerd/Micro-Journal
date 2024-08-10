package com.vzerd.microjournal.Repository;

import com.vzerd.microjournal.Model.JournalModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.sql.Date;

public interface JournalRepository extends JpaRepository<JournalModel, Integer> {

    @Query("SELECT journal FROM JournalModel WHERE id = ?1")
    String getJournalById(int id);

    @Query("SELECT id FROM JournalModel WHERE uid = ?1 AND day = ?2")
    Integer getIdByUidAndDate(int uid, Date date);

    @Modifying
    @Query("DELETE FROM JournalModel WHERE uid = ?1 AND day = ?2")
    Integer deleteByUidAndDate(int uid, Date date);
}
