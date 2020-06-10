package com.lingogame.service;

import com.lingogame.model.Word;

import java.util.List;

public interface IWordService {
    List<Word> findAll();

    Word findRandom(int length);
}
