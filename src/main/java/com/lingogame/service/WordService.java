package com.lingogame.service;

import com.lingogame.model.Word;
import com.lingogame.repository.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.*;

@Service
public class WordService implements IWordService {

    @Autowired
    private WordRepository repository;

    @Override
    public List<Word> findAll() {
        return (List<Word>) repository.findAll();
    }

    @Override
    public Word findRandom(int length) {
        List<Word> wordList = (List<Word>) repository.findAll();

        List<Word> filtered = new ArrayList<Word>();

        for (Word word : wordList) {
            if (word.getWord().length() == length) {
                filtered.add(word);
            }
        }

        SecureRandom random = new SecureRandom();

        return filtered.get(random.nextInt(filtered.size()));
    }

}
