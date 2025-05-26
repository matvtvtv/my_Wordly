package by.my_wordly.spring_wordly.repository;

import by.my_wordly.spring_wordly.model.User;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.Locale.filter;
@Service
public class InMemoryUserDAO
{
    private final List<User> USERS =new ArrayList<>();
    public List<User> findAllUser(){
        return USERS;
    }

    public User saveUser(User user) {
        USERS.add(user);
        return user;
    }

    public User findByLogin(String login) {
        return USERS.stream()
                .filter(element -> element.getLogin().equals(login))
                .findFirst()
                .orElse(null);
    }
    public User findByLoginAndPassword (String login, String password) {
        return USERS.stream()
                .filter(element -> element.getLogin().equals(login))
                .filter(element -> element.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }


    public User updateUser(User user) {
        var userIndex= IntStream.range(0, USERS.size())
                .filter(index->USERS.get(index).getLogin().equals(user.getLogin()))
                .findFirst()
                .orElse(-1);
        if (userIndex>-1){
            USERS.set(userIndex,user);
            return user;
        }
        return null;

    }

    public void deleteUser(String login) {
        var user =findByLogin(login);
        if (user != null){
            USERS.remove(user);
        }

    }
}
