package com.lingogame.service;

import com.lingogame.model.Word;

import java.util.List;

public interface IWordService {

    List<Word> findAll();

    Word findRandom(int length);

    Iterable<Word> saveAll(Iterable<Word> words);

    Word save(Word words);
}
