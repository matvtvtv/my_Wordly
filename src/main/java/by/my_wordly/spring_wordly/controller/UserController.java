package by.my_wordly.spring_wordly.controller;


import by.my_wordly.spring_wordly.model.User;
import by.my_wordly.spring_wordly.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService service;

    @GetMapping
    public List<User> findAllUser(){
        return service.findAllUser();
    }

    @PostMapping("save_user")
    public String saveUser(@RequestBody   User user){

         service.saveUser(user);
        return "Use3r is saved";
    }
    @GetMapping ("/{login}")
    public User findByLogin(@PathVariable String login){
        return service.findByLogin(login);
    }

    @GetMapping ("/{login}/{password}")
    public User findByLoginAndPassword (@PathVariable String login ,@PathVariable String password){
        return service.findByLoginAndPassword (login, password);
    }


    @PutMapping("update_student")
    public User updateUser(@RequestBody User user){
        return service.updateUser(user);
    }
    @DeleteMapping("delete_user/{login}")
    public void deleteUser(@PathVariable String login){
        service.deleteUser(login);
    }
}
