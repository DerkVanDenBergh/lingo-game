package com.lingogame.repository;

import com.lingogame.model.Score;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreRepository extends CrudRepository<Score, Long> {
    public List<Score> findAllByOrderByScoreDesc();
}
