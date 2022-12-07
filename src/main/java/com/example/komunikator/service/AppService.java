package com.example.komunikator.service;

import com.example.komunikator.domain.Conversation;
import com.example.komunikator.domain.Message;
import com.example.komunikator.domain.Role;
import com.example.komunikator.domain.User;
import com.example.komunikator.repository.ConversationRepo;
import com.example.komunikator.repository.MessageRepo;
import com.example.komunikator.repository.RoleRepo;
import com.example.komunikator.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AppService {
    private final RoleRepo roleRepository;
    private final UserRepo userRepository;
    private final ConversationRepo conversationRepository;
    private final MessageRepo messageRepository;


    public Role addRole(String role){
        String[] possibleRoles = {"ROLE_ADMIN", "ROLE_USER"};
        if(!Arrays.asList(possibleRoles).contains(role)){
            throw new IllegalStateException("Niezaimplementowana rola");
        }
        Role foundRole = roleRepository.findByName(role);
        if(foundRole==null){
            Role newRole = new Role();
            newRole.setName(role);
            roleRepository.save(newRole);
            foundRole = roleRepository.findByName(role);
        }
        return foundRole;
    }

    public void addUser(String username, String password){
        if(!(username.contains(" ") || password.contains(" ") || username.equals("") || password.equals(""))){
            User user = new User();
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            Collection<Role> defaultRole = new ArrayList<>();
            defaultRole.add(addRole("ROLE_USER"));
            user.setRoles(defaultRole);
            userRepository.save(user);
        }
        else{
            System.out.println("Login lub hasło ze spacją lub puste");
        }
    }


    public void addRoleToUser(String username, String role){
        User user = userRepository.findByUsername(username).get();
        Collection<Role> roles = user.getRoles();
        roles.add(addRole(role));
        user.setRoles(roles);
        userRepository.save(user);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
    public User getUserByUsername(String username){
        return userRepository.findByUsername(username).get();
    }
    public User getUserById(int id){
        return userRepository.findById(id).get();
    }

    public int startConversaton(int user1id, int user2id){
        if(findConversation(user1id,user2id)==-1){
            Collection<User> users = new ArrayList<>();
            users.add(userRepository.findById(user1id).get());
            users.add(userRepository.findById(user2id).get());
            Conversation conversation = new Conversation();
            conversation.setUsers(users);
            conversationRepository.save(conversation);
            return conversation.getId();
        }
        return findConversation(user1id, user2id);
    }

    public List<Integer> findConversation(int userId){
        List<Integer> conversations = new ArrayList<>();
        for(Conversation conversation : conversationRepository.findAll()){
            for(User user : conversation.getUsers()){
                if(user.getId()==userId){
                    conversations.add(conversation.getId());
                }
            }
        }
        return conversations;
    }

    public int findConversation(int user1id, int user2id){
        Boolean flag = true;
        for(Conversation conversation : conversationRepository.findAll()){
            for(User user : conversation.getUsers()){
                if(!(user.getId()==user1id || user.getId()==user2id)){
                    flag = false;
                }
            }
            if(flag){
                return conversation.getId();
            }
            flag = true;
        }
        return -1;
    }

    public Conversation getConversationById(int id){
        return conversationRepository.findById(id).get();
    }

    public List<String> sortMessages(Conversation conversation){
        List<String> messages = new ArrayList<>();
        if(conversation.getMessages()!=null){
            for(Message message : conversation.getMessages()){
                messages.add(getUserById(message.getFromId()).getUsername()+": "+message.getText());
            }
        }
        return messages;
    }

    public void addMessage(String text, int fromId, int conversationId){
        Message message = new Message();
        message.setText(text);
        message.setFromId(fromId);
        message.setConversation(conversationRepository.findById(conversationId).get());
        messageRepository.save(message);
    }
}
