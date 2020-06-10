package com.lingogame.service;

import com.lingogame.model.Game;
import com.lingogame.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Optional<Game> findById(Long id) {
        return repository.findById(id);
    }
}
