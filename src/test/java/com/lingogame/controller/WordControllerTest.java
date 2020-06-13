package com.lingogame.controller;

import com.lingogame.model.Word;
import com.lingogame.service.IWordService;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(WordController.class)
public class WordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IWordService wordService;

    @Test
    public void importingWordsShouldReturnSucces() throws Exception {

        ArrayList<String> jsonWords = new ArrayList<>();

        jsonWords.add("tests");

        var params = new HashMap<String, Object>();
        params.put("words", jsonWords);

        ArrayList<Word> words = new ArrayList<>();

        words.add(new Word("tests"));

        Mockito.when(wordService.saveAll(words)).thenReturn(words);

        MvcResult result = mockMvc.perform(post("/words/import")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new JSONObject(params).toString()))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("succes", result.getResponse().getContentAsString());
    }

    @Test
    public void getWordsRouteShouldReturnValidJson() throws Exception {


        ArrayList<Word> words = new ArrayList<>();

        words.add(new Word("tests"));

        var params = new HashMap<String, Object>();
        params.put("words", words);

        Mockito.when(wordService.findAll()).thenReturn(words);

        MvcResult result = mockMvc.perform(get("/words")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(new JSONObject(params).toString(), result.getResponse().getContentAsString());
    }
}
