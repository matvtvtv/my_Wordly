package by.my_wordly.spring_wordly.service.Impl;

import by.my_wordly.spring_wordly.model.MultiUser;
import by.my_wordly.spring_wordly.repository.MultiUserRepository;
import by.my_wordly.spring_wordly.repository.UserRepository;
import by.my_wordly.spring_wordly.service.MultiUserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Primary
public class MultiUserServiceImpl implements MultiUserService {
    private final MultiUserRepository repository;

    @Override
    public MultiUser saveMultiUser(MultiUser multiUser) {

        return repository.save(multiUser);
    }

    @Override
    public List<MultiUser> findByLoginGuessing (String loginGuessing) {
        return repository.findByLoginGuessing( loginGuessing);
    }

}
