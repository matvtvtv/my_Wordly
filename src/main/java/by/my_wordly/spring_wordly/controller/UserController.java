package by.my_wordly.spring_wordly.controller;

import by.my_wordly.spring_wordly.model.User;
import by.my_wordly.spring_wordly.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService service;

    @GetMapping
    public List<User> findAllUser() {
        return service.findAllUser();
    }


    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        try {
            User updated = service.updateUser(user);
            return ResponseEntity.ok(updated);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found with login: " + user.getLogin());
        }
    }

    @GetMapping("/top-users")
    public List<User> getTopUsers() {
        return service.findTop10ByOrderByLevelDesc();
    }

    @GetMapping("/{login}")
    public ResponseEntity<?> findByLogin(@PathVariable String login) {
        User found = service.findByLogin(login);
        if (found == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found with login: " + login);
        }
        return ResponseEntity.ok(found);
    }

    @GetMapping("/entrance/{login}/{password}")
    public User findByLoginAndPassword(@PathVariable String login, @PathVariable String password) {
        System.out.println("старт entrance");
        return service.findByLoginAndPassword(login, password);
    }

    @GetMapping("/registration/{login}/{password}")
    public ResponseEntity<?> registrationByLoginAndPassword(@PathVariable String login, @PathVariable String password) {
        System.out.println("старт reg");
        User created = service.registrationByLoginAndPassword(login, password);
        if (created == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("User already exists with login: " + login);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/delete_user/{login}")
    public ResponseEntity<?> deleteUser(@PathVariable String login) {
        User deleted = service.deleteUser(login);
        if (deleted == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found with login: " + login);
        }
        return ResponseEntity.ok(deleted);
    }
}
