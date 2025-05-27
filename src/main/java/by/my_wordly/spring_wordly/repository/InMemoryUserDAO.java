package by.my_wordly.spring_wordly.repository;

import by.my_wordly.spring_wordly.model.User;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.Locale.filter;
@Service
@Repository
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
    public User registrationByLoginAndPassword(String login, String password) {
       var user =findByLogin(login);
        if (user == null){
            User newUser = User.builder()
                    .userId(System.currentTimeMillis()) // простая генерация ID
                    .login(login)
                    .password(password)
                    .level(1)
                    .allGames(0)
                    .gamesWin(0)
                    .maxSeriesWins(0)
                    .currentSeriesWins(0)
                    .bestAttempt(0)
                    .oneAttempt(0)
                    .twoAttempt(0)
                    .threeAttempt(0)
                    .fourAttempt(0)
                    .fiveAttempt(0)
                    .sixAttempt(0)
                    .money(0)
                    .wordDay(null)
                    .build();

            USERS.add(newUser);
            return newUser;
        }
        else{
            return null;
        }

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
