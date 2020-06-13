package com.lingogame.controller;

import com.lingogame.model.Word;
import com.lingogame.service.IWordService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;

@RestController
public class WordController {

    @Autowired
    private IWordService wordService;

    @PostMapping(path = "/words/import", consumes = "application/json")
    public String importWords(@RequestBody String params) {
        JSONObject jsonObj = new JSONObject(params);
        JSONArray jsonArray = (JSONArray) jsonObj.get("words");

        ArrayList<Word> words = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            words.add(new Word(jsonArray.getString(i)));
        }

        wordService.saveAll(words);

        return "succes";
    }

    @GetMapping(path = "/words", produces = "application/json")
    public String getWords() {
        var params = new HashMap<String, Object>();
        params.put("words", wordService.findAll());

        return new JSONObject(params).toString();
    }
}
