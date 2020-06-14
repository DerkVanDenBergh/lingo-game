package com.lingogame.service;

import com.lingogame.configuration.SecurityConfig;
import com.lingogame.configuration.security.UserDetailsServiceImpl;
import com.lingogame.configuration.security.UserRepository;
import com.lingogame.model.Score;
import com.lingogame.model.Word;
import com.lingogame.repository.GameRepository;
import com.lingogame.repository.ScoreRepository;
import com.lingogame.repository.WordRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import java.security.SecureRandom;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScoreService.class)
public class ScoreServiceTest {

    @Autowired
    private IScoreService scoreService;

    @MockBean
    private ScoreRepository repository;

    @Test
    public void wordServiceSaveShouldSaveWordToRepository() {

        Score score = new Score("user1", 150);

        Mockito.when(repository.save(score)).thenReturn(score);

        assertEquals(score, scoreService.save(score));
    }

    @Test
    public void wordServiceFindAllByOrderByScoreDescShouldOrderedScoresFromRepository() {

        ArrayList<Score> scores = new ArrayList<>();

        scores.add(new Score("user1", 150));
        scores.add(new Score("user2", 150));
        scores.add(new Score("user3", 150));
        scores.add(new Score("user4", 150));
        scores.add(new Score("user5", 150));
        scores.add(new Score("user6", 150));
        scores.add(new Score("user7", 150));
        scores.add(new Score("user8", 150));
        scores.add(new Score("user9", 150));
        scores.add(new Score("user10", 150));
        scores.add(new Score("user11", 150));


        Mockito.when(repository.findAllByOrderByScoreDesc()).thenReturn(scores);

        scores.remove(10);

        assertEquals(scores, scoreService.findAllByOrderByScoreDesc());
    }
}
