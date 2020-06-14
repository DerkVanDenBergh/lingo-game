package com.lingogame.controller;

import com.lingogame.model.Game;
import com.lingogame.model.Score;
import com.lingogame.model.Word;
import com.lingogame.service.IGameService;
import com.lingogame.service.IScoreService;
import com.lingogame.service.IWordService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class GameController {

    @Autowired
    private IGameService gameService;

    @Autowired
    private IWordService wordService;

    @Autowired
    private IScoreService scoreService;

    @GetMapping(path = "/games/new", produces = "application/json")
    public String newGame(Principal principal) {

        String username = principal.getName();

        HashMap<String, Object> params = gameService.createGame(username, wordService);

        return new JSONObject(params).toString();
    }

    @GetMapping(path = "/games/{id}/checkWord/{word}", produces = "application/json")
    public String checkWord(@PathVariable String id, @PathVariable String word, Principal principal) {

        Game game = gameService.findById(Long.parseLong(id));

        if(!principal.getName().equals(game.getUsername())) {
            return "invalid user";
        }

        List<Word> words = wordService.findAll();

        List<String> wordList = words.stream().map(Word::getWord).collect(Collectors.toList());

        boolean wordExists = true;

        if(!wordList.contains(word)) {
            wordExists = false;
        }

        HashMap<String, Object> params = gameService.processWord(game, word, wordExists);

        return new JSONObject(params).toString();
    }

    @GetMapping(path = "/games/{id}/nextRound", produces = "application/json")
    public String nextRound(@PathVariable String id, Principal principal) {

        Game game = gameService.findById(Long.parseLong(id));

        if(!principal.getName().equals(game.getUsername())) {
            return "invalid user";
        }

        HashMap<String, Object> params = gameService.processRound(game, wordService);

        return new JSONObject(params).toString();
    }

    @GetMapping(path = "/games/{id}/end", produces = "application/json")
    public String endGame(@PathVariable String id, Principal principal) {

        Game game = gameService.findById(Long.parseLong(id));

        if(!principal.getName().equals(game.getUsername())) {
            return "invalid user";
        }

        HashMap<String, Object> params = gameService.endGame(game);

        scoreService.save(new Score(principal.getName(), game.getScore()));

        return new JSONObject(params).toString();
    }
}
