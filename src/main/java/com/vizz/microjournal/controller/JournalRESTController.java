package com.vizz.microjournal.controller;

import com.vizz.microjournal.model.JournalDTO;
import com.vizz.microjournal.service.JournalRESTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.sql.Date;

@RestController
@CrossOrigin
@RequestMapping("/v1/journal")
public class JournalRESTController {

    JournalRESTService journalRESTService;
    @Autowired
    JournalRESTController(JournalRESTService journalRESTService){
        this.journalRESTService = journalRESTService;
    }

    @PostMapping("/save-journal")
    ResponseEntity<String> saveJournal(@RequestBody JournalDTO journalDTO){
        return journalRESTService.saveJournalService(journalDTO);
    }

    @PostMapping("/update-journal")
    ResponseEntity<String> updateJournal(@RequestBody JournalDTO journalDTO){
        return journalRESTService.updateJournalService(journalDTO);
    }

    @GetMapping("/get-journal")
    ResponseEntity<String> getJournal(@RequestParam String token, Date date){
        return journalRESTService.getJournalService(token, date);
    }

    @DeleteMapping("/delete-journal")
    ResponseEntity<String> deleteJournal(@RequestBody JournalDTO journalDTO){
        return journalRESTService.deleteJournalService(journalDTO);
    }
}
