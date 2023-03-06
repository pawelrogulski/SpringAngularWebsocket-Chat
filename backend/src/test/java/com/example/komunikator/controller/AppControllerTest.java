package com.example.komunikator.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AppControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Test
    void homepageIsOk() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().isOk());
    }

    @Test
    void showRegistrationFormIsOk() throws Exception {
        mockMvc.perform(get("/register")).andExpect(status().isOk());
    }

    @Test
    void usersListNotLoginRedirectAndIsFound() throws Exception {
        mockMvc.perform(get("/add_friend")).andExpect(status().isFound());
    }
    @Test
    @WithMockUser(roles = "USER")
    void usersListLoginAsUserIsOk() throws Exception {
        mockMvc.perform(get("/add_friend")).andExpect(status().isOk());
    }

    @Test
    void findPeopleToChatNotLoginRedirectAndIsFound() throws Exception {
        mockMvc.perform(get("/conversation/1")).andExpect(status().isFound());
    }
}