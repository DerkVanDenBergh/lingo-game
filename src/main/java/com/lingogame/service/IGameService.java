package com.lingogame.service;

import com.lingogame.model.Game;

import java.util.List;
import java.util.Optional;

public interface IGameService {

    List<Game> findAll();

    Game save(Game game);

    Optional<Game> findById(Long id);

    public String generateFeedback(String correctWord, String guessedWord);
}
