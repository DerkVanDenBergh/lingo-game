package com.lingogame.controller;

import com.lingogame.LingoGameApplication;
import com.lingogame.configuration.SecurityConfig;
import com.lingogame.configuration.security.UserController;
import com.lingogame.model.Game;
import com.lingogame.service.IGameService;
import com.lingogame.service.IWordService;
import com.lingogame.service.ScoreService;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.Collection;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(GameController.class)
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IGameService gameService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private UserController userController;

    @MockBean
    private IWordService wordService;

    @MockBean
    private ScoreService scoreService;

    @Test
    @WithMockUser(username = "user1", password = "pwd", roles = "USER")
    public void createGameRouteShouldReturnValidJson() throws Exception {

        var params = new HashMap<String, Object>();
        params.put("gameId", 1L);
        params.put("firstLetter", "t");

        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("user1");

        Mockito.when(gameService.createGame(eq("user1"), any(IWordService.class))).thenReturn(params);

        MvcResult result = mockMvc.perform(get("/games/new")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(new JSONObject(params).toString(), result.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(username = "user1", password = "pwd", roles = "USER")
    public void checkWordRouteShouldReturnErrorToIncorrectUser() throws Exception {

        var params = new HashMap<String, Object>();
        params.put("status", "won");
        params.put("feedback", "ccccc");
        params.put("score", 50);
        params.put("turn", 1);

        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("user1");

        Game mockGame = new Game(1L, "user2", "tests");

        Mockito.when(gameService.findById(any(Long.class))).thenReturn(mockGame);

        Mockito.when(gameService.processWord(any(Game.class), eq("tests"))).thenReturn(params);

        MvcResult result = mockMvc.perform(get("/games/1/checkWord/tests")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("invalid user", result.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(username = "user1", password = "pwd", roles = "USER")
    public void checkWordRouteShouldReturnValidJsonToUserThatGameBelongsTo() throws Exception {

        var params = new HashMap<String, Object>();
        params.put("status", "won");
        params.put("feedback", "ccccc");
        params.put("score", 50);
        params.put("turn", 1);

        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("user1");

        Game mockGame = new Game(1L, "user1", "tests");

        Mockito.when(gameService.findById(any(Long.class))).thenReturn(mockGame);

        Mockito.when(gameService.processWord(any(Game.class), eq("tests"))).thenReturn(params);

        MvcResult result = mockMvc.perform(get("/games/1/checkWord/tests")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(new JSONObject(params).toString(), result.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(username = "user1", password = "pwd", roles = "USER")
    public void nextRoundRouteShouldReturnErrorToIncorrectUser() throws Exception {

        var params = new HashMap<String, Object>();
        params.put("firstLetter", "t");
        params.put("wordLength", 5);

        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("user1");

        Game mockGame = new Game(1L, "user2", "tests");

        Mockito.when(gameService.findById(any(Long.class))).thenReturn(mockGame);

        Mockito.when(gameService.processRound(any(Game.class), any(IWordService.class))).thenReturn(params);

        MvcResult result = mockMvc.perform(get("/games/1/nextRound")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("invalid user", result.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(username = "user1", password = "pwd", roles = "USER")
    public void nextRoundRouteShouldReturnValidJsonToUserThatGameBelongsTo() throws Exception {

        var params = new HashMap<String, Object>();
        params.put("firstLetter", "t");
        params.put("wordLength", 5);

        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("user1");

        Game mockGame = new Game(1L, "user1", "tests");

        Mockito.when(gameService.findById(any(Long.class))).thenReturn(mockGame);

        Mockito.when(gameService.processRound(any(Game.class), any(IWordService.class))).thenReturn(params);

        MvcResult result = mockMvc.perform(get("/games/1/nextRound")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(new JSONObject(params).toString(), result.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(username = "user1", password = "pwd", roles = "USER")
    public void endGameRouteShouldReturnErrorToIncorrectUser() throws Exception {

        var params = new HashMap<String, Object>();
        params.put("word", "tests");

        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("user1");

        Game mockGame = new Game(1L, "user2", "tests");

        Mockito.when(gameService.findById(any(Long.class))).thenReturn(mockGame);

        Mockito.when(gameService.endGame(any(Game.class))).thenReturn(params);

        MvcResult result = mockMvc.perform(get("/games/1/end")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("invalid user", result.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(username = "user1", password = "pwd", roles = "USER")
    public void endGameRouteShouldReturnValidJsonToUserThatGameBelongsTo() throws Exception {

        var params = new HashMap<String, Object>();
        params.put("word", "tests");

        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("user1");

        Game mockGame = new Game(1L, "user1", "tests");

        Mockito.when(gameService.findById(any(Long.class))).thenReturn(mockGame);

        Mockito.when(gameService.endGame(any(Game.class))).thenReturn(params);

        MvcResult result = mockMvc.perform(get("/games/1/end")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(new JSONObject(params).toString(), result.getResponse().getContentAsString());
    }

}

