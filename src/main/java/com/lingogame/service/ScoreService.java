package com.lingogame.service;

import com.lingogame.model.Game;
import com.lingogame.model.Score;
import com.lingogame.repository.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScoreService implements IScoreService {

    @Autowired
    private ScoreRepository repository;

    @Override
    public Score save(Score score) {
        return repository.save(score);
    }

    @Override
    public List<Score> findAllByOrderByScoreDesc() {

        List<Score> scores = repository.findAllByOrderByScoreDesc();

        scores.removeIf(u -> scores.indexOf(u) > 9);

        return scores;
    }

}
