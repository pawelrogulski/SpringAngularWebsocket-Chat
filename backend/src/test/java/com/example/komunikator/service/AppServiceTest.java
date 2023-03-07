package com.example.komunikator.service;

import com.example.komunikator.domain.Role;
import com.example.komunikator.domain.User;
import com.example.komunikator.repository.ConversationRepo;
import com.example.komunikator.repository.MessageRepo;
import com.example.komunikator.repository.RoleRepo;
import com.example.komunikator.repository.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
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
//    @Test
//    void usernameShouldNotBeEmpty() {
//        UserRegister user = new UserRegister();
//        user.setUsername("");
//        user.setPassword("password");
//        assertThatThrownBy(() ->appService.addUser(user)).isInstanceOf(IllegalStateException.class);
//    }
//    @Test
//    void passwordShouldNotBeEmpty() {
//        UserRegister user = new UserRegister();
//        user.setUsername("username");
//        user.setPassword("");
//        assertThatThrownBy(() ->appService.addUser(user)).isInstanceOf(IllegalStateException.class);
//    }
//    @Test
//    void usernameShouldNotContainSpace() {
//        UserRegister user = new UserRegister();
//        user.setUsername("user name");
//        user.setPassword("password");
//        assertThatThrownBy(() ->appService.addUser(user)).isInstanceOf(IllegalStateException.class);
//    }
//    @Test
//    void passwordShouldNotContainSpace() {
//        UserRegister user = new UserRegister();
//        user.setUsername("username");
//        user.setPassword("pass word");
//        assertThatThrownBy(() ->appService.addUser(user)).isInstanceOf(IllegalStateException.class);
//    }


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