package by.my_wordly.spring_wordly.repository;

import by.my_wordly.spring_wordly.model.MultiUser;
import by.my_wordly.spring_wordly.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MultiUserRepository extends JpaRepository<MultiUser, Long> {
    List <MultiUser> findByLoginGuessing(String loginGuessing);
    //MultiUser saveMultiUser(MultiUser multiUser);
}
