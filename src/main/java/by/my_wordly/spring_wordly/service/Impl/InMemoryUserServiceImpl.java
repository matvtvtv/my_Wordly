package by.my_wordly.spring_wordly.service.Impl;
import by.my_wordly.spring_wordly.repository.InMemoryUserDAO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import by.my_wordly.spring_wordly.model.User;
import by.my_wordly.spring_wordly.service.UserService;


import java.util.List;

@Service
@AllArgsConstructor
public class InMemoryUserServiceImpl implements UserService  {
    private final InMemoryUserDAO repository;
    @Override
    public List<User> findAllUser(){
        return repository.findAllUser();
    }

    @Override
    public User saveUser(User user) {
        return repository.saveUser(user);
    }

    @Override
    public User findByLogin(String login) {
        return repository.findByLogin(login);
    }

    @Override
    public User findByLoginAndPassword (String login, String password) {
        return repository.findByLoginAndPassword (login, password);
    }

    @Override
    public User registrationByLoginAndPassword (String login, String password){
        return repository.registrationByLoginAndPassword(login,password);
    }

    @Override
    public User updateUser(User user) {
        return repository.updateUser(user);
    }

    @Override
    public void deleteUser(String login) {
         repository.deleteUser(login);
    }


}
