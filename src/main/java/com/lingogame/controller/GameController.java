package com.lingogame.controller;

import com.lingogame.model.Game;
import com.lingogame.service.IGameService;
import com.lingogame.service.IWordService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;

@RestController
public class GameController {

    @Autowired
    private IGameService gameService;

    @Autowired
    private IWordService wordService;

    @GetMapping(path = "/games/new", produces = "application/json")
    public String newGame(Principal principal) {

        String username = principal.getName();

        Game game = gameService.save(new Game(username, wordService.findRandom(5).getWord()));

        var params = new HashMap<String, Object>();
        params.put("gameId", game.getId());
        params.put("firstLetter", game.getWord().charAt(0));

        return new JSONObject(params).toString();
    }

    @GetMapping(path = "/games/{id}/checkWord/{word}", produces = "application/json")
    public String checkWord(@PathVariable String id, @PathVariable String word, Principal principal) {

        Game game = gameService.findById(Long.parseLong(id)).orElseThrow();

        if(!principal.getName().equals(game.getUsername())) {
            return "invalid user";
        }

        var params = new HashMap<String, Object>();

        if(game.getWord().equals(word)) {
            game.setFeedback(generateFeedback(game.getWord(), word));
            game.setScore(game.getScore() + (game.getWord().length() * 10));
            game.setTurn(game.getTurn() + 1);
            params.put("status", "won");
        } else {
            if(game.getTurn() >= 4) {
                game.setConcluded(true);
                game.setTurn(game.getTurn() + 1);
                params.put("status", "lost");
            } else {
                game.setFeedback(generateFeedback(game.getWord(), word));
                game.setTurn(game.getTurn() + 1);
                params.put("status", "ongoing");
            }
        }

        gameService.save(game);

        params.put("feedback", game.getFeedback());
        params.put("score", game.getScore());
        params.put("turn", game.getTurn() - 1);

        return new JSONObject(params).toString();
    }

    @GetMapping(path = "/games/{id}/nextRound", produces = "application/json")
    public String nextRound(@PathVariable String id, Principal principal) {

        Game game = gameService.findById(Long.parseLong(id)).orElseThrow();

        if(!principal.getName().equals(game.getUsername())) {
            return "invalid user";
        }

        var params = new HashMap<String, Object>();

        if(!game.getFeedback().contains("a") && !game.getFeedback().contains("a")) {
            game.setWord(wordService.findRandom(5 + ((game.getWord().length() - 5) + 1) % 3).getWord());
            game.setTurn(0);
        } else {
            System.out.println("somebody is cheatin");
        }

        gameService.save(game);

        params.put("firstLetter", game.getWord().charAt(0));
        params.put("wordLength", game.getWord().length());

        return new JSONObject(params).toString();
    }

    @GetMapping(path = "/games/{id}/end", produces = "application/json")
    public String endGame(@PathVariable String id, Principal principal) {

        Game game = gameService.findById(Long.parseLong(id)).orElseThrow();

        if(!principal.getName().equals(game.getUsername())) {
            return "invalid user";
        }

        game.setConcluded(true);

        gameService.save(game);

        return "succes";
    }

    @GetMapping(path = "/games", produces = "application/json")
    public String getGames() {

        var params = new HashMap<String, Object>();
        params.put("games", gameService.findAll());

        return new JSONObject(params).toString();
    }

    public String generateFeedback(String correctWord, String guessedWord) {
        StringBuilder feedback = new StringBuilder();
        for(int i = 0; i < correctWord.length(); i++) {
            if(guessedWord.charAt(i) == correctWord.charAt(i)) {
                feedback.append("c");
            } else if(correctWord.indexOf(guessedWord.charAt(i)) != -1) {
                feedback.append("a");
            } else {
                feedback.append("f");
            }
        }

        return feedback.toString();
    }
}
