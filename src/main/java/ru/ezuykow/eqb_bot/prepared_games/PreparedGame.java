package ru.ezuykow.eqb_bot.prepared_games;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.ezuykow.eqb_bot.games.Game;

import java.time.LocalDateTime;
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

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "minutes_to_end")
    private Integer minToEnd;
}