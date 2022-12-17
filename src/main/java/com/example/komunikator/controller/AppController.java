package com.example.komunikator.controller;

import com.example.komunikator.domain.MyUserDetails;
import com.example.komunikator.domain.User;
import com.example.komunikator.service.AppService;
import com.example.komunikator.service.data.UserRegister;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class AppController {
    private final AppService appService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @MessageMapping("/chat")
    public void send(SimpMessageHeaderAccessor sha, @Payload String recipientAndMessage, Principal principal) {
        String sendFrom = principal.getName();
        //wiadomość składa się z 3 elementów, loginu odbiorcy, spacji  i treści
        int space = recipientAndMessage.indexOf(" "); //spacja
        String sendTo = recipientAndMessage.substring(0,space); //login
        String message = recipientAndMessage.substring(space+1,recipientAndMessage.length()); //treść
        int conversationId = appService.findConversation(appService.getUserByUsername(sendFrom).getId(),appService.getUserByUsername(sendTo).getId());
        appService.addMessage(message,appService.getUserByUsername(sendFrom).getId(),conversationId); //wysłanie do bazy danych
        simpMessagingTemplate.convertAndSendToUser(sendTo, "/queue/messages", sendFrom+": "+message); //wysłanie przez WebSocket do odbiorcy
    }


    @GetMapping("")
    public String viewHomePage() {
        return "index";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegister());
        return "register";
    }
    @PostMapping("/process_register")
    public String processRegister(UserRegister user) {
        appService.addUser(user.getUsername(),user.getPassword());
        return "redirect:/";
    }
    @GetMapping("/add_friend")
    public String usersList(Model model){
        List<User> users = appService.getAllUsers();
        String username;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof MyUserDetails) {username = ((MyUserDetails)principal).getUsername();}
        else {username = "";}
        List<User> usersWithoutMe = users.stream()  //uniemożliwienie pisania wiadomości do samego siebie
                .filter(user -> !user.getUsername().equals(username))
                .collect(Collectors.toList());
        model.addAttribute("users", usersWithoutMe);
        return "add_friend";
    }

    @GetMapping("/conversation/{id}")//id to id użytkownika z którym prowadzimy konwersacje
    public String ChatTo(@PathVariable String id, Model model){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";
        if (principal instanceof MyUserDetails) {username = ((MyUserDetails)principal).getUsername();}
        int conversationId = appService.startConversaton(appService.getUserByUsername(username).getId(),Integer.parseInt(id));
        String message = "";
        model.addAttribute("conversation", appService.sortMessagesByUsername(appService.getConversationById(conversationId)));
        model.addAttribute("recipient", appService.getUserById(Integer.parseInt(id)).getUsername());
        model.addAttribute("newMessage", message);
        model.addAttribute("username",username);
        return "/conversation";
    }
}