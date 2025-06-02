package by.my_wordly.spring_wordly.service.Impl;

import by.my_wordly.spring_wordly.model.User;
import by.my_wordly.spring_wordly.repository.UserRepository;
import by.my_wordly.spring_wordly.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
@Primary
public class UserServiceImpl implements UserService {
private final UserRepository repository;

    @Override
    public List<User> findAllUser() {
        return repository.findAll();
    }


    @Override
    @Transactional
    public User updateUser(User user) {

        User existing = repository.findByLogin(user.getLogin());

        if (existing == null) {

            throw new EntityNotFoundException("User not found with login: " + user.getLogin());
        }


        user.setUserId(existing.getUserId());


        return repository.save(user);
    }

    @Override
    public User findByLogin(String login) {
        return repository.findByLogin(login);
    }

    @Override
    public User findByLoginAndPassword(String login, String password) {
        return repository.findByLoginAndPassword(login, password);
    }

    @Override
    public User registrationByLoginAndPassword(String login, String password) {
        System.out.println("cont reg");
        // Проверяем, есть ли в БД пользователь с точно таким же login
        if (repository.findByLogin(login) == null) {
            // Если нет — создаём нового с начальными полями
            User newUser = new User(
                    login,
                    password,
                    1,  // level
                    0,  // allGames
                    0,  // gamesWin
                    0,  // maxSeriesWins
                    0,  // currentSeriesWins
                    0,  // bestAttempt
                    0,  // oneAttempt
                    0,  // twoAttempt
                    0,  // threeAttempt
                    0,  // fourAttempt
                    0,  // fiveAttempt
                    0,  // sixAttempt
                    20, // money
                    null // wordDay
            );
            repository.save(newUser);
            return repository.findByLoginAndPassword(login, password);
        } else {
            // Пользователь уже существует: можно вернуть null или бросить своё исключение
            return null;
        }
    }

    @Override
    public List<User> findTop10ByOrderByLevelDesc() {
        return repository.findTop10ByOrderByLevelDesc();
    }

    @Override
    @Transactional
    public User deleteUser(String login) {
        return repository.deleteByLogin(login);
    }
}
