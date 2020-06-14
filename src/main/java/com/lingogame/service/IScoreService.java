package com.lingogame.service;

import com.lingogame.model.Score;

import java.util.List;

public interface IScoreService {

    List<Score> findAllByOrderByScoreDesc();

    Score save(Score score);
}
