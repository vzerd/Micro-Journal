package com.vzerd.microjournal.Controller;

import com.vzerd.microjournal.Model.JournalDTO;
import com.vzerd.microjournal.Service.JournalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.sql.Date;

@RestController
@CrossOrigin
@RequestMapping("/v1/journal")
public class JournalController {

    JournalService journalService;
    @Autowired
    JournalController(JournalService journalService){
        this.journalService = journalService;
    }

    @PostMapping("/save-journal")
    ResponseEntity<String> saveJournal(@RequestBody JournalDTO journalDTO){
        return journalService.saveJournalService(journalDTO);
    }

    @PostMapping("/update-journal")
    ResponseEntity<String> updateJournal(@RequestBody JournalDTO journalDTO){
        return journalService.updateJournalService(journalDTO);
    }

    @GetMapping("/get-journal")
    ResponseEntity<String> getJournal(@RequestParam String token, Date date){
        return journalService.getJournalService(token, date);
    }

    @DeleteMapping("/delete-journal")
    ResponseEntity<String> deleteJournal(@RequestBody JournalDTO journalDTO){
        return journalService.deleteJournalService(journalDTO);
    }
}
