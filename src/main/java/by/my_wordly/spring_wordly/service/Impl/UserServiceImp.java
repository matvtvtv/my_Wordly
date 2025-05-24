package by.my_wordly.spring_wordly.service.Impl;

import by.my_wordly.spring_wordly.model.User;
import by.my_wordly.spring_wordly.repository.UserRepository;
import by.my_wordly.spring_wordly.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
@Primary
public class UserServiceImp implements UserService {
private final UserRepository repository;
    @Override
    public List<User> findAllUser() {
        return repository.findAll();
    }

    @Override
    public User saveUser(User user) {
        return repository.save(user);
    }

    @Override
    public User findByLogin(String login) {
        return repository.findByLogin(login);
    }

    @Override
    public User updateUser(User user) {
        return repository.save(user);
    }

    @Override
    public void deleteUser(String login) {
        repository.deleteByLogin(login);
    }
}
