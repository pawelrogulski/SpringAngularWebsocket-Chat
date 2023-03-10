package com.example.komunikator.controller;

import com.example.komunikator.domain.Conversation;
import com.example.komunikator.domain.MyUserDetails;
import com.example.komunikator.domain.User;
import com.example.komunikator.repository.ConversationRepo;
import com.example.komunikator.service.AppService;
import com.example.komunikator.service.data.Chat;
import com.example.komunikator.service.data.IdUsername;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/app")
public class AppController {
    private final AppService appService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chat")
    public void send(@Payload String recipientAndMessage, Principal principal) {
        String sendFrom ="";
        try{sendFrom = principal.getName();}
        catch (NullPointerException e){
            System.out.println("Not authorized user");
        }
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
    @PostMapping("/register")
    public User processRegister(@RequestBody User user) {
        return appService.addUser(user);
    }
    @GetMapping("/add_friend")
    public List<IdUsername> usersList(){
        List<User> users = appService.getAllUsers();
        String username = appService.getPrincipalUsername(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return users.stream()  //uniemożliwienie pisania wiadomości do samego siebie
                .filter(user -> !user.getUsername().equals(username))
                .map(user -> new IdUsername(user.getId(),user.getUsername()))//zmapowanie na obiekt nie zawierający pól z wrażliwymi danymi
                .collect(Collectors.toList());
    }

    @GetMapping("/conversation/{id}")//id to id użytkownika z którym prowadzimy konwersację
    public ResponseEntity<Chat> ChatTo(@PathVariable String id){
        String username = appService.getPrincipalUsername(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return ResponseEntity.ok(new Chat(appService.sortMessagesByUsername(appService.getConversationById(Integer.parseInt(id))),
                appService.findRecipientUsername(Integer.parseInt(id),username),
                username));
    }

    @GetMapping("user/{id}")
    public int getConversationId(@PathVariable String id){
        String username = appService.getPrincipalUsername(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return appService.startConversation(appService.getUserByUsername(username).getId(),Integer.parseInt(id));
    }
}