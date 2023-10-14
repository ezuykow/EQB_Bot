package ru.ezuykow.eqb_bot.games;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.ezuykow.eqb_bot.prepared_games.PreparedGame;
import ru.ezuykow.eqb_bot.users.User;

import java.util.List;
import java.util.UUID;

/**
 * @author ezuykow
 */
@Entity
@Table(name = "game")
@Getter
@Setter
public class Game {

    @Id
    @Column(name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    @Column(name = "name")
    private String name;

    @Column(name = "duration")
    private int duration;

    @Column(name = "questions_count")
    private int questionsCount;

    @Column(name = "max_hints_per_question")
    private int maxHintsPerQuestion;

    @Column(name = "additional_info")
    private String additionalInfo;

    @Column(name = "additional_with_question")
    private boolean additionalWithQuestion;

    @OneToMany(mappedBy = "game")
    private List<PreparedGame> preparedGames;
}