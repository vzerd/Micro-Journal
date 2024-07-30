package com.vizz.microjournal.service;

import com.vizz.microjournal.model.JournalDTO;
import com.vizz.microjournal.model.Journals;
import com.vizz.microjournal.repository.JournalRepository;
import com.vizz.microjournal.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Date;
import java.util.UUID;

@Service
public class JournalRESTService {

    JournalRepository journalRepository;
    UsersRepository usersRepository;
    @Autowired
    JournalRESTService(JournalRepository journalRepository, UsersRepository usersRepository){
        this.journalRepository = journalRepository;
        this.usersRepository = usersRepository;
    }

    @Transactional
    public ResponseEntity<String> saveJournalService(JournalDTO journalDTO){
        if(journalDTO.getToken() == null || journalDTO.getToken().toString().isBlank() ||
                journalDTO.getDate() == null || journalDTO.getDate().toString().isBlank() ||
                journalDTO.getJournal() == null || journalDTO.getJournal().isBlank()){
            return new ResponseEntity<>("{\"payload\":\"Field(s) is/are incomplete or empty.\"}",
                    HttpStatus.valueOf(406));
        }
        Journals journal = new Journals();
        journal.setDay(journalDTO.getDate());
        try{
            Integer uidValue = usersRepository.getIdByToken(journalDTO.getToken());
            if(uidValue == null){
                return new ResponseEntity<>("{\"payload\":\"Given token is not found.\"}",
                        HttpStatus.valueOf(404));
            }
            journal.setUid(uidValue);
            Integer journalId = journalRepository.getIdByUidAndDate(uidValue, journalDTO.getDate());
            if(journalId == null){
                journal.setJournal(journalDTO.getJournal());
            }else{
                String journalText = journalRepository.getJournalById(journalId);
                journalText += ("<><>" + journalDTO.getJournal());
                journal.setJournal(journalText);
                journal.setId(journalId);
            }

            journalRepository.save(journal);
            return new ResponseEntity<>("{\"payload\":\"success\"}",
                    HttpStatus.valueOf(200));
        }catch(DataAccessException e){
            return new ResponseEntity<>("{\"payload\":\"Server error: save journal failed - journal persistence failed.\"}",
                    HttpStatus.valueOf(500));
        }
    }

    @Transactional
    public ResponseEntity<String> updateJournalService(JournalDTO journalDTO){
        if(journalDTO.getToken() == null || journalDTO.getToken().toString().isBlank() ||
                journalDTO.getDate() == null || journalDTO.getDate().toString().isBlank() ||
                journalDTO.getJournal() == null || journalDTO.getJournal().isBlank()){
            return new ResponseEntity<>("{\"payload\":\"Field(s) is/are incomplete or empty.\"}",
                    HttpStatus.valueOf(406));
        }
        Journals journal = new Journals();
        journal.setDay(journalDTO.getDate());
        try{
            Integer uidValue = usersRepository.getIdByToken(journalDTO.getToken());
            if(uidValue == null){
                return new ResponseEntity<>("{\"payload\":\"Given token is not found.\"}",
                        HttpStatus.valueOf(404));
            }
            journal.setUid(uidValue);
            Integer journalId = journalRepository.getIdByUidAndDate(uidValue, journalDTO.getDate());
            if(journalId == null){
                return new ResponseEntity<>("{\"payload\":\"No previous journal found to be updated upon.\"}",
                        HttpStatus.valueOf(404));
            }else{
                journal.setJournal(journalDTO.getJournal());
                journal.setId(journalId);
            }

            journalRepository.save(journal);
            return new ResponseEntity<>("{\"payload\":\"success\"}",
                    HttpStatus.valueOf(200));
        }catch(DataAccessException e){
            return new ResponseEntity<>("{\"payload\":\"Server error: save journal failed - journal persistence failed.\"}",
                    HttpStatus.valueOf(500));
        }
    }

    public ResponseEntity<String> getJournalService(String token, Date date){
        if(token == null || token.isBlank() || date == null || date.toString().isBlank()) {
            return new ResponseEntity<>("{\"payload\":\"Field(s) is/are incomplete or empty.\"}",
                    HttpStatus.valueOf(406));
        }
        try{
            Integer uidValue = usersRepository.getIdByToken(UUID.fromString(token));
            if(uidValue == null){
                return new ResponseEntity<>("{\"payload\":\"Given token is not found.\"}",
                        HttpStatus.valueOf(404));
            }
            Integer journalId = journalRepository.getIdByUidAndDate(uidValue, date);
            if(journalId == null){
                return new ResponseEntity<>("{\"payload\":\"No journal records found.\"}",
                        HttpStatus.valueOf(406));
            }else{
                String journalText = journalRepository.getJournalById(journalId);
                return new ResponseEntity<>("{\"payload\":\"" + journalText + "\"}",
                        HttpStatus.valueOf(200));
            }
        }catch(DataAccessException e){
            return new ResponseEntity<>("{\"payload\":\"Server error: get journal failed - journal fetch failed.\"}",
                    HttpStatus.valueOf(500));
        }
    }

    @Transactional
    public ResponseEntity<String> deleteJournalService(JournalDTO journalDTO){
        if(journalDTO.getToken() == null || journalDTO.getToken().toString().isBlank() ||
                journalDTO.getDate() == null || journalDTO.getDate().toString().isBlank()) {
            return new ResponseEntity<>("{\"payload\":\"Field(s) is/are incomplete or empty.\"}",
                    HttpStatus.valueOf(406));
        }
        try{
            Integer uidValue = usersRepository.getIdByToken(journalDTO.getToken());
            if(uidValue == null){
                return new ResponseEntity<>("{\"payload\":\"Given token is not found.\"}",
                        HttpStatus.valueOf(404));
            }
            Integer deletionResponse = journalRepository.deleteByUidAndDate(uidValue, journalDTO.getDate());
            if(deletionResponse == null){
                return new ResponseEntity<>("{\"payload\":\"No journal records found.\"}",
                        HttpStatus.valueOf(406));
            }else{
                return new ResponseEntity<>("{\"payload\":\"Journal deleted!\"}",
                        HttpStatus.valueOf(200));
            }
        }catch(DataAccessException e){
            return new ResponseEntity<>("{\"payload\":\"Server error: journal deletion failed.\"}",
                    HttpStatus.valueOf(500));
        }
    }
}
