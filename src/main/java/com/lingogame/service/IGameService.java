package com.lingogame.service;

import com.lingogame.model.Game;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface IGameService {

    List<Game> findAll();

    Game save(Game game);

    Game findById(Long id);

    String generateFeedback(String correctWord, String guessedWord);

    HashMap<String, Object> createGame(String username, IWordService wordService);

    HashMap<String, Object> processWord(Game game, String word);

    HashMap<String, Object> processRound(Game game, IWordService wordService);

    HashMap<String, Object> endGame(Game game);

}
