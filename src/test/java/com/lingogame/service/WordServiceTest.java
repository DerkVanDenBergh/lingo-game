package com.lingogame.service;

import com.lingogame.model.Game;
import com.lingogame.model.Word;
import com.lingogame.repository.WordRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.Principal;
import java.security.SecureRandom;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@WebMvcTest(WordService.class)
public class WordServiceTest {

    @Autowired
    private IWordService wordService;

    @MockBean
    private WordRepository repository;

    @Test
    public void wordServiceFindAllShouldReturnAllWordsFromRepository() {

        ArrayList<Word> words = new ArrayList<>();

        words.add(new Word("tests"));
        words.add(new Word("spring"));

        Mockito.when(repository.findAll()).thenReturn(words);

        assertEquals(words, wordService.findAll());
    }

    @Test
    public void wordServiceSaveShouldSaveWordToRepository() {

        Word word = new Word("tests");

        Mockito.when(repository.save(word)).thenReturn(word);

        assertEquals(word, wordService.save(word));
    }

    @Test
    public void wordServiceSaveAllShouldSaveAllWordsToRepository() {

        ArrayList<Word> words = new ArrayList<>();

        words.add(new Word("tests"));
        words.add(new Word("spring"));

        Mockito.when(repository.saveAll(words)).thenReturn(words);

        assertEquals(words, wordService.saveAll(words));
    }

    @Test
    public void wordServiceFindRandomShouldReturnRandomWordFromRepository() {

        ArrayList<Word> words = new ArrayList<>();

        words.add(new Word("tests"));
        words.add(new Word("spring"));

        Mockito.when(repository.findAll()).thenReturn(words);

        // 'Unrandom' the random element in this function
        SecureRandom mockSecureRandom = Mockito.mock(SecureRandom.class);
        Mockito.when(mockSecureRandom.nextInt(any(Integer.class))).thenReturn(0);

        assertEquals("tests", wordService.findRandom(5).getWord());
    }



}
