package com.lingogame.controller;

import com.lingogame.model.Score;
import com.lingogame.service.IScoreService;
import com.lingogame.service.ScoreService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
public class ScoreController {

    @Autowired
    private IScoreService service;

    @GetMapping(path = "/scores", produces = "application/json")
    public String getScores() {
        List<Score> scores = service.findAllByOrderByScoreDesc();

        var params = new HashMap<String, Object>();
        params.put("scores", scores);

        return new JSONObject(params).toString();
    }
}
