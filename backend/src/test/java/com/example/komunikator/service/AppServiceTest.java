package com.example.komunikator.service;

import com.example.komunikator.domain.Role;
import com.example.komunikator.domain.User;
import com.example.komunikator.repository.ConversationRepo;
import com.example.komunikator.repository.MessageRepo;
import com.example.komunikator.repository.UserRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class AppServiceTest {
    @Autowired
    AppService appService;
    @Mock
    AppService mockedAppService;
    @Autowired
    ConversationRepo conversationRepo;
    @Autowired
    MessageRepo messageRepo;
    @Autowired
    UserRepo userRepo;
    @AfterEach
    public void cleanUpDatabase(){
        conversationRepo.deleteAll();
        userRepo.deleteAll();
        messageRepo.deleteAll();
    }

    @Test
    void addRoleReturnTest() {
        Role role = new Role();
        role.setName("ROLE_USER");
        given(mockedAppService.addRole("ROLE_USER")).willReturn(role);
        Role returnedRole = mockedAppService.addRole("ROLE_USER");
        assertEquals(role,returnedRole);

    }
    @Test
    void usernameShouldNotBeEmpty() {
        User user = new User();
        user.setUsername("");
        user.setPassword("password");
        assertThatThrownBy(() ->appService.addUser(user)).isInstanceOf(IllegalStateException.class);
    }
    @Test
    void passwordShouldNotBeEmpty() {
        User user = new User();
        user.setUsername("username");
        user.setPassword("");
        assertThatThrownBy(() ->appService.addUser(user)).isInstanceOf(IllegalStateException.class);
    }
    @Test
    void usernameShouldNotContainSpace() {
        User user = new User();
        user.setUsername("user name");
        user.setPassword("password");
        assertThatThrownBy(() ->appService.addUser(user)).isInstanceOf(IllegalStateException.class);
    }
    @Test
    void passwordShouldNotContainSpace() {
        User user = new User();
        user.setUsername("username");
        user.setPassword("pass word");
        assertThatThrownBy(() ->appService.addUser(user)).isInstanceOf(IllegalStateException.class);
    }


    @Test
    void getAllUsersShouldReturnSameDataAndListSize() {
        User user = appService.addUser(new User(1,"1","1",null,null));
        assertEquals(appService.getAllUsers().size(),1);
        assertEquals(appService.getAllUsers().get(0).getId(),user.getId());
        assertEquals(appService.getAllUsers().get(0).getUsername(),user.getUsername());
        assertEquals(appService.getAllUsers().get(0).getPassword(),user.getPassword());
    }
    @Test
    void saveAndGetUser(){
        User user = appService.addUser(new User(1,"1","1",null,null));
        assertEquals(appService.getUserByUsername("1").getPassword(),user.getPassword());
    }
}