package com.example.komunikator.service;

import com.example.komunikator.domain.Role;
import com.example.komunikator.domain.User;
import com.example.komunikator.repository.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppServiceTest {
    @Mock
    AppService appService;
    @Mock
    UserRepo userRepo;
    @Test
    void addRoleReturnTest() {
        Role role = new Role();
        role.setName("ROLE_USER");
        given(appService.addRole("ROLE_USER")).willReturn(role);
        Role returnedRole = appService.addRole("ROLE_USER");
        assertEquals(role,returnedRole);

    }
    @Test
    void addUserDefaultRoleShouldBeUserRole() {
    }

    @Test
    void addRoleToUser() {
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

    @Test
    void getUserByUsernameShouldReturnSameUsernameAsGiven() {
        when(appService.getUserByUsername("user1")).thenReturn(new User(1,"user1","",null,null));
        assertEquals("user1",appService.getUserByUsername("user1").getUsername());
    }

    @Test
    void getUserByIdShouldReturnSameUsernameAsGiven() {
        when(appService.getUserById(1)).thenReturn(new User(1,"user1","",null,null));
        assertEquals("user1",appService.getUserById(1).getUsername());
    }

    @Test
    void startConversaton() {
    }

    @Test
    void findConversation() {
    }

    @Test
    void testFindConversation() {
    }

    @Test
    void getConversationById() {
    }

    @Test
    void sortMessagesByUsername() {
    }

    @Test
    void addMessage() {
    }

    private List<User> mockDB(){
        List<User> users = new ArrayList<>();
        users.add(new User(1,"user1","",null,null));
        return users;
    }
}