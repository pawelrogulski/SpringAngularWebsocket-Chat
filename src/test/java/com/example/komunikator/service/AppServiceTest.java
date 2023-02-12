package com.example.komunikator.service;

import com.example.komunikator.domain.Conversation;
import com.example.komunikator.domain.Message;
import com.example.komunikator.domain.Role;
import com.example.komunikator.domain.User;
import com.example.komunikator.repository.ConversationRepo;
import com.example.komunikator.repository.MessageRepo;
import com.example.komunikator.repository.RoleRepo;
import com.example.komunikator.repository.UserRepo;
import org.jboss.logging.Messages;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppServiceTest {
    @InjectMocks
    AppService appService;
    @Mock
    UserRepo userRepo;
    @Mock
    RoleRepo roleRepo;
    @Mock
    MessageRepo messageRepo;
    @Mock
    ConversationRepo conversationRepo;

    @Test
    void addRoleReturnTest() {
        Role role = new Role();
        role.setName("ROLE_USER");
        given(appService.addRole("ROLE_USER")).willReturn(role);
        Role returnedRole = appService.addRole("ROLE_USER");
        assertEquals(role,returnedRole);

    }
    @Test
    void usernameShouldNotBeEmpty() {
        assertThatThrownBy(() ->appService.addUser("","password")).isInstanceOf(IllegalStateException.class);
    }
    @Test
    void passwordShouldNotBeEmpty() {
        assertThatThrownBy(() ->appService.addUser("username","")).isInstanceOf(IllegalStateException.class);
    }
    @Test
    void usernameShouldNotContainSpace() {
        assertThatThrownBy(() ->appService.addUser("user name","password")).isInstanceOf(IllegalStateException.class);
    }
    @Test
    void passwordShouldNotContainSpace() {
        assertThatThrownBy(() ->appService.addUser("username","pass word")).isInstanceOf(IllegalStateException.class);
    }


    @Test
    void getAllUsersShouldReturnSameDataAndListSize() {
        when(appService.getAllUsers()).thenReturn(mockDB());
        List<User> users = new ArrayList<>();
        users.add(new User(1,"user1","",null,null));
        assertEquals(appService.getAllUsers().size(),users.size());
        assertEquals(appService.getAllUsers().get(0).getId(),users.get(0).getId());
        assertEquals(appService.getAllUsers().get(0).getUsername(),users.get(0).getUsername());
        assertEquals(appService.getAllUsers().get(0).getPassword(),users.get(0).getPassword());
    }

    private List<User> mockDB(){
        List<User> users = new ArrayList<>();
        users.add(new User(1,"user1","",null,null));
        return users;
    }
}