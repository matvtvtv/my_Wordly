package by.my_wordly.spring_wordly.service;

import by.my_wordly.spring_wordly.model.User;

import java.util.List;

public interface UserService {
     List<User> findAllUser();

     User saveUser(User user);
     User findByLogin(String login);
     User updateUser(User user);
     User findByLoginAndPassword (String login, String password);
     User registrationByLoginAndPassword (String login, String password);
     void deleteUser(String login);

}
