package com.lingogame.controller;

import com.lingogame.configuration.security.UserController;
import com.lingogame.model.Score;
import com.lingogame.service.IScoreService;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ScoreController.class)
public class ScoreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private IScoreService scoreService;

    @MockBean
    private UserController userController;


    @Test
    @WithMockUser(username = "user1", password = "pwd", roles = "USER")
    public void scoresRouteShouldReturnValidJson() throws Exception {

        List<Score> scores = new ArrayList<>();

        scores.add(new Score("user1", 150));

        var params = new HashMap<String, Object>();
        params.put("scores", scores);

        Mockito.when(scoreService.findAllByOrderByScoreDesc()).thenReturn(scores);

        MvcResult result = mockMvc.perform(get("/scores")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(new JSONObject(params).toString(), result.getResponse().getContentAsString());
    }

}
