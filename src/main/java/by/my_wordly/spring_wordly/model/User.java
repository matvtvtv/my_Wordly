package by.my_wordly.spring_wordly.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
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

    private String wordDay;

    public User(String login, String password, int level, int allGames, int gamesWin, int maxSeriesWins,
            int currentSeriesWins, int bestAttempt, int oneAttempt, int twoAttempt, int threeAttempt, int fourAttempt,
            int fiveAttempt, int sixAttempt, int money, String wordDay) {
        this.login = login;
        this.password = password;
        this.level = level;
        this.allGames = allGames;
        this.gamesWin = gamesWin;
        this.maxSeriesWins = maxSeriesWins;
        this.currentSeriesWins = currentSeriesWins;
        this.bestAttempt = bestAttempt;
        this.oneAttempt = oneAttempt;
        this.twoAttempt = twoAttempt;
        this.threeAttempt = threeAttempt;
        this.fourAttempt = fourAttempt;
        this.fiveAttempt = fiveAttempt;
        this.sixAttempt = sixAttempt;
        this.money = money;
        this.wordDay = wordDay;
    }

    
}
