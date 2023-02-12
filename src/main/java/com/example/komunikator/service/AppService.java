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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.ClientAlreadyExistsException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.util.*;

@RequiredArgsConstructor
@Service
public class AppService {
    private final RoleRepo roleRepository;
    private final UserRepo userRepository;
    private final ConversationRepo conversationRepository;
    private final MessageRepo messageRepository;


    public Role addRole(String role){
        String[] possibleRoles = {"ROLE_USER"}; //lista wszystkich zaimplementowanych w aplikacji ról
        if(!Arrays.asList(possibleRoles).contains(role)){
            throw new IllegalStateException("Niezaimplementowana rola");
        }
        Role foundRole = roleRepository.findByName(role);
        if(foundRole==null){ //jeśli zaimplementowanej roli nie ma jeszcze w bazie danych
            Role newRole = new Role();
            newRole.setName(role);
            roleRepository.save(newRole);
            foundRole = roleRepository.findByName(role);
        }
        return foundRole;
    }

    public void addUser(String username, String password){
        if(username.contains(" ")){throw new IllegalStateException("Forbidden sign");}
        if(password.contains(" ")){throw new IllegalStateException("Forbidden sign");}
        if(username.equals("")){throw new IllegalStateException("Forbidden sign");}
        if(password.equals("")){throw new IllegalStateException("Forbidden sign");}
        if(!userRepository.findByUsername(username).equals(Optional.empty())){throw new EntityExistsException("Login in use");}

        User user = new User();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        Collection<Role> defaultRole = new ArrayList<>();
        defaultRole.add(addRole("ROLE_USER")); //domyślnie użytkownik tworzony jest jedynie z rolą "user"
        user.setRoles(defaultRole);
        userRepository.save(user);

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

    public int startConversation(int user1id, int user2id){
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

    public List<Integer> findConversation(int userId){ //zwraca listę id konwersacji danego użytkownika
        List<Integer> conversations = new ArrayList<>();
        for(Conversation conversation : conversationRepository.findAll()){ //przeszukuje wszystkie konwersacje
            for(User user : conversation.getUsers()){ //przeszukuje wszystkich użytkowników konwersacji
                if(user.getId()==userId){ //jeśli id użytkownika pokrywa się z szukanym id, zapisuje go do listy
                    conversations.add(conversation.getId());
                }
            }
        }
        return conversations;
    }

    public int findConversation(int user1id, int user2id){ //zwraca id konwersacji dwóch użytkowników
        Boolean flag = true;
        for(Conversation conversation : conversationRepository.findAll()){ //przeszukuje wszystkie konwersacje
            for(User user : conversation.getUsers()){
                if(!(user.getId()==user1id || user.getId()==user2id)){ //sprawdza czy id użytkowników zgadzają się z szukanymi id
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

    public List<String> sortMessagesByUsername(Conversation conversation){ // zwraca chronologicznie wiadomości z danej konwersacji
        List<String> messages = new ArrayList<>();
        if(conversation.getMessages()!=null){
            for(Message message : conversation.getMessages()){
                //dodaje do wiadomości prefix z loginem użytkownika, który wysłał daną wiadomość
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