package com.vzerd.microjournal.Model;

import java.sql.Date;
import java.util.UUID;

public class JournalDTO {
    private UUID token;
    private Date date;
    private String journal;

    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getJournal() {
        return journal;
    }

    public void setJournal(String journal) {
        this.journal = journal;
    }
}
