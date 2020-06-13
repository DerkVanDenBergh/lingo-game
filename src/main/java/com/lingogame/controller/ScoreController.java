package com.lingogame.controller;

import com.lingogame.model.Score;
import com.lingogame.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
public class ScoreController {

    @Autowired
    private ScoreService service;

    @PostMapping(path = "/scores/add", consumes = "application/json")
    public void postScore(@RequestBody Score score) {
        //service.save(score);
    }

    @GetMapping(path = "/scores", produces = "application/json")
    public HashMap<String, Object> getScores() {
        //var scores = (List<Score>) service.findAll();

        var params = new HashMap<String, Object>();
        //params.put("scores", scores);

        return params;
    }
}
