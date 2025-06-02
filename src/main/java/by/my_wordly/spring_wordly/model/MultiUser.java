package by.my_wordly.spring_wordly.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "multiusers")

public class MultiUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long wordId;
    private String loginGuess;
    private String loginGuessing;
    private String word;

    //private int flag;
    private int flagOfCheck;

}
