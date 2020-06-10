package com.lingogame.controller;

import com.lingogame.model.Word;
import com.lingogame.service.IWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class WordsetController {

    @Autowired
    private IWordService wordService;

    @PostMapping(path = "/wordsets/import", consumes = "application/json")
    public void importWordset(@RequestBody Word wordset) {
        // wordset code
    }

    @PostMapping(path = "/wordsets/add", consumes = "application/json")
    public void addWordset(@RequestBody Word wordset) {
        // wordset code
    }
}
