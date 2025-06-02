package by.my_wordly.spring_wordly.controller;

import by.my_wordly.spring_wordly.model.MultiUser;
import by.my_wordly.spring_wordly.model.User;
import by.my_wordly.spring_wordly.service.MultiUserService;
import by.my_wordly.spring_wordly.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/multi_users")
public class MultiUserController {
    private final MultiUserService service;

    @PutMapping("/new_guess_word")
    public ResponseEntity<?> saveMultiUser(@RequestBody MultiUser newWord) {
        MultiUser new_word=service.saveMultiUser(newWord);
        return ResponseEntity.ok(new_word);
    }

    @GetMapping("/guess_word/{loginGuessing}")
    public ResponseEntity<?> findByLoginGuessing(@PathVariable String loginGuessing) {
        List <MultiUser> found = service.findByLoginGuessing(loginGuessing);
        if (found == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found with login: " + loginGuessing);
        }
        return ResponseEntity.ok(found);
    }

}
