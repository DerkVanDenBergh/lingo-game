package com.lingogame.service;

import com.lingogame.model.Game;
import com.lingogame.model.Word;
import com.lingogame.repository.GameRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GameService.class)
public class GameServiceTest {

    @Autowired
    private IGameService gameService;

    @MockBean
    private GameRepository repository;

    @MockBean
    private IWordService wordService;

    @Test
    public void gameServiceFindAllShouldReturnAllGamesFromRepository() {

        ArrayList<Game> games = new ArrayList<>();

        games.add(new Game(1L, "user1", "tests"));
        games.add(new Game(2L, "user2", "tests"));

        Mockito.when(repository.findAll()).thenReturn(games);

        assertEquals(games, gameService.findAll());
    }

    @Test
    public void gameServiceSaveShouldSaveGameToRepository() {

        Game game = new Game(2L, "user2", "tests");

        Mockito.when(repository.save(game)).thenReturn(game);

        assertEquals(game, gameService.save(game));
    }

    @Test
    public void gameServiceFindByIdShouldFindGameInRepository() {

        Game game = new Game(2L, "user2", "tests");

        Mockito.when(repository.findById(2L)).thenReturn(java.util.Optional.of(game));

        assertEquals(game, gameService.findById(2L));
    }

    // Integration-esque tests:

    @Test
    public void gameServiceCreateGameShouldCreateGameInRepositoryAndReturnValidHashMap() {

        var params = new HashMap<String, Object>();
        params.put("gameId", 1L);
        params.put("firstLetter", "t");

        Mockito.when(wordService.findRandom(any(Integer.class))).thenReturn(new Word("tests"));

        Mockito.when(repository.save(any(Game.class))).thenReturn(new Game(1L,"user1", "tests"));

        // To string because comparing hashmaps is not possible
        assertEquals(params.toString(), gameService.createGame("user1", wordService).toString());
    }

    @Test
    public void gameServiceProcessWordShouldUpdateGameAndReturnValidHashMapOnCorrectWord() {

        var params = new HashMap<String, Object>();
        params.put("status", "won");
        params.put("feedback", "ccccc");
        params.put("score", 50);
        params.put("turn", 0);

        Game game = new Game(1L, "user1", "tests");

        // This return doesnt matter, so no changes to 'thenReturn'
        Mockito.when(repository.save(any(Game.class))).thenReturn(new Game(1L,"user1", "tests"));

        // To string because comparing hashmaps is not possible
        assertEquals(params.toString(), gameService.processWord(game, "tests").toString());
    }

    @Test
    public void gameServiceProcessWordShouldUpdateGameAndReturnValidHashMapOnIncorrectWord() {

        var params = new HashMap<String, Object>();
        params.put("status", "ongoing");
        params.put("feedback", "fffff");
        params.put("score", 0);
        params.put("turn", 0);

        Game game = new Game(1L, "user1", "tests");

        // This return doesnt matter, so no changes to 'thenReturn'
        Mockito.when(repository.save(any(Game.class))).thenReturn(new Game(1L,"user1", "tests"));

        // To string because comparing hashmaps is not possible
        assertEquals(params.toString(), gameService.processWord(game, "round").toString());
    }

    @Test
    public void gameServiceProcessWordShouldUpdateGameAndReturnValidHashMapOnLastRoundAndIncorrectWord() {

        var params = new HashMap<String, Object>();
        params.put("status", "lost");
        params.put("feedback", "fffff");
        params.put("score", 0);
        params.put("turn", 4);

        Game game = new Game(1L, "user1", "tests");

        game.setTurn(4);

        // This return doesnt matter, so no changes to 'thenReturn'
        Mockito.when(repository.save(any(Game.class))).thenReturn(new Game(1L,"user1", "tests"));

        // To string because comparing hashmaps is not possible
        assertEquals(params.toString(), gameService.processWord(game, "round").toString());
    }

    @Test
    public void gameServiceProcessRoundShouldUpdateGameAndReturnValidHashMapOnCorrectProgression() {

        var params = new HashMap<String, Object>();
        params.put("firstLetter", "s");
        params.put("wordLength", 6);

        Game game = new Game(1L, "user1", "tests");

        game.setFeedback("ccccc");

        Mockito.when(wordService.findRandom(any(Integer.class))).thenReturn(new Word("spring"));

        // This return doesnt matter, so no changes to 'thenReturn'
        Mockito.when(repository.save(any(Game.class))).thenReturn(new Game(1L,"user1", "tests"));

        // To string because comparing hashmaps is not possible
        assertEquals(params.toString(), gameService.processRound(game, wordService).toString());
    }

    @Test
    public void gameServiceProcessRoundShouldReturnErrorAndReturnValidHashMapOnInvalidProgression() {

        var params = new HashMap<String, Object>();
        params.put("error", "Invalid game progression");

        Game game = new Game(1L, "user1", "tests");

        game.setFeedback("caccc");

        Mockito.when(wordService.findRandom(any(Integer.class))).thenReturn(new Word("spring"));

        // This return doesnt matter, so no changes to 'thenReturn'
        Mockito.when(repository.save(any(Game.class))).thenReturn(new Game(1L,"user1", "tests"));

        // To string because comparing hashmaps is not possible
        assertEquals(params.toString(), gameService.processRound(game, wordService).toString());
    }

    @Test
    public void gameServiceEndGameShouldUpdateGameAndReturnValidHashMap() {

        var params = new HashMap<String, Object>();
        params.put("word", "tests");

        Game game = new Game(1L, "user1", "tests");

        // This return doesnt matter, so no changes to 'thenReturn'
        Mockito.when(repository.save(any(Game.class))).thenReturn(new Game(1L,"user1", "tests"));

        // To string because comparing hashmaps is not possible
        assertEquals(params.toString(), gameService.endGame(game).toString());
    }



}
