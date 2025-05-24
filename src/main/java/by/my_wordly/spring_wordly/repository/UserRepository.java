package by.my_wordly.spring_wordly.repository;

import by.my_wordly.spring_wordly.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    void deleteByLogin(String login);
    User findByLogin(String login);

}
