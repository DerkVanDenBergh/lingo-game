package com.lingogame.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(LingoGameErrorController.class)
public class LingoGameErrorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "user1", password = "pwd", roles = "USER")
    public void invalidRouteShouldReturnCustomErrorHtml() throws Exception {
        MvcResult result = mockMvc.perform(get("/error")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        String responseString = "<html><body><h1>Foutmelding!!</h1><div>Er is iets fout gegaan. Neem contact op met de ontwikkelaar. <br><br>Status code: <b>null</b></div><div>Exception Message: <b>N/A</b></div><body></html>";
        assertEquals(responseString, result.getResponse().getContentAsString());
    }
}
