package ru.ezuykow.eqb_bot.hints;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.ezuykow.eqb_bot.questions.Question;

import java.util.UUID;

/**
 * @author ezuykow
 */
@Entity
@Table(name = "hint")
@Getter
@Setter
public class Hint {

    @Id
    @Column(name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    @Column(name = "text")
    private String text;
}