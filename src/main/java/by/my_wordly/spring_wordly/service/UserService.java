package by.my_wordly.spring_wordly.service;

import by.my_wordly.spring_wordly.model.User;

import java.util.List;

public interface UserService {
     List<User> findAllUser();
     List<User> findTop10ByOrderByLevelDesc();

     User updateUser(User user);
     User findByLogin(String login);
     User findByLoginAndPassword (String login, String password);
     User registrationByLoginAndPassword (String login, String password);
     User deleteUser(String login);

}
