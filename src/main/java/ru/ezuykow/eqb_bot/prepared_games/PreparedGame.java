package ru.ezuykow.eqb_bot.prepared_games;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.ezuykow.eqb_bot.games.Game;
import ru.ezuykow.eqb_bot.questions_groups.QuestionsGroup;

import java.util.Set;
import java.util.UUID;

/**
 * @author ezuykow
 */
@Entity
@Table(name = "prepared_game")
@Getter
@Setter
public class PreparedGame {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "key_")
    private String key;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id")
    private Game game;

    @JoinTable(name = "PREPARED_GAME_QUESTIONS_GROUP_LINK",
            joinColumns = @JoinColumn(name = "PREPARED_GAME_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "QUESTIONS_GROUP_ID", referencedColumnName = "ID"))
    @ManyToMany
    private Set<QuestionsGroup> questionGroups;
}