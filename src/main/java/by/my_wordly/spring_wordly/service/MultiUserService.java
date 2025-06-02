package by.my_wordly.spring_wordly.service;
import by.my_wordly.spring_wordly.model.MultiUser;

import java.util.List;

public interface MultiUserService {
    MultiUser saveMultiUser(MultiUser multiUser);
   List <MultiUser> findByLoginGuessing(String loginGuessing);
}
