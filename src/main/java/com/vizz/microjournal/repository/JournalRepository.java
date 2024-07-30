package com.vizz.microjournal.repository;


import com.vizz.microjournal.model.Journals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.List;

public interface JournalRepository extends JpaRepository<Journals, Integer> {

    @Query("SELECT journal FROM Journals WHERE id = ?1")
    String getJournalById(int id);

    @Query("SELECT id FROM Journals WHERE uid = ?1 AND day = ?2")
    Integer getIdByUidAndDate(int uid, Date date);

    @Modifying
    @Query("DELETE FROM Journals WHERE uid = ?1 AND day = ?2")
    Integer deleteByUidAndDate(int uid, Date date);
}
