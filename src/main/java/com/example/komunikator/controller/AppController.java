package com.example.komunikator.controller;

import com.example.komunikator.domain.MyUserDetails;
import com.example.komunikator.domain.User;
import com.example.komunikator.service.AppService;
import com.example.komunikator.service.data.UserRegister;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class AppController {
    private final AppService appService;
    private Map<Integer,Double> webSocketKeys = new HashMap<>();
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @MessageMapping("/hello")
    public void send(SimpMessageHeaderAccessor sha, @Payload String recipientAndMessage, Principal principal) {
        String sendFrom = principal.getName();
        int space = recipientAndMessage.indexOf(" ");
        String sendTo = recipientAndMessage.substring(0,space);
        String message = recipientAndMessage.substring(space+1,recipientAndMessage.length());
        int conversationId = appService.findConversation(appService.getUserByUsername(sendFrom).getId(),appService.getUserByUsername(sendTo).getId());
        appService.addMessage(recipientAndMessage,appService.getUserByUsername(sendFrom).getId(),conversationId);
        simpMessagingTemplate.convertAndSendToUser(sendTo, "/queue/messages", sendFrom+": "+message);
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
        List<User> usersWithoutMe = new ArrayList<>();
        String username = "";
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof MyUserDetails) {username = ((MyUserDetails)principal).getUsername();}
        else {username = principal.toString();}
        for(User user : users){
            if(!user.getUsername().equals(username)){
                usersWithoutMe.add(user);
            }
        }
        model.addAttribute("users", usersWithoutMe);
        return "add_friend";
    }

    @GetMapping("/conversation/{id}")
    public String ChatTo(@PathVariable String id, Model model){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";
        if (principal instanceof MyUserDetails) {username = ((MyUserDetails)principal).getUsername();}
        else {username = principal.toString();}
        int idInt =0;
        if(id.matches("[0-9]+")){
            idInt = Integer.parseInt(id);
        }
        else {
            idInt = appService.findConversation(appService.getUserByUsername(username).getId(),appService.getUserByUsername(id).getId());
        }
        int conversationId = appService.startConversaton(appService.getUserByUsername(username).getId(),idInt);
        String message = "";
        model.addAttribute("conversation", appService.sortMessages(appService.getConversationById(conversationId)));
        model.addAttribute("chatWith", appService.getUserById(idInt).getUsername());
        model.addAttribute("newMessage", message);
        model.addAttribute("username",username);

        return "/conversation";
    }
}

