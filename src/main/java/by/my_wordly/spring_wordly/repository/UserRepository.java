package by.my_wordly.spring_wordly.repository;

import by.my_wordly.spring_wordly.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findTop10ByOrderByLevelDesc();

    User deleteByLogin(String login);
    User findByLogin(String login);
    User findByLoginAndPassword (String login, String password);
}
