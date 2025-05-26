package by.my_wordly.spring_wordly.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users") // как выше

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;
    @Column(unique=true)
    private String login;
    private String password;
    private int level;
    private int allGames;

    private int gamesWin;
    private int maxSeriesWins;
    private int currentSeriesWins;
    private int bestAttempt;
    private int oneAttempt;
    private int twoAttempt;
    private int threeAttempt;
    private int fourAttempt;
    private int fiveAttempt;
    private int sixAttempt;
    private int money;

    private String z;
}
