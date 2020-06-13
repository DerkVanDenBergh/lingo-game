package com.lingogame.service;

import com.lingogame.model.Game;
import com.lingogame.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.desktop.SystemSleepListener;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class GameService implements IGameService {

    @Autowired
    private GameRepository repository;

    @Override
    public List<Game> findAll() {
        return (List<Game>) repository.findAll();
    }

    @Override
    public Game save(Game game) {
        return repository.save(game);
    }

    @Override
    public Game findById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    @Override
    public String generateFeedback(String correctWord, String guessedWord) {
        StringBuilder feedbackBuilder = new StringBuilder();
        for(int i = 0; i < correctWord.length(); i++) {
            if(guessedWord.charAt(i) == correctWord.charAt(i)) {
                guessedWord = guessedWord.substring(0, i) + "." + guessedWord.substring(i + 1);
                correctWord = correctWord.substring(0, i) + "." + correctWord.substring(i + 1);
                feedbackBuilder.append("c");
            } else {
                feedbackBuilder.append("f");
            }
            System.out.println("Ronde " + i + " f/c: \n(correct: " + correctWord + ")\n(geraden: " + guessedWord + ")" + "\n(feedback: " + feedbackBuilder.toString() + ")\n\n");
        }

        String feedback = feedbackBuilder.toString();

        for(int j = 0; j < correctWord.length(); j++) {
            if((correctWord.indexOf(guessedWord.charAt(j)) != -1) && (guessedWord.charAt(j) != '.')) {
                feedback = feedback.substring(0, j) + "a" + feedback.substring(j + 1);
                guessedWord = guessedWord.substring(0, j) + "." + guessedWord.substring(j + 1);
                correctWord = correctWord.substring(0, correctWord.indexOf(guessedWord.charAt(j))) + "." + correctWord.substring(correctWord.indexOf(guessedWord.charAt(j)) + 1);
            }

            System.out.println("Ronde " + j + " f/c: \n(correct: " + correctWord + ")\n(geraden: " + guessedWord + ")" + "\n(feedback: " + feedback + ")\n\n");
        }

        return feedback;
    }

    @Override
    public HashMap<String, Object> createGame(String username, IWordService wordService) {

        Game game = save(new Game(username, wordService.findRandom(5).getWord()));

        var params = new HashMap<String, Object>();

        params.put("gameId", game.getId());
        params.put("firstLetter", game.getWord().charAt(0));

        return params;
    }

    @Override
    public HashMap<String, Object> processWord(Game game, String word) {

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
                game.setFeedback(generateFeedback(game.getWord(), word));
                params.put("status", "lost");
            } else {
                game.setFeedback(generateFeedback(game.getWord(), word));
                game.setTurn(game.getTurn() + 1);
                params.put("status", "ongoing");
            }
        }

        save(game);

        params.put("feedback", game.getFeedback());
        params.put("score", game.getScore());
        params.put("turn", game.getTurn() - 1);

        return params;
    }

    @Override
    public HashMap<String, Object> processRound(Game game, IWordService wordService) {

        var params = new HashMap<String, Object>();

        if(!game.getFeedback().contains("a") && !game.getFeedback().contains("f")) {
            game.setWord(wordService.findRandom(5 + ((game.getWord().length() - 5) + 1) % 3).getWord());
            game.setTurn(0);
        } else {
            game.setConcluded(true);
            params.put("error", "Invalid game progression");
            return params;
        }

        save(game);

        params.put("firstLetter", game.getWord().charAt(0));
        params.put("wordLength", game.getWord().length());

        return params;
    }

    @Override
    public HashMap<String, Object> endGame(Game game) {

        var params = new HashMap<String, Object>();

        game.setConcluded(true);

        save(game);

        params.put("word", game.getWord());

        return params;
    }
}
