package ru.ezuykow.eqb_bot.teams;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.ezuykow.eqb_bot.prepared_games.PreparedGame;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author ezuykow
 */
@Entity
@Table(name = "team")
@Getter
@Setter
public class Team {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "prepared_game_id")
    private PreparedGame preparedGame;

    @Column(name = "chat_id")
    private long chatId;

    @Column(name = "question_answered")
    private int questionAnswered;

    @Column(name = "hints_used")
    private int hintsUsed;

    @Column(name = "place")
    private int place;

    @Column(name = "version")
    private int version;

    @Column(name = "last_question_answered_at")
    private LocalDateTime lastQuestionAnsweredAt;
}