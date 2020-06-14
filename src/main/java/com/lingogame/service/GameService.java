package com.lingogame.service;

import com.lingogame.model.Game;
import com.lingogame.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        ArrayList<Character> correctWordList = new ArrayList<>();
        ArrayList<Character> guessedWordList = new ArrayList<>();
        ArrayList<Character> remainingChars = new ArrayList<>();
        char[] feedback = new char[correctWord.length()];

        for (int i = 0; i < correctWord.length(); i++) {
            feedback[i] = 'f';
        }

        for (int i = 0; i < correctWord.length(); i++) {
            correctWordList.add(correctWord.charAt(i));
        }

        for (int i = 0; i < guessedWord.length(); i++) {
            guessedWordList.add(guessedWord.charAt(i));
        }

        for(int i = 0; i < correctWordList.size(); i++) {
            if(guessedWordList.get(i).equals(correctWordList.get(i))) {
                feedback[i] = 'c';
            } else {
                remainingChars.add(correctWordList.get(i));
            }
        }

        for(int i = 0; i < guessedWordList.size(); i++) {
            if(feedback[i] != 'c' && remainingChars.contains(guessedWordList.get(i))) {
                feedback[i] = 'a';
                remainingChars.remove(guessedWordList.get(i));
            }
        }

        return new String(feedback);
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
    public HashMap<String, Object> processWord(Game game, String word, boolean wordExists) {

        var params = new HashMap<String, Object>();

        if(game.getWord().equals(word) && wordExists) {
            game.setFeedback(generateFeedback(game.getWord(), word));
            game.setScore(game.getScore() + (game.getWord().length() * 10));
            game.setTurn(game.getTurn() + 1);
            params.put("status", "won");
        } else {
            if (game.getTurn() >= 4) {
                game.setConcluded(true);
                game.setTurn(game.getTurn() + 1);
                game.setFeedback(generateFeedback(game.getWord(), word));
                params.put("status", "lost");
            } else if (!wordExists) {
                game.setFeedback("f".repeat(word.length()));
                game.setTurn(game.getTurn() + 1);
                params.put("status", "ongoing");
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
